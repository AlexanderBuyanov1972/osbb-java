package com.example.osbb.service.select;

import com.example.osbb.entity.Address;
import com.example.osbb.entity.Select;

import java.util.List;

public interface ISelectService {

    // ------------------- one -----------------------
    public Object createSelect(Select select);

    public Object updateSelect(Select select);

    public Object getSelect(Long id);

    public Object deleteSelect(Long id);

    // ------------------ all ----------------

    public Object createAllSelect(List<Select> selects);

    public Object updateAllSelect(List<Select> selects);

    public Object getAllSelect();

    public Object deleteAllSelect();

}
