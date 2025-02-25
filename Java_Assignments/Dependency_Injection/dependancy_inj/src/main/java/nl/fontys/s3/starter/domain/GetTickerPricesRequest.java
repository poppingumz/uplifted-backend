package nl.fontys.s3.starter.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTickerPricesRequest {
    private String fromCurrency;
    private String toCurrency;
}
