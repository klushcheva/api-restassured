package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlightMatrixRequestModel {
    @JsonProperty("one_way")
    private Boolean oneWay;
    @JsonProperty("show_to_affiliates")
    private Boolean showToAffiliates;
    @JsonProperty("trip_duration")
    private int tripDuration;
    private String currency, origin, destination, month, market;
}