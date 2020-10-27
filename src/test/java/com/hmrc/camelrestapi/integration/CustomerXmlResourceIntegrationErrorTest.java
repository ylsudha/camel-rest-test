package com.hmrc.camelrestapi.integration;

import org.apache.camel.CamelContext;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.hmrc.camelrestapi.constants.Constants.INTERNAL_SERVER_ERROR;
import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerXmlResourceIntegrationErrorTest {

    @Autowired
    CamelContext context;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testCustomerResourceServerErrorNotHappyPath() throws Exception{
        simulateErrorUsingInterceptors();
        ResponseEntity<String> response = restTemplate.postForEntity("/camelrestapi/customers/transform/customerxml", readFileToString("classpath:xml/customer-request.xml"),String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("text/plain");
        assertThat(response.getBody()).isEqualTo(INTERNAL_SERVER_ERROR.toString());

    }

    private void simulateErrorUsingInterceptors() throws Exception {
        ModelCamelContext modelCamelContext = context.adapt(ModelCamelContext.class);
        RouteDefinition route = modelCamelContext.getRouteDefinition("customerToPersonXMlTransformer");
        //override the default send endpoint to mock.
        RouteReifier.adviceWith(route, modelCamelContext, new RouteBuilder() {
            public void configure() throws Exception {
                interceptSendToEndpoint("validator:{{schema.xsd.person}}")
                        .skipSendToOriginalEndpoint()
                        .to("validator:classpath:xsd/person-invalid.xsd");
            }
        });
    }



}
