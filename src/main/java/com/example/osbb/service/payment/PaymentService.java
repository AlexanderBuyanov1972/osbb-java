package com.example.osbb.service.payment;

import com.example.osbb.dao.account.PaymentDAO;
import com.example.osbb.dao.account.RateDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.pojo.Room;
import com.example.osbb.dto.response.InvoiceNotification;
import com.example.osbb.dto.response.*;
import com.example.osbb.entity.account.Payment;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfBill;
import com.example.osbb.service.ServiceMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    PaymentDAO paymentDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    @Autowired
    RateDAO rateDAO;

    // one ------------------------
    @Override
    public Object createPayment(Payment payment) {
        try {
            payment.setDate(LocalDateTime.now());
            return Response
                    .builder()
                    .data(paymentDAO.save(payment))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getPayment(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (!paymentDAO.existsById(id)) {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(paymentDAO.findById(id).orElse(new Payment()))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deletePayment(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (paymentDAO.existsById(id)) {
                paymentDAO.deleteById(id);
            } else {
                errors.add(ServiceMessages.NOT_EXISTS);
            }
            return errors.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(errors);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // all ------------------------------
    @Override
    public Object createAllPayment(List<Payment> payments) {
        List<Payment> result = new ArrayList<>();
        try {
            for (Payment payment : payments) {
                if (!paymentDAO.existsById(payment.getId())) {
                    result.add(paymentDAO.save(payment));
                }
            }
            return result.isEmpty() ? new ResponseMessages(List
                    .of(ServiceMessages.NOT_CREATED))
                    : Response
                    .builder()
                    .data(listSortedByDate(result))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getAllPayment() {
        try {
            List<Payment> result = paymentDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(listSortedByDate(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deleteAllPayment() {
        try {
            paymentDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }


    // sorted ------------------------
    @Override
    public Object getAllPaymentByBill(String bill) {
        try {
            List<Payment> result = paymentDAO.findAllByBill(bill);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(listSortedByDate(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getAllPaymentByDescription(String description) {
        try {
            List<Payment> result = paymentDAO.findAllByDescription(description);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(listSortedByDate(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }


    @Override
    public Object getAllPaymentByBillAndDateBetween(String bill, LocalDateTime from, LocalDateTime to) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByBillAndDateBetween(
                    bill,
                    from,
                    to);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(listSortedByDate(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getAllPaymentByDescriptionAndDateBetween(String description, LocalDateTime from, LocalDateTime to) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByDescriptionAndDateBetween(
                    description,
                    from,
                    to);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(listSortedByDate(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // summa ------------------------------------
    @Override
    public Object getBalanceAllPayment() {
        try {
            return Response
                    .builder()
                    .data(getSummaFromListPayment(
                            paymentDAO.findAllByTypeBill(TypeOfBill.COMING))
                            - getSummaFromListPayment(paymentDAO.findAllByTypeBill(TypeOfBill.CONSUMPTION)))
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getBalanceHouse() {
        try {
            List<EntryBalanceHouse> result = new ArrayList();
            LocalDate from = LocalDate.of(2021, 1, 1);
            LocalDate to = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
            // cycle -------------------
            ownershipDAO.findAll().forEach(el -> {
                String bill = el.getBill();
                String apartment = el.getAddress().getApartment();
                Double totalAreaRoom = el.getTotalArea();
                Double summaPaid = getSummaFromListPayment(paymentDAO.findAllByBill(bill));
                Double summaAccrued = getSummaAccrued(totalAreaRoom, from);
                Double debtAtBeginningPeriod = 0.00;
                Double recalculationForServicesNotReceived = 0.00;
                Double subsidyMonetization = 0.00;
                Double monetizationBenefits = 0.00;
                Double debt = debtAtBeginningPeriod + summaAccrued
                        - summaPaid
                        - monetizationBenefits
                        - subsidyMonetization
                        - recalculationForServicesNotReceived;
                result.add(EntryBalanceHouse
                        .builder()
                        .bill(bill)
                        .apartment(apartment)
                        .summa(formatDoubleValue(debt))
                        .build());
            });
            return Response
                    .builder()
                    .data(listSortedByApartment(result))
                    .messages(List.of(ServiceMessages.OK, "Получено " + result.size() + " записей"))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getSummaAllPaymentByBill(String bill) {
        try {
            List<Payment> result = paymentDAO.findAllByBill(bill);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(getSummaFromListPayment(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getSummaAllPaymentByDescription(String description) {
        try {
            List<Payment> result = paymentDAO.findAllByDescription(description);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(getSummaFromListPayment(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getSummaAllPaymentByBillAndDateBetween(String bill, LocalDateTime from, LocalDateTime to) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByBillAndDateBetween(bill, from, to);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(getSummaFromListPayment(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getSummaAllPaymentByDescriptionAndDateBetween(String description, LocalDateTime from, LocalDateTime to) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByDescriptionAndDateBetween(description, from, to);
            return result.isEmpty() ?
                    new ResponseMessages(List.of(ServiceMessages.DB_EMPTY))
                    :
                    Response
                            .builder()
                            .data(getSummaFromListPayment(result))
                            .messages(List.of(ServiceMessages.OK))
                            .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // get invoice notification by apartment --------------
    @Override
    public Object getDebtByApartment(String apartment) {
        // dates --------------
        LocalDate from = LocalDate.of(2021, 1, 1);
        LocalDate to = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        //
        String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
        Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
        Double summaPaid = getSummaFromListPayment(paymentDAO.findAllByBill(bill));
        Double summaAccrued = getSummaAccrued(totalAreaRoom, from);
        Double debtAtBeginningPeriod = 0.00;
        Double recalculationForServicesNotReceived = 0.00;
        Double subsidyMonetization = 0.00;
        Double monetizationBenefits = 0.00;
        Double debt = debtAtBeginningPeriod + summaAccrued
                - summaPaid
                - monetizationBenefits
                - subsidyMonetization
                - recalculationForServicesNotReceived;

        HeaderInvoiceNotification header = HeaderInvoiceNotification
                .builder()
                .address(ownershipDAO.findByAddressApartment(apartment).getAddress())
                .bill(bill)
                .area(formatDoubleValue(totalAreaRoom))
                .currentTime(LocalDateTime.now())
                .build();

        BodyInvoiceNotification body = BodyInvoiceNotification
                .builder()
                .beginningPeriod(from)
                .debtAtBeginningPeriod(formatDoubleValue(debtAtBeginningPeriod))
                .rate(formatDoubleValue(rateDAO.findByDate(to).getValue()))
                .accrued(summaAccrued)
                .recalculationForServicesNotReceived(formatDoubleValue(recalculationForServicesNotReceived))
                .subsidyMonetization(formatDoubleValue(subsidyMonetization))
                .monetizationBenefits(formatDoubleValue(monetizationBenefits))
                .paid(summaPaid)
                .debtAtFinalizingPeriod(formatDoubleValue(debt))
                .finalizingPeriod(LocalDate.now())
                .build();
        return Response
                .builder()
                .data(new InvoiceNotification(header, body))
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public Object getDetailsDebtByApartment(String apartment) {
        // header --------------------------
        String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
        Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
        HeaderInvoiceNotification header = HeaderInvoiceNotification
                .builder()
                .address(ownershipDAO.findByAddressApartment(apartment).getAddress())
                .bill(bill)
                .area(formatDoubleValue(totalAreaRoom))
                .currentTime(LocalDateTime.now())
                .build();
        // body const ---------------------
        Double debtAtBeginningPeriod = 0.00;
        Double recalculationForServicesNotReceived = 0.00;
        Double subsidyMonetization = 0.00;
        Double monetizationBenefits = 0.00;
        LocalDate from = LocalDate.of(2021, 1, 1);
        LocalDate to = from.plusMonths(1);
        LocalDate dateFinish = LocalDate.now();
        List<BodyInvoiceNotification> list = new ArrayList<>();


        // begin cycle --------------------------------------------
        while (from.isBefore(dateFinish)) {
            LocalDateTime fromLDT = mapLocalDateToLocaldateTime(from);
            LocalDateTime toLDT = mapLocalDateToLocaldateTime(to);
            Double summaPaid = getSummaFromListPayment(paymentDAO
                    .findAllPaymentByBillAndDateBetween(bill, fromLDT, toLDT));
            Double summaAccrued = getSummaAccruedByDate(totalAreaRoom, from);
            Double debt = debtAtBeginningPeriod + summaAccrued
                    - summaPaid
                    - monetizationBenefits
                    - subsidyMonetization
                    - recalculationForServicesNotReceived;
            BodyInvoiceNotification body = BodyInvoiceNotification
                    .builder()
                    .beginningPeriod(from)
                    .debtAtBeginningPeriod(formatDoubleValue(debtAtBeginningPeriod))
                    .rate(formatDoubleValue(rateDAO.findByDate(from).getValue()))
                    .accrued(summaAccrued)
                    .recalculationForServicesNotReceived(formatDoubleValue(recalculationForServicesNotReceived))
                    .subsidyMonetization(formatDoubleValue(subsidyMonetization))
                    .monetizationBenefits(formatDoubleValue(monetizationBenefits))
                    .paid(summaPaid)
                    .debtAtFinalizingPeriod(formatDoubleValue(debt))
                    .finalizingPeriod(to.minusDays(1))
                    .build();

            list.add(body);
            debtAtBeginningPeriod = debt;
            from = to;
            to = to.plusMonths(1);
        }


        return Response
                .builder()
                .data(List.of(header, list))
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    // help functions ------------------------------------
    private LocalDateTime mapLocalDateToLocaldateTime(LocalDate date) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
    }

    private Double getSummaAccrued(Double area, LocalDate date) {
        return formatDoubleValue(
                rateDAO.findAll()
                        .stream()
                        .filter(el -> el.getDate().isAfter(date.minusDays(1)) && el.getDate().isBefore(LocalDate.now()))
                        .map(el -> el.getValue() * area)
                        .mapToDouble(Double::doubleValue).sum()
        );
    }

    private Double getSummaAccruedByDate(Double totalAreaRoom, LocalDate date) {
        return formatDoubleValue(rateDAO.findAll()
                .stream()
                .filter(el -> el.getDate().isEqual(date))
                .map(el -> el.getValue() * totalAreaRoom)
                .findFirst().orElse(0.00));
    }

    // getSummaFromListPayment ------------------

    private Double getSummaFromListPayment(List<Payment> list) {
        return formatDoubleValue(list.stream().mapToDouble(Payment::getSumma).sum());
    }

    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    // sorted ------------------------
    private List<Payment> listSortedById(List<Payment> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<EntryBalanceHouse> listSortedByApartment(List<EntryBalanceHouse> list) {
        return list.stream().sorted((a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment())).collect(Collectors.toList());
    }

    private List<Payment> listSortedByDate(List<Payment> list) {
        return list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
    }

    //.sorted(comparatorByApartment())
    private Comparator<EntryBalanceHouse> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment());
    }


}
