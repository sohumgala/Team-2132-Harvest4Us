import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


# handles the create new user event

def postProducer(event, context):
    body = unwrap(event['body'])
    if "username" not in body.keys() or "password" not in body.keys():
        # Username or password not present in body fields
        status_code = 404
        message = "invalid input"
    else:
        check = execute_statement(format_query(check_query, {'username' : body['username']}))
        if len(check['records']) == 1:
            # Username already found in database
            status_code = 304
            message = f"{body['username']} already registered user"
        else:
            # Create user
            execute_statement(format_query(post_user_query, body))
            # Create producer
            execute_statement(format_query(post_producer_query, body))
            status_code = 200
            message = f"{body['username']} was registered"
    return {
        "statusCode": status_code,
        'headers': header,
        "body": json.dumps({"message": message})
    }

# process event body
def unwrap(body):
    from re import compile
    pattern = compile(r'([A-Za-z_]+)" *: *"([A-Za-z@\._0-9]+)')
    body_dict = dict()
    for var, val in pattern.findall(body):
        body_dict[var] = val
    return body_dict



##### DB ACCESSS #####


# Access Credientals 
rds_client = boto3.client('rds-data')
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'

# db and table names
database_name = 'farmerex'
producer_table = 'producer_users'
users_table = 'users'

# table fields
producer_fields = [
    'username', 
    'description', 
    'business_name', 
    'street', 
    'zip_code', 
    'city', 
    'st'
    ]

user_fields = [
    'username', 
    'first_name', 
    'last_name'
    ]

# post new user query
post_user_query = f"INSERT INTO {database_name}.{users_table} ({' ,'.join(user_fields)}) VALUES ({''.join(['-' + s + '-' for s in user_fields])})"

# post new producer query
post_producer_query = f"INSERT INTO {database_name}.{producer_table} ({' ,'.join(producer_fields)}) VALUES ({''.join(['-' + s + '-' for s in producer_fields])})"

# check that producer is not registered query
check_query = f'SELECT 1 FROM {database_name}.{users_table}  WHERE username = "-username-"'

# execute sql query
def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse

# format query
def format_query(query, body):
    for k, v in body.items():
        format_query = format_query.replace('-' + k + '-', v)
    return format_query