import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def addToCart(event, context):
    body = unwrap(event['body'])
    if set(body.keys()) != set(table_fields) :
       status_code = 404
       message = json.dumps({"message" : str(set(table_fields - body.keys()))})
    else:
        pass
        check = execute_statement(format_query(check_username_query.replace('-consumer-', body['consumer']), {}))
        if len(check['records']) == 0:
            status_code = 404
            message = f"User {body['consumer']} does not exist"
        else:
            check = execute_statement(format_query(check_query.replace('-consumer-', body['consumer']).replace('-producer-', body['producer']).replace('-product_id-', body['product_id']), {}))
            if len(check['records']) != 0:
                quantity = parse_reponse(check)[0]['quantity']
                check_avaible = int(list(execute_statement(format_query(check_quantity, body))['records'][0][0].items())[0][1])
                body['quantity'] = int(body['quantity']) + int(quantity)
                if  body['quantity'] > check_avaible:
                    body['quantity'] = check_avaible
                execute_statement(format_query(add_quantity, body))
                status_code = 200
                message = f"{' '.join([body[i] for i in check_items])} was updated"
            else:
                execute_statement(format_query_insert(post_query, body))
                status_code = 200
                message = f"{' '.join([body[i] for i in check_items])} was added"
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
table_name = 'carts'

post_query = "INSERT INTO -DB_NAME-.-TABLE_NAME- (-values-) VALUES (-new-vars-)"
check_username_query = 'SELECT username FROM -DB_NAME-.consumer_users WHERE username = "-consumer-"'
check_query = 'SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE consumer = "-consumer-" AND producer = "-producer-" AND product_id = -product_id-'
check_items = ['consumer', 'producer', 'product_id']
add_quantity = 'UPDATE -DB_NAME-.-TABLE_NAME- SET quantity = -quantity-, date_added = "-date_added-" WHERE consumer = "-consumer-" AND producer = "-producer-" AND product_id = -product_id-'
check_quantity = 'SELECT availableQuantity FROM -DB_NAME-.inventory WHERE producer = "-producer-" AND product_id = -product_id-'


table_fields = [
    'producer',
    'product_id',
    'consumer',
    'date_added',
    'quantity'
]

int_fields = {
    'product_id',
    'quantity'
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
        print(f"-{k}-",  str(v))
        if k in int_fields:
            f_query = f_query.replace(f"-{k}-",  str(v))
        else:
            f_query =f_query.replace(f"-{k}-",  v)
    return f_query
    
def format_query_insert(query, body):
    f_query = query.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name)
    update_statement = ""
    values_statement = ""
    for k, v in body.items():
        if k in int_fields:
            update_statement += f"{v}, "
        else:
            update_statement += f"'{v}', "
        values_statement += f"{k}, "
    f_query = f_query.replace('-new-vars-', update_statement[:-2])
    f_query = f_query.replace('-values-', values_statement[:-2])
    return f_query
        
def parse_reponse(response):
    return [{f: list(v.values())[0] for f, v in zip(table_fields, i)} for i in response['records']]