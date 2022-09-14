import boto3
import json 

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def evaluateOrder(event, context):
    body = unwrap(event['body'])
    if 'product_id' not in body.keys() or 'producer' not in body.keys() or 'consumer' not in body.keys() or 'sale_id' not in body.keys():
        status_code = 404
        message = "missing producer, consumer, sale_id, or product_it"
    if 'approved' not in body.keys():
        status_code = 404
        message = "missing approved"
    else:
        check = execute_statement(format_query(check_query, body))
        if len(check['records']) == 0:
            status_code = 404
            message = f"{body['product_id']} does not exist"
        else:
            execute_statement(format_query(update_query, body))
            status_code = 200
            message = f"{body['producer']} {body['product_id']} was {'approved' if body['approved']=='True' else 'rejected'}"
            if body['approved']=='True':
                current_quantity = int(list(execute_statement(format_query(check_quantity, body))['records'][0][0].items())[0][1])
                new_quantity = current_quantity - int(list(check['records'][0][0].items())[0][1])
                if new_quantity < 0:
                    new_quantity = 0
                body['quantity'] = new_quantity
                execute_statement(format_query(update_quantity_query, body))
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": message
    }

def unwrap(body):
    from re import compile
    pattern = compile(r'([A-Za-z_]+)" *: *"?([0-9A-Za-z@\._/]+)')    
    body_dict = dict()
    for var, val in pattern.findall(body):
        body_dict[var] = val
    return body_dict


# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'orders'

update_query = "UPDATE -DB_NAME-.-TABLE_NAME- SET pendingReview = 0, approved = -approved- WHERE sale_id = -sale_id- AND product_id = -product_id- AND producer = '-producer-' AND consumer = '-consumer-'"
check_query = 'SELECT quantity FROM -DB_NAME-.-TABLE_NAME- WHERE sale_id = -sale_id- AND product_id = -product_id- AND producer = "-producer-" AND consumer = "-consumer-"'
check_quantity = 'SELECT availableQuantity FROM -DB_NAME-.inventory WHERE producer = "-producer-" AND product_id = -product_id-'
update_quantity_query = "UPDATE -DB_NAME-.inventory SET availableQuantity = -quantity- WHERE product_id = -product_id- AND producer = '-producer-'"


def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse


def format_query(query, body):
    f_query = query.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name)
    for k, v in body.items():
        if k == 'product_id':
            f_query = f_query.replace('-product_id-', v)
        if k == 'producer':
            f_query = f_query.replace('-producer-', v)
        if k == 'consumer':
            f_query = f_query.replace('-consumer-', v)
        if k == 'sale_id':
            f_query = f_query.replace('-sale_id-', v)
        if k == 'approved':
            f_query = f_query.replace('-approved-', str(1 if v=="True" else 0))
        if k == 'quantity':
            f_query = f_query.replace('-quantity-', str(v))
    return f_query