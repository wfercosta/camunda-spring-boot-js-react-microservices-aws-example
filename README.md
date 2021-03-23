# Camunda and automation test dojo


<p align="center">
  <img src="./docs/dojo.png" />
</p>

## How the process is configured?

|Task Name|Task Type|Inputs|Outputs|Topic/ Message/ Signal Name|
|----|----|-------|-------|-------|
|Restore order for processing|External Task| order_id|order|order_restore|
|Request Payment|External Task|order|payment_correlation_id|order_payment_request|
|Reserve in warehouse|External Task|order|N/A|order_items_reserve|
|Wait for payment confirmation|Receive Task|payment_correlation_id|N/A|order_payment_confirmation|
|Process Invoice|External Task|order|order, invoice|order_invoice_process|
|Order Invoiced|Signal End Event|N/A|N/A|order_invoiced|  
|Revert Order Processing|Compesation Intermediate Throw Event| N/A | N/A | N/A |
|Release order items|External Task| N/A | N/A | order_items_release |
|Refund Payment|External Task| N/A | N/A | order_payment_refund |
|Order Refunded|Signal End Event| N/A | N/A | order_refunded |


### Task: Restore order for processing

| | |
|-|-|
| | |

## How to start Wiremock for local testing

```
$ cd /app/wiremock
$ java -jar wiremock-standalone-2.27.2.jar --port 9000 --verbose
```

```
curl --request POST \
  --url http://localhost:8080/engine-rest/process-definition/key/dojo/start \
  --header 'Content-Type: application/json' \
  --data '{
  "variables": {
    "order_id" : {
        "value" : 1,
        "type": "Long"
    }
  }
}'
```

```
curl --request POST \
  --url http://localhost:8080/engine-rest/message \
  --header 'Content-Type: application/json' \
  --data '{
  "messageName" : "order_payment_confirmation",
  "correlationKeys" : {
    "payment_correlation_id" : {
			"value" : "674e0b0a-8b58-11eb-8dcd-0242ac130003", 
			"type": "String"
		}
  },
  "resultEnabled" : true
}'
```