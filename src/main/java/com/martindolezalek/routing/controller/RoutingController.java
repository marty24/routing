package com.martindolezalek.routing.controller;

import com.martindolezalek.routing.service.RoutingService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoutingController {

    @Autowired
    private RoutingService routingService;

    @GetMapping("/routing/{origin}/{destination}")
    public RouteResponse getRoute(@PathVariable String origin, @PathVariable String destination) {
        List<String> route = routingService.findRoute(origin, destination);
        return new RouteResponse(route);
    }

    @Getter
    @Setter
    private static class RouteResponse {
        private List<String> route;

        public RouteResponse(List<String> route) {
            this.route = route;
        }
    }
}