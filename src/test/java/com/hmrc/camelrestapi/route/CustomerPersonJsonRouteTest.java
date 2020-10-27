package com.hmrc.camelrestapi.route;

import com.hmrc.camelrestapi.pojo.PersonJson;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

import static com.hmrc.camelrestapi.util.FileUtil.readFileToString;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest()
@RunWith(CamelSpringBootRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerPersonJsonRouteTest {

    @Autowired
    CamelContext context;

    @Autowired
    protected ProducerTemplate template;

    @Test
    public void testCustomerPersonJsonTransformerRouteTest() throws Exception{
        PersonJson personJson = (PersonJson) template.requestBody("direct:customerToPersonJsonTransformer", readFileToString("classpath:json/customer-request.json"));
        LinkedHashMap personDetails = (LinkedHashMap) personJson.getDetails();
        LinkedHashMap person =  (LinkedHashMap)personDetails.get("person");
        LinkedHashMap address = (LinkedHashMap) person.get("address");
        LinkedHashMap user = (LinkedHashMap) person.get("user");
        assertNotNull(personJson);
        assertNotNull(person);
        assertNotNull(address);
        assertNotNull(user);
    }

}
