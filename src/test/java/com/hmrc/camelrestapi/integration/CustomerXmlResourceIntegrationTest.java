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
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.assertj.XmlAssert;

import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CustomerXmlResourceIntegrationTest {

    @Autowired
    private CamelContext context;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testCustomerResourceXmlHappyPath() throws Exception{
        ResponseEntity<String> response = restTemplate.postForEntity("/camelrestapi/customers/transform/customerxml", readFileToString("classpath:xml/customer-request.xml"),String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        XmlAssert.assertThat(response.getBody())
                .and(readFileToString("classpath:xml/person-response.xml"))
                .ignoreWhitespace().areIdentical();

    }

}
