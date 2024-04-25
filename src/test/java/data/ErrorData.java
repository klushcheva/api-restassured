package data;

public class ErrorData {
    public static final String errorLocationCode = "bad request: unknown location code `";
    public static final String errorCurrencyCode01 = "unreachable external service error: invalid currency code ";
    public static final String errorCurrencyCode02 = ": invalid currency code format: ";
    public static final String errorDateInterval = "bad request: depart_date: diff between max depart date and min return date exceeds supported maximum of 30.";
    public static final String errorNoLocationCode = "bad request: unknown location code `<no value>`";
    public static final String errorNoLocations = "bad request: destination: you must set origin either destination.";
    public static final String errorNoDateIntersection = "bad request: depart and return dates ranges have no intersection: invalid params";
    public static final String errorBadCurrency01 = "bad request: unknown currency code ";
    public static final String errorBadCurrency02 = ": no rates for currency ";
    public static final String errorBadCurrencyLength = "bad request: currency: the length must be exactly 3.";
    public static final String errorBadOriginLength = "bad request: origin: the length must be between 2 and 3.";
    public static final String errorBadDestinationLength = "bad request: destination: the length must be between 2 and 3.";
    public static final String errorTripDuration = "bad request: trip_duration: must contain digits only.";
    public static final String errorLongTripDuration01 = "unreachable external service error: requested minimum trip duration ";
    public static final String errorLongTripDuration02 = " exceeds supported maximum of 30: empty result guaranteed";
    public static final String errorBadPeriod = "bad request: beginning_of_period: string doesn't match any date formats: 2006-01, 2006-01-02, 2006.";
    public static final String errorBadSorting = "bad request: sorting: incorrect value, allows: [price route distance_unit_price].";
    public static final String errorBadPeriodType = "bad request: period_type: incorrect value, allows: [day year month season].";
    public static final String errorBadPage = "bad request: page: too small value, minimum allowed value is 1.";
    public static final String errorTooLargePage = "bad request: too high paging depth (limit/offset parameters) for the request: invalid params";
    public static final String errorBadClass = "bad request: trip_class: incorrect value, allows: [0 1 2].";
    public static final String errorNoOrigin = "bad request: origin parameter is skipped";
    public static final String errorNoDestination = "bad request: destination parameter is skipped";
    public static final String errorCantFindOrigin = "bad request: failed to set closest cities to an origin: failed to find a city or an airport with iata code = ";
    public static final String errorCantFindDestination = "bad request: failed to set closest cities to a destination: failed to find a city or an airport with iata code = ";
    public static final String errorBadFlexibilityName = "bad request: flexibility: must contain digits only.";
    public static final String errorBadFlexibility = "bad request: flexibility: too big value, maximum allowed value is 7.";
    public static final String errorBadDistance = "bad request: distance: must contain digits only.";
    public static final String errorSmallDistance = "bad request: distance: too small value, minimum allowed value is 1.";
    public static final String errorBigDistance = "bad request: distance: too big value, maximum allowed value is 1000.";
    public static final String errorBadPageType = "bad request: page: must contain digits only.";
}
