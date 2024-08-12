package com.example.osbb.service.placework;

import com.example.osbb.entity.owner.PlaceWork;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPlaceWorkService {

    // ----- one -----
    ResponseEntity<?> createPlaceWork(PlaceWork placeWork);

    ResponseEntity<?> updatePlaceWork(PlaceWork placeWork);

    ResponseEntity<?> getPlaceWork(Long id);

    ResponseEntity<?> deletePlaceWork(Long id);

    // ----- all -----

    ResponseEntity<?> createAllPlaceWork(List<PlaceWork> list);

    ResponseEntity<?> updateAllPlaceWork(List<PlaceWork> list);

    ResponseEntity<?> getAllPlaceWork();

    ResponseEntity<?> deleteAllPlaceWork();
}
