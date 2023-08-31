package com.example.osbb.service.ownership;

import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Ownership;
import com.example.osbb.enums.TypeOfRoom;
import com.example.osbb.consts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnershipService implements IOwnershipService {

    @Autowired
    private OwnershipDAO ownershipDAO;

    // ------------- one --------------------

    @Override
    public Object createOwnership(Ownership one) {
        try {
            List<String> list = new ArrayList<>();
            if (ownershipDAO.existsById(one.getId()))
                list.add(ObjectMessages.withSuchIdAlreadyExists("Ownership"));
            return list.isEmpty() ?
                    List.of(ownershipDAO.save(one))
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
                list.add(ObjectMessages.withSuchIdNotExists("Ownership"));
            return list.isEmpty() ?
                    List.of(ownershipDAO.save(one))
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
                    List.of(ownershipDAO.findById(id).get())
                    :
                    new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Ownership")));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object deleteOwnership(Long id) {
        try {
            if (ownershipDAO.existsById(id)) {
                ownershipDAO.deleteById(id);
                return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
            }
            return new ErrorResponseMessages(List.of(ObjectMessages.withSuchIdNotExists("Ownership")));
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
                    ObjectMessages.noObjectCreated("Ownership"))) : returnListSorted(result);
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
                    ObjectMessages.noObjectUpdated("Ownership"))) : returnListSorted(result);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllOwnership() {
        try {
            List<Ownership> result = ownershipDAO.findAll();
            return result.isEmpty() ? new ResponseMessages(List.of(ObjectMessages.listEmpty()))
                    : returnListSorted(result);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }

    }

    @Override
    public Object deleteAllOwnership() {
        try {
            ownershipDAO.deleteAll();
            return new ResponseMessages(List.of(ObjectMessages.deletionCompleted()));
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
                    .mapToDouble(Ownership::getAreaRoomThatIsInProperty)
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
                    .mapToDouble(Ownership::getAreaRoomThatIsInProperty)
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
                    .mapToDouble(Ownership::getAreaRoomThatIsInProperty)
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
}
