package com.example.osbb.service.ownership;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.dto.pojo.Room;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
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
    private RecordDAO recordDAO;
    @Autowired
    private OwnerDAO ownerDAO;

    // one --------------------------------------------

    @Override
    @Transactional
    public Object createOwnership(Ownership ownership) {
        try {
            List<String> errors = new ArrayList<>();
            if (ownershipDAO.existsById(ownership.getId()))
                errors.add(ServiceMessages.ALREADY_EXISTS);
            if (ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment()))
                errors.add("Помещение с таким APARTMENT уже существует");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(ownershipDAO.save(ownership))
                    .messages(List.of(ServiceMessages.OK))
                    .build()
                    :
                    new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwnership(Ownership ownership) {
        try {
            List<String> errors = new ArrayList<>();
            if (!ownershipDAO.existsById(ownership.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            if (!ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment()))
                errors.add("Помещение с таким APARTMENT не существует");
            return errors.isEmpty() ? Response
                    .builder()
                    .data(ownershipDAO.save(ownership))
                    .messages(List.of(ServiceMessages.OK))
                    .build()
                    :
                    new ResponseMessages(errors);

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
                            .data(ownershipDAO.findById(id).orElse(new Ownership()))
                            .messages(List.of(ServiceMessages.OK))
                            .build()
                    :
                    new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));

        }

    }

    @Override
    public Object getOwnershipByApartment(String apartment) {
        try {
            return Response
                    .builder()
                    .data(ownershipDAO.findByAddressApartment(apartment))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getPersonalAccountByApartment(String apartment) {
        try {
            return Response
                    .builder()
                    .data(ownershipDAO.findByAddressApartment(apartment).getPersonalAccount())
                    .messages(List.of(ServiceMessages.OK))
                    .build();
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
                        .messages(List.of(ServiceMessages.OK))
                        .build();
            }
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // all -------------------------------------

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
                    ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(sortedById(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(sortedById(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        try {
            List<Room> result = ownershipDAO.findAll().stream()
                    .map(s -> new Room(s, 0.00))
                    .sorted(comparatorByApartment())
                    .toList();
            return result.isEmpty() ? new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    : Response
                    .builder()
                    .data(result)
                    .messages(List.of(ServiceMessages.OK, "Получено " + result.size() + " помещений"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    private Comparator<Room> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment());
    }


    @Override
    @Transactional
    public Object deleteAllOwnership() {
        try {
            ownershipDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // summa ----------------------

    @Override
    public Object summaAreaRooms() {
        try {
            double summa = ownershipDAO.findAll().stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Общая площадь помещений дома составляет " + summa + " м2."))
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
                    .messages(List.of("Общая площадь всех квартир дома составляет " + summa + " м2."))
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
                    .messages(List.of("Общая жилая площадь всех квартир дома составляет " + summa + " м2."))
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
                    .messages(List.of("Общая площадь всех нежилых помещений дома составляет " + summa + " м2."))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    // count --------------------

    @Override
    public Object countRooms() {
        try {
            long count = ownershipDAO.count();
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of("Общее количество всех помещений дома составляет " + count + " единиц."))
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
                    .messages(List.of("Общее количество всех жилых помещений дома составляет " + count + " единиц."))
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
                    .messages(List.of("Общее количество всех нежилых помещений дома составляет " + count + " единиц."))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }


    // room --------------------------------
    @Override
    public Object getRoomByApartment(String apartment) {
        try {
            return Response
                    .builder()
                    .data(new Room(ownershipDAO.findByAddressApartment(apartment), 0.00))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getListApartmentsByFullName(String fullName) {
        try {
            String[] fios = fullName.split(" ");
            Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);

            List<String> result = recordDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == owner.getId())
                    .map(s -> "Квартира № " + s.getOwnership().getAddress().getApartment())
                    .collect(Collectors.toList());
            if(result.isEmpty()){
                owner.setActive(false);
                ownerDAO.save(owner);
            }


            return Response
                    .builder()
                    .data(result.isEmpty() ?
                            List.of("По данному собственнику не зарегистрировано ни одно помещение")
                            : result)
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // sorted -----------------------------------
    private List<Ownership> sortedById(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

}
