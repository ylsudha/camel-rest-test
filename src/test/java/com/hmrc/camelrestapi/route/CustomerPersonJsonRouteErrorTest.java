package com.hmrc.camelrestapi.route;

import com.networknt.schema.ValidationMessage;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.reifier.RouteReifier;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static com.hmrc.camelrestapi.constants.Constants.INTERNAL_SERVER_ERROR;
import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest()
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerPersonJsonRouteErrorTest {

    @Autowired
    CamelContext context;

    @Autowired
    protected ProducerTemplate template;

    @BeforeAll
    public void beforeAll() throws Exception {
        ModelCamelContext modelCamelContext = context.adapt(ModelCamelContext.class);
        RouteDefinition route = modelCamelContext.getRouteDefinition("customerToPersonJsonTransformer");
        //override the default send endpoint to mock.
        RouteReifier.adviceWith(route, modelCamelContext, new RouteBuilder() {
            public void configure() throws Exception {
            interceptSendToEndpoint("json-validator:{{schema.json.person}}")
                    .skipSendToOriginalEndpoint()
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            throw new JsonValidationException(exchange, null, (Set<ValidationMessage>)null);

                        }
                    });
            }
        });
    }



    @Test
    public void testCustomerPersonJsonTransformerRouteExceptionMessageOnError() throws Exception{
        String errorMessage =(String)template.requestBody("direct:customerToPersonJsonTransformer", readFileToString("classpath:json/customer-request.json"));
        assertThat(errorMessage).isEqualTo(INTERNAL_SERVER_ERROR.toString());

    }








}
