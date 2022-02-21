import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def deleteItem(event, context):
    body = unwrap(event['body'])
    if set(body.keys()) != set(table_fields) :
       status_code = 404
       message = json.dumps({"message" : str(set(table_fields - body.keys()))})
    else:
        check = execute_statement(format_query(check_username_query, body))
        if len(check['records']) == 0:
            status_code = 404
            message = f"User {body['producer']} does not exist"
        else:
            execute_statement(format_query(delete_query, body))
            status_code = 200
            message = f"{body['producer']} {body['product_id']} deleted"
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
table_name = 'inventory'
check_items = [
    'producer',
    'product_id'
]
delete_query = 'DELETE FROM -DB_NAME-.-TABLE_NAME- WHERE product_id = -product_id- AND producer = "-producer-"'
check_username_query = 'SELECT username FROM -DB_NAME-.producer_users WHERE username = "-producer-"'

table_fields = [
    'producer',
    'product_id'
]

int_fields = {
    'product_id',
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
        if k in int_fields:
            f_query = f_query.replace(f"-{k}-",  str(v))
        else:
            f_query =f_query.replace(f"-{k}-",  v)
    return f_query

def parse_reponse(response):
    return [{f: list(v.values())[0] for f, v in zip(table_fields, i)} for i in response['records']]
        