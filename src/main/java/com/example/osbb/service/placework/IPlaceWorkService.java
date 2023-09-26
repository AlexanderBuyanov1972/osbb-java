package com.example.osbb.service.placework;

import com.example.osbb.entity.PlaceWork;
import com.example.osbb.entity.Rate;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IPlaceWorkService {

    // ----- one -----
    Object createPlaceWork(PlaceWork placeWork);

    Object updatePlaceWork(PlaceWork placeWork);

    Object getPlaceWork(Long id);

    Object deletePlaceWork(Long id);

    // ----- all -----

    Object createAllPlaceWork(List<PlaceWork> list);

    Object updateAllPlaceWork(List<PlaceWork> list);

    Object getAllPlaceWork();

    Object deleteAllPlaceWork();
}
