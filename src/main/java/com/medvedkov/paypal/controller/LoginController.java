package com.medvedkov.paypal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Controller
public class LoginController {

    @RequestMapping(value = "/signin")
    public String signin() {
        return "login.html";
    }

//    @PostMapping(value = "/local")
//    public void loginViaResourceAccount(@RequestBody String userName, @RequestBody String password){
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken(userName,
//                        null, /*Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER"))*/null));
//    }

//    @RequestMapping(value = "/google")
//    public void loginViaGoogle() {
//
//    }
}
