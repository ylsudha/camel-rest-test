package com.hmrc.camelrestapi.converters;

import com.hmrc.camelrestapi.pojo.CustomerJson;
import com.hmrc.camelrestapi.pojo.PersonJson;
import org.apache.camel.util.json.JsonObject;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerToPersonConverterTest {


    @Test
    public void customerToPersonConverterTest(){
        CustomerJson customerJson = buildCustomer();
        PersonJson personJson = CustomerToPersonConverter.customerToPerson(customerJson);
        LinkedHashMap personDetails = (LinkedHashMap) personJson.getDetails();
        JsonObject person =  (JsonObject)personDetails.get("person");
        JsonObject address = (JsonObject) person.get("address");
        JsonObject user = (JsonObject) person.get("user");

        JsonObject customer = (JsonObject) customerJson.getDetails().get("customer");
        JsonObject customerAddress = (JsonObject)customer .get("address");
        assertNotNull(personJson);
        assertNotNull(address);
        assertNotNull(user);
        assertThat(address.get("line1")).isEqualTo(customerAddress.get("street"));
        assertThat(address.get("line2")).isEqualTo(customerAddress.get("city"));
        assertThat(address.get("country")).isEqualTo(customerAddress.get("country"));
        assertThat(address.get("postcode")).isEqualTo(customerAddress.get("zip"));
        assertThat(user.get("user_first_name")).isEqualTo(customer.get("first_name"));
        assertThat(user.get("user_last_name")).isEqualTo(customer.get("last_name"));
        assertNotNull(person);
    }

    private CustomerJson buildCustomer() {
       CustomerJson customerJson =  new CustomerJson();
        JsonObject customer = new JsonObject();
        JsonObject address = new JsonObject();
        customer.put("address", address);
        customer.put("first_name","adf");
        customer.put("last_name", "abc");
        address.put("street","501");
        address.put("city","london");
        address.put("country","uk");
        address.put("zip","2323");
        customerJson.setDetail("customer",customer);
        return customerJson;
    }
}
