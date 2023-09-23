package com.example.osbb.service;

import com.example.osbb.entity.Address;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface IEntityService {
    // ------------------- one -----------------------
    public Object createEntity(Object entity);

    public Object updateEntity(Object entity);

    public Object getEntity(Long id);

    public Object deleteEntity(Long id);

    // ------------------ all ----------------

    public Object createAllEntity(List<Object> list);

    public Object updateAllEntity(List<Object> list);

    public Object getAllEntity();

    public Object deleteAllEntity();
}
