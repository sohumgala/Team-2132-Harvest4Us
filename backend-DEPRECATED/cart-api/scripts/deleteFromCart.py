import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def deleteFromCart(event, context):
    body = unwrap(event['body'])
    if set(body.keys()) != set(table_fields) :
       status_code = 404
       message = json.dumps({"message" : str(set(table_fields - body.keys()))})
    else:
        check = execute_statement(format_query(check_username_query, body))
        if len(check['records']) == 0:
            status_code = 404
            message = f"User {body['consumer']} does not exist"
        else:
            check = execute_statement(format_query(check_query, body))
            if len(check['records']) != 0:
                quantity = int(parse_reponse(check)[0]['quantity'])
                if quantity == 1:
                    execute_statement(format_query(delete_query, body))
                    message = f"{' '.join([body[i] for i in check_items])} was removed"
                else:
                    body['quantity'] =  int(quantity) - int(body['quantity']) 
                    if body['quantity'] <= 0:
                        message = f"{' '.join([body[i] for i in check_items])} was removed"
                        execute_statement(format_query(delete_query, body))
                    else: 
                        execute_statement(format_query(remove_quantity, body))
                        message = f"{' '.join([body[i] for i in check_items])} was updated"
                status_code = 200
            else:
                status_code = 404
                message = f"{body['consumer']} does not have this item in their cart"
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": message
    }

def unwrap(body):
    from re import compile
    pattern = compile(r'([A-Za-z_]+)" *: *"?([0-9A-Za-z@\._/ -]+)')    
    body_dict = dict()
    for var, val in pattern.findall(body):
        body_dict[var] = val
    return body_dict



# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'carts'
check_items = [
    'producer',
    'product_id',
    'consumer'
]
delete_query = 'DELETE FROM -DB_NAME-.-TABLE_NAME- WHERE consumer = "-consumer-" AND product_id = -product_id- AND producer = "-producer-"'
check_username_query = 'SELECT username FROM -DB_NAME-.consumer_users WHERE username = "-consumer-"'
check_query = 'SELECT producer, product_id, consumer, quantity FROM -DB_NAME-.-TABLE_NAME- WHERE consumer = "-consumer-" AND producer = "-producer-" AND product_id = -product_id-'
remove_quantity = 'UPDATE -DB_NAME-.-TABLE_NAME- SET quantity = -quantity- WHERE consumer = "-consumer-" AND producer = "-producer-" AND product_id = -product_id-'


table_fields = [
    'producer',
    'product_id',
    'consumer',
    'quantity'
]

int_fields = {
    'product_id',
    'quantity'
}


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
        print(f"-{k}-",  str(v))
        if k in int_fields:
            f_query = f_query.replace(f"-{k}-",  str(v))
        else:
            f_query =f_query.replace(f"-{k}-",  v)
    return f_query

def parse_reponse(response):
    return [{f: list(v.values())[0] for f, v in zip(table_fields, i)} for i in response['records']]
        