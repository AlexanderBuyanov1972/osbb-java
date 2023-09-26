package com.example.osbb.service.ownership;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.Auth;
import com.example.osbb.dto.ErrorResponseMessages;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ResponseMessages;
import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnershipService implements IOwnershipService {

    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private OwnerDAO ownerDAO;
    @Autowired
    private AddressDAO addressDAO;

    // ------------- one --------------------

    @Override
    @Transactional
    public Object createOwnership(Ownership ownership) {
        try {
            List<String> errors = new ArrayList<>();
            if (ownershipDAO.existsById(ownership.getId()))
                errors.add("Недвижимость с таким ID уже существует.");
            if (addressDAO.existsByApartment(ownership.getAddress().getApartment())
                    && ownerDAO.existsByLastNameAndFirstNameAndSecondName(
                    ownership.getOwner().getLastName(),
                    ownership.getOwner().getFirstName(),
                    ownership.getOwner().getSecondName())) {
                errors.add("Недвижимость с помещением № " +
                        ownership.getAddress().getApartment() + "  и собственником " +
                        ownership.getOwner().getLastName() + " " + ownership.getOwner().getFirstName() + " " +
                        ownership.getOwner().getSecondName() + " уже существует.");

            }
            return errors.isEmpty() ? List.of(Response
                    .builder()
                    .data(ownershipDAO.save(ownership))
                    .messages(List.of("Объект недвижимости создан успешно.", "Удачного дня!"))
                    .build())
                    :
                    new ResponseMessages(errors);

        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwnership(Ownership one) {
        try {
            List<String> list = new ArrayList<>();
            if (!ownershipDAO.existsById(one.getId()))
                list.add("Недвижимость с таким ID не существует.");
            return list.isEmpty() ? Response
                    .builder()
                    .data(ownershipDAO.save(one))
                    .messages(List.of("Объект недвижимости обновлён успешно.", "Удачного дня!"))
                    .build()
                    :
                    new ResponseMessages(list);

        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object getOwnership(Long id) {
        try {
            return ownershipDAO.existsById(id) ?
                    Response
                            .builder()
                            .data(ownershipDAO.findById(id).get())
                            .messages(List.of("Объект недвижимости отправлен успешно.", "Удачного дня!"))
                            .build()
                    :
                    new ResponseMessages(List.of("Недвижимость с таким ID не существует."));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));

        }

    }


    @Override
    @Transactional
    public Object deleteOwnership(Long id) {
        try {
            if (ownershipDAO.existsById(id)) {
                ownershipDAO.deleteById(id);
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Недвижимость удаленна успешно.", "Удачного дня!"))
                        .build();
            }
            return new ResponseMessages(List.of("Недвижимость с таким ID не существует."));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // ------------------ all -------------------

    @Override
    @Transactional
    public Object createAllOwnership(List<Ownership> ownerships) {
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (!ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List.of(
                    "Ни один из объектов недвижимости создан не был. Недвижимость с такими ID уже существует."))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of("Успешно создано " + result.size() + " объектов недвижимости из " + ownerships.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllOwnership(List<Ownership> ownerships) {
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(List.of(
                    "Ни один из объектов недвижимости обновлён не был. Недвижимость с такими ID уже существует."))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of("Успешно обнавлено " + result.size() + " объектов недвижимости из " + ownerships.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        try {
            List<Ownership> result = ownershipDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of("В базе данных объектов недвижимости не существует."))
                    : Response
                    .builder()
                    .data(returnListSortedByApartment(result))
                    .messages(List.of("Список объектов недвижимости в количестве " + result.size() + " отправлен успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwnership() {
        try {
            ownershipDAO.deleteAll();
            return new ResponseMessages(List.of("Все объекты недвижимости удалены успешно.", "Удачного дня!"));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // ------------------ summa ----------------------

    @Override
    public Object summaAreaRooms() {
        try {
            double summa = ownershipDAO.findAll().stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь помещений дома составляет " + summa + " м2.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object summaAreaApartment() {
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь всех квартир дома составляет " + summa + " м2.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object summaAreaLivingApartment() {
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getLivingArea)
                    .sum();
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая жилая площадь всех квартир дома составляет " + summa + " м2.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object summaAreaNonResidentialRoom() {
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь всех нежилых помещений дома составляет " + summa + " м2.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    // --------------- count --------------------

    @Override
    public Object countRooms() {
        try {
            long count = ownershipDAO.count();
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех помещений дома составляет " + count + " единиц.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object countApartment() {
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT);
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех жилых помещений дома составляет " + count + " единиц.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object countNonResidentialRoom() {
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM);
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех нежилых помещений дома составляет " + count + " единиц.", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object getIdOwnershipByApartment(String apartment) {
        try {
            Long id = ownershipDAO.findAll()
                    .stream()
                    .filter((el -> el.getAddress()
                            .getApartment()
                            .equals(apartment)))
                    .findFirst()
                    .get()
                    .getId();
            return Response
                    .builder()
                    .data(id)
                    .messages(List.of("ID объекта недвижимости успешно райден.", "ID = " + id + ".", "Удачного дня!"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object getOwnersByApartment(String apartment) {
        try {
            List<String> list = ownershipDAO.findAll()
                    .stream()
                    .filter(s -> s.getAddress().getApartment().equals(apartment))
                    .map(el -> el.getOwner().getLastName() + " " + el.getOwner().getFirstName() + " " + el.getOwner().getSecondName())
                    .collect(Collectors.toList());
            list.add(0, "Собственниками помещения № " + apartment + " являются  " + list.size() + " человека : ");
            return Response
                    .builder()
                    .data(list)
                    .messages(list)
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // sorted ************************************************************
    private List<Ownership> returnListSortedById(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Ownership> returnListSortedByApartment(List<Ownership> list) {
        return list.stream().sorted((a, b) -> Integer.parseInt(a.getAddress().getApartment())
                - Integer.parseInt(b.getAddress().getApartment())).collect(Collectors.toList());
    }

}
