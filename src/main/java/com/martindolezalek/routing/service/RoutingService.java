package com.martindolezalek.routing.service;

import com.martindolezalek.routing.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class RoutingService {

    @Autowired
    private CountryService countryService;

    public List<String> findRoute(String originCca3, String destinationCca3) {
        Country origin = countryService.getCountryByCca3(originCca3);
        Country destination = countryService.getCountryByCca3(destinationCca3);

        if (origin == null || destination == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid country code.");
        }

        if (origin.equals(destination)) {
            return Collections.singletonList(originCca3);
        }

        Queue<List<String>> queue = new LinkedList<>();
        queue.add(Collections.singletonList(originCca3));
        Set<String> visited = new HashSet<>();
        visited.add(originCca3);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String lastCountryCode = path.getLast();
            Country lastCountry = countryService.getCountryByCca3(lastCountryCode);

            for (String neighbor : lastCountry.getBorders()) {
                if (visited.contains(neighbor)) continue;

                List<String> newPath = new ArrayList<>(path);
                newPath.add(neighbor);

                if (neighbor.equals(destinationCca3)) {
                    return newPath;
                }

                queue.add(newPath);
                visited.add(neighbor);
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No land route found.");
    }
}