package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlacesMatrixRequestModel {
    @JsonProperty("show_to_affiliates")
    private Boolean showToAffiliates;
    private int limit, flexibility;
    private String currency, origin, destination, month, market;
    @JsonProperty("depart_date")
    private String departDate;
    @JsonProperty("return_date")
    private String returnDate;
}
