package models;

// /v3/get-latest-prices

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LatestPriceRequestModel {
    private Boolean oneWay;
    private int page, limit;
    @JsonProperty("trip_duration")
    private int tripDuration;
    @JsonProperty("trip_class")
    private int tripClass;
    private String currency, origin, destination, sorting;
    @JsonProperty("period_type")
    private String periodType;
    @JsonProperty("beginning_of_period")
    private String beginningOfPeriod;
}
