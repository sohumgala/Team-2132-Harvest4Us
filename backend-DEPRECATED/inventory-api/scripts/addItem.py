import boto3
import json

header = {
    "Content-Type": "application-json",
    "Access-Control-Allow-Origin": '*'
}


def addItem(event, context):
    body = unwrap(event['body'])
    if set(body.keys()) != set(table_fields):
       status_code = 404
       message = json.dumps({"message" : str(set(table_fields - body.keys()))})
    else:
        check = execute_statement(format_query(check_producer_query, {'producer': body['producer']}))
        if len(check['records']) == 0:
            status_code = 404
            message = f"User {body['producer']} does not exist"
        else:
            check_unique = execute_statement(format_query(check_query, {i: body[i] for i in check_items}))
            if len(check_unique['records']) != 0:
                status_code = 304
                message = f"{' '.join([body[i] for i in check_items])} already exists"
            else:
                body['product_id'] = execute_statement(format_query(get_quantity_query, {i: body[i] for i in check_items}))['records'][0][0]['longValue'] + 1
                execute_statement(format_query(post_query, body))
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
table_name = 'inventory'

post_query = "INSERT INTO -DB_NAME-.-TABLE_NAME- (producer, product_id, produceCategory, produceType, -values-) VALUES ('-producer-', -product_id-, '-produceCategory-', '-produceType-',  -new-vars-)"
check_producer_query = 'SELECT * FROM -DB_NAME-.producer_users WHERE username = "-producer-"'
check_query = 'SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE producer = "-producer-" AND produceCategory = "-produceCategory-" AND produceType = "-produceType-"'
get_quantity_query = 'SELECT COUNT(*) FROM -DB_NAME-.-TABLE_NAME- where producer = "-producer-"'
check_items = ['producer', 'produceCategory', 'produceType', 'organic', 'usdaGrade']

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


def format_query(query, body):
    f_query = query.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name)
    update_statement = ""
    values_statement = ""
    for k, v in body.items():
        if k == 'product_id':
            f_query = f_query.replace('-product_id-', str(v))
        elif k == 'producer':
            f_query = f_query.replace('-producer-', str(v))  
        elif k == 'produceCategory':
            f_query = f_query.replace('-produceCategory-', str(v))  
        elif k == 'produceType':
            f_query = f_query.replace('-produceType-', str(v))  
        else:
            if k in int_fields:
                update_statement += f"{v}, "
            elif k in bool_fields:
                update_statement += f"{1 if v=='true' else 0}, "
            else:
                update_statement += f"'{v}', "
            values_statement += f"{k}, "
    f_query = f_query.replace('-new-vars-', update_statement[:-2])
    f_query = f_query.replace('-values-', values_statement[:-2])
    return f_query
        