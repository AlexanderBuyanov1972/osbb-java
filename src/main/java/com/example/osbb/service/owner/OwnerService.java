package com.example.osbb.service.owner;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Owner;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.consts.ObjectMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService implements IOwnerService {
    @Autowired
    private OwnerDAO ownerDAO;

    // ---------------- one -----------------

    @Override
    public Object createOwner(Owner owner) {
        try {
            return ownerDAO.existsById(owner.getId()) ?
                    new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdAlreadyExists("Owner")))
                    :
                    List.of(ownerDAO.save(owner));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object updateOwner(Owner owner) {
        try {
            return !ownerDAO.existsById(owner.getId()) ?
                    new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Owner")))
                    :
                    List.of(ownerDAO.save(owner));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getOwner(Long id) {
        try {
            return ownerDAO.existsById(id) ?
                    List.of(ownerDAO.findById(id).get())
                    :
                    new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Owner")));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteOwner(Long id) {
        try {
            if (ownerDAO.existsById(id)) {
                ownerDAO.deleteById(id);
                return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
            }
            return new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Owner")));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // ------------------ all -----------------------

    @Override
    public Object createAllOwner(List<Owner> contacts) {
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner contact : contacts) {
                if (!ownerDAO.existsById(contact.getId())) {
                    ownerDAO.save(contact);
                    result.add(contact);
                }
            }
            return result.isEmpty() ?
                    new ErrorResponseMessages(List.of(ObjectMessages.noObjectCreated("Owner")))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object updateAllOwner(List<Owner> contacts) {
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner contact : contacts) {
                if (ownerDAO.existsById(contact.getId())) {
                    ownerDAO.save(contact);
                    result.add(contact);
                }
            }
            return result.isEmpty() ?
                    new ErrorResponseMessages(List.of(ObjectMessages.noObjectUpdated("Owner")))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllOwner() {
        try {
            List<Owner> result = ownerDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of(ObjectMessages.listEmpty()))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object deleteAllOwner() {
        try {
            ownerDAO.deleteAll();
            return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getListOwnerByListId(List<Long> list) {
        try {
            List<Owner> result = new ArrayList<>();
            list.forEach(el -> {
                if(ownerDAO.existsById(el)){
                    Owner owner =ownerDAO.findById(el).get();
                    result.add(owner);
                }
            });
            return result.isEmpty() ? new ResponseMessages(List.of("Лист собственников по листу id пустой"))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<Owner> returnListSorted(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

}
