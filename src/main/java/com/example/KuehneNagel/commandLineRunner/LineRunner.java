package com.example.KuehneNagel.commandLineRunner;

import com.example.KuehneNagel.model.CurrentRate;
import com.example.KuehneNagel.model.HistoricalRate;
import com.example.KuehneNagel.service.BitcoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Slf4j
public class LineRunner implements CommandLineRunner {

    @Autowired
    private BitcoinService service;

    @Override
    public void run(String... args) {

        log.info("CURRENCY");
        Scanner scanner = new Scanner(System.in);

        String stop = "stop";
        try {
            String currency = scanner.nextLine();
            fetch(currency.toUpperCase());
        } catch (Exception e) {
            log.info("Unsupported currency");
        }
    }

    public void fetch(String currency) throws IOException {
        final CurrentRate currentPrice = service.getCurrentPrice(currency);
        final HistoricalRate historicalPrice = service.getHistorical(currency);

        log.info("The current Bitcoin rate: " + currentPrice.getRate() + " " + currency);
        log.info("The highest Bitcoin rate in the last 180 days: " + historicalPrice.getMaxPrice().toString() + " " + currency);
        log.info("The lowest Bitcoin rate in the last 180 days: " + historicalPrice.getMinPrice().toString() + " " + currency);
    }
}
