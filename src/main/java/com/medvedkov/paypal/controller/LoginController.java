package com.medvedkov.paypal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @RequestMapping(value = "/")
    public void loginViaResourceAccount(){

    }

    @RequestMapping(value = "/google")
    public void loginViaGoogle() {

    }
}
