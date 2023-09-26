package com.example.osbb.service.rate;

import com.example.osbb.entity.Rate;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IRateService {

    // ----- one -----
    @Transactional
    Object createRate(Rate rate);

    @Transactional
    Object updateRate(Rate rate);

    Object getRate(Long id);

    Object deleteRate(Long id);

    // ----- all -----

    Object createAllRate(List<Rate> list);

    Object updateAllRate(List<Rate> list);

    Object getAllRate();

    Object deleteAllRate();


}
