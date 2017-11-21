package com.medvedkov.paypal.controller;

import com.medvedkov.paypal.security.SignInAdapterImpl;
import com.paypal.api.payments.*;
import com.paypal.base.exception.PayPalException;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/payment")
@PreAuthorize("hasAuthority('STANDARD_USER')")
public class PayPalController {

    @Value("${client_id}")
    private String CLIENT_ID;
    @Value("${client_secret}")
    private String CLIENT_SECRET;
    @Value("${mode}")
    private String MODE;
    private static String paypalRedirectLink;
    private static final Logger logger = LoggerFactory.getLogger(SignInAdapterImpl.class);

    @RequestMapping("/create")
    public ResponseEntity<Object> create(@RequestParam String total) {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(total);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("This is the payment transaction description.");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/payment/create/");
        redirectUrls.setReturnUrl("http://localhost:8080/payment/execute/");
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = null;
        try {
            createdPayment = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            logger.error(e.getMessage());
        }

        Iterator<Links> links = createdPayment.getLinks().iterator();
        while (links.hasNext()) {
            Links link = links.next();
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                paypalRedirectLink = link.getHref();
            }
        }

        HttpHeaders redirectHeaders = new HttpHeaders();
        redirectHeaders.add("Location", paypalRedirectLink);


        ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(redirectHeaders, HttpStatus.FOUND);

        return objectResponseEntity;
    }

    @RequestMapping("/execute")

    public String execute(@RequestParam(value = "paymentId") String paymentId, @RequestParam(value = "token") String token, @RequestParam(value = "PayerID") String payerID) {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerID);

        Payment executedPayment = null;
        try {
            executedPayment = payment.execute(apiContext, paymentExecute);
        } catch (PayPalRESTException e) {
            logger.error(e.getMessage());
        }
        return "sucess";
    }

}




