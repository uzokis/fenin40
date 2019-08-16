import requests
import json

#URL = "http://192.168.43.4:48964/status"
host = "https://cyan.fenego.zone"
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/"
URL = host+path

#----------------------
#---------SETUP--------
#----------------------
print("----------------------")
print("---------SETUP--------")
print("----------------------")
HEADERS = { 'User-Agent': 'PostmanRuntime/7.15.2',\
			'Accept': '*/*',\
			'Cache-Control': 'no-cache',\
			'Postman-Token': '642dd986-247e-48f9-98ac-95491bd8993a,822e3289-19b9-4d94-8973-ba9c8e2d9b22',\
			'Host': 'cyan.fenego.zone',\
			'Accept-Encoding': 'gzip, deflate',\
			'Content-Length': "",\
			 'Connection': 'keep-alive',\
			 'cache-control': 'no-cache'
			 }

r = requests.head(url = URL, headers = HEADERS)

#data = r.json()

print(r.text)

#----------------------
#-------REGISTER-------
#----------------------
print("----------------------")
print("-------REGISTER-------")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/customers"
URL = host+path
PAYLOAD = "{\n  \"type\": \"SMBCustomer\",\
				\n  \"customerNo\": \"dc59d081-15b3-4b6d-b9a7-14009d2d31d0\",\
				\n  \"companyName\": \"AgroNet\",\
				\n  \"credentials\": {\n    \"login\": \"eli@tesfqsqlmk.com\",\
										\n    \"password\": \"!InterShop00!\",\
										\n    \"securityQuestion\": \"what was the name of your first pet?\",\
										\n    \"securityQuestionAnswer\": \"Snoopy\"\n  },\
				\n  \"address\": {\n    \"title\": \"Mrs.\",\
									\n    \"firstName\": \"Patricia\",\
									\n    \"lastName\": \"Miller\",\
									\n    \"addressLine1\": \"Berliner Str. 20\",\
									\n    \"postalCode\": \"14482\",\
									\n    \"email\": \"info@intershop.de\",\
									\n    \"phoneMobile\": \"+49364112677\",\
									\n    \"country\": \"Germany\",\
									\n    \"countryCode\": \"DE\",\
									\n    \"city\": \"Potsdam\"\n  },\
				\n  \"user\": {\n    \"firstName\": \"Eli\",\
								\n    \"lastName\": \"Van fraeyenhove\",\
								\n    \"businessPartnerNo\": \"fc01d42a-cf0c-47b2-a708-66202334b476\",\
								\n    \"login\": \"eli@tesfqsqlmk.com\",\
								\n    \"phoneMobile\": \"4211324\",\
								\n    \"email\": \"eli@tesfqsqlmk.com\",\
								\n    \"preferredLanguage\": \"de_DE\"\n  }\n\
			}"
