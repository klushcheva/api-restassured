package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LatestPriceResponseModel {
    private boolean success;
    private List<FlightData> data;
    @Data
    public static class FlightData {
        private boolean actual;
        @JsonProperty("show_to_affiliates")
        private boolean showToAffiliates;
        private double value;
        @JsonProperty("number_of_changes")
        private int numberOfChanges;
        private String origin, destination, distance;
        @JsonProperty("found_at")
        private String foundAt;
        @JsonProperty("depart_date")
        private String departDate;
        @JsonProperty("return_date")
        private String returnDate;
    }
}
