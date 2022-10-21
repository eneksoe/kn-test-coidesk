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

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                log.info("Enter CURRENCY USD, EUR, EEK etc ... or stop");
                String currency = scanner.nextLine();
                if (currency.equals("stop")) {
                    break;
                }
                fetch(currency.toUpperCase());
            } catch (Exception e) {
                log.info("Unsupported currency");
            }
        }
        scanner.close();
    }

    public void fetch(String currency) throws IOException {
        final CurrentRate currentPrice = service.getCurrentPrice(currency);
        final HistoricalRate historicalPrice = service.getHistorical(currency);

        log.info("The current Bitcoin rate: " + currentPrice.getRate() + " " + currentPrice.getCode());
        log.info("The highest Bitcoin rate in the last 180 days: " + historicalPrice.getMaxPrice().toString() + " " + currency);
        log.info("The lowest Bitcoin rate in the last 180 days: " + historicalPrice.getMinPrice().toString() + " " + currency);
    }
}
