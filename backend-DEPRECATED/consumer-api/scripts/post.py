import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def post(event, context):
    table = boto3.resource('dynamodb').Table('customer')
    body = unwrap(event['body'])
    if "username" not in body.keys() or "password" not in body.keys():
        status_code = 404
        message = "invalid input"
    else:
        check = execute_statement(format_query(check_query, {'username' : body['username']}))
        if len(check['records']) == 1:
            status_code = 304
            message = f"{body['username']} already registered user"
        else:
            execute_statement(format_query(post_query, body))
            execute_statement(format_query(post_query_consumer, body))
            status_code = 200
            message = f"{body['username']} was registered"
    return {
        "statusCode": status_code,
        'headers': header,
        "body": json.dumps({"message": message})
    }


def unwrap(body):
    from re import compile
    pattern = compile(r'([A-Za-z_]+)" *: *"([A-Za-z@\._0-9]+)')
    body_dict = dict()
    for var, val in pattern.findall(body):
        body_dict[var] = val
    return body_dict


rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name = 'consumer_users'

post_query = "INSERT INTO -DB_NAME-.users VALUES ('-username-', '-password-', '-first_name-', '-last_name-')"
post_query_consumer = "INSERT INTO -DB_NAME-.-TABLE_NAME- (username) VALUES ('-username-')"
check_query = 'SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE username = "-username-"'

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