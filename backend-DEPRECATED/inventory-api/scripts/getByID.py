import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def getByID(event, context):
    producer, id = extract(event['pathParameters']['id'])
    response = execute_statement(format_query(get_query, id, producer))
    if len(response['records']) != 0:
        status_code = 200
        message = parse_reponse(response)
    else:
        status_code = 404
        message = f"{id} not found"
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": json.dumps({"message": message})
        }


def extract(body):
    body = body.replace('%7C', "|")
    ex = body.split('||')
    if len(ex) == 2:
        return ex
    else:
        return (None, None)

# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'inventory'

table_fields = [
    'producer',
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


get_query = 'SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE product_id = -product_id- AND producer = "-producer-"'.replace('*', ', '.join(table_fields))

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


def format_query(query, id, producer):
    f_query = query.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name)
    f_query = f_query.replace('-product_id-', id).replace('-producer-', producer)
    return f_query


def parse_reponse(response):
    return {f: list(v.values())[0] for f, v in zip(table_fields, response['records'][0])}