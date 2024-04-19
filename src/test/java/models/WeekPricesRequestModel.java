package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeekPricesRequestModel {
    @JsonProperty("show_to_affiliates")
    private Boolean showToAffiliates;
    private String currency, origin, destination, market;
    @JsonProperty("depart_date")
    private String departDate;
    @JsonProperty("return_date")
    private String returnDate;
}
