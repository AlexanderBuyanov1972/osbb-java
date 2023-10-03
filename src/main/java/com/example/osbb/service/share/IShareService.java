package com.example.osbb.service.share;

import com.example.osbb.entity.Share;

import java.util.List;

public interface IShareService {
    // ------------------- one -----------------------
    public Object createShare(Share share);

    public Object updateShare(Share share);

    public Object getShare(Long id);

    public Object deleteShare(Long id);

    // ------------------ all ----------------

    public Object createAllShare(List<Share> list);

    public Object updateAllShare(List<Share> list);

    public Object getAllShare();

    public Object deleteAllShare();
    // other ----------------------
    public Object getShareByApartmentAndFullName(String apartment, String fullName);
}
