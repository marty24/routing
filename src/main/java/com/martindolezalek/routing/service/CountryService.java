package com.martindolezalek.routing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martindolezalek.routing.model.Country;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CountryService {
    private List<Country> countries;

    @PostConstruct
    private void init() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Country>> typeReference = new TypeReference<List<Country>>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/countries.json");
        try {
            countries = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Country getCountryByCca3(String cca3) {
        return countries.stream()
                .filter(country -> country.getCca3().equalsIgnoreCase(cca3))
                .findFirst()
                .orElse(null);
    }
}
