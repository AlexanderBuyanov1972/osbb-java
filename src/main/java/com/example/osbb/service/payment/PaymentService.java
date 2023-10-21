package com.example.osbb.service.payment;

import com.example.osbb.dao.account.PaymentDAO;
import com.example.osbb.dao.account.RateDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.BodyInvoiceNotification;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderInvoiceNotification;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.*;
import com.example.osbb.entity.account.Payment;
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


    // getAllPaymentByBill ------------------------
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

    // получить текущее сальдо платёжных операций ---------------------
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

    // получить сальдо платежей по помещениям всего дома
    @Override
    public Object getBalanceHouse() {
        try {
            return Response
                    .builder()
                    .data(getListEntryBalanceHouse())
                    .messages(List.of(ServiceMessages.OK, "Баланс сформирован успешно."))
                    .build();
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // получить сумму платежей по данному лицевому счёту
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

    // получить сумму платежей посредством описания
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
        return Response
                .builder()
                .data(getDebtInvoiceNotificationByApartment(apartment))
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public InvoiceNotification getDebtInvoiceNotificationByApartment(String apartment) {
        // начальная точка времени 2021-01-01
        LocalDate from = LocalDate.of(2021, 1, 1);
        // первое число прошлого месяца  2021-09-01
        LocalDate to = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().minus(1), 1);
        // получаем лицевой счёт по номеру помещения
        String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
        // получаем общую площадь по номеру помещения
        Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
        // получаем уплоченную сумму за всё время по данному счёту
        Double summaPaid = getSummaFromListPayment(paymentDAO.findAllPaymentByBillAndDateBetween(
                bill,
                mapLocalDateToLocaldateTime(from),
                mapLocalDateToLocaldateTime(to.minusMonths(1))));
        // получаем начисленную сумму за промежуток времени от начальная точка времени  до первого числа прошлого месяца
        Double debtAtBeginningPeriod = getSummaAccruedBetweenDateAndDateNow(totalAreaRoom, from);
        // получаем начисленную сумму за промежуток времени от первого числа прошлого месяца  до сегодняшнего дня минус один месяц
        Double summaAccrued = getSummaAccruedBetweenDateAndDateNow(totalAreaRoom, to.minusMonths(1));

        //---------------------------------------------------------------------------
        Double recalculationForServicesNotReceived = 0.00;
        Double subsidyMonetization = 0.00;
        Double monetizationBenefits = 0.00;
        Double debt = debtAtBeginningPeriod
                + summaAccrued
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
                .beginningPeriod(to)
                .debtAtBeginningPeriod(formatDoubleValue(debtAtBeginningPeriod))
                .rate(formatDoubleValue(rateDAO.findByDate(to).getValue()))
                .accrued(summaAccrued)
                .recalculationForServicesNotReceived(formatDoubleValue(recalculationForServicesNotReceived))
                .subsidyMonetization(formatDoubleValue(subsidyMonetization))
                .monetizationBenefits(formatDoubleValue(monetizationBenefits))
                .paid(summaPaid)
                .debtAtFinalizingPeriod(formatDoubleValue(debt))
                .finalizingPeriod(to.plusMonths(1).minusDays(1))
                .build();
        return new InvoiceNotification(header, body);
    }

    @Override
    public Object getDetailsDebtByApartment(String apartment) {
        return Response
                .builder()
                .data(getDetailsDebtInvoiceNotificationByApartment(apartment))
                .messages(List.of(ServiceMessages.OK))
                .build();
    }

    @Override
    public DebtDetails getDetailsDebtInvoiceNotificationByApartment(String apartment) {
        // находим лицевой счёт по номеру помещения
        String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
        // по номеру помещения получаем его общую площадь
        Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
        // заполняем заголовок ответа
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
        // подготовка к циклу по дате, интервал - один месяц
        // начальная точка -------------------
        LocalDate from = LocalDate.of(2021, 1, 1);
        // шаг
        LocalDate to = from.plusMonths(1);
        // финишная дата ---------------------
        LocalDate dateFinish = LocalDate.now();
        // лист для тела ответа ----------------
        List<BodyInvoiceNotification> list = new ArrayList<>();
        // начало цикла  --------------------------------------------
        // проверка условия
        while (from.isBefore(dateFinish)) {
            // получаем сумму платёжек за циклический месяц
            Double summaPaid = getSummaFromListPayment(paymentDAO
                    .findAllPaymentByBillAndDateBetween(bill,
                            mapLocalDateToLocaldateTime(from),
                            mapLocalDateToLocaldateTime(to)));
            // получаем начисленную сумму за оплату услуг за месяц
            Double summaAccrued = getSummaAccruedByDate(totalAreaRoom, from);
            // получаем месячное сально долга
            Double debt = debtAtBeginningPeriod + summaAccrued
                    - summaPaid
                    - monetizationBenefits
                    - subsidyMonetization
                    - recalculationForServicesNotReceived;
            // создаём элемента тела для ответа (возвращаем лист)
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
            // добавляем элемент ответа в лист тела ответа
            list.add(body);
            // подготовка для следующей итерации
            debtAtBeginningPeriod = debt;
            from = to;
            to = to.plusMonths(1);
        }
        return new DebtDetails(header, list);
    }
    @Override
    public List<EntryBalanceHouse> getListEntryBalanceHouse() {
        List<EntryBalanceHouse> result = new ArrayList<>();
        LocalDate from = LocalDate.of(2021, 1, 1);
        LocalDate to = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        // cycle -------------------
        ownershipDAO.findAll().forEach(el -> {
            String bill = el.getBill();
            String apartment = el.getAddress().getApartment();
            Double totalAreaRoom = el.getTotalArea();
            Double summaPaid = getSummaFromListPayment(paymentDAO.findAllByBill(bill));
            Double summaAccrued = getSummaAccrued(totalAreaRoom);
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
        return listSortedByApartment(result);
    }

    // help functions
    private LocalDateTime mapLocalDateToLocaldateTime(LocalDate date) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
    }

    // получить начисленную сумму за весь период
    private Double getSummaAccrued(Double area) {
        return formatDoubleValue(
                rateDAO.findAll()
                        .stream()
                        .filter(el -> el.getDate().isBefore(LocalDate.now()))
                        .map(el -> el.getValue() * area)
                        .mapToDouble(Double::doubleValue).sum()
        );
    }

    // получить начисленную сумму за промежуток времени от date до сегодняшнего дня минус один месяц
    private Double getSummaAccruedBetweenDateAndDateNow(Double area, LocalDate date) {
        return formatDoubleValue(
                rateDAO.findAll()
                        .stream()
                        .filter(el -> el.getDate().isAfter(date.minusDays(1))
                                && el.getDate().isBefore(LocalDate.now().minusMonths(2)))
                        .map(el -> el.getValue() * area)
                        .mapToDouble(Double::doubleValue).sum()
        );
    }

    // получить начисленную сумму за один месяц по тарифу за конкретную дату
    private Double getSummaAccruedByDate(Double totalAreaRoom, LocalDate date) {
        return formatDoubleValue(rateDAO.findAll()
                .stream()
                .filter(el -> el.getDate().isEqual(date))
                .map(el -> el.getValue() * totalAreaRoom)
                .findFirst().orElse(0.00));
    }

    // получить общую сумму всех платёжек в переданном листе

    private Double getSummaFromListPayment(List<Payment> list) {
        return formatDoubleValue(list.stream().mapToDouble(Payment::getSumma).sum());
    }

    //Округление дробной части до 2-х запятых
    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    // sorted by id ------------------------
    private List<Payment> listSortedById(List<Payment> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    // sorted by apartment --------------------
    private List<EntryBalanceHouse> listSortedByApartment(List<EntryBalanceHouse> list) {
        return list.stream().sorted((a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment())).collect(Collectors.toList());
    }

    // sorted by Date -------------------
    private List<Payment> listSortedByDate(List<Payment> list) {
        return list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
    }

    //.sorted(comparatorByApartment())
    private Comparator<EntryBalanceHouse> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment());
    }


}
