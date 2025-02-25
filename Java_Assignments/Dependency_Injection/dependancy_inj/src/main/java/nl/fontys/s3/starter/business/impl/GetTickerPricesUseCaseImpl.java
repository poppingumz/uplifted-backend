package nl.fontys.s3.starter.business.impl;

import nl.fontys.s3.starter.business.GetCheapestTickerPriceUseCase;
import nl.fontys.s3.starter.business.GetTickerPricesUseCase;
import nl.fontys.s3.starter.domain.*;
import nl.fontys.s3.starter.persistence.TickerPriceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GetTickerPricesUseCaseImpl implements GetTickerPricesUseCase, GetCheapestTickerPriceUseCase {
    private final List<TickerPriceRepository> tickerPriceRepositories;

    public GetTickerPricesUseCaseImpl(List<TickerPriceRepository> tickerPriceRepositories) {
        this.tickerPriceRepositories = tickerPriceRepositories;
    }

    @Override
    public GetTickerPricesResponse getTickerPrices(GetTickerPricesRequest request) {
        List<TickerPrice> prices = new ArrayList<>();

        for (TickerPriceRepository repository : tickerPriceRepositories) {
            TickerPrice price = repository.getCurrentPrice(request.getFromCurrency(), request.getToCurrency());
            if (price != null) {
                prices.add(price);
            }
        }

        return GetTickerPricesResponse.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .currentPrices(prices)
                .build();
    }

    @Override
    public GetCheapestTickerPriceResponse getCheapestTickerPrice(GetTickerPricesRequest request) {
        List<TickerPrice> prices = new ArrayList<>();

        for (TickerPriceRepository repository : tickerPriceRepositories) {
            TickerPrice price = repository.getCurrentPrice(request.getFromCurrency(), request.getToCurrency());
            if (price != null) {
                prices.add(price);
            }
        }

        if (prices.isEmpty()) {
            throw new RuntimeException("No prices available");
        }

        // Find cheapest
        TickerPrice cheapestPrice = prices.stream()
                .min(Comparator.comparing(TickerPrice::getPrice))
                .orElseThrow();

        // Calculate max price diff
        double maxPrice = prices.stream().mapToDouble(TickerPrice::getPrice).max().orElse(0);
        double priceDifference = maxPrice - cheapestPrice.getPrice();

        return GetCheapestTickerPriceResponse.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .cheapestPrice(cheapestPrice.getPrice())
                .cheapestExchange(cheapestPrice.getExchangeName())
                .priceDifference(priceDifference)
                .build();
    }
}
