package com.example.osbb.service.payment;

import com.example.osbb.dao.PaymentDAO;
import com.example.osbb.dao.RateDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.BodyInvoiceNotification;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderInvoiceNotification;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.*;
import com.example.osbb.entity.Payment;
import com.example.osbb.enums.TypeOfBill;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {
    private static final Logger log = Logger.getLogger(PaymentService.class);
    @Autowired
    PaymentDAO paymentDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    @Autowired
    RateDAO rateDAO;
    @Value("${start.date.rate}")
    private String startLocalDateRate;
    private Double saldo;

    // one ------------------------
    @Override
    public Object createPayment(Payment payment) {
        try {
            log.info("Method createPayment : enter");
            payment.setDate(LocalDateTime.now());
            payment = paymentDAO.save(payment);
            log.info("Платёж с лицевого счёта № " + payment.getBill() + " успешно создан");
            log.info("Method createPayment : exit");
            return Response
                    .builder()
                    .data(payment)
                    .messages(List.of("Платёж с лицевого счёта № " + payment.getBill() + " успешно создан"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    @Override
    public Object getPayment(Long id) {
        log.info("Method getPayment : enter");
        try {
            if (!paymentDAO.existsById(id)) {
                log.info("Платёж с id= " + id + " не существует");
                log.info("Method getPayment : exit");
                return new ResponseMessages(List.of("Платёж с id= " + id + " не существует"));
            }
            Payment payment = paymentDAO.findById(id).get();
            log.info("Платёж получен успешно");
            log.info("Method getPayment : exit");
            return Response
                    .builder()
                    .data(payment)
                    .messages(List.of("Платёж получен успешно"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    @Override
    public Object deletePayment(Long id) {
        log.info("Method deletePayment : enter");
        try {
            if (paymentDAO.existsById(id)) {
                paymentDAO.deleteById(id);
                log.info("Удаление платежа прошло успешно");
                log.info("Method deletePayment : exit");
                return Response
                        .builder()
                        .data(id)
                        .messages(List.of("Удаление платежа прошло успешно"))
                        .build();
            } else {
                log.info("Платёж с id= " + id + " не существует");
                log.info("Method deletePayment : exit");
                return new ResponseMessages(List.of("Платёж с id= " + id + " не существует"));
            }
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    // all ------------------------------
    @Override
    public Object createAllPayment(List<Payment> payments) {
        log.info("Method createAllPayment : enter");
        List<Payment> result = new ArrayList<>();
        try {
            for (Payment payment : payments) {
                if (!paymentDAO.existsById(payment.getId())) {
                    payment = paymentDAO.save(payment);
                    result.add(payment);
                    log.info("Платёж с лицевого счёта № " + payment.getBill() + " успешно создан");
                }
            }
            if (result.isEmpty()) {
                log.info("Не создано ни одного платежа");
                log.info("Method createAllPayment : exit");
                return new ResponseMessages(List.of("Не создано ни одного платежа"));
            }
            log.info("Создано " + result.size() + " платежей");
            log.info("Method createAllPayment : exit");
            return Response
                    .builder()
                    .data(listSortedByDate(result))
                    .messages(List.of("Создано " + result.size() + " платежей"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    @Override
    public Object getAllPayment() {
        log.info("Method getAllPayment : enter");
        try {
            List<Payment> result = paymentDAO.findAll();
            log.info("Получено " + result.size() + " платежей");
            log.info("Method getAllPayment : exit");
            return Response.builder()
                    .data(listSortedByDate(result))
                    .messages(List.of("Получено " + result.size() + " платежей"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    @Override
    public Object deleteAllPayment() {
        log.info("Method deleteAllPayment : enter");
        try {
            paymentDAO.deleteAll();
            log.info("Удалены все платежи");
            log.info("Method deleteAllPayment : exit");
            return new ResponseMessages(List.of("Удалены все платежи"));
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    // Получение платёжек по счёту в штуках (лист)
    @Override
    public Object getAllPaymentByBill(String bill) {
        log.info("Method getAllPaymentByBill : enter");
        try {
            List<Payment> result = paymentDAO.findAllByBill(bill);
            log.info("Платёжки по счёту = " + bill + " получены в количестве = " + result.size() + " штук");
            log.info("Method getAllPaymentByBill : exit");
            return Response
                    .builder()
                    .data(listSortedByDate(result))
                    .messages(List.of("Платёжки по счёту = " + bill + " получены в количестве = " + result.size() + " штук"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    // Получение платёжек по описанию в штуках (лист)
    @Override
    public Object getAllPaymentByDescription(String description) {
        log.info("Method getAllPaymentByDescription : enter");
        try {
            List<Payment> result = paymentDAO.findAllByDescription(description);
            log.info("Платёжки по описанию = " + description + " получены в количестве = " + result.size() + " штук");
            log.info("Method getAllPaymentByDescription : exit");
            return Response
                    .builder()
                    .data(listSortedByDate(result))
                    .messages(List.of("Платёжки по описанию = " + description + " получены в количестве = " + result.size() + " штук"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    // Получение платёжек по счёту за период (от и до ) в штуках (лист)
    @Override
    public Object getAllPaymentByBillAndDateBetween(String bill, LocalDateTime from, LocalDateTime to) {
        log.info("Method getAllPaymentByBillAndDateBetween : enter");
        try {
            List<Payment> result = paymentDAO.findAllPaymentByBillAndDateBetween(
                    bill,
                    from,
                    to);
            log.info("Платёжки по счёту = " + bill + " за период от "
                    + from.toString() + " и до " + to.toString() + " получены в количестве = "
                    + result.size() + " штук");
            log.info("Method getAllPaymentByBillAndDateBetween : exit");
            return Response
                    .builder()
                    .data(listSortedByDate(result))
                    .messages(List.of("Платёжки по счёту = " + bill + " за период от "
                            + from.toString() + " и до " + to.toString() + " получены в количестве = "
                            + result.size() + " штук"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    @Override
    public Object getAllPaymentByDescriptionAndDateBetween(String description, LocalDateTime from, LocalDateTime to) {
        try {
            log.info("Method getAllPaymentByDescriptionAndDateBetween : enter");
            List<Payment> result = paymentDAO.findAllPaymentByDescriptionAndDateBetween(
                    description,
                    from,
                    to);
            log.info("Платёжки по описанию = " + description + " за период от "
                    + from.toString() + " и до " + to.toString() + " получены в количестве = "
                    + result.size() + " штук");
            log.info("Method getAllPaymentByDescriptionAndDateBetween : exit");
            return Response
                    .builder()
                    .data(listSortedByDate(result))
                    .messages(List.of("Платёжки по описанию = " + description + " за период от "
                            + from.toString() + " и до " + to.toString() + " получены в количестве = "
                            + result.size() + " штук"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    // получить текущее сальдо платёжных операций (доходы минус расходы) верхний правый угол
    @Override
    public Object getBalanceAllPayment() {
        log.info("Method getBalanceAllPayment : enter");
        try {
            Double saldo = getSummaFromListPayment(
                    paymentDAO.findAllByTypeBill(TypeOfBill.COMING))
                    - getSummaFromListPayment(paymentDAO.findAllByTypeBill(TypeOfBill.CONSUMPTION));
            log.info("Сальдо текущих операций составляет : " + saldo + " грн");
            log.info("Method getBalanceAllPayment : exit");
            return Response
                    .builder()
                    .data(saldo)
                    .messages(List.of("Сальдо текущих операций составляет : " + saldo + " грн"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    // получить сальдо платежей по помещениям всего дома
    @Override
    public Object getBalanceHouse() {
        log.info("Method getBalanceHouse : enter");
        try {
            List<EntryBalanceHouse> list = getListEntryBalanceHouse();
            log.info("Баланс сформирован успешно");
            log.info("Method getBalanceHouse : exit");
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Баланс сформирован успешно"))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", e.getMessage()));
        }
    }

    @Override
    public List<EntryBalanceHouse> getListEntryBalanceHouse() {
        try {
            log.info("Method getListEntryBalanceHouse : enter");
            List<EntryBalanceHouse> result = new ArrayList<>();
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
            log.info("Сформировано " + result.size() + " записей");
            log.info("Method getListEntryBalanceHouse : exit");
            return listSortedByApartment(result);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    // получить начисленную сумму за весь период
    private Double getSummaAccrued(Double area) {
        try {
            log.info("Method getSummaAccrued : enter");
            LocalDate from = getStartLocalDateRate();
            log.info("Начальная точка начисления за оплату = " + from);
            Double result = formatDoubleValue(rateDAO.findAll()
                    .stream()
                    .filter(el -> el.getDate().isBefore(LocalDate.now()))
                    .filter(el -> from.isBefore(el.getDate()))
                    .filter(el -> el.getDate().minusDays(1).isBefore(from))
                    .map(el -> el.getValue() * area)
                    .mapToDouble(Double::doubleValue).sum());
            log.info("Начисленная сумма = " + result);
            log.info("Method getSummaAccrued : exit");
            return result;
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    // начальная точка начисления оплаты
    private LocalDate getStartLocalDateRate() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return LocalDate.parse(startLocalDateRate, df);
    }

    // получить сумму платежей по данному лицевому счёту
    @Override
    public Object getSummaAllPaymentByBill(String bill) {
        log.info("Method getSummaAllPaymentByBill : enter");
        try {
            List<Payment> result = paymentDAO.findAllByBill(bill);
            Double summa = getSummaFromListPayment(result);
            log.info("Получена сумма = " + summa + " по счёту = " + bill);
            log.info("Method getSummaAllPaymentByBill : exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Получена сумма = " + summa + " по счёту = " + bill))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // получить сумму платежей посредством описания
    @Override
    public Object getSummaAllPaymentByDescription(String description) {
        log.info("Method getSummaAllPaymentByDescription : enter");
        try {
            Double summa = getSummaFromListPayment(paymentDAO.findAllByDescription(description));
            log.info("Получена сумма = " + summa + " по описанию = " + description);
            log.info("Method getSummaAllPaymentByDescription : exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of("Получена сумма = " + summa + " по описанию = " + description))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // получить сумму платежей посредством счёта и за период
    @Override
    public Object getSummaAllPaymentByBillAndDateBetween(String bill, LocalDateTime from, LocalDateTime to) {
        log.info("Method getSummaAllPaymentByBillAndDateBetween : enter");
        try {
            Double summa = getSummaFromListPayment(paymentDAO.findAllPaymentByBillAndDateBetween(bill, from, to));
            String string = "Получена сумма платежей по счёту = " + bill + " за период от "
                    + from.toString() + " и до " + to.toString() + " на сумму = " + summa;
            log.info(string);
            log.info("Method getSummaAllPaymentByBillAndDateBetween : exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of(string))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    @Override
    public Object getSummaAllPaymentByDescriptionAndDateBetween(String description, LocalDateTime from, LocalDateTime to) {
        log.info("Method getSummaAllPaymentByDescriptionAndDateBetween : enter");
        try {
            Double summa = getSummaFromListPayment(paymentDAO.findAllPaymentByDescriptionAndDateBetween(description, from, to));
            String string = "Получена сумма платежей по описанию = " + description + " за период от "
                    + from.toString() + " и до " + to.toString() + " на сумму = " + summa;
            log.info(string);
            log.info("Method getSummaAllPaymentByDescriptionAndDateBetween : exit");
            return Response
                    .builder()
                    .data(summa)
                    .messages(List.of(string))
                    .build();
        } catch (Exception e) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // get invoice notification by apartment за последний месяц
    @Override
    public Object getDebtByApartment(String apartment) {
        log.info("Method getDebtByApartment : enter");
        try {
            InvoiceNotification in = getDebtInvoiceNotificationByApartment(apartment);
            log.info("Method getDebtByApartment : exit");
            if (in.getBody().getDebtAtFinalizingPeriod() == 0.00) {
                log.info("Долги не могут быть начислены, нет полного месяца обслуживания");
                return Response
                        .builder()
                        .data(in)
                        .messages(List.of("Долги не могут быть начислены, нет полного месяца обслуживания"))
                        .build();
            }
            log.info("Платёжное поручение по помещению № " + apartment + " получено успешно");
            return Response
                    .builder()
                    .data(in)
                    .messages(List.of("Платёжное поручение по помещению № " + apartment + " получено успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }

    }

    @Override
    public InvoiceNotification getDebtInvoiceNotificationByApartment(String apartment) {
        try {
            log.info("Method getDebtInvoiceNotificationByApartment : enter");
            log.info("startLocalDateRate = " + startLocalDateRate);
            LocalDate from = getStartLocalDateRate();
            log.info("Начальная дата начисления (2023-11-01) LocalDate from = " + from);
            LocalDate to = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().minus(1), 1);
            log.info("Первое число прошлого месяца от текущей даты LocalDate to = " + to);
            Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
            log.info("Общая площадь по номеру помещения = " + totalAreaRoom);
            String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
            log.info("Номер лицевого счёта по номеру помещения = " + bill);

            HeaderInvoiceNotification header = HeaderInvoiceNotification
                    .builder()
                    .address(ownershipDAO.findByAddressApartment(apartment).getAddress())
                    .bill(bill)
                    .area(formatDoubleValue(totalAreaRoom))
                    .currentTime(LocalDateTime.now())
                    .build();

            if (from.isBefore(to) && to.minusDays(1).isAfter(from)) {
                Double summaPaid = getSummaFromListPayment(paymentDAO.findAllPaymentByBillAndDateBetween(
                        bill,
                        mapLocalDateToLocaldateTime(from),
                        mapLocalDateToLocaldateTime(to.minusMonths(1))));
                log.info("Уплоченная сумма за всё время по данному счёту = " + summaPaid);
                Double debtAtBeginningPeriod = getSummaAccruedBetweenDateAndDateNow(totalAreaRoom, from);
                log.info("Начисленная сумма от начальной точки начисления  до первого числа прошлого месяца текущего года = " + debtAtBeginningPeriod);
                Double summaAccrued = getSummaAccruedBetweenDateAndDateNow(totalAreaRoom, to.minusMonths(1));
                log.info("Начисленная сумма за промежуток времени от заданой даты до сегодняшнего дня минус один месяц = " + summaAccrued);

                Double recalculationForServicesNotReceived = 0.00;
                Double subsidyMonetization = 0.00;
                Double monetizationBenefits = 0.00;
                Double debt = debtAtBeginningPeriod
                        + summaAccrued
                        - summaPaid
                        - monetizationBenefits
                        - subsidyMonetization
                        - recalculationForServicesNotReceived;
                log.info("Итого долг составляет = " + debt);
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
                log.info("Method getDebtInvoiceNotificationByApartment : exit");
                return new InvoiceNotification(header, body);
            } else {
                BodyInvoiceNotification body = BodyInvoiceNotification
                        .builder()
                        .beginningPeriod(to)
                        .debtAtBeginningPeriod(0.00)
                        .rate(0.00)
                        .accrued(0.00)
                        .recalculationForServicesNotReceived(0.00)
                        .subsidyMonetization(0.00)
                        .monetizationBenefits(0.00)
                        .paid(0.00)
                        .debtAtFinalizingPeriod(formatDoubleValue(0.00))
                        .finalizingPeriod(to.plusMonths(1).minusDays(1))
                        .build();
                log.info("Долги не могут быть начислены, нет полного месяца обслуживания");
                log.info("Method getDebtInvoiceNotificationByApartment : exit");
                return new InvoiceNotification(header, body);
            }
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }


    }

    @Override
    public Object getDetailsDebtByApartment(String apartment) {
        log.info("Method getDetailsDebtByApartment : enter");
        try {
            DebtDetails dd = getDetailsDebtInvoiceNotificationByApartment(apartment);
            log.info("Детализация долга по помещению № " + apartment + " получена успешно");
            log.info("Method getDetailsDebtByApartment : exit");
            return Response
                    .builder()
                    .data(dd)
                    .messages(List.of("Детализация долга по помещению № " + apartment + " получена успешно"))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));

        }

    }

    @Override
    public DebtDetails getDetailsDebtInvoiceNotificationByApartment(String apartment) {
        try {
            log.info("Method getDetailsDebtInvoiceNotificationByApartment : enter");
            String bill = ownershipDAO.findByAddressApartment(apartment).getBill();
            log.info("Лицевой счёт по номеру помещения = " + bill);
            Double totalAreaRoom = ownershipDAO.findByAddressApartment(apartment).getTotalArea();
            log.info("Общая площадь по номеру помещения = " + totalAreaRoom);
            HeaderInvoiceNotification header = HeaderInvoiceNotification
                    .builder()
                    .address(ownershipDAO.findByAddressApartment(apartment).getAddress())
                    .bill(bill)
                    .area(formatDoubleValue(totalAreaRoom))
                    .currentTime(LocalDateTime.now())
                    .build();
            Double debtAtBeginningPeriod = 0.00;
            Double recalculationForServicesNotReceived = 0.00;
            Double subsidyMonetization = 0.00;
            Double monetizationBenefits = 0.00;
            log.info("Подготовка к циклу по дате, интервал - один месяц");
            log.info("startLocalDateRate = " + startLocalDateRate);
            LocalDate from = getStartLocalDateRate();
            log.info("Начальная дата начисления (2023-11-01) LocalDate from= " + from);
            LocalDate to = from.plusMonths(1);
            log.info("Шаг to = " + to);
            LocalDate dateFinish = LocalDate.now();
            log.info("Конечная дата начисления = " + dateFinish);
            List<BodyInvoiceNotification> list = new ArrayList<>();
            log.info("Начало цикла");
            log.info("Проверка условия : " + from.isBefore(dateFinish));
            while (from.isBefore(dateFinish)) {
                Double summaPaid = getSummaFromListPayment(paymentDAO
                        .findAllPaymentByBillAndDateBetween(bill,
                                mapLocalDateToLocaldateTime(from),
                                mapLocalDateToLocaldateTime(to)));
                log.info("Сумма платёжек за циклический месяц = " + summaPaid);
                Double summaAccrued = getSummaAccruedByDate(totalAreaRoom, from);
                log.info("Начисленная сумма за оплату услуг за месяц = " + summaAccrued);
                Double debt = debtAtBeginningPeriod + summaAccrued
                        - summaPaid
                        - monetizationBenefits
                        - subsidyMonetization
                        - recalculationForServicesNotReceived;
                log.info(" Месячное сально долга : " + debt);
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
                log.info("Начальная дата  : " + body.getBeginningPeriod()
                        + ", начальный долг : " + body.getDebtAtBeginningPeriod()
                        + ", конечная дата : " + body.getFinalizingPeriod()
                        + ", конечный долг : " + body.getDebtAtFinalizingPeriod());
                // добавляем элемент ответа в лист тела ответа
                list.add(body);
                // подготовка для следующей итерации
                debtAtBeginningPeriod = debt;
                from = to;
                to = to.plusMonths(1);
            }
            log.info("Method getDetailsDebtInvoiceNotificationByApartment : exit");
            return new DebtDetails(header, list);
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }


    // help functions
    private LocalDateTime mapLocalDateToLocaldateTime(LocalDate date) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
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
