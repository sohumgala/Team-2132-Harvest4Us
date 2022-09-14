import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def get(event, context):
    username, password = extract(event['pathParameters']['id'])
    if username and password:
        response = execute_statement(format_query(get_query, {'username' : username}))
        if len(response['records']) == 0:
            message = username + " does not exist."
            status_code = 404
        elif parse_reponse(response)['password'] == password:
            message = f"Correct credentials for {username}"
            status_code = 200
        else:
            message = "Invalid password"
            status_code = 401
    else:
        status_code = 404
        message = "invalid input"
    return {
        "statusCode": status_code,
        'headers': header,
        "body": json.dumps({"message": message})
    }


def extract(body):
    ex = body.split('%7C%7C')
    if len(ex) == 2:
        return ex
    else:
        return (None, None)


# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'producer_users'
table_name_2 = 'users'
table_fields = ['username', 'password']


get_query = "SELECT username, password FROM -DB_NAME-.-TABLE_NAME- NATURAL JOIN -DB_NAME-.-TABLE_NAME_2- WHERE username = '-username-'"

def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse

def format_query(query, body):
    f_query = query.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name).replace('-TABLE_NAME_2-', table_name_2)
    for k, v in body.items():
        f_query = f_query.replace('-' + k + '-', v)
    return f_query
        
def parse_reponse(response):
    return {f: v.get('stringValue') for f, v in zip(table_fields, response['records'][0])}