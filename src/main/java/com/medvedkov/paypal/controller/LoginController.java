package com.medvedkov.paypal.controller;

import com.medvedkov.paypal.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping(value = "/login/local")
    public void loginViaResourceAccount(User user){
        System.out.println(user);
    }

    @RequestMapping(value = "/google")
    public void loginViaGoogle() {

    }
}
