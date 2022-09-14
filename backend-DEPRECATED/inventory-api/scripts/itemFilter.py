import boto3
import json
import requests
from difflib import SequenceMatcher

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}


def itemFilter(event, context):
    filters.clear()
    if "price_range" in event['queryStringParameters'].keys():
        upper, lower = event['queryStringParameters']['price_range'].split(',')
        format_price(upper, lower)
    if "search_key" in event['queryStringParameters'].keys():
        message = execute_statement(format_query())
        message = parse_reponse(message)
        if 'search_key' in event['queryStringParameters'].keys():
            message = search_sort(message, event['queryStringParameters']['search_key'])
        status_code = 200
    else:
        status_code = 200 
        message = execute_statement(format_query() +  " " + order_by)
        message = parse_reponse(message)
    if "distance" in event['queryStringParameters'].keys():
        distance = event['queryStringParameters']['distance']
        if distance != 'X':
            if "consumer" not in event['queryStringParameters'].keys():
                status_code = 304
                message = "missing consumer username for distance filter"
            else:
                consumer = event['queryStringParameters']['consumer']
                distance = int(distance)
                message = dist_filter(message, consumer, distance)
    filters.clear()
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
table_name = 'inventory'
consumer_users = 'consumer_users'
producer_users = 'producer_users'

inventory_fields = [
    'producer',
    'product_id',
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

distance_fields = ['street', 'city', 'st', 'zip_code']

get_query = "SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE -filters-"
get_userquery = "SELECT -address- FROM -DB_NAME-.-TABLE_NAME- WHERE -filters-"
blank = "SELECT * FROM -DB_NAME-.-TABLE_NAME-"
order_by = "ORDER BY price"
get_address = f"SELECT {' ,'.join(distance_fields)} FROM {database_name}.-TABLE_NAME- WHERE username = '-username-'"

api_key ='MY_API_KEY'
url ='https://maps.googleapis.com/maps/api/distancematrix/json?'


filters = list()

def execute_statement(sql):
    reponse = rds_client.execute_statement(
        secretArn = db_crendentials_secrets_store_arn,
        database = database_name,
        resourceArn = db_cluster_arn,
        sql = sql
    )
    return reponse


def format_price(upper, lower):
    bounds_statement = f'price > {lower} '
    bounds_statement += f"AND price < {upper}" if upper != 'X' else ''
    filters.append(bounds_statement)

def format_query():
    if not filters:
            return blank.replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name).replace('*', ', '.join(inventory_fields))
    return get_query.replace('-filters-', ' AND '.join(filters)).replace('-DB_NAME-', database_name).replace('-TABLE_NAME-', table_name).replace('*', ', '.join(inventory_fields))
        
def parse_reponse(response):
    return [{f: list(v.values())[0] for f, v in zip(inventory_fields, i)} for i in response['records']]

def parse_reponse_distance(response):
    return [{f: list(v.values())[0] for f, v in zip(distance_fields, i)} for i in response['records']]


def format_userquery(id, producer=True):
    address = 'business_address' if producer else 'address'
    return get_userquery.replace('-address-', )

def make_request(source, dest):
    request = f"{url}key={api_key}&destinations={dest}&origins={source}"
    r = requests.get(request)
    return int(r.json()['rows'][0]['elements'][0]['distance']['value'])

def calc_distance(item, user):
    consumer_addr = ', '.join(parse_reponse_distance(execute_statement(get_address.replace('-TABLE_NAME-', consumer_users).replace('-username-', user)))[0].values())
    producer_addr = ', '.join(parse_reponse_distance(execute_statement(get_address.replace('-TABLE_NAME-', producer_users).replace('-username-', item['producer'])))[0].values())
    return make_request(consumer_addr, producer_addr)
### Sorting

sorting = {
            'search' : lambda i: i['produceType'] + i['produceCategory']
           }
def initlize_sorting(search):
    sorting['search_sort'] = lambda i : SequenceMatcher(None, search, i[1]).ratio()

def restructure(items):
    return {i['producer'] + str(i['product_id']) : i for i in items}

def create_baskets(items, key):
    return [(i['producer'] + str(i['product_id']), key(i)) for i in items]

def order_items(items, key=lambda x:x, sort_key=lambda x:x, reverse=False):
    id_items = restructure(items)
    sorted_items = sorted(create_baskets(items, key), key=sort_key, reverse=True) 
    return [id_items[i[0]] for i in sorted_items]
     
def search_sort(items, search):
    initlize_sorting(search)
    return order_items(items, key=sorting['search'], sort_key=sorting['search_sort'])

def dist_filter(items, user, maximum):
    filtered_items = []
    for i in items:
        dis = calc_distance(i, user)
        if dis <= maximum:
            filtered_items += [i]
    return filtered_items
