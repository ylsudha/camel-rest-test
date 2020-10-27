package com.hmrc.camelrestapi.route;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.reifier.RouteReifier;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.hmrc.camelrestapi.constants.Constants.INTERNAL_SERVER_ERROR;
import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest()
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerPersonXmlRouteErrorTest {

    @Autowired
    CamelContext context;

    @Autowired
    protected ProducerTemplate template;

    @Test
    public void testCustomerPersonXmlTransformerRouteExceptionMessageOnError() throws Exception{
        context.start();
        ModelCamelContext modelCamelContext = context.adapt(ModelCamelContext.class);
        simulateErrorUsingInterceptors(modelCamelContext);

       String errorMessage =(String)template.requestBody("direct:customerToPersonXMlTransformer",readFileToString("classpath:xml/customer-request.xml"));
        assertThat(errorMessage).isEqualTo(INTERNAL_SERVER_ERROR.toString());
        context.stop();

    }

    private void simulateErrorUsingInterceptors(ModelCamelContext modelCamelContext) throws Exception {

        RouteDefinition route = modelCamelContext.getRouteDefinition("customerToPersonXMlTransformer");
        //override the default send endpoint to mock.
        RouteReifier.adviceWith(route, modelCamelContext, new RouteBuilder() {
            public void configure() throws Exception {
                interceptSendToEndpoint("validator:{{schema.xsd.person}}")
                        .skipSendToOriginalEndpoint()
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                throw new ValidationException(exchange,"error");

                            }
                        });
            }
        });
    }




}
