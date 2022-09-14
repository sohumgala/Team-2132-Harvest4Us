import boto3
import json

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def checkout(event, context):
    if {'username', 'date', 'total_cost'} != set(event['queryStringParameters'].keys()):
        status_code = 404
        message = "missing params"
    else:
        consumer = event['queryStringParameters']['username']
        check = execute_statement(format_query(check_username_query.replace('-consumer-', consumer)))
        if len(check['records']) == 0:
            status_code = 404
            message = f"User {consumer} does not exist"
        else:
            check = execute_statement(format_query(check_query.replace('-consumer-', consumer)))
            if len(check['records']) > 0:
                checkout_cart = parse_reponse(check)
                sale_id = int(list(execute_statement(format_query(count_query))['records'][0][0].items())[0][1]) + 1
                extra_attributes = {
                    'date_placed' : event['queryStringParameters']['date'],
                    'total_cost' : event['queryStringParameters']['total_cost'],
                    'shipping' : 0.00,
                    'pendingReview' : 1,
                    'approved' : 0,
                    'sale_id' : sale_id,
                    'consumer' : consumer
                }
                make_orders(checkout_cart, extra_attributes)
                status_code = 200
                message = f"{consumer} was checked out"
            else:
                status_code = 304
                message = f"{consumer}'s cart is empty!"
    return {
        "statusCode": status_code, 
        'headers': header,
        "body": message
    }

# DB ACCESSS MOVE TO REPO FILE ######

rds_client = boto3.client('rds-data')

database_name = 'farmerex'
db_cluster_arn = 'MY_CLUSTER_ARN'
db_crendentials_secrets_store_arn = 'MY_SECRET_ARN'
table_cart = 'carts'
table_order = 'orders'
table_consumer = 'consumer_users'

cart_table_fields = [
    'producer',
    'product_id',
    'consumer',
    'quantity'
]

post_query = f"INSERT INTO -DB_NAME-.{table_order} (-values-) VALUES (-new-vars-)"
check_username_query = f'SELECT username FROM -DB_NAME-.{table_consumer} WHERE username = "-consumer-"'
check_query = f'SELECT {", ".join(cart_table_fields)}  FROM -DB_NAME-.{table_cart} WHERE consumer = "-consumer-"'
count_query = f'SELECT MAX(sale_id)  FROM -DB_NAME-.{table_order}'
delete_query = f'DELETE  FROM -DB_NAME-.{table_cart} WHERE consumer = "-consumer-"'

order_table_fields = [
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

int_fields = {
    'product_id',
    'quantity',
    'approved',
    'sale_id', 
    "pendingReview"  ,
}

def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse


def format_query(query):
    f_query = query.replace('-DB_NAME-', database_name)
    return f_query
    
def format_query_insert(query, body):
    f_query = query.replace('-DB_NAME-', database_name)
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
    return [{f: list(v.values())[0] for f, v in zip(cart_table_fields, i)} for i in response['records']]

def make_orders(checkout_cart, extra_attributes):
    for item in checkout_cart:
        for k, v in extra_attributes.items():
            item[k] = v
        execute_statement(format_query_insert(post_query, item))
    execute_statement(format_query(delete_query).replace('-consumer-', extra_attributes['consumer']))
