package com.hmrc.camelrestapi.route;

import com.hmrc.camelrestapi.error.ErrorHandler;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerPersonXmlRoute extends RouteBuilder {

    @Resource
    private ErrorHandler errorHandler;

    @Override
    public void configure() throws Exception {
        errorHandler.configure(this);
        from("direct:customerToPersonXMlTransformer")
            .routeId("customerToPersonXMlTransformer")
            .to("xslt:{{xslt.customer.person}}") // transformation
            .log(LoggingLevel.DEBUG, "Transformed Message XML : ${body}")
            .to("validator:{{schema.xsd.person}}") // backend schema validation
            .log(LoggingLevel.DEBUG, "Person Response Json: ${body}");
    }
}
