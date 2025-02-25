package nl.fontys.s3.starter.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCheapestTickerPriceResponse {
    private String fromCurrency;
    private String toCurrency;
    private double cheapestPrice;
    private String cheapestExchange;
    private double priceDifference;
}
