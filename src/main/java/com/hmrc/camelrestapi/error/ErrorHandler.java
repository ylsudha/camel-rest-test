package com.hmrc.camelrestapi.error;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.hmrc.camelrestapi.constants.Constants.INTERNAL_SERVER_ERROR;
import static org.apache.camel.builder.Builder.simple;
import static org.apache.camel.language.constant.ConstantLanguage.constant;

@Component
public class ErrorHandler {

    public void configure(RouteBuilder routeBuilder) {

        routeBuilder
            .onException(Exception.class)
            .handled(true)
            .log(LoggingLevel.ERROR, "Exception message: ${exception.stacktrace}")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.INTERNAL_SERVER_ERROR.value()))
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .setBody(simple(INTERNAL_SERVER_ERROR.toString()))
            .end();



    }


}
