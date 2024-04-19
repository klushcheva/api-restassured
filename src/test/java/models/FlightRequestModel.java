package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// /v3/prices_for_dates
@Data
public class FlightRequestModel {
    private boolean direct, unique;
    @JsonProperty("one_way")
    private boolean oneWay;
    private int page, limit;
    private String currency, origin, destination, market, sorting;
    @JsonProperty("depart_date")
    private String departDate;
    @JsonProperty("return_date")
    private String returnDate;
}
