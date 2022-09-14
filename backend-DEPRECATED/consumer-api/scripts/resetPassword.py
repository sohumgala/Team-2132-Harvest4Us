import boto3
import json
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

header = {
    "Content-Type": "application-json",
    "access-control-allow-origin": '*'
}

def resetPassword(event, context):
    body = unwrap(event['body'])
    if "username" not in body.keys() or "password" not in body.keys():
        status_code = 404
        message = "invalid input"
    else:
        check = execute_statement(format_query(check_query, {'username' : body['username']}))
        if len(check['records']) == 0:
            status_code = 404
            message = f"{body['username']} is not a registered user"
        else:
            execute_statement(format_query(update_query, body))
            consumer_name = str(list(execute_statement(format_query(get_name_query, body))['records'][0][0].items())[0][1])
            send_email(body['username'], consumer_name)
            status_code = 200
            message = f"{body['username']} password was reset"
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

update_query = 'UPDATE -DB_NAME-.users SET password = "-password-" WHERE username = "-username-"'
check_query = 'SELECT * FROM -DB_NAME-.-TABLE_NAME- WHERE username = "-username-"'
get_name_query = 'SELECT first_name FROM -DB_NAME-.users WHERE username = "-username-"'

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

MY_ADDRESS = "har4us@outlook.com"
MY_PASSWORD = "orcax2021"

def send_email(email, name):
    # set up the SMTP server
    s = smtplib.SMTP(host='smtp-mail.outlook.com', port=587)
    s.starttls()
    # login
    s.login(MY_ADDRESS, MY_PASSWORD)
    msg = MIMEMultipart()
    msg['From']=MY_ADDRESS
    msg['To']=email
    msg['Subject']="Reset Password"
    message = f"Dear {name}, \n\n \t Your password has been reset."
    msg.attach(MIMEText(message, 'plain'))
    s.send_message(msg)    
    del msg
    s.quit()
