package nl.fontys.s3.starter.controller;

import nl.fontys.s3.starter.business.GetCheapestTickerPriceUseCase;
import nl.fontys.s3.starter.business.GetTickerPricesUseCase;
import nl.fontys.s3.starter.domain.GetCheapestTickerPriceResponse;
import nl.fontys.s3.starter.domain.GetTickerPricesRequest;
import nl.fontys.s3.starter.domain.GetTickerPricesResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickers")
public class TickerPriceController {
    private final GetTickerPricesUseCase getTickerPricesUseCase;
    private final GetCheapestTickerPriceUseCase getCheapestTickerPriceUseCase;

    public TickerPriceController(GetTickerPricesUseCase getTickerPricesUseCase, GetCheapestTickerPriceUseCase getCheapestTickerPriceUseCase) {
        this.getTickerPricesUseCase = getTickerPricesUseCase;
        this.getCheapestTickerPriceUseCase = getCheapestTickerPriceUseCase;
    }

    @GetMapping("/prices")
    public GetTickerPricesResponse getTickerPrices(@RequestParam String from, @RequestParam String to) {
        GetTickerPricesRequest request = new GetTickerPricesRequest(from, to);
        return getTickerPricesUseCase.getTickerPrices(request);
    }

    @GetMapping("/cheapest")
    public GetCheapestTickerPriceResponse getCheapestTickerPrice(@RequestParam String from, @RequestParam String to) {
        GetTickerPricesRequest request = new GetTickerPricesRequest(from, to); // Now it works
        return getCheapestTickerPriceUseCase.getCheapestTickerPrice(request);
    }
}
