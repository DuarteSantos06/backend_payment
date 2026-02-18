package com.example.demo.Service.Payment;

import com.example.demo.Repository.Entities.Payment;
import com.example.demo.Repository.Payment.PaymentRepository;
import com.example.demo.Repository.User.UserRepository;
import com.example.demo.Service.JWTService;
import com.example.demo.Service.User.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.server.ResponseStatusException;


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
        long id=jwtService.getIdByToken(token);

        if(userRepo.findById(id)==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
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
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        Payment payment=new Payment(id,amount,currency);
        paymentRepo.save(payment);
        return paymentIntent.getClientSecret();
    }
}
