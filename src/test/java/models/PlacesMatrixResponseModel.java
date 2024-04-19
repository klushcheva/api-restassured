package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlacesMatrixResponseModel {
    private List<PriceData> prices;
    private List<String> origins, destinations;
    private Map<String, Object> errors;

    @Data
    public static class PriceData {
        private boolean actual;
        @JsonProperty("show_to_affiliates")
        private Boolean showToAffiliates;
        private double value;
        private int distance;
        @JsonProperty("number_of_changes")
        private int numberOfChanges;
        @JsonProperty("trip_class")
        private int tripClass;
        private String origin, gate, duration, destination;
        @JsonProperty("found_at")
        private String foundAt;
        @JsonProperty("depart_date")
        private String departDate;
        @JsonProperty("return_date")
        private String returnDate;
    }

}
