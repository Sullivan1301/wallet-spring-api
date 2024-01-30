package org.example.walletapi.controller;
//This is a controller for having notion with postman, it's not really for the Wallet

import org.springframework.web.bind.annotation.*;

public class HelloController {
    @GetMapping("/account/{id}")
    public String accountById(@PathVariable(name = "id") String accountId) {
        return "Account ID = " + accountId;
    }

    @GetMapping("/hello")
    public String ping(@RequestParam(required = false) Integer page,
                       @RequestParam(required = false) Integer size) {
        return "This is a GET request with params page = " + page + " and size=" + size;
    }

    @PostMapping("/hello")
    public String postHello(@RequestBody(required = false) String input) {
        return "This is a POST request and this is the input : " + input;
    }

    @PutMapping("/hello")
    public String putHello() {
        return "This is a PUT request";
    }

    @PatchMapping("/hello")
    public String patchtHello() {
        return "This is a PATCH request";
    }

    @DeleteMapping("/hello")
    public String deletetHello() {
        return "This is a DELETE request";
    }
}
