import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def getProducers(event, context):
    message = parse_reponse(execute_statement(get_query.replace('-DB_NAME-', 'farmerex').replace('-TABLE_NAME-', 'producer_users')))
    status_code = 200
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
table_name = 'producer_users'

get_query = "SELECT username FROM -DB_NAME-.-TABLE_NAME-"

table_fields = ['username']

def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse
        
def parse_reponse(response):
    return [{f: list(v.values())[0] for f, v in zip(table_fields, i)} for i in response['records']]