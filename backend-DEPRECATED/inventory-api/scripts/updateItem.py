import boto3
import json 

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def updateItem(event, context):
    body = unwrap(event['body'])
    if 'product_id' not in body.keys() or 'producer' not in body.keys():
        status_code = 403
        message = "missing id or producer"
    else:
        status_code = 200
        if len(set(body.keys()) - set(table_fields)) != 0:
            status_code = 403
            message = "invalid input " + ", ".join(set(body.keys()) - set(table_fields))
        else:
            check = execute_statement(format_query(check_query, {'product_id' : body['product_id'], 'producer': body['producer']}))
            if len(check['records']) == 0:
                status_code = 404
                message = f"{body['product_id']} does not exist"
            else:
                execute_statement(format_query(update_query, body))
                status_code = 200
                message = f"{body['producer']} {body['product_id']} was updated"
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": message
    }

def unwrap(body):
    from re import compile
    pattern = compile(r'([A-Za-z_]+)" *: *"?([-0-9A-Za-z@\._/]+)')    
    body_dict = dict()
    for var, val in pattern.findall(body):
        body_dict[var] = val
    return body_dict


# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'inventory'

update_query = "UPDATE -DB_NAME-.-TABLE_NAME- SET -new-vars- WHERE product_id = -product_id- AND producer = '-producer-'"
check_query = 'SELECT 1 FROM -DB_NAME-.-TABLE_NAME- WHERE product_id = -product_id- AND producer = "-producer-"'


table_fields = [
    'producer',
    'product_id',
    'produceType',
    'unit',
    'usdaGrade',
    'active',
    'availableQuantity',
    'dateEdited',
    'organic',
    'price',
    'produceCategory'
]


int_fields = {
    'availableQuantity',
    'price',
}
bool_fields = {
    'active',
    'organic'
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
    update_statement = ""
    for k, v in body.items():
        if k == 'product_id':
            f_query = f_query.replace('-product_id-', v)
        if k == 'producer':
            f_query = f_query.replace('-producer-', v)
        elif k in int_fields:
            update_statement += f"{k} = {v}, "
        elif k in bool_fields:
            update_statement += f"{k} = {1 if bool(v) else 0}, "
        else:
            update_statement += f"{k} = '{v}', "
    f_query = f_query.replace('-new-vars-', update_statement[:-2])
    return f_query