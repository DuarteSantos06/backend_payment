package com.example.demo.Controllers.Payment;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;


    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try{
            event=Webhook.constructEvent(payload,sigHeader,endpointSecret);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        return switch (event.getType()) {
            case "payment_intent.succeeded" -> ResponseEntity.ok("Success");
            case "payment_intent.payment_failed" -> ResponseEntity.badRequest().body("Payment failed");
            default -> ResponseEntity.badRequest().body("Invalid event");
        };
    }

}
