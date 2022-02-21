import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

# handles the get producer information by username endpoint


def getProducersbyUsername(event, context):
    username = event['pathParameters']['username'] #extract the username from path
    message = execute_statement(get_query.replace('-USERNAME-', username)) #format and execute query
    if len(message['records']) !=0 : 
        # User data found
        message = parse_reponse(message) #format response
        status_code = 200
    else:
        # User not found
        status_code = 404
        message = f"{username} not found"
    return {
        "statusCode": status_code,
        'headers': header,
        "body": json.dumps({"message": message})
    }



##### DB ACCESSS #####


# Access Credientals 
rds_client = boto3.client('rds-data')
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'

# db and table names
database_name = 'farmerex'
table_name = 'producer_users'

# db fields
table_fields = [
    'username', 
    'first_name', 
    'last_name', 
    'description', 
    'business_name', 
    'street', 
    'zip_code', 
    'city',
    'st'
    ]

# db fields with allias
table_fields_w_allias = [
    'username', 
    'first_name', 
    'last_name', 
    'description as about', 
    'business_name', 
    'street as business_street', 
    'zip_code as business_zip', 
    'city as business_city', 
    'st as business_state'
    ]

# db labels for parsing
table_labels = [
    'username',
    'first_name', 
    'last_name', 
    'about', 
    'business_name', 
    'business_street', 
    'business_zip', 
    'business_city', 
    'business_state'
    ]

# get query
get_query = f"SELECT {', '.join(table_fields_w_allias)} FROM {database_name}.{table_name} NATURAL JOIN {database_name}.users WHERE username = '-USERNAME-'"


# execute sql query
def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse

# parse reponse and map them onto table_labels        
def parse_reponse(response):
    return {f: list(v.values())[0] for f, v in zip(table_labels, response['records'][0])}