package com.hmrc.camelrestapi.resourceApi;

import com.hmrc.camelrestapi.error.ClientDataErrorHandler;
import com.hmrc.camelrestapi.error.ErrorHandler;
import com.hmrc.camelrestapi.pojo.Customer;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerResource extends RouteBuilder {

    @Resource
    private ErrorHandler errorHandler;
    @Resource
    private ClientDataErrorHandler clientDataErrorHandler;

    @Override
    public void configure() {
        errorHandler.configure(this);
        clientDataErrorHandler.configure(this);
        restConfiguration().component("servlet").port("{{server.port}}");

        // Rest Service - transforms front end json to backend json
        rest("/customers/transform").post("/customer")
                .id("customerJsonRoute")
                .bindingMode(RestBindingMode.json)
                .consumes("application/json; charset=UTF-8") // consumes post call with json data
                .produces("application/json; charset=UTF-8")
                .route()
                .log(LoggingLevel.INFO, "Customer Request Json: ${body}")
                .marshal().json(JsonLibrary.Jackson)
                .to("json-validator:{{schema.json.customer}}").routeId("customerJsonClientValidatorRoute") // frontend schema validation
                .to("direct:customerToPersonJsonTransformer");


        //Rest Service - transforms frontend xml to backend xml
        rest("/customers/transform").post("/customerxml")
            .id("customerXmlRoute")
            .bindingMode(RestBindingMode.xml)
            .consumes("application/xml; charset=UTF-8") //consumes post call with xml data
            .produces("application/xml; charset=UTF-8")
            .type(Customer.class)
            .route()
            .log(LoggingLevel.INFO, "Customer Request XML: ${body}")
            .to("validator:{{schema.xsd.customer}}").routeId("customerXmlClientValidatorRoute")  // frontend schema validation
            .to("direct:customerToPersonXMlTransformer");
    }




}
