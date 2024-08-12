package com.example.osbb.service.rate;

import com.example.osbb.entity.Rate;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRateService {

    // ----- one -----
    @Transactional
    ResponseEntity<?> createRate(Rate rate);

    @Transactional
    ResponseEntity<?> updateRate(Rate rate);

    ResponseEntity<?> getRate(Long id);

    ResponseEntity<?> deleteRate(Long id);

    // ----- all -----

    ResponseEntity<?> createAllRate(List<Rate> list);

    ResponseEntity<?> updateAllRate(List<Rate> list);

    ResponseEntity<?> getAllRate();

    ResponseEntity<?> deleteAllRate();


}
