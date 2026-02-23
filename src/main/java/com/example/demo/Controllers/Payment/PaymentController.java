package com.example.demo.Controllers.Payment;

import com.example.demo.Controllers.InputModels.PaymentRequest;
import com.example.demo.Service.DTO.PaymentDTO;
import com.example.demo.Service.Payment.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        String clientSecret = paymentService.createPayment(
                request.amount(),
                token,
                request.currency()
        );

        return ResponseEntity.ok(clientSecret);
    }

    @GetMapping()
    public ResponseEntity<List<PaymentDTO>>getAllPaymentsById(@RequestHeader("Authorization")String authHeader){
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token=authHeader.substring(7);

        return ResponseEntity.ok(paymentService.getAllPaymentsById(token));
    }
}
