package com.example.demo.Service.Payment;

import com.example.demo.Repository.Entities.Payment;
import com.example.demo.Repository.Entities.User;
import com.example.demo.Repository.Payment.PaymentRepository;
import com.example.demo.Repository.User.UserRepository;
import com.example.demo.Service.JWT.JWTService;
import com.example.demo.Service.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class PaymentService {

    public UserService userService;
    public PaymentRepository paymentRepo;
    public UserRepository userRepo;
    public JWTService jwtService;

    public PaymentService(UserService userService, PaymentRepository paymentRepo,UserRepository userRepo,JWTService jwtService) {
        this.userService = userService;
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public String createPayment(long amount,String token,String currency)throws Exception{
        long userId = jwtService.getIdByToken(token);

        userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        PaymentIntent paymentIntent = PaymentIntent.create(PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setPaymentMethod("pm_card_visa")
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .setAllowRedirects(
                                        PaymentIntentCreateParams
                                                .AutomaticPaymentMethods
                                                .AllowRedirects.NEVER
                                )
                                .build()
                )
                .build());

        paymentRepo.save(new Payment(userId, amount, currency));

        return paymentIntent.getClientSecret();
    }
}
