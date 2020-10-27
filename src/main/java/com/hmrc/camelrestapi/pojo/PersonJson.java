package com.hmrc.camelrestapi.pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class PersonJson implements Serializable {

   private Map<String, Object> details = new LinkedHashMap<>();

    @JsonAnySetter
    public void setDetail(String key, Object value) {
        details.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getDetails() {
        return details;
    }
}
