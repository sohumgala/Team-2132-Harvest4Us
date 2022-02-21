import boto3
import json 

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def updateProfile(event, context):
    body = unwrap(event['body'])
    if 'username' not in body.keys():
        status_code = 403
        message = "missing username"
    else:
        status_code = 200
        if len((set(body.keys()) - set(table_1_fields)) - set(table_2_fields))!= 0:
            status_code = 403
            message = "invalid input " + ", ".join(set(body.keys()) - set(table_1_fields) - set(table_2_fields))
        else:
            check = execute_statement(format_query(check_query, {'username' : body['username']}, table_name_1))
            if len(check['records']) == 0:
                status_code = 404
                message = f"{body['username']} not registered producer"
            else:
                body_1 = {k:v for k,v in body.items() if k in table_1_fields}
                body_2 = {k:v for k,v in body.items() if k in table_2_fields}
                if len(body_1.keys()) > 1:
                    execute_statement(format_query(update_query, body_1, table_name_1))
                if len(body_2.keys()) > 1:
                    execute_statement(format_query(update_query, body_2, table_name_2))
                status_code = 200
                message = f"{body['username']} was updated"
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": message
    }

def unwrap(body):
    from re import compile
    pattern = compile(r'([A-Za-z_]+)" *: *"?([, \' 0-9A-Za-z@\._/ ]+)')    
    body_dict = dict()
    for var, val in pattern.findall(body):
        body_dict[var] = val
    return body_dict


# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_name_1 = 'producer_users'
table_name_2 = 'users'

update_query = 'UPDATE -DB_NAME-.-TABLE_NAME- SET -new-vars- WHERE username = "-username-"'
check_query = 'SELECT 1 FROM -DB_NAME-.-TABLE_NAME- WHERE username = "-username-"'


table_1_fields = ['username', 'description', 'business_name', 'street', 'zip_code', 'city', 'st']
table_2_fields = ['username', 'first_name', 'last_name']


def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse


def format_query(query, body, table):
    f_query = query.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table)
    update_statement = ""
    for k, v in body.items():
        if k == 'username':
            f_query = f_query.replace('-username-', v)
        else:
            update_statement += f'{k} = "{v}", '
    f_query = f_query.replace('-new-vars-', update_statement[:-2])
    return f_query
        