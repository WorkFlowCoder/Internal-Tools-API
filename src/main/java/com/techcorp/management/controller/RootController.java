package com.techcorp.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    // Puisque ton context-path est déjà /api, 
    // mapper sur "" ou "/" signifie l'URL racine de l'API.
    @GetMapping({"", "/"})
    public String sayBonjour() {
        return "Bonjour";
    }
}