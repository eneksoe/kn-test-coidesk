package com.example.KuehneNagel.service.impl;

import com.example.KuehneNagel.model.CurrentRate;
import com.example.KuehneNagel.model.HistoricalRate;
import com.example.KuehneNagel.service.BitcoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class BitcoinServiceImpl implements BitcoinService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper mapper;

    private final static String CURRENT_RATE_URL = "https://api.coindesk.com/v1/bpi/currentprice/%s.json";

    private final static String HISTORICAL_RATE_URL = "https://api.coindesk.com/v1/bpi/historical/close.json?start=%s&end=%s&currency=%s";

    private final static LocalDate START_DATE = LocalDate.now().minusDays(180);

    private final static LocalDate END_DATE = LocalDate.now();


    @Override
    public CurrentRate getCurrentPrice(String currency) throws IOException {

        ResponseEntity<String> response = restTemplate.getForEntity(String.format(CURRENT_RATE_URL, currency), String.class);

        JsonNode responseBody = mapper.readTree(response.getBody());
        JsonNode getResponse = responseBody.path("bpi").get(currency);

        return mapper.convertValue(getResponse, CurrentRate.class);
    }

    @Override
    public HistoricalRate getHistorical(String currency) throws IOException {

        ResponseEntity<String> response = restTemplate.getForEntity(
                String.format(HISTORICAL_RATE_URL, START_DATE, END_DATE, currency), String.class);

        JsonNode responseBody = mapper.readTree(response.getBody());
        Iterator<JsonNode> getResponse = responseBody.path("bpi").iterator();
        Iterable<JsonNode> iterable = () -> getResponse;
        List<Double> doubles = new ArrayList<>();
        StreamSupport.stream(iterable.spliterator(), false).mapToDouble(JsonNode::asDouble)
                .forEach(doubles::add);
        Double max = Collections.max(doubles);
        Double min = Collections.min(doubles);


        return HistoricalRate.builder()
                .maxPrice(max)
                .minPrice(min)
                .build();
    }

}
