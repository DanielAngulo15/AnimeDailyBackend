package com.animedaily.animedailybackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HomeController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

@GetMapping(value = "/", produces = "application/json")
public List<Map<String, Object>> listAllRoutes() {
    return requestMappingHandlerMapping.getHandlerMethods()
        .keySet()
        .stream()
        .map(mapping -> Map.of(
            "ruta", mapping.getPatternValues().stream().findFirst().orElse(""), // Toma el primer valor
            "metodos", mapping.getMethodsCondition().getMethods()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList())
        ))
        .collect(Collectors.toList());
}
}