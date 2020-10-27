# CamelSpringBootRestAPI

## What does is do?
1. It's a Restful Service using Camel Rest DSL.
2. Customer object is Converted to Person object.
3. The user posts Customer object either in xml/json formart as request and gets Person object as response xml/json formart.
4. It exposes two URI's
   1. /camelrestapi/customers/transform/customer which accepts json data in custom format. 
   2. /camelrestapi/customers/transform/customerxml which accepts xml data in custom format.
4. Business Process
   1. The client customer data is validated against json or xsd schemas to check if the data is in required format. 
	  If fails the user gets '400' bad request error back. 
   2. The client customer data is transformed into person data
   3. The converted person data is validated against json or xsd schemas to check if the data is in required format again. 
   4. The response is send back to user in the same format. 
   5. Incase of unexpected internal server errror , user gets '500' internal error back.    
 
## Implementation?
1. Implemented as maven project using intelliJ.
2. Camel and Spring boot and other dependency jars.
3. Logging and Error handling.
4. Unit Tests and Integration Tests.
5. Docker Image using Docker file. Ran the application using docker container in local and pushed the image to docker hub respository.


  
 # camel-rest-test
