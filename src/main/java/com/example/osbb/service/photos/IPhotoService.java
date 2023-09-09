package com.example.osbb.service.photos;

import com.example.osbb.entity.Address;
import com.example.osbb.entity.Photo;

import java.util.List;

public interface IPhotoService {
    // ------------------- one -----------------------
    public Object createPhoto(Photo photo);

    public Object updatePhoto(Photo photo);

    public Object getPhoto(Long id);

    public Object deletePhoto(Long id);

    // ------------------ all ----------------

    public Object createAllPhoto(List<Photo> photos);

    public Object updateAllPhoto(List<Photo> photos);

    public Object getAllPhoto();

    public Object deleteAllPhoto();
}
