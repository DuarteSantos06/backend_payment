package com.example.demo.Controllers.Payment;

import com.example.demo.Controllers.InputModels.PaymentRequest;
import com.example.demo.Service.Payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping()
    public ResponseEntity<String>createPayment(@RequestBody PaymentRequest request,
                                          @RequestHeader("Authorization")String authHeader){
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        String clientSecret = paymentService.createPayment(
                request.amount(),
                token,
                request.currency()
        );

        return ResponseEntity.ok(clientSecret);
    }
}
