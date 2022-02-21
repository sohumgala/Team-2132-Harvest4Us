import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def getOrdersByProducer(event, context):
    producer = event['pathParameters']['producer']
    response = execute_statement(format_query(get_query, {'producer' : producer}))
    if len(response['records']) != 0:
        status_code = 200
        message = parse_reponse(response)
    else:
        status_code = 404
        message = f"{producer} not found"
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
table_name = 'orders'

table_fields = [
    "producer",
    "product_id" ,
    'quantity',
    "consumer" , 
    "date_placed" , 
    "shipping",     
    "total_cost",     
    "approved"  ,
    "sale_id"  ,
    "pendingReview"  ,
    "approved"  ,
]

get_query = "SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE producer = '-producer-'".replace('*', ', '.join(table_fields))



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