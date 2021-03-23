# Dojo: Camunda and test automation
This material was build in order to illustrate how Camunda BPM works and how to implement unit, integration and component tests with Spring Boot.
## About the business process

This is a sample process about Puchase Order processing that is fired as soon as it is received to process the payment and items reservation in Warehouse before the shipping.

Next, you will find example of the camunda process used for the study case:

<p align="center">
  <img src="./docs/dojo.png" />
</p>

### Configuration
In the next table you will find a simple view about de confiugration applyed to each task in the process:

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



## How to use this project

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

## License

This project is under the MIT license. See the [LICENSE](./LICENSE) for more information.