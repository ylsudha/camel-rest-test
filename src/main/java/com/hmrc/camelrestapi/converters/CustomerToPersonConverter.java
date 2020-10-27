package com.hmrc.camelrestapi.converters;

import com.hmrc.camelrestapi.pojo.CustomerJson;
import com.hmrc.camelrestapi.pojo.PersonJson;
import org.apache.camel.Converter;
import org.apache.camel.util.json.JsonObject;

import java.util.LinkedHashMap;
import java.util.Optional;

@Converter(allowNull = true, generateLoader = true)
public class CustomerToPersonConverter {

    @Converter
    public static PersonJson customerToPerson(CustomerJson customerJson){
        PersonJson personJson = new PersonJson();
        JsonObject person = new JsonObject();
        JsonObject user = new JsonObject();
        JsonObject address = new JsonObject();

        LinkedHashMap customer = (LinkedHashMap) customerJson.getDetails().get("customer");
        if(Optional.ofNullable(customer).isPresent()) {
            user.put("user_first_name", customer.get("first_name"));
            user.put("user_last_name", customer.get("last_name"));
            LinkedHashMap addressMap = (LinkedHashMap) customer.get("address");
            if (Optional.ofNullable(addressMap).isPresent()) {
                address.put("line1", addressMap.get("street"));
                address.put("line2", addressMap.get("city"));
                address.put("country", addressMap.get("country"));
                address.put("postcode", addressMap.get("zip"));
                person.put("address", address);
            }
            person.put("user", user);
            personJson.setDetail("person",person);
        }
        return personJson;
    }

}
