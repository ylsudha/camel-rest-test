package com.hmrc.camelrestapi.mockresourceapi;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.xmlunit.assertj.XmlAssert;

import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MockCustomerResourceXmlServiceTest {

    @Autowired
    CamelContext context;
    @EndpointInject("mock:customerToPersonXMlTransformer")
    MockEndpoint mockCustomerToPersonXml;
    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public void beforeAll() throws Exception {
        ModelCamelContext modelCamelContext = context.adapt(ModelCamelContext.class);
        RouteDefinition route = modelCamelContext.getRouteDefinition("customerXmlRoute");
        //override the default send endpoint to mock.
        RouteReifier.adviceWith(route,modelCamelContext, new RouteBuilder() {
            public void configure() throws Exception {
                interceptSendToEndpoint("direct:customerToPersonXMlTransformer")
                        .skipSendToOriginalEndpoint()
                        .to("mock:customerToPersonXMlTransformer");
            }
        });
    }


    @Test
    public void testCustomerResourceMockXml() throws Exception{

        mockCustomerToPersonXml.returnReplyBody( new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return  (T) readFileToString("classpath:xml/person-mock-response.xml");

            }
        });
        mockCustomerToPersonXml.expectedMessageCount(1);
        ResponseEntity<String> response = restTemplate.postForEntity("/camelrestapi/customers/transform/customerxml", readFileToString("classpath:xml/customer-mock-request.xml"),String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        XmlAssert.assertThat(response.getBody())
                .and(readFileToString("classpath:xml/person-mock-response.xml"))
                .ignoreWhitespace().areIdentical();
        mockCustomerToPersonXml.assertIsSatisfied();
    }


}
