import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def getCart(event, context):
    consumer = event['pathParameters']['username']
    response = execute_statement(format_query(get_query, {'consumer' : consumer}))
    if len(response['records']) != 0:
        status_code = 200
        message = parse_reponse(response)
    else:
        status_code = 200
        message = []
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": json.dumps({"message": message})
        }

# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'carts'

get_query = "SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE consumer = '-consumer-'"

table_fields = [
    'producer',
    'product_id',
    'consumer',
    'date_added',
    'quantity'
]

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
        f_query = f_query.replace('-' + k + '-', v)
    return f_query
        
def parse_reponse(response):
    return [{f: list(v.values())[0] for f, v in zip(table_fields, i)} for i in response['records']]