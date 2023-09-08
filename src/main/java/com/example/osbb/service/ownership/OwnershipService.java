package com.example.osbb.service.ownership;

import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import com.example.osbb.consts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnershipService implements IOwnershipService {

    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private OwnerDAO ownerDAO;

    // ------------- one --------------------

    @Override
    public Object createOwnership(Ownership ownership) {
        try {
            List<String> list = new ArrayList<>();
            if (ownershipDAO.existsById(ownership.getId()))
                list.add("Недвижимость с таким ID уже существует.");
            return list.isEmpty() ?
                    List.of(ownershipDAO.save(ownership))
                    :
                    new ErrorResponseMessages(list);

        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object updateOwnership(Ownership one) {
        try {
            List<String> list = new ArrayList<>();
            if (!ownershipDAO.existsById(one.getId()))
                list.add("Недвижимость с таким ID не существует.");
            return list.isEmpty() ?
                    List.of(Response
                            .builder()
                            .data(List.of(ownershipDAO.save(one)))
                            .messages(List.of("Объект недвижимости обновлён успешно.", "Удачного дня!"))
                            .build())
                    :
                    new ErrorResponseMessages(list);

        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object getOwnership(Long id) {
        try {
            return ownershipDAO.existsById(id) ?
                    List.of(Response
                            .builder()
                            .data(List.of(ownershipDAO.findById(id).get()))
                            .messages(List.of("Объект недвижемости отправлен успешно.", "Удачного дня!"))
                            .build())
                    :
                    new ErrorResponseMessages(List.of("Недвижимость с таким ID не существует."));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }


    @Override
    public Object deleteOwnership(Long id) {
        try {
            if (ownershipDAO.existsById(id)) {
                ownershipDAO.deleteById(id);
                return new ResponseMessages(List.of("Недвижимость удаленна успешно."));
            }
            return new ErrorResponseMessages(List.of("Недвижимость с таким ID не существует."));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // ------------------ all -------------------

    @Override
    public Object createAllOwnership(List<Ownership> list) {
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : list) {
                if (!ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ErrorResponseMessages(List.of(
                    "Ни один из объектов недвижимости создан не был. Недвижимость с такими ID уже существует."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object updateAllOwnership(List<Ownership> list) {
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : list) {
                if (ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ErrorResponseMessages(List.of(
                    "Ни один из объектов недвижимости обновлён не был. Недвижимость с такими ID не существует."))
                    : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        try {
            List<Ownership> result = ownershipDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных объектов недвижимости не существует."))
                    : List.of(Response
                    .builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Список объектов недвижимости отправлен успешно.", "Удачного дня!"))
                    .build());
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deleteAllOwnership() {
        try {
            ownershipDAO.deleteAll();
            return new ResponseMessages(List.of("Объекты недвижимости удалены успешно."));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // ------------------ summa ----------------------

    @Override
    public Object summaAreaRooms() {
        try {
            List<Ownership> list = ownershipDAO.findAll();
            return list.isEmpty() ? 0 : list.stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object summaAreaApartment() {
        List<Ownership> list = ownershipDAO.findAll();
        try {
            return list.isEmpty() ? 0 : list.stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object summaAreaLivingApartment() {
        List<Ownership> list = ownershipDAO.findAll();
        try {
            return list.isEmpty() ? 0 : list.stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getLivingArea)
                    .sum();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object summaAreaNonResidentialRoom() {
        try {
            List<Ownership> list = ownershipDAO.findAll();
            return list.isEmpty() ? 0 : list.stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    // --------------- count --------------------

    @Override
    public Object countRooms() {
        try {
            List<Ownership> list = ownershipDAO.findAll();
            return list.isEmpty() ? 0 : list.size();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object countApartment() {
        try {
            return ownershipDAO.findAll().isEmpty() ? 0 : ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object countNonResidentialRoom() {
        try {
            return ownershipDAO.findAll().isEmpty() ? 0 : ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    private List<Ownership> returnListSorted(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Owner> returnListSortedOwner(List<Owner> list) {
        return list.stream().sorted(Comparator.comparing(Owner::getLastName)).collect(Collectors.toList());
    }

    private Owner createOwner(Owner one) {
        return Owner
                .builder()
                .id(one.getId())
                .firstName(one.getFirstName())
                .secondName(one.getSecondName())
                .lastName(one.getLastName())
                .gender(one.getGender())
                .email(one.getEmail())
                .phoneNumber(one.getPhoneNumber())
                .password(one.getPassword())
                .build();
    }
}
