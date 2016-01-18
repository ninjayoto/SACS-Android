
package com.sabre.api.sacs.domain.instaflight_gen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "LocationCode"
})
public class ArrivalAirport {

    @JsonProperty("LocationCode")
    private String LocationCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The LocationCode
     */
    @JsonProperty("LocationCode")
    public String getLocationCode() {
        return LocationCode;
    }

    /**
     * 
     * @param LocationCode
     *     The LocationCode
     */
    @JsonProperty("LocationCode")
    public void setLocationCode(String LocationCode) {
        this.LocationCode = LocationCode;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
