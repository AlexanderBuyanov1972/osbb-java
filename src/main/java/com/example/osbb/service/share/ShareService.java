package com.example.osbb.service.share;

import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ShareDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Share;
import com.example.osbb.service.ServiceMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareService implements IShareService {
    @Autowired
    ShareDAO shareDAO;
    @Autowired
    OwnerDAO ownerDAO;

    @Override
    public Object createShare(Share share) {
        List<String> errors = new ArrayList<>();
        try {
            if (share.getOwnership() == null)
                errors.add("В этой доле помещение не существует");
            if (share.getOwner() == null)
                errors.add("В этой доле собственник не существует");
            if (shareDAO.existsById(share.getId()))
                errors.add(ServiceMessages.ALREADY_EXISTS);
            if (isContentShare(share))
                errors.addAll(List.of("Не может быть двух долей для одной пары собственник-собственность",
                        "Проверте правильность заполнения данных"));
            if(errors.isEmpty()){
                Owner owner = share.getOwner();
                owner.setActive(true);
                share.setOwner(owner);
                ownerDAO.save(owner);
            }

            return errors.isEmpty() ? Response
                    .builder()
                    .data(shareDAO.save(share))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    private boolean isContentShare(Share share) {
        for (Share one : shareDAO.findAll()) {
            if (one.getOwner().getId() == share.getOwner().getId() &&
                    one.getOwnership().getId() == share.getOwnership().getId()) {
                return true;
            }
        }
        return false;
    }

    public Share getShareByOwnerAndOwnership(Owner owner, Ownership ownership) {
        for (Share one : shareDAO.findAll()) {
            if (one.getOwner().getId() == owner.getId() &&
                    one.getOwnership().getId() == ownership.getId()) {
                return one;
            }
        }
        return new Share();
    }

    @Override
    public Object updateShare(Share share) {
        List<String> errors = new ArrayList<>();
        try {
            if (share.getOwnership() == null)
                errors.add("В этой доле помещение не существует");
            if (share.getOwner() == null)
                errors.add("В этой доле собственник не существует");
            if (!shareDAO.existsById(share.getId()))
                errors.add(ServiceMessages.NOT_EXISTS);
            if (!isContentShare(share))
                errors.addAll(List.of("Не ни одной пары собственник-собственность по такой доле",
                        "Проверте правильность заполнения данных"));
            return errors.isEmpty() ? Response
                    .builder()
                    .data(shareDAO.save(share))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getShare(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (!shareDAO.existsById(id))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ? Response
                    .builder()
                    .data(shareDAO.findById(id).orElse(new Share()))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deleteShare(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (shareDAO.existsById(id)) {
                shareDAO.deleteById(id);
            } else {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object createAllShare(List<Share> list) {
        List<Share> result = new ArrayList<>();
        try {
            for (Share share : list) {
                if (!shareDAO.existsById(share.getId())) {
                    result.add(shareDAO.save(share));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object updateAllShare(List<Share> list) {
        List<Share> result = new ArrayList<>();
        try {
            for (Share share : list) {
                if (shareDAO.existsById(share.getId())) {
                    result.add(shareDAO.save(share));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_UPDATED))
                    : Response
                    .builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllShare() {
        try {
            List<Share> result = shareDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(returnListSortedById(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteAllShare() {
        try {
            shareDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getShareByApartmentAndFullName(String apartment, String fullName) {
        String[] fios = fullName.split(" ");
        if (!shareDAO.existsByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                apartment,
                fios[0],
                fios[1],
                fios[2])) {
            return new ResponseMessages(List.of(ServiceMessages.NOT_EXISTS));
        }

        Share share = shareDAO.findByOwnershipAddressApartmentAndOwnerLastNameAndOwnerFirstNameAndOwnerSecondName(
                apartment,
                fios[0],
                fios[1],
                fios[2]);
        return Response
                .builder()
                .data(share)
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public Object deleteShareByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        try {
            Share share = shareDAO.findAll().stream()
                    .filter(s -> s.getOwner().getId() == ownerId)
                    .filter(s -> s.getOwnership().getId() == ownershipId)
                    .findFirst().orElse(null);
            if (share == null) {
                return Response
                        .builder()
                        .data(new Share())
                        .messages(List.of("По данным ID собственника и ID помещения не существует"))
                        .build();
            }
            shareDAO.deleteById(share.getId());
            if (isShareListEmptyByOwnerId(ownerId)) {
                Owner owner = ownerDAO.findById(ownerId).get();
                // деактивация собственника, по причине отсутствия записей с его участием
                owner.setActive(false);
                ownerDAO.save(owner);
            }

            return Response
                    .builder()
                    .data(share.getId())
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    public boolean isShareListEmptyByOwnerId(Long id) {
        return shareDAO.findAll()
                .stream()
                .filter(s -> s.getOwner().getId() == id)
                .toList().isEmpty();
    }

    // sorted -------------------------------
    private List<Share> returnListSortedById(List<Share> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }
}
