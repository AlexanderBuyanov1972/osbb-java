package com.example.osbb.service.share;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ShareDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.Share;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareService implements IShareService {
    private static final Logger log = Logger.getLogger(ShareService.class);
    @Autowired
    ShareDAO shareDAO;
    @Autowired
    OwnerDAO ownerDAO;

    @Override
    public Object createShare(Share share) {
        log.info("Method createShare : enter");
        List<String> errors = new ArrayList<>();
        try {
            if (share.getOwnership() == null) {
                log.info("В этой доле помещение не существует");
                errors.add("В этой доле помещение не существует");
            }

            if (share.getOwner() == null) {
                log.info("В этой доле собственник не существует");
                errors.add("В этой доле собственник не существует");
            }

            if (shareDAO.existsById(share.getId())) {
                log.info("Доля с ID : " + share.getId() + " уже существует");
                errors.add("Доля с ID : " + share.getId() + " уже существует");
            }

            if (isContentShare(share)) {
                log.info("Не может быть двух долей для одной пары собственник-собственность");
                errors.addAll(List.of("Не может быть двух долей для одной пары собственник-собственность",
                        "Проверьте правильность заполнения данных"));
            }

            if (errors.isEmpty()) {
                Owner owner = share.getOwner();
                owner.setActive(true);
                share.setOwner(owner);
                ownerDAO.save(owner);
            }
            log.info("Method createShare : exit");
            if (errors.isEmpty()) {
                share = shareDAO.save(share);
                log.info("Доля с ID : " + share.getId() + " создана успешно");
                log.info("Method createShare : exit");
                return Response
                        .builder()
                        .data(share)
                        .messages(List.of("Доля с ID : " + share.getId() + " создана успешно"))
                        .build();
            }
            log.info("Доля не создана по причине : ");
            errors.forEach(log::info);
            log.info("Method createShare : exit");
            return new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    private boolean isContentShare(Share share) {
        log.info("Method isContentShare : enter");
        for (Share one : shareDAO.findAll()) {
            if (one.getOwner().getId() == share.getOwner().getId() &&
                    one.getOwnership().getId() == share.getOwnership().getId()) {
                log.info("Return true");
                log.info("Method isContentShare : exit");
                return true;
            }
        }
        log.info("Return false");
        log.info("Method isContentShare : exit");
        return false;
    }

    @Override
    public Object updateShare(Share share) {
        log.info("Method updateShare : enter");
        List<String> errors = new ArrayList<>();
        try {
            if (share.getOwnership() == null) {
                log.info("В этой доле помещение не существует");
                errors.add("В этой доле помещение не существует");
            }

            if (share.getOwner() == null) {
                log.info("В этой доле собственник не существует");
                errors.add("В этой доле собственник не существует");
            }

            if (!shareDAO.existsById(share.getId())) {
                log.info("Доля с ID : " + share.getId() + " не существует");
                errors.add("Доля с ID : " + share.getId() + " не существует");
            }

            if (!isContentShare(share)) {
                log.info("Не ни одной пары собственник-собственность по такой доле");
                errors.addAll(List.of("Не ни одной пары собственник-собственность по такой доле",
                        "Проверьте правильность заполнения данных"));
            }
            if (errors.isEmpty()) {
                share = shareDAO.save(share);
                log.info("Доля с ID : " + share.getId() + " обновлена успешно");
                log.info("Method updateShare : exit");
                return Response
                        .builder()
                        .data(share)
                        .messages(List.of("Доля с ID : " + share.getId() + " обновлена успешно"))
                        .build();
            }
            log.info("Доля не обновлена по причине : ");
            errors.forEach(log::info);
            log.info("Method updateShare : exit");
            return new ResponseMessages(errors);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getShare(Long id) {
        log.info("Method getShare : enter");
        try {
            if (!shareDAO.existsById(id)) {
                log.info("Доля с ID : " + id + " не существует ");
                log.info("Method getShare : exit");
                return new ResponseMessages(List.of("Доля с ID : " + id + " не существует "));
            }
            Share share = shareDAO.findById(id).get();
            log.info("Доля с ID : " + id + " получена успешно");
            log.info("Method getShare : exit");
            return Response
                    .builder()
                    .data(share)
                    .messages(List.of("Доля с ID : " + id + " получена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteShare(Long id) {
        log.info("Method deleteShare : enter");
        try {
            if (shareDAO.existsById(id)) {
                shareDAO.deleteById(id);
                log.info("Удаление доли с ID : " + id + " прошло успешно");
                log.info("Method deleteShare : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Удаление доли с ID : " + id + " прошло успешно"))
                        .build();
            } else {
                log.info("Доля с ID : " + id + " не существует");
                log.info("Method deleteShare : exit");
                return new ResponseMessages(List.of("Доля с ID : " + id + " не существует"));
            }
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object createAllShare(List<Share> list) {
        log.info("Method createAllShare : enter");
        List<Share> result = new ArrayList<>();
        try {
            for (Share share : list) {
                if (!shareDAO.existsById(share.getId())) {
                    share = shareDAO.save(share);
                    result.add(share);
                    log.info("Доля с ID : " + share.getId() + " создана успешно");
                }
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одной доли");
                log.info("Method createAllShare : exit");
                return new ResponseMessages(List.of("Не создано ни одной доли"));
            }
            log.info("Создано " + result.size() + " долей");
            log.info("Method createAllShare : exit");
            return Response
                    .builder()
                    .data(listSortedById(result))
                    .messages(List.of("Создано " + result.size() + " долей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object updateAllShare(List<Share> list) {
        log.info("Method updateAllShare : enter");
        List<Share> result = new ArrayList<>();
        try {
            for (Share share : list) {
                if (shareDAO.existsById(share.getId())) {
                    share = shareDAO.save(share);
                    result.add(share);
                    log.info("Доля с ID : " + share.getId() + " обновлено успешно");
                }
            }
            if (result.isEmpty()) {
                log.info("Не обновлено ни одной доли");
                log.info("Method updateAllShare : exit");
                return new ResponseMessages(List.of("Не обновлено ни одной доли"));
            }
            log.info("Обновлено " + result.size() + " долей");
            log.info("Method updateAllShare : exit");
            return Response
                    .builder()
                    .data(listSortedById(result))
                    .messages(List.of("Обновлено " + result.size() + " долей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getAllShare() {
        log.info("Method getAllShare : enter");
        try {
            List<Share> result = shareDAO.findAll();
            log.info("Получено " + result.size() + " долей");
            log.info("Method getAllShare : exit");
            return Response
                    .builder()
                    .data(listSortedById(result))
                    .messages(List.of("Получено " + result.size() + " долей"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteAllShare() {
        log.info("Method getAllShare : enter");
        try {
            shareDAO.deleteAll();
            log.info("Все доли удалены успешно");
            log.info("Method getAllShare : exit");
            return new ResponseMessages(List.of("Все доли удалены успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object getShareByApartmentAndFullName(String apartment, String fullName) {
        log.info("Method getShareByApartmentAndFullName : enter");
        try {
            String[] fios = fullName.split(" ");
            if (!shareDAO.existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    apartment, fios[0], fios[1], fios[2])) {
                log.info("Доля с номером помещения : " + apartment
                        + " и ФИО : " + fullName + " не существует");
                log.info("Method getShareByApartmentAndFullName : exit");
                return new ResponseMessages(List.of("Доля с номером помещения : " + apartment
                        + " и ФИО : " + fullName + " не существует"));
            }
            Share share = shareDAO.findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                    apartment, fios[0], fios[1], fios[2]);
            log.info("share.getValue() : " + share.getValue() + ", apartment : " + apartment + ", fullName : " + fullName);
            log.info("Method getShareByApartmentAndFullName : exit");
            return Response
                    .builder()
                    .data(share)
                    .messages(List.of("share.getValue() : " + share.getValue() + ", apartment : " + apartment + ", fullName : " + fullName))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object deleteShareByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        log.info("Method deleteShareByOwnerIdAndOwnershipId : enter");
        try {
            Share share = shareDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == ownerId)
                    .filter(s -> s.getOwnership().getId() == ownershipId)
                    .findFirst().orElse(null);
            if (share == null) {
                log.info("По данным ID собственника : " + ownerId + " и ID помещения : " + ownershipId + " не существует");
                log.info("Method deleteShareByOwnerIdAndOwnershipId : exit");
                return Response
                        .builder()
                        .data(new Share())
                        .messages(List.of("По данным ID собственника : "
                                + ownerId + " и ID помещения : "
                                + ownershipId + " не существует"))
                        .build();
            }
            shareDAO.deleteById(share.getId());
            if (isShareListEmptyByOwnerId(ownerId)) {
                Owner owner = ownerDAO.findById(ownerId).get();
                log.info("Деактивация собственника, по причине отсутствия записей с его участием");
                owner.setActive(false);
                ownerDAO.save(owner);
            }
            log.info("Удаление доли прошло успешно");
            log.info("Method deleteShareByOwnerIdAndOwnershipId : exit");
            return Response
                    .builder()
                    .data(share.getId())
                    .messages(List.of("Удаление доли прошло успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    public boolean isShareListEmptyByOwnerId(Long id) {
        return shareDAO.findAll()
                .stream()
                .filter(s -> s.getOwner().getId() == id)
                .toList().isEmpty();
    }

    // sorted -------------------------------
    private List<Share> listSortedById(List<Share> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
