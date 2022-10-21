package com.example.KuehneNagel.service;

import com.example.KuehneNagel.model.CurrentRate;
import com.example.KuehneNagel.model.HistoricalRate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface BitcoinService {

    CurrentRate getCurrentPrice(String currency) throws IOException;

    HistoricalRate getHistorical(String currency) throws IOException;
}
