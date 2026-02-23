package com.example.demo.Repository.Payment;

import com.example.demo.Repository.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    List<Payment> findByUserId(Long userId);
}
