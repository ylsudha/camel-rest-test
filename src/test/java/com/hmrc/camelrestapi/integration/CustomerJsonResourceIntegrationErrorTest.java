package com.hmrc.camelrestapi.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.hmrc.camelrestapi.constants.Constants.BAD_REQUEST_ERROR;
import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CustomerJsonResourceIntegrationErrorTest {

    @Autowired
    private CamelContext context;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCustomerResourceJsonInvalidRequestErrorPath(){

        ResponseEntity<String> response = restTemplate.postForEntity("/camelrestapi/customers/transform/customer", readFileToString("classpath:json/customer-invalid-request.json"),String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("text/plain");
        assertThat(response.getBody()).isEqualTo(BAD_REQUEST_ERROR.toString());

    }





}
