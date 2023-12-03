package com.example.osbb.service.payment;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.PaymentDAO;
import com.example.osbb.dao.RateDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.BodyInvoiceNotification;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderInvoiceNotification;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.*;
import com.example.osbb.entity.Payment;
import com.example.osbb.entity.ownership.Address;
import com.example.osbb.entity.ownership.Ownership;
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
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
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
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            payment.setDate(LocalDateTime.now());
            payment = paymentDAO.save(payment);
            String messageResponse = "Платёж с лицевого счёта № " + payment.getBill() + " успешно создан";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(payment, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public Object getPayment(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Платёж с ID = " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            Payment payment = paymentDAO.findById(id).orElse(null);
            if (payment != null)
                messageResponse = "Платёж с ID = " + id + " получен успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(payment, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public Object deletePayment(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Платёж с ID = " + id + " не существует";
        log.info(messageEnter(methodName));
        try {
            if (paymentDAO.existsById(id)) {
                paymentDAO.deleteById(id);
                messageResponse = "Платёж с ID = " + id + " удалён успешно";
            }
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(id, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // all ------------------------------
    @Override
    public Object createAllPayment(List<Payment> payments) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Не создано ни одного платежа";
        log.info(messageEnter(methodName));
        List<Payment> result = new ArrayList<>();
        try {
            for (Payment payment : payments) {
                if (!paymentDAO.existsById(payment.getId())) {
                    payment = paymentDAO.save(payment);
                    result.add(payment);
                    log.info("Платёж с лицевого счёта № " + payment.getBill() + " успешно создан");
                }
            }
            messageResponse = result.isEmpty() ? messageResponse : "Создано " + result.size() + " платежей";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result.stream().sorted(comparatorByDate()).toList(), List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public Object getAllPayment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Payment> result = paymentDAO.findAll().stream().sorted(comparatorByDate()).toList();
            String messageResponse = "Получено " + result.size() + " платежей";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public Object deleteAllPayment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Удалены все платежи";
        log.info(messageEnter(methodName));
        try {
            paymentDAO.deleteAll();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // Получение платёжек по счёту в штуках (лист)
    @Override
    public Object getAllPaymentByBill(String bill) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Payment> result = paymentDAO.findAllByBill(bill).stream().sorted(comparatorByDate()).toList();
            String messageResponse = "Платёжки по счёту = " + bill
                    + " получены в количестве = " + result.size() + " штук";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // Получение платёжек по описанию в штуках (лист)
    @Override
    public Object getAllPaymentByDescription(String description) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Payment> result = paymentDAO.findAllByDescription(description).stream().sorted(comparatorByDate()).toList();
            String messageResponse = "Платёжки по описанию = " + description
                    + " получены в количестве = " + result.size() + " штук";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // Получение платёжек по счёту за период (от и до ) в штуках (лист)
    @Override
    public Object getAllPaymentByBillAndDateBetween(String bill, LocalDateTime from, LocalDateTime to) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        log.info(messageEnter(methodName));
        try {
            List<Payment> result = paymentDAO.findAllPaymentByBillAndDateBetween(bill, from, to)
                    .stream().sorted(comparatorByDate()).toList();
            String messageResponse = "Платёжки по счёту = " + bill + " за период от "
                    + from.toString() + " и до " + to.toString() + " получены в количестве = "
                    + result.size() + " штук";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public Object getAllPaymentByDescriptionAndDateBetween(String description, LocalDateTime from, LocalDateTime to) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            List<Payment> result = paymentDAO.findAllPaymentByDescriptionAndDateBetween(
                    description, from, to).stream().sorted(comparatorByDate()).toList();
            String messageResponse = "Платёжки по описанию = " + description + " за период от "
                    + from.toString() + " и до " + to.toString() + " получены в количестве = "
                    + result.size() + " штук";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(result, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // получить текущее сальдо платёжных операций (доходы минус расходы) верхний правый угол
    @Override
    public Object getBalanceAllPayment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Double saldo = getSummaFromListPayment(
                    paymentDAO.findAllByTypeBill(TypeOfBill.COMING))
                    - getSummaFromListPayment(paymentDAO.findAllByTypeBill(TypeOfBill.CONSUMPTION));
            String messageResponse = "Сальдо текущих операций составляет : " + saldo + " грн";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(saldo, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // получить сальдо платежей по помещениям всего дома
    @Override
    public Object getBalanceHouse() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "Баланс сформирован успешно";
        log.info(messageEnter(methodName));
        try {
            List<EntryBalanceHouse> list = getListEntryBalanceHouse();
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(list, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public List<EntryBalanceHouse> getListEntryBalanceHouse() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
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
            String messageResponse = "Сформировано " + result.size() + " записей";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return sortedByApartment(result);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    // получить начисленную сумму за весь период
    private Double getSummaAccrued(Double area) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
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
            log.info(messageExit(methodName));
            return result;
        } catch (Exception error) {
            log.error(ERROR_SERVER);
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
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Payment> result = paymentDAO.findAllByBill(bill);
            Double summa = getSummaFromListPayment(result);
            String messageResponse = "Получена сумма = " + summa + " по счёту = " + bill;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // получить сумму платежей посредством описания
    @Override
    public Object getSummaAllPaymentByDescription(String description) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Double summa = getSummaFromListPayment(paymentDAO.findAllByDescription(description));
            String messageResponse = "Получена сумма = " + summa + " по описанию = " + description;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // получить сумму платежей посредством счёта и за период
    @Override
    public Object getSummaAllPaymentByBillAndDateBetween(String bill, LocalDateTime from, LocalDateTime to) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Double summa = getSummaFromListPayment(paymentDAO.findAllPaymentByBillAndDateBetween(bill, from, to));
            String messageResponse = "Получена сумма платежей по счёту = " + bill + " за период от "
                    + from.toString() + " и до " + to.toString() + " на сумму = " + summa;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    @Override
    public Object getSummaAllPaymentByDescriptionAndDateBetween(String description, LocalDateTime from, LocalDateTime to) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Double summa = getSummaFromListPayment(paymentDAO.findAllPaymentByDescriptionAndDateBetween(description, from, to));
            String messageResponse = "Получена сумма платежей по описанию = " + description + " за период от "
                    + from.toString() + " и до " + to.toString() + " на сумму = " + summa;
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(summa, List.of(messageResponse));
        } catch (Exception e) {
            log.error(ERROR_SERVER);
            log.error(e.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, e.getMessage()));
        }
    }

    // get invoice notification by apartment за последний месяц
    @Override
    public Object getDebtById(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String messageResponse = "";
        log.info(messageEnter(methodName));
        try {
            Ownership ownership = ownershipDAO.findById(id).orElse(null);
            if (ownership == null) {
                messageResponse = "Помещения с ID : " + id + " не существует. Долг не может быть получен";
                log.info(messageResponse);
                log.info(messageExit(methodName));
                return new Response(List.of(messageResponse));
            }

            InvoiceNotification in = getDebtInvoiceNotificationByBill(ownership.getBill());
            messageResponse = " Уведомление о задолженности по помещению № : "
                    + ownership.getAddress().getApartment() + " получено успешно";
            log.info("Уведомление по долгу  : " + in.toString());
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(in, List.of(messageResponse));
        } catch (
                Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public InvoiceNotification getDebtInvoiceNotificationByBill(String bill) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageEmpty = "Долги не могут быть начислены, нет полного месяца обслуживания";
        try {
            log.info(messageEnter(methodName));
            log.info("startLocalDateRate : " + startLocalDateRate);
            LocalDate from = getStartLocalDateRate();
            log.info("Начальная дата начисления (2023-11-01) LocalDate from : " + from);
            LocalDate to = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().minus(1), 1);
            log.info("Первое число прошлого месяца от текущей даты LocalDate to : " + to);
            Ownership ownership = ownershipDAO.findByBill(bill);
            log.info("Ownership ownership = " + ownership);
            Double totalAreaRoom = ownership.getTotalArea();
            log.info("Общая площадь по лицевому счёту № " + bill + " составляет " + totalAreaRoom + " м2");
            Address address = ownership.getAddress();
            log.info("Address : " + address);
            HeaderInvoiceNotification header = createHeaderInvoiceNotification(bill, address, totalAreaRoom);
            if (from.isBefore(to) && to.minusDays(1).isAfter(from)) {
                Double summaPaid = getSummaFromListPayment(paymentDAO.findAllPaymentByBillAndDateBetween(
                        bill,
                        mapLocalDateToLocaldateTime(from),
                        mapLocalDateToLocaldateTime(to.minusMonths(1))));
                log.info("Уплоченная сумма за всё время по данному счёту = " + summaPaid);
                Double debtAtBeginningPeriod = getSummaAccruedBetweenDateAndDateNow(totalAreaRoom, from);
                log.info("Начисленная сумма от начальной точки начисления  до первого числа прошлого месяца текущего года = "
                        + debtAtBeginningPeriod);
                Double summaAccrued = getSummaAccruedBetweenDateAndDateNow(totalAreaRoom, to.minusMonths(1));
                log.info("Начисленная сумма за промежуток времени от заданой даты до сегодняшнего дня минус один месяц = "
                        + summaAccrued);
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
                log.info(messageExit(methodName));
                return new InvoiceNotification(header, body);
            } else {
                BodyInvoiceNotification body = createEmptyBodyInvoiceNotification(to);
                log.info(messageEmpty);
                log.info(messageExit(methodName));
                return new InvoiceNotification(header, body);
            }
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private HeaderInvoiceNotification createHeaderInvoiceNotification(String bill, Address address, Double totalArea) {
        return HeaderInvoiceNotification
                .builder()
                .address(address)
                .bill(bill)
                .area(formatDoubleValue(totalArea))
                .currentTime(LocalDateTime.now())
                .build();
    }

    private BodyInvoiceNotification createEmptyBodyInvoiceNotification(LocalDate to) {
        return BodyInvoiceNotification
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
    }

    @Override
    public Object getDetailsDebtById(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageResponse = "";
        log.info(messageEnter(methodName));
        try {
            Ownership ownership = ownershipDAO.findById(id).orElse(null);
            if (ownership == null) {
                messageResponse = "Помещения с ID : " + id + " не существует. Детализированный долг не может быть получен";
                log.info(messageResponse);
                log.info(messageExit(methodName));
                return new Response(List.of(messageResponse));
            }
            DebtDetails dd = getDetailsDebtInvoiceNotificationByBill(ownership.getBill());
            log.info("Уведомление по детализации долга получено успешно : " + dd.toString());
            messageResponse = "Уведомление о детализации долга по помещению № : " + ownership.getAddress().getApartment()
                    + " получено успешно";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return new Response(dd, List.of(messageResponse));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));

        }

    }

    @Override
    public DebtDetails getDetailsDebtInvoiceNotificationByBill(String bill) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            Double totalAreaRoom = ownershipDAO.findByBill(bill).getTotalArea();
            log.info("Общая площадь по номеру помещения = " + totalAreaRoom);
            Address address = ownershipDAO.findByBill(bill).getAddress();
            HeaderInvoiceNotification header = createHeaderInvoiceNotification(bill, address, totalAreaRoom);
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
            log.info(messageExit(methodName));
            return new DebtDetails(header, list);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
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
    private List<Payment> sortedById(List<Payment> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    // sorted by apartment --------------------
    private List<EntryBalanceHouse> sortedByApartment(List<EntryBalanceHouse> list) {
        return list.stream().sorted((a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment())).collect(Collectors.toList());
    }

    //.sorted(comparatorByApartment())
    private Comparator<Payment> comparatorByDate() {
        return (a, b) -> b.getDate().compareTo(a.getDate());
    }

    private Comparator<EntryBalanceHouse> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment());
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }


}

//    @Override
//    public Object getDetailsDebtByBill(String bill) {
//        String methodName = new Object() {
//        }.getClass().getEnclosingMethod().getName();
//        String messageResponse = "Детализация долга по лицевому счёту № " + bill + " получена успешно";
//        log.info(messageEnter(methodName));
//        try {
//            DebtDetails dd = getDetailsDebtInvoiceNotificationByBill(bill);
//            log.info(messageResponse);
//            log.info(messageExit(methodName));
//            return new Response(dd, List.of(messageResponse));
//        } catch (Exception error) {
//            log.error(ERROR_SERVER);
//            log.error(error.getMessage());
//            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
//
//        }
//
//    }

//    @Override
//    public Object getDebtByBill(String bill) {
//        String methodName = new Object() {
//        }.getClass().getEnclosingMethod().getName();
//        String messageEmpty = "Долги не могут быть начислены, нет полного месяца обслуживания";
//        String messageSuccessfully = "Долг по лицевому счёту № " + bill + " получен успешно";
//        log.info(messageEnter(methodName));
//        try {
//            InvoiceNotification in = getDebtInvoiceNotificationByBill(bill);
//            String messageResponse = in.getBody().getDebtAtFinalizingPeriod() == 0.00 ? messageEmpty : messageSuccessfully;
//            log.info(messageResponse);
//            log.info(messageExit(methodName));
//            return new Response(in, List.of(messageResponse));
//        } catch (Exception error) {
//            log.error(ERROR_SERVER);
//            log.error(error.getMessage());
//            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
//        }
//
//    }