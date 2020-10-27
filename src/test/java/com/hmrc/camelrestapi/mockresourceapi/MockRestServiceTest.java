package com.hmrc.camelrestapi.mockresourceapi;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.xmlunit.assertj.XmlAssert;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MockRestServiceTest extends CamelTestSupport {

    @Autowired
    CamelContext context;
    @EndpointInject("mock:customerMockResponse")
    MockEndpoint mockCustomerResponseEndPoint;
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProducerTemplate producerTemplate;

    @BeforeAll
    public void beforeAll() throws Exception {
        ModelCamelContext modelCamelContext = context.adapt(ModelCamelContext.class);
        modelCamelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
               // restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);

                // use the rest DSL to define the rest services
                rest("/customers").id("mockRestService")
                        .post("/customer-mock").type(Object.class)
                        .to("mock:customerMockResponse");
            }
        });

    }


    @Test
    public void testCustomerResourceMockXml() throws Exception{

        mockCustomerResponseEndPoint.returnReplyBody( new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return  (T) readFile("classpath:xml/person-mock-response.xml");

            }
        });
        mockCustomerResponseEndPoint.expectedMessageCount(1);
       // mockCustomerResponseEndPoint.message(0).body().isInstanceOf(String.class);
        ResponseEntity<String> response = restTemplate.postForEntity("/camelrestapi/customers/customer-mock", "{}",String.class);
        assertEquals(response.getStatusCodeValue(),200);

        XmlAssert.assertThat(response.getBody())
                .and(readFile("classpath:xml/person-mock-response.xml"))
                .ignoreWhitespace().areIdentical();
        mockCustomerResponseEndPoint.assertIsSatisfied();
        context.removeRoute("mockRestService");
    }

    private String readFile(String filePath) {

        try {
            return FileUtils.readFileToString(ResourceUtils.getFile(filePath), "UTF-8");

        } catch (IOException e) {
            return "";
        }
    }



}
