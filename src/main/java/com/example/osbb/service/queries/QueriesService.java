package com.example.osbb.service.queries;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.queries.ApartmentHeatSupply;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.service.pdf.IPdfService;
import com.example.osbb.service.pdf.PdfService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class QueriesService implements IQueriesService {
    private static final Logger log = Logger.getLogger(PdfService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final String PRINT_SUCCESSFULLY = "Все PDF файлы напечатаны успешно";
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private IPdfService iPdfService;



    @Override
    public Object queryListHeatSupplyForApartment() {
        String methodName = "queryListHeatSupplyForApartment";
        log.info(messageEnter(methodName));
        try {
            Map<String, List<ApartmentHeatSupply>> map = ownershipDAO.findAll()
                    .stream()
                    .map(ApartmentHeatSupply::new)
                    .sorted(comparatorApartmentHeatSupply())
                    .collect(groupingBy(ApartmentHeatSupply::getHeatSupply));
            iPdfService.printQueryListHeatSupplyForApartment(map);
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new ResponseMessages(List.of("Наличие и типизация квартирного отопления в доме", PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

// sorted ---------------------
    private Comparator<ApartmentHeatSupply> comparatorApartmentHeatSupply() {
        return (a, b) -> {
            return a.getHeatSupply().compareTo(b.getHeatSupply()) != 0 ?
                    a.getHeatSupply().compareTo(b.getHeatSupply()) :
                    Integer.parseInt(a.getApartment())
                            - Integer.parseInt(b.getApartment());
        };
    }

    // log services ----------------------
    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
