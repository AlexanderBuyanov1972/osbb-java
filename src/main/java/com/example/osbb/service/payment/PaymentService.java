package com.example.osbb.service.payment;

import com.example.osbb.dao.account.PaymentDAO;
import com.example.osbb.dao.account.RateDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.*;
import com.example.osbb.entity.account.Payment;
import com.example.osbb.service.ServiceMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<String> list = new ArrayList<>();
        try {
            if (!paymentDAO.existsById(id)) {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(paymentDAO.findById(id).orElse(new Payment()))
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object deletePayment(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (paymentDAO.existsById(id)) {
                paymentDAO.deleteById(id);
            } else {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
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
    public Object getAllPaymentByPersonalAccount(String personalAccount) {
        try {
            List<Payment> result = paymentDAO.findAllByPersonalAccount(personalAccount);
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
    public Object getAllPaymentByPersonalAccountAndDateLessThan(String personalAccount, LocalDateTime date) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByPersonalAccountAndDateLessThan(personalAccount, date);
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
    public Object getAllPaymentByPersonalAccountAndDateBetween(
            String personalAccount,
            LocalDateTime from,
            LocalDateTime to) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByPersonalAccountAndDateBetween(
                    personalAccount,
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
    public Object getSummaAllPayment() {
        try {
            List<Payment> result = paymentDAO.findAll();
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
    public Object getSummaAllPaymentByPersonalAccount(String personalAccount) {
        try {
            List<Payment> result = paymentDAO.findAllByPersonalAccount(personalAccount);
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
    public Object getSummaAllPaymentByPersonalAccountAndDateLessThan(String personalAccount, LocalDateTime date) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByPersonalAccountAndDateLessThan(personalAccount, date);
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
    public Object getSummaAllPaymentByPersonalAccountAndDateBetween(String personalAccount, LocalDateTime from, LocalDateTime to) {
        try {
            List<Payment> result = paymentDAO.findAllPaymentByPersonalAccountAndDateBetween(
                    personalAccount,
                    from,
                    to);
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
        String personalAccount = ownershipDAO.findByAddressApartment(apartment).getPersonalAccount();
        Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
        Double summaPaid = getSummaFromListPayment(paymentDAO.findAllByPersonalAccount(personalAccount));
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
                .personalAccount(personalAccount)
                .totalArea(formatDoubleValue(totalAreaRoom))
                .currentDateTime(LocalDateTime.now())
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
        String personalAccount = ownershipDAO.findByAddressApartment(apartment).getPersonalAccount();
        Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
        HeaderInvoiceNotification header = HeaderInvoiceNotification
                .builder()
                .address(ownershipDAO.findByAddressApartment(apartment).getAddress())
                .personalAccount(personalAccount)
                .totalArea(formatDoubleValue(totalAreaRoom))
                .currentDateTime(LocalDateTime.now())
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
                    .findAllPaymentByPersonalAccountAndDateBetween(personalAccount, fromLDT, toLDT));
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
                    .finalizingPeriod(to)
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

    private LocalDateTime mapLocalDateToLocaldateTime(LocalDate date) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
    }

    private Double getSummaAccrued(Double area, LocalDate date) {
        return formatDoubleValue(rateDAO.findAll()
                .stream()
                .filter(el -> el.getDate().isAfter(date))
                .map(el -> el.getValue() * area)
                .reduce(0.00, Double::sum));
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

    private List<Payment> listSortedByDate(List<Payment> list) {
        return list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
    }
}
