package com.example.osbb.service.ownership;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnershipService implements IOwnershipService {
    private static final Logger log = Logger.getLogger(IOwnershipService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
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
        String methodName = "createOwnership";
        String messageIsExists = "Помещение с номером лицевого счёта " + ownership.getBill() + " уже существует";
        String messageSuccessfully = "Помещение создано успешно";

        log.info(messageEnter(methodName));
        try {
            if (ownershipDAO.existsByBill(ownership.getBill())) {
                log.info(messageIsExists);
                log.info(messageExit(methodName));
                return new ResponseMessages(List.of(messageIsExists));
            }
            ownership = ownershipDAO.save(ownership);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwnership(Ownership ownership) {
        String methodName = "updateOwnership";
        String messageId = "Помещение с ID : " + ownership.getId() + " не существует";
        String messageApartment = "Помещение с номером помещения " + ownership.getAddress().getApartment() + " не существует";
        String messageBill = "Помещение с номером лицевого счёта " + ownership.getBill() + " не существует";
        String messageSuccessfully = "Помещение обновлено успешно";

        log.info(messageEnter(methodName));
        try {
            List<String> errors = new ArrayList<>();
            if (!ownershipDAO.existsById(ownership.getId())) {
                log.info(messageId);
                errors.add(messageId);
            }
            if (!ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment())) {
                log.info(messageApartment);
                errors.add(messageApartment);
            }
            if (!ownershipDAO.existsByBill(ownership.getBill())) {
                log.info(messageBill);
                errors.add(messageBill);
            }
            if (!errors.isEmpty()) {
                log.info(messageExit(methodName));
                return new ResponseMessages(errors);
            }
            ownership = ownershipDAO.save(ownership);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getOwnership(Long id) {
        String methodName = "getOwnership";
        String messageIdNotExists = "Помещение с ID : " + id + " не существует";
        String messageSuccessfully = "Помещение с ID : " + id + " получено успешно";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            Ownership ownership = ownershipDAO.findById(id).orElse(null);
            messageResponse = ownership == null ? messageIdNotExists : messageSuccessfully;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(ownership)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllBillByApartment(String apartment) {
        String methodName = "getAllBillByApartment";
        String messageNotExists = "По помещению № : " + apartment + " лицевые счёта не найдены";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            List<String> bills = ownershipDAO.findByAddressApartment(apartment).stream().map(Ownership::getBill).toList();
            messageResponse = bills.isEmpty() ? messageNotExists :
                    "По помещению № : " + apartment + " найдено " + bills.size() + " лицевых счетов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(bills)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (
                Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }


    @Override
    @Transactional
    public Object deleteOwnership(Long id) {
        String methodName = "deleteOwnership";
        String messageSuccessfully = "Собственность удалена успешно";
        String messageResponse = "Собственность с ID : " + id + " не существует";

        log.info(messageEnter(methodName));
        try {
            if (ownershipDAO.existsById(id)) {
                ownershipDAO.deleteById(id);
                messageResponse = messageSuccessfully;
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(id)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // all -------------------------------------

    @Override
    @Transactional
    public Object createAllOwnership(List<Ownership> ownerships) {
        String methodName = "createAllOwnership";
        String messageNotCreate = "Не создано ни одного объекта собственности";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (!ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            messageResponse = result.isEmpty() ? messageNotCreate :
                    "Создано " + result.size() + " объектов собственности";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(sortedById(result))
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllOwnership(List<Ownership> ownerships) {
        String methodName = "updateAllOwnership";
        String messageNotUpdate = "Не обновлено ни одного объекта собственности";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            messageResponse = result.isEmpty() ? messageNotUpdate : "Обновлено " + result.size() + " помещений";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(sortedById(result))
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        String methodName = "getAllOwnership";
        String messageNotExists = "Не получено ни одного объекта собственности";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            List<Ownership> result = ownershipDAO.findAll()
                    .stream()
                    .sorted(comparatorOwnershipByApartment())
                    .toList();
            messageResponse = result.isEmpty() ? messageNotExists : "Получено " + result.size() + " помещений";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwnership() {
        String methodName = "deleteAllOwnership";
        String messageSuccessfully = "Все объекты собственности успешно удалены";

        log.info(messageEnter(methodName));
        try {
            ownershipDAO.deleteAll();
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new ResponseMessages(List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // summa ----------------------

    @Override
    public Object summaAreaRooms() {
        String methodName = "summaAreaRooms";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            messageSuccessfully = "Общая площадь помещений дома составляет " + summa + " м2.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object summaAreaApartment() {
        String methodName = "summaAreaApartment";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            messageSuccessfully = "Общая площадь всех квартир дома составляет " + summa + " м2.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object summaAreaLivingApartment() {
        String methodName = "summaAreaLivingApartment";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getLivingArea)
                    .sum();
            messageSuccessfully = "Общая жилая площадь всех квартир дома составляет " + summa + " м2.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object summaAreaNonResidentialRoom() {
        String methodName = "summaAreaNonResidentialRoom";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            messageSuccessfully = "Общая площадь всех нежилых помещений дома составляет " + summa + " м2.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // count --------------------

    @Override
    public Object countRooms() {
        String methodName = "countRooms";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            long count = ownershipDAO.count();
            messageSuccessfully = "Общее количество всех помещений дома составляет " + count + " единиц.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object countApartment() {
        String methodName = "countApartment";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT);
            messageSuccessfully = "Общее количество всех жилых помещений дома составляет " + count + " единиц.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object countNonResidentialRoom() {
        String methodName = "countNonResidentialRoom";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM);
            messageSuccessfully = "Общее количество всех нежилых помещений дома составляет " + count + " единиц.";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getAllOwnershipByApartment(String apartment) {
        String methodName = "getOwnershipsByApartment";
        String messageNotExists = "Объекты недвижимости по помещению № : " + apartment + " не зарегистрированы";
        String messageSuccessfully = "Объекты недвижимости с помещением № : " + apartment + " получено успешно";

        log.info(messageEnter(methodName));
        try {
            List<Ownership> ownerships = ownershipDAO.findByAddressApartment(apartment)
                    .stream().sorted(comparatorOwnershipByBill()).collect(Collectors.toList());
            String messageResponse = ownerships.isEmpty() ? messageNotExists : messageSuccessfully;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(ownerships)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllApartmentByFullName(String fullName) {
        String methodName = "getListApartmentsByFullName";
        String messageNotOwnership = "По данному Ф.И.О. : " + fullName + " объектов недвижимости не найдено";
        String messageNotOwner = "По данному Ф.И.О. : " + fullName + " собственник не найден";
        String messageResponse = "";

        log.info(messageEnter(methodName));
        try {
            String[] fios = fullName.split(" ");
            Owner owner = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2]);
            if (owner == null) {
                log.info(messageNotOwner);
                log.info(messageExit(methodName));
                return new ResponseMessages(List.of(messageNotOwner));
            }
            List<String> result = recordDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == owner.getId())
                    .map(s -> "Помещение № " + s.getOwnership().getAddress().getApartment())
                    .collect(Collectors.toList());
            if (result.isEmpty()) {
                owner.setActive(false);
                ownerDAO.save(owner);
                log.info(messageNotOwnership);
                log.info(messageExit(methodName));
                return new ResponseMessages(List.of(messageNotOwnership));
            }
            messageResponse = "Получено " + result.size() + " записей";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(result)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // sorted -----------------------------------
    private List<Ownership> sortedById(List<Ownership> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    //.sorted(comparatorByApartment())
    private Comparator<Ownership> comparatorOwnershipByApartment() {
        return (a, b) -> Integer.parseInt(a.getAddress().getApartment())
                - Integer.parseInt(b.getAddress().getApartment());
    }

    //.sorted(comparatorByBill())
    private Comparator<Ownership> comparatorOwnershipByBill() {
        return (a, b) -> Integer.parseInt(a.getBill())
                - Integer.parseInt(b.getBill());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
