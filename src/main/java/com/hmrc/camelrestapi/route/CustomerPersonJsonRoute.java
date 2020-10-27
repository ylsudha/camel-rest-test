package com.hmrc.camelrestapi.route;

import com.hmrc.camelrestapi.error.ErrorHandler;
import com.hmrc.camelrestapi.pojo.CustomerJson;
import com.hmrc.camelrestapi.pojo.PersonJson;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerPersonJsonRoute extends RouteBuilder {
    @Resource
    private ErrorHandler errorHandler;

    @Override
    public void configure()  {
      errorHandler.configure(this);
      from("direct:customerToPersonJsonTransformer").routeId("customerToPersonJsonTransformer")
        .unmarshal().json(JsonLibrary.Jackson, CustomerJson.class)
        .convertBodyTo(PersonJson.class) //Transformation
        .log(LoggingLevel.DEBUG, "Transformed Message Json : ${body}")
        .marshal().json(JsonLibrary.Jackson)
        .to("json-validator:{{schema.json.person}}")  // backend schema validation
        .unmarshal().json(JsonLibrary.Jackson,PersonJson.class)
        .log(LoggingLevel.INFO, "Person Response Json: ${body}");
    }
}
