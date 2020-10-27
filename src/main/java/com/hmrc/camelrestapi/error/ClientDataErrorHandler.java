package com.hmrc.camelrestapi.error;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.hmrc.camelrestapi.constants.Constants.BAD_REQUEST_ERROR;
import static org.apache.camel.builder.Builder.simple;
import static org.apache.camel.language.constant.ConstantLanguage.constant;

@Component
public class ClientDataErrorHandler {

    public void configure(RouteBuilder routeBuilder) {

        routeBuilder
        .onException(JsonValidationException.class, ValidationException.class)
                .handled(true)
                .log(LoggingLevel.WARN, "${exception.stacktrace}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST.value()))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(simple(BAD_REQUEST_ERROR.toString()))
                .end();

    }


}
