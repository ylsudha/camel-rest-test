package com.hmrc.camelrestapi.mockresourceapi;

import com.hmrc.camelrestapi.pojo.PersonJson;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MockCustomerResourceJsonServiceTest {

    @Autowired
    private CamelContext context;
    @EndpointInject("mock:customerToPersonJsonTransformer")
    private MockEndpoint mockCustomerToPersonJson;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    public void before() throws Exception {
        ModelCamelContext modelCamelContext = context.adapt(ModelCamelContext.class);
        RouteDefinition route = modelCamelContext.getRouteDefinition("customerJsonRoute");
        //override the default send endpoint to mock.
        RouteReifier.adviceWith(route,modelCamelContext, new RouteBuilder() {
            public void configure() throws Exception {
                interceptSendToEndpoint("direct:customerToPersonJsonTransformer")
                        .skipSendToOriginalEndpoint()
                        .to("mock:customerToPersonJsonTransformer");
            }
        });
    }



    @Test
    public void testCustomerResourceMockJson() throws Exception{

        mockCustomerToPersonJson.returnReplyBody( new Expression() {
            @Override
            public  <T> T evaluate(Exchange exchange, Class<T> type) {
                PersonJson json=new PersonJson();
                json.setDetail("mock-response","mock-response-json");
                return   (T)json;

            }
        });
        mockCustomerToPersonJson.expectedMessageCount(1);
        ResponseEntity<PersonJson> response = restTemplate.postForEntity("/camelrestapi/customers/transform/customer", readFileToString("classpath:json/customer-mock-request.json"), PersonJson.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDetails().get("mock-response")).isEqualTo("mock-response-json");
        mockCustomerToPersonJson.assertIsSatisfied();
    }





}
