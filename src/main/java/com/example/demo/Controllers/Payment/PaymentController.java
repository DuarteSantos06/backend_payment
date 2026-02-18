package com.example.demo.Controllers.Payment;

import com.example.demo.Service.Payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping()
    public ResponseEntity<?>createPayment(@RequestParam long amount, @RequestHeader("Authorization")String authHeader)throws Exception{
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        return ResponseEntity.ok(paymentService.createPayment(amount,token,"eur"));
    }
}
