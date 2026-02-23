package com.example.demo.Service.Payment;

import com.example.demo.Exceptions.InvalidAmountException;
import com.example.demo.Exceptions.PaymentProcessingException;
import com.example.demo.Exceptions.UserDoesNotExistException;
import com.example.demo.Repository.Entities.Payment;
import com.example.demo.Repository.Entities.User;
import com.example.demo.Repository.Payment.PaymentRepository;
import com.example.demo.Repository.User.UserRepository;
import com.example.demo.Service.DTO.PaymentDTO;
import com.example.demo.Service.JWT.JWTService;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.List;
import java.util.Optional;


@Service
public class PaymentService {

    public final PaymentRepository paymentRepo;
    public final UserRepository userRepo;
    public final JWTService jwtService;

    public PaymentService( PaymentRepository paymentRepo,UserRepository userRepo,JWTService jwtService) {
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public String createPayment(long amount,
                                String token,
                                String currency
            )throws UserDoesNotExistException,InvalidAmountException,PaymentProcessingException {

        long userId = jwtService.getIdByToken(token);
        if(amount<=0){
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        userRepo.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User doesn't exist"));

        PaymentIntent paymentIntent;
        try{
            paymentIntent = PaymentIntent.create(PaymentIntentCreateParams.builder()
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
        }catch(StripeException e){
            throw new PaymentProcessingException("Payment provider error");
        }

        paymentRepo.save(new Payment(userId, amount, currency));

        return paymentIntent.getClientSecret();
    }


    public List<PaymentDTO> getAllPaymentsById(String token){
        long userId = jwtService.getIdByToken(token);
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){
            throw new UserDoesNotExistException("User doesn't exist");
        }

        return paymentRepo.findByUserId(userId)
                .stream()
                .map(p -> new PaymentDTO(
                        p.getId(),
                        p.getAmount(),
                        p.getCurrency(),
                        p.getTimestamp()
                ))
                .toList();
    }
}
