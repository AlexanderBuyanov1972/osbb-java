package com.example.osbb.service.ownership;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.IdAndBill;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.Survey;
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
import java.util.Map;
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
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String messageResponse = "Помещение с номером лицевого счёта " + ownership.getBill() + " уже существует";
        log.info(messageEnter(methodName));
        try {
            if (!ownershipDAO.existsByBill(ownership.getBill())) {
                ownership = ownershipDAO.save(ownership);
                messageResponse = "Помещение № " + ownership.getAddress().getApartment()+ " создано успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(ownership, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateOwnership(Ownership ownership) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageId = "Помещение с ID : " + ownership.getId() + " не существует";
        String messageApartment = "Помещение с номером помещения " + ownership.getAddress().getApartment() + " не существует";
        String messageBill = "Помещение с номером лицевого счёта " + ownership.getBill() + " не существует";
        String messageSuccessfully = "Помещение обновлено успешно";

        log.info(messageEnter(methodName));
        try {
            if (!ownershipDAO.existsById(ownership.getId())) {
                log.info(messageId);
                return new Response(List.of(messageId));
            }
            if (!ownershipDAO.existsByAddressApartment(ownership.getAddress().getApartment())) {
                log.info(messageApartment);
                return new Response(List.of(messageApartment));
            }
            if (!ownershipDAO.existsByBill(ownership.getBill())) {
                log.info(messageBill);
                return new Response(List.of(messageBill));
            }
            ownership = ownershipDAO.save(ownership);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(ownership, List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object getOwnership(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Помещение с ID : " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            Ownership ownership = ownershipDAO.findById(id).orElse(null);
            messageResponse = ownership == null ? messageResponse :
                    "Помещение № " + ownership.getAddress().getApartment() + " получено успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(ownership, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    @Transactional
    public Object deleteOwnership(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Собственность с ID : " + id + " не существует";

        log.info(messageEnter(methodName));
        try {
            if (ownershipDAO.existsById(id)) {
                ownershipDAO.deleteById(id);
                messageResponse = "Собственность удалена успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id, List.of(messageResponse));
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
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не создано ни одного объекта собственности";
        log.info(messageEnter(methodName));
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (!ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse :
                    "Создано " + result.size() + " объектов собственности";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedById(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllOwnership(List<Ownership> ownerships) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не обновлено ни одного объекта собственности";
        log.info(messageEnter(methodName));
        try {
            List<Ownership> result = new ArrayList<>();
            for (Ownership one : ownerships) {
                if (ownershipDAO.existsById(one.getId())) {
                    ownershipDAO.save(one);
                    result.add(one);
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Обновлено " + result.size() + " помещений";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(sortedById(result), List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не получено ни одного объекта собственности";
        log.info(messageEnter(methodName));
        try {
            List<Ownership> result = ownershipDAO.findAll()
                    .stream()
                    .sorted(comparatorOwnershipByApartment())
                    .toList();
            messageResponse = result.isEmpty() ? messageResponse : "Получено " + result.size() + " помещений";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllOwnership() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Все объекты собственности успешно удалены";

        log.info(messageEnter(methodName));
        try {
            ownershipDAO.deleteAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // summa ----------------------
    @Override
    public Object summaAreaRooms() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream().mapToDouble(Ownership::getTotalArea).sum();
            String messageResponse = "Общая площадь помещений дома составляет " + summa + " м2.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object summaAreaApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            String messageResponse = "Общая площадь всех квартир дома составляет " + summa + " м2.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object summaAreaLivingApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.APARTMENT))
                    .mapToDouble(Ownership::getLivingArea).sum();
            String messageResponse = "Общая жилая площадь всех квартир дома составляет " + summa + " м2.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object summaAreaNonResidentialRoom() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            double summa = ownershipDAO.findAll().stream()
                    .filter(x -> x.getTypeRoom().equals(TypeOfRoom.NON_RESIDENTIAL_ROOM))
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            String messageResponse = "Общая площадь всех нежилых помещений дома составляет " + summa + " м2.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // count --------------------
    @Override
    public Object countRooms() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            long count = ownershipDAO.count();
            String messageResponse = "Общее количество всех помещений дома составляет " + count + " единиц.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(count, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object countApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.APARTMENT);
            String messageResponse = "Общее количество всех жилых помещений дома составляет " + count + " единиц.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(count, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object countNonResidentialRoom() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            long count = ownershipDAO.countByTypeRoom(TypeOfRoom.NON_RESIDENTIAL_ROOM);
            String messageResponse = "Общее количество всех нежилых помещений дома составляет " + count + " единиц.";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(count, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    // получить все помещения по номеру помещения --------------
    @Override
    public Object getAllOwnershipByApartment(String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Объекты недвижимости по помещению № : " + apartment + " не зарегистрированы";
        log.info(messageEnter(methodName));
        try {
            List<Ownership> ownerships = ownershipDAO.findByAddressApartment(apartment)
                    .stream()
                    .sorted(comparatorOwnershipByBill())
                    .collect(Collectors.toList());
            messageResponse = ownerships.isEmpty() ? messageResponse
                    : "Объекты недвижимости с помещением № : " + apartment + " получено успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(ownerships, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    //  получить все помещения по ФИО --------------------
    @Override
    public Object getAllApartmentByFullName(String fullName) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
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
                return new Response(List.of(messageNotOwner));
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
                return new Response(List.of(messageNotOwnership));
            }
            messageResponse = "Получено " + result.size() + " записей";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // получить помещение по лицевому счёту -------------
    @Override
    public Object getOwnershipByBill(String bill) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String messageResponse = "Помещение по лицевому счёту № : " + bill + " не существует";
        log.info(messageEnter(methodName));
        try {
            Ownership ownership = ownershipDAO.findByBill(bill);
            if (ownership != null)
                messageResponse = "Помещение по лицевому счёту № : " + bill + " успешно получено";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(ownership, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getMapApartmentListIdAndBill() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Map<String, List<IdAndBill>> map = ownershipDAO.findAll().stream()
                    .collect(Collectors.groupingBy(s -> s.getAddress().getApartment(),
                            Collectors.mapping(s -> new IdAndBill(s.getId(), s.getBill()), Collectors.toList())));
            log.info("Map<String, List<IdAndBill>> map = " + map);
            log.info(messageExit(methodName));
            return new Response(map, List.of("Карта получена успешно"));
        } catch (
                Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // получить все лицевые счета по номеру помещения ----------------
    @Override
    public Object getAllBillByApartment(String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "По помещению № : " + apartment + " лицевые счёта не найдены";
        log.info(messageEnter(methodName));
        try {
            List<String> bills = ownershipDAO.findByAddressApartment(apartment).stream().map(Ownership::getBill).toList();
            messageResponse = bills.isEmpty() ? messageResponse :
                    "По помещению № : " + apartment + " найдено " + bills.size() + " лицевых счетов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(bills, List.of(messageResponse));
        } catch (
                Exception error) {
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
        return (a, b) -> a.getBill().compareTo(b.getBill());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
