package com.martindolezalek.routing.service;

import com.martindolezalek.routing.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class RoutingServiceTest {

    @Mock
    private CountryService countryService;

    @InjectMocks
    private RoutingService routingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindRouteSuccess() {
        Country cze = Country.builder()
                .name(Country.Name.builder()
                        .common("Czech Republic")
                        .official("Czech Republic")
                        .build())
                .cca3("CZE")
                .borders(List.of("AUT"))
                .build();
        Country aut = Country.builder()
                .name(Country.Name.builder()
                        .common("Austria")
                        .official("Austria")
                        .build())
                .cca3("AUT")
                .borders(Arrays.asList("CZE", "ITA"))
                .build();
        Country ita = Country.builder()
                .name(Country.Name.builder()
                        .common("Italy")
                        .official("Italy")
                        .build())
                .cca3("ITA")
                .borders(List.of("AUT"))
                .build();

        when(countryService.getCountryByCca3("CZE")).thenReturn(cze);
        when(countryService.getCountryByCca3("AUT")).thenReturn(aut);
        when(countryService.getCountryByCca3("ITA")).thenReturn(ita);

        List<String> expectedRoute = Arrays.asList("CZE", "AUT", "ITA");
        List<String> actualRoute = routingService.findRoute("CZE", "ITA");

        assertEquals(expectedRoute, actualRoute);
    }

    @Test
    void testFindRouteSameOriginAndDestination() {
        Country cze = Country.builder()
                .name(Country.Name.builder()
                        .common("Czech Republic")
                        .official("Czech Republic")
                        .build())
                .cca3("CZE")
                .borders(List.of("AUT"))
                .build();

        when(countryService.getCountryByCca3("CZE")).thenReturn(cze);

        List<String> expectedRoute = List.of("CZE");
        List<String> actualRoute = routingService.findRoute("CZE", "CZE");

        assertEquals(expectedRoute, actualRoute);
    }

    @Test
    void testFindRouteInvalidCountryCode() {
        when(countryService.getCountryByCca3("XYZ")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> routingService.findRoute("XYZ", "ITA"));
    }
}