HEADERS = {'Content-Type': "application/json",\
    'Accept': "application/json",\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "829c6e47-9fb3-4d62-87b7-b765598e0490,9a416c8e-952b-4a3b-b151-868fe3640add",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Content-Length': "885",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.post(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)


#----------------------
#---------LOGIN--------
#----------------------
print("----------------------")
print("---------LOGIN--------")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/customers/-"
URL = host+path
PAYLOAD = ""
HEADERS = {'Content-Type': "application/json",\
    'Authorization': "Basic ZWxpQHRlc2Zxc3FsbWsuY29tOiFJbnRlclNob3AwMCE=",\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "dd250daa-e523-40c7-a0ac-93fbd37deef7,cd242fde-e81c-4ead-ae59-35533d234769",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.get(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']


#----------------------
#------CHECK TOKEN-----
#----------------------
print("----------------------")
print("------CHECK TOKEN-----")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/customers/-"
URL = host+path
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "a3fd445c-e018-407a-9c2b-a83f6e4f5adb,36d2cd33-ccf2-423b-8f0b-0db0319d7c20",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.get(url = URL, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']
print("token: " + authentToken)


#----------------------
#-----CREATE BASKET----
#----------------------
print("----------------------")
print("-----CREATE BASKET----")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/baskets"
URL = host+path
PAYLOAD = ""
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "561ed547-ba31-4565-a96b-070d0378c361,0e39b318-fd0b-4007-af2f-aaa77b6b24da",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    #'Content-Length': "",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.post(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']

data = r.json()
title = data['title']


#----------------------
#----EXAMINE BASKET----
#----------------------
print("----------------------")
print("----EXAMINE BASKET----")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/baskets/" + title
URL = host + path
PAYLOAD = ""
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "766998c4-9fc5-424b-bdc3-b4b9f0f6a76e,8de4677c-b465-4bc6-a5df-bb402ba1351f",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.get(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']


#----------------------
#--ADD ITEM TO BASKET--
#----------------------
print("----------------------")
print("--ADD ITEM TO BASKET--")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/baskets/" + title + "/items"
URL = host + path
PAYLOAD = "{\r\n    \"elements\":\r\n    [\r\n        {\r\n            \"sku\":\"11089966\",\
				\r\n            \"quantity\": { \"value\": 1 }\r\n        }\r\n    ]\r\n}"
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "f7ba7d57-d227-4357-bc30-9cee07571700,8ef6a8b2-4936-40fd-8864-16732d569270",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Content-Length': "128",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.post(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']

#--------------------------------
#--ADD PAYMENT METHOD TO BASKET--
#--------------------------------
print("--------------------------------")
print("--ADD PAYMENT METHOD TO BASKET--")
print("--------------------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/baskets/" + title + "/payments"
URL = host + path
PAYLOAD = "{\n\t\"name\": \"ISH_INVOICE\"\n}"
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "738f0443-316a-4d72-b205-b8f4bb8f5586,dd6f6c6b-de7b-4aa0-baad-4469e49f37f5",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Content-Length': "26",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.post(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']


#-----------------------------------------
#--CREATE ADDRESS IN BASKET ADDRESS BOOK--
#-----------------------------------------
print("-----------------------------------------")
print("--CREATE ADDRESS IN BASKET ADDRESS BOOK--")
print("-----------------------------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/baskets/" + title + "/addresses"
URL = host + path
PAYLOAD = "{\n    \"title\": \"Mrs.\",\
				\n    \"firstName\": \"Patricia\",\
				\n    \"lastName\": \"Miller\",\
				\n    \"addressLine1\": \"Berliner Str. 20\",\
				\n    \"postalCode\": \"14482\",\
				\n    \"email\": \"pmiller@intershop.de\",\
				\n    \"phoneMobile\": \"+49364112677\",\
				\n    \"country\": \"Germany\",\
				\n    \"countryCode\": \"DE\",\
				\n    \"city\": \"Potsdam\"\n}"
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "151724d6-800c-4ba2-a5f3-66865583d5cb,00edb3ff-c894-438f-8bad-f9ebd542ce47",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Content-Length': "291",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.post(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']

data = r.json()
urn = data['data']['urn']


#-----------------------------------------------------
#--SET ADDRESS AS SHIPPING ADDRESS & INVOICE ADDRESS--
#-----------------------------------------------------
print("-----------------------------------------------------")
print("--SET ADDRESS AS SHIPPING ADDRESS & INVOICE ADDRESS--")
print("-----------------------------------------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/baskets/" + title
URL = host + path
PAYLOAD = "{\n\t\"commonShipToAddress\": \"" + urn + "\",\
			\n\t\"invoiceToAddress\": \"" + urn + "\",\
			\n\t\"commonShippingMethod\": \"STD_GROUND\"\n}"
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'User-Agent': "PostmanRuntime/7.15.2",\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "6c334c09-38b5-47b2-8435-6e0f0da6c9c9,c663337f-ccf9-4816-b360-6c4ee9ea35fe",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Content-Length': "230",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.patch(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)

data = r.headers
authentToken = data['authentication-token']


#----------------------
#-----CREATE ORDER-----
#----------------------
print("----------------------")
print("-----CREATE ORDER-----")
print("----------------------")
path = "/INTERSHOP/rest/WFS/inSPIRED-inTRONICS_Business-Site/-/orders"
URL = host + path
PAYLOAD = "{\n  \"basketID\": \"" + title + "\",\
			\n  \"acceptTermsAndConditions\": true\n}"
HEADERS = {'Content-Type': "application/json",\
    'authentication-token': authentToken,\
    'Accept': "*/*",\
    'Cache-Control': "no-cache",\
    'Postman-Token': "b5140bbb-42d9-493d-9b17-246764e7fefc,91ec50ef-23a0-4839-9e65-462d3d6b762c",\
    'Host': "cyan.fenego.zone",\
    'Accept-Encoding': "gzip, deflate",\
    'Content-Length': "80",\
    'Connection': "keep-alive",\
    'cache-control': "no-cache"\
    }

r = requests.post(url = URL, data = PAYLOAD, headers = HEADERS)

print(r.text)