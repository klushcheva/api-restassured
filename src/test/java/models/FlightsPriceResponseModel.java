package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightsPriceResponseModel {
    private boolean success;
    private List<FlightData> data;

    @Data
    public static class FlightData {

        private boolean actual;
        @JsonProperty("show_to_affiliates")
        private Boolean showToAffiliates;
        private double value;
        private int distance;
        @JsonProperty("number_of_changes")
        private int numberOfChanges;
        private String origin, destination;
        @JsonProperty("found_at")
        private String foundAt;
        @JsonProperty("depart_date")
        private String departDate;
        @JsonProperty("return_date")
        private String returnDate;

    }
}
