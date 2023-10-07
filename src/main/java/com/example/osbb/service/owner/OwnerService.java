package com.example.osbb.service.owner;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.dto.pojo.Client;
import com.example.osbb.entity.Owner;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService implements IOwnerService {

    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------
    @Override
    @Transactional
    public Object createOwner(Owner owner) {
        try {
            List<String> errors = new ArrayList<>();
            if (ownerDAO.existsById(owner.getId())) {
                errors.add(ServiceMessages.ALREADY_EXISTS);
            }
           // активация собственника при создании
            owner.setActive(true);
            return !errors.isEmpty() ?
                    new ResponseMessages(errors)
                    : Response.builder()
                    .data(ownerDAO.save(owner))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwner(Owner owner) {
        try {
            List<String> errors = new ArrayList<>();
            return ownerDAO.existsById(owner.getId()) ?
                   Response.builder()
                           .data(ownerDAO.save(owner))
                           .messages(List.of(ServiceMessages.OK))
                           .build() : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getOwner(Long id) {
        try {
            return ownerDAO.existsById(id) ?
                    Response
                            .builder()
                            .data(ownerDAO.findById(id).orElse(new Owner()))
                            .messages(List.of(ServiceMessages.OK))
                            .build()
                    :
                    new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getOwnerByFullName(String fullName) {
        try {
            String[] fios = fullName.split(" ");
            return Response
                    .builder()
                    .data(ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteOwner(Long id) {
        try {
            if (ownerDAO.existsById(id)) {
                ownerDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of(ServiceMessages.OK))
                        .build();

            }
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // all -----------------------

    @Override
    @Transactional
    public Object createAllOwner(List<Owner> owners) {
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner one : owners) {
                if (!ownerDAO.existsById(one.getId())) {
                    // активация собственников при создании
                    one.setActive(true);
                    result.add(ownerDAO.save(one));
                }
            }
            return result.isEmpty() ?
                    new ResponseMessages(List
                            .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(sortedByLastName(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object updateAllOwner(List<Owner> owners) {
        try {
            List<Owner> result = new ArrayList<>();
            for (Owner contact : owners) {
                if (ownerDAO.existsById(contact.getId())) {
                    ownerDAO.save(contact);
                    result.add(contact);
                }
            }
            return result.isEmpty() ?
                    new ResponseMessages(List
                            .of(ServiceMessages.NOT_UPDATED))
                    :
                    Response
                            .builder()
                            .data(sortedByLastName(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public Object getAllOwner() {
        try {
            List<Client> result = ownerDAO.findAll()
                    .stream()
                    .map(s -> new Client(s, Double.parseDouble("0")))
//                    .filter(Client::isActive)
                    .sorted((a, b) -> a.getLastName().compareTo(b.getLastName()))
                    .toList();
            return result.isEmpty() ? new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    : Response
                    .builder()
                    .data(result)
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwner() {
        try {
            ownerDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // count ------------------------------
    @Override
    public Object countOwners() {
        long count = ownerDAO.count();
        try {
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }


    // sorted -------------------------------------------------------------
    private List<Owner> sortedByLastName(List<Owner> list) {
        return list.stream().sorted((a, b) -> a.getLastName().compareTo(b.getLastName())).collect(Collectors.toList());
    }

}
