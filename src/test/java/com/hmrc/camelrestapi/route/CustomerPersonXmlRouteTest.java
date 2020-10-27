package com.hmrc.camelrestapi.route;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xmlunit.assertj.XmlAssert;

import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CustomerPersonXmlRouteTest {

    @Autowired
    CamelContext context;

    @Autowired
    private ProducerTemplate template;

    @Test
    public void testCustomerPersonXmlTransformerRouteTest() throws Exception{

       String person = (String) template.requestBody("direct:customerToPersonXMlTransformer",readFileToString("classpath:xml/customer-request.xml"));
        XmlAssert.assertThat(person)
                .and(readFileToString("classpath:xml/person-response.xml"))
                .ignoreWhitespace().areIdentical();

    }


}
