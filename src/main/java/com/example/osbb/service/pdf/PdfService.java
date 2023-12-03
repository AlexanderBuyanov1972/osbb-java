package com.example.osbb.service.pdf;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderInvoiceNotification;
import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.dto.queries.ApartmentHeatSupply;
import com.example.osbb.dto.response.EntryBalanceHouse;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfAnswer;
import com.example.osbb.service.payment.IPaymentService;
import com.example.osbb.service.survey.ISurveyService;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PdfService implements IPdfService {
    private static final Logger log = Logger.getLogger(PdfService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final String PRINT_SUCCESSFULLY = "Все PDF файлы напечатаны успешно";
    @Autowired
    IPaymentService iPaymentService;
    @Autowired
    ISurveyService iSurveyService;
    @Autowired
    OwnershipDAO ownershipDAO;

    // debt -----------------------------------------------------------------------------------------------
    // печатать задолженность за последний календарный месяц по номеру помещения в pdf файл
    @Override
    public Object printPdfDebtByApartment(InvoiceNotification in) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            printPdfFile(in);
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в pdf файл каждый отдельно
    @Override
    public Object printListPdfDebtAllApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtInvoiceNotificationByBill(
                            el.getBill()))
                    .forEach(this::printPdfFile);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private void printPdfFile(InvoiceNotification in) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/payments");
            PdfWriter writer = new PdfWriter("D:/pdf/payments/payment_" + in.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObject(in, doc);
            doc.close();
            log.info(messageExit(methodName));
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в один pdf файл
    @Override
    public Object printAllTInOnePdfDebtAllApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/allInOne");
            AtomicInteger count = new AtomicInteger(1);
            PdfWriter writer = new PdfWriter("D:/pdf/allInOne/paymentAllInOne.pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtInvoiceNotificationByBill(
                            el.getBill()))
                    .sorted(comparatorByApartment())
                    .forEach(el -> {
                        writeOnePdfObject(el, doc);
                        if (count.get() % 4 == 0)
                            doc.add(new AreaBreak());
                        count.getAndIncrement();
                    });
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // debt details -------------------------------------------------------------------------------------------
    // печатать по номеру помещения детализированный долг от начальной точки до текущего месяца один pdf файл
    @Override
    public Object printPdfDebtDetailsByApartment(String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<DebtDetails> list = new ArrayList<>();
            List<String> bills = getListBillByApartment(apartment);
            bills.forEach(bill -> {
                list.add(iPaymentService.getDetailsDebtInvoiceNotificationByBill(bill));
            });
            list.forEach(this::printPdfDetailsFile);
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private List<String> getListBillByApartment(String apartment) {
        return ownershipDAO.findByAddressApartment(apartment)
                .stream().map(Ownership::getBill).collect(Collectors.toList());
    }

    // печатать детализированный долг от начальной точки до текущего месяца по каждому помещению в отдельный файл
    @Override
    public Object printPdfDebtDetailsAllApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDetailsDebtInvoiceNotificationByBill(
                            el.getBill()))
                    .forEach(this::printPdfDetailsFile);
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private void printPdfDetailsFile(DebtDetails details) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/payments_details");
            PdfWriter writer = new PdfWriter("D:/pdf/payments_details/payment_details_" + details.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObjectDetails(details, doc);
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // balance ------------------------------------------------------------------------------------------------
    // печатать баланс дома по помещениям (задолженность/переплата)
    @Override
    public Object printPdfBalanceHouse() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<EntryBalanceHouse> list = iPaymentService.getListEntryBalanceHouse();
            Double summa = formatDoubleValue(list.stream().mapToDouble(EntryBalanceHouse::getSumma).reduce(0, (a, b) -> a + b));
            checkDir("D:/pdf/balance");
            PdfWriter writer = new PdfWriter("D:/pdf/balance/balance_house.pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            // заголовок -----------------------
            createHeader("Задолженость по оплате за услуги ОСББ по помещениям", doc, font);
            // шапка -> параграфы
            LocalDate ld = LocalDate.now();
            List<String> listHeader = List.of(
                    "Текущее время : " + LocalDateTime.now().toString()
                            .replace("T", " ")
                            .substring(0, LocalDateTime.now().toString().indexOf(".")),
                    "Итоговая дата баланса : " + LocalDate.of(ld.getYear(), ld.getMonth().plus(1), 1),
                    "Итоговый баланс дома по оплате услуг ОСББ составляет : " + summa + " грн"
            );
            for (String line : listHeader) {
                Paragraph paragraph = new Paragraph(line);
                paragraph.setFont(font).setFontSize(12).setMarginTop(3).setMarginBottom(3).setFontColor(Color.GRAY);
                doc.add(paragraph);
            }
            for (EntryBalanceHouse entry : list) {
                String line = "Л/c № " + entry.getBill()
                        + " // квартира № " + entry.getApartment()
                        + " // " + getString(entry.getSumma()) + " грн";
                Paragraph paragraph = new Paragraph(line);
                Color colorBlue = new DeviceRgb(0, 128, 128);
                paragraph.setFont(font).setFontSize(12).setMarginTop(5).setMarginBottom(5).setFontColor(colorBlue);
                if (entry.getSumma() > 0)
                    paragraph.setFontColor(Color.RED);
                doc.add(paragraph);
            }
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // опросы ------------------------------------------------------------------------------------------
    // печатать результаты опросов по теме -------------------
    @Override
    public Object printResultQuestionnaire(String title) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            ResultSurvey result = iSurveyService.getResultSurveyByTitleForPrint(title);
            checkDir("D:/pdf/questionnaire_result");
            PdfWriter writer = new PdfWriter("D:/pdf/questionnaire_result/" + title + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            Color teal = new DeviceRgb(0, 128, 128);
            Color blueviolet = new DeviceRgb(138, 43, 226);
            // start ------------------
            createHeaderBlueviolet("Тема опроса : " + title, doc, font);
            // 1 ---------------
            createHeader("Обработано " + result.getProcessingPercentageArea()
                    + "% в метрах квадратных", doc, font);
            createHeader("Всего проголосовало : " + result.getAreaVoted()
                    + " из " + result.getSummaArea()
                    + " метров квадратных", doc, font);
            for (String key : result.getMapVotedArea().keySet()) {
                Paragraph header = new Paragraph(key);
                header.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(12).setFontColor(teal);
                doc.add(header);

                String text = "ЗА : " + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.BEHIND));
                Paragraph line = new Paragraph(text);
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                text = "ПРОТИВ : " + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.AGAINST));
                line = new Paragraph(text);
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                text = "ВОЗДЕРЖАЛИСЬ : " + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.ABSTAINED));
                line = new Paragraph(text);
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);
            }
            // 2 ------------
            createHeader("Обработано " + result.getProcessingPercentageOwner()
                    + "% в голосах собственников", doc, font);
            createHeader("Всего проголосовало : " + result.getOwnerVoted()
                    + " из " + result.getCountOwner()
                    + " собственников", doc, font);
            for (String key : result.getMapVotedOwner().keySet()) {
                Paragraph header = new Paragraph(key);
                header.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(12).setFontColor(teal);
                doc.add(header);

                String text = "ЗА : " + result.getMapVotedOwner().get(key).get(TypeOfAnswer.BEHIND);
                Paragraph line = new Paragraph(text);
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                text = "ПРОТИВ : " + result.getMapVotedOwner().get(key).get(TypeOfAnswer.AGAINST);
                line = new Paragraph(text);
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                text = "ВОЗДЕРЖАЛИСЬ : " + result.getMapVotedOwner().get(key).get(TypeOfAnswer.ABSTAINED);
                line = new Paragraph(text);
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);
            }
            // finish -----------------
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // формирование объекта задолженности
    private void writeOnePdfObject(InvoiceNotification in, Document doc) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            PdfFont font = createFont();
            // заголовок -----------------------
            createHeader("Счёт-уведомление по оплате за услуги по управлению ОСББ", doc, font);
            // шапка -> параграфы
            fillHeaderPdfFile(in.getHeader(), doc, font);
            // таблица ----------------------------
            float[] pointColumnWidths = {40F, 40F, 40F, 40F, 40F, 40F, 40F};
            Table table = new Table(pointColumnWidths);
            table.setMarginTop(8).setMarginBottom(8);
            // первая линия --------------------
            fillListCellFirstRowDebt(table, font,
                    in.getBody().getBeginningPeriod().toString(),
                    in.getBody().getFinalizingPeriod().toString());

            // вторая линия -----------------------------
            List<String> listCellTwo = List.of(
                    in.getBody().getDebtAtBeginningPeriod().toString(),
                    in.getBody().getRate().toString(),
                    in.getBody().getAccrued().toString(),
                    in.getBody().getSubsidyMonetization().toString(),
                    in.getBody().getMonetizationBenefits().toString(),
                    in.getBody().getPaid().toString(),
                    in.getBody().getDebtAtFinalizingPeriod().toString()
            );
            for (String line : listCellTwo) {
                Cell cell = new Cell();
                cell.add(line).setTextAlignment(TextAlignment.CENTER).setFont(font);
                table.addCell(cell);
            }
            doc.add(table);
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // формирование детализации объекта задолженность
    public void writeOnePdfObjectDetails(DebtDetails debtDetails, Document doc) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            PdfFont font = createFont();
            // заголовок -----------------------
            createHeader("Детализация долга за услуги по управлению ОСББ", doc, font);
            // шапка -> параграфы
            fillHeaderPdfFile(debtDetails.getHeader(), doc, font);
            // таблица ----------------------------
            float[] pointColumnWidths = {40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F};
            Table table = new Table(pointColumnWidths);
            table.setMarginTop(8).setMarginBottom(8);
            // первая линия --------------------
            fillListCellFirstRowDebtDetails(table, font);
            // вторая линия и далее -----------------------------
            debtDetails.getList().forEach(
                    el -> {
                        List<String> listCellTwo = List.of(
                                el.getBeginningPeriod().toString(),
                                el.getDebtAtBeginningPeriod().toString(),
                                el.getRate().toString(),
                                el.getAccrued().toString(),
                                el.getSubsidyMonetization().toString(),
                                el.getMonetizationBenefits().toString(),
                                el.getPaid().toString(),
                                el.getDebtAtFinalizingPeriod().toString(),
                                el.getFinalizingPeriod().toString()
                        );
                        for (String line : listCellTwo) {
                            Cell cell = new Cell();
                            cell.add(line).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(9);
                            table.addCell(cell);
                        }
                    }
            );
            doc.add(table);
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // отдельные элементы для конструирования файла pdf
    // шапка для квитанций
    private void fillHeaderPdfFile(HeaderInvoiceNotification header, Document doc, PdfFont font) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            String str = header.getCurrentTime().toString();
            List<String> list = List.of(
                    "51931, Украина, Днепропетровская область, Каменское, Свободы, дом № 51, кв. № "
                            + header.getAddress().getApartment(),
                    "Лицевой счёт № " + header.getBill(),
                    "Общая площадь помещения, на которую начисляется оплата: "
                            + header.getArea() + " м2.",
                    "Текущая дата: " + str.substring(0, str.indexOf("T")),
                    "Текущее время: " + str.substring(str.indexOf("T") + 1, str.indexOf('.'))
            );
            for (String line : list) {
                Paragraph paragraph = new Paragraph(line);
                paragraph.setFont(font).setFontSize(10).setMarginTop(0).setMarginBottom(0);
                doc.add(paragraph);
            }
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // заголовок
    private void createHeader(String text, Document doc, PdfFont font) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Paragraph header = new Paragraph(text);
            header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(13);
            doc.add(header);
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private void createHeaderBlueviolet(String text, Document doc, PdfFont font) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Color blueviolet = new DeviceRgb(138, 43, 226);
            Paragraph header = new Paragraph(text);
            header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(13).setFontColor(blueviolet);
            doc.add(header);
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // заголовки в таблице debt
    private void fillListCellFirstRowDebt(Table table, PdfFont font, String beginDate, String finalDate) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        List<String> list = List.of(
                "Долг на \n " + beginDate + " составляет, грн",
                "Текущий тариф, грн/м2", "Начислено, грн", "Монетизация субсидий, грн", "Монетизация льгот, грн",
                "Оплачено, грн", "Долг на \n" + finalDate + " составляет, грн"
        );
        for (String line : list) {
            Cell cell = new Cell();
            cell.add(line).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font);
            table.addCell(cell);
        }
        log.info(messageExit(methodName));
    }

    // заголовки в таблице debt details
    private void fillListCellFirstRowDebtDetails(Table table, PdfFont font) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        List<String> list = List.of(
                "Начальный период", "Долг на начало периода, грн", "Текущий тариф, грн/м2", "Начислено, грн",
                "Монетизация субсидий, грн", "Монетизация льгот, грн",
                "Оплачено, грн", "Долг на конец период, грн", "  Конечный период  "
        );
        for (String line : list) {
            Cell cell = new Cell();
            cell.add(line).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font);
            table.addCell(cell);
        }
        log.info(messageExit(methodName));
    }

    // sorted ----------
    private Comparator<InvoiceNotification> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getHeader().getAddress().getApartment())
                - Integer.parseInt(b.getHeader().getAddress().getApartment());
    }

    //Округление дробной части до 2-х запятых
    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    private String getString(Double summa) {
        return summa > 0 ? "Задолженость : " + summa : "Переплата : " + summa;
    }

    private void checkDir(String str) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            String[] lines = str.split("/");
            String path = lines[0];
            for (int i = 1; i < lines.length; i++) {
                path = path + "/" + lines[i];
                File folder = new File(path);
                if (!folder.exists())
                    folder.mkdir();
            }
            log.info(messageExit(methodName));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    @Override
    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    public Object fillPdfNewBillForPayServiceOSBB() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            printPdfNewBillForPayServiceOSBB();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of("Распечатать платёжные реквизиты ОСББ", PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }


    private void printPdfNewBillForPayServiceOSBB() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            checkDir("D:/pdf/queries");
            PdfWriter writer = new PdfWriter("D:/pdf/queries/" + "Платёжные реквизиты ОСББ" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            List<Paragraph> list = new ArrayList<>();
            list.add(new Paragraph("Реквізити:")
                    .setFontSize(12)
                    .setUnderline()
                    .setBold());

            Paragraph p1 = new Paragraph();
            p1.add(new Text("Отримувач: ").setFontSize(12).setBold());
            p1.add("ОСББ \"Свободи 51\"");
            list.add(p1);

            Paragraph p2 = new Paragraph();
            p2.add("Код ЄДРПОУ: ");
            p2.add(new Text("44987443,").setUnderline());
            list.add(p2);
            list.add(new Paragraph("МФО 305299"));

            Paragraph p3 = new Paragraph();
            p3.add("р/р ");
            p3.add(new Text("UA 9130 5299 0000 0260 0005 0586 588    в АТ КБ \"ПРИВАТ БАНК\"").setUnderline());
            list.add(p3);

            list.add(new Paragraph("Призначення: ")
                    .setFontSize(12)
                    .setMarginBottom(0)
                    .setBold()
            );

            list.add(new Paragraph("Внесок за утримання/управління будинку та прибудинкової територїї, кв.№ ____ або ")
                    .setMarginTop(0).setMarginBottom(0));
            list.add(new Paragraph(" № __________________ (особового рахунку).").setMarginTop(0));

            list.add(new Paragraph("Телефон цілодобової аварійної служби ОСББ \"Свободи 51\": ").setFontSize(12)
                    .setMarginBottom(0));

            list.add(new Paragraph("097-659-29-10")
                    .setFontSize(14)
                    .setUnderline()
                    .setMarginTop(0)
                    .setBold());
            for (Paragraph paragraph : list) {
                paragraph.setFont(font);
                doc.add(paragraph);
            }
            doc.close();
            log.info(messageExit(methodName));
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    // возвращает отсортированный лист номер квартира - тип отопления ( SELECT, CENTER, AUTO_GAZ, AUTO_ELECTRO)
    @Override
    public void printQueryListHeatSupplyForApartment(Map<String, List<ApartmentHeatSupply>> map) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/queries");
            PdfWriter writer = new PdfWriter("D:/pdf/queries/" + "Поквартирная типизация отопления" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            doc.add(new Paragraph().add(dateTimeNow()).setFont(font));
            createTable(map.get("CENTER"), doc, font, "централизованное");
            createTable(map.get("AUTO_GAZ"), doc, font, "автономное (газовое)");
            createTable(map.get("AUTO_ELECTRO"), doc, font, "автономное (электрическое)");
            doc.close();
            log.info(messageExit(methodName));
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    @Override
    public void printQueryReport_2023_11() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/queries");
            PdfWriter writer = new PdfWriter("D:/pdf/queries/" + "Отчёт о деятельности ОСББ за ноябрь 2023 года" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();

            Paragraph ph = new Paragraph("Отчёт о работе ОСББ Свободы-51 за ноябрь 2023 года.");
            ph.setFont(font);
            ph.setTextAlignment(TextAlignment.CENTER).setFontSize(14).setBold();
            doc.add(ph);

            List<String> list = List.of("1. Поданы документы на участие в городской программе софинансирования (выделение денег на модернизацию системы энергоснабжения дома).",

                    "2. Обратились к мэру города с просьбой о благоустройстве двора и установки забора со стороны ул.Широкой и с пр. Свободы",

                    "3. Проведена обрезка кустов и кронирование деревьев.",

                    "4. Установлены бетонные столбы и налажено освещение двора.",

                    "5. Произведен осмотр подвальных помещений, крыши, электрощитовой и подъездов, определён объём работ.",

                    "6. В подвале подъезда №2  отведено место для уборки двора  и подъездов. Наняты уборщицы.  Благодарим Владимира (квартира №40) за установку замка.",

                    "7. В подвале подъезда №6 проведен свет, установлены лампочки и выключатель.",

                    "8. На чердаке (над квартирой №38), в связи с аварийной ситуацией, произведена замены участка трубы длиной 2,7 метра.",

                    "9. В квартире № 44 проводятся технические работы по теплоснабжению.",

                    "10. Произведена замена сальника на задвижке подачи теплоносителя в подвале подъезда № 6.");

            for (String line : list) {
                Paragraph paragraph = new Paragraph();
                paragraph.setFont(font);
                paragraph.add(line);
                doc.add(paragraph);
            }
            Paragraph pf = new Paragraph("Уважаемые жильцы!!! Убедительная просьба платить вовремя за услуги по содержанию нашего дома. Сделаем вместе наш дом уютным и комфортным");
            pf.setFont(font);
            pf.setTextAlignment(TextAlignment.CENTER).setFontSize(14).setBold().setMarginBottom(20).setMarginTop(20);
            doc.add(pf);

//            --------------------------------------------------------------
            List<Paragraph> list2 = new ArrayList<>();

            list2.add(new Paragraph("Реквізити:")
                    .setFontSize(12)
                    .setUnderline()
                    .setMarginTop(20)
                    .setBold());

            Paragraph p1 = new Paragraph();
            p1.add(new Text("Отримувач: ").setFontSize(12).setBold());
            p1.add("ОСББ \"Свободи 51\"");
            list2.add(p1);

            Paragraph p2 = new Paragraph();
            p2.add("Код ЄДРПОУ: ");
            p2.add(new Text("44987443,").setUnderline());
            list2.add(p2);
            list2.add(new Paragraph("МФО 305299"));

            Paragraph p3 = new Paragraph();
            p3.add("р/р ");
            p3.add(new Text("UA 9130 5299 0000 0260 0005 0586 588    в АТ КБ \"ПРИВАТ БАНК\"").setUnderline());
            list2.add(p3);

            list2.add(new Paragraph("Призначення: ")
                    .setFontSize(12)
                    .setMarginBottom(0)
                    .setBold()
            );

            list2.add(new Paragraph("Внесок за утримання/управління будинку та прибудинкової територїї, кв.№ ____ або ")
                    .setMarginTop(0).setMarginBottom(0));
            list2.add(new Paragraph(" № __________________ (особового рахунку).").setMarginTop(0));

            list2.add(new Paragraph("Телефон цілодобової аварійної служби ОСББ \"Свободи 51\": ").setFontSize(12)
                    .setMarginBottom(0));

            list2.add(new Paragraph("097-659-29-10")
                    .setFontSize(14)
                    .setUnderline()
                    .setMarginTop(0)
                    .setBold());
            for (Paragraph paragraph : list2) {
                paragraph.setFont(font);
                doc.add(paragraph);
            }


//            ----------------------------------------------------------------
            doc.close();
            log.info(messageExit(methodName));
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private void createTable(List<ApartmentHeatSupply> list, Document doc, PdfFont font, String message) {
        // заголовок раздела - Тип отопления
        Paragraph p = new Paragraph();
        p.add("Тип отопления - " + message + ", в количестве " + list.size() + " л.с.").setFont(font);
        doc.add(p);
        //создаём таблицу
        float[] pointColumnWidths = {200F, 200F};
        Table table1 = new Table(pointColumnWidths);
        table1.setMarginTop(7).setMarginBottom(7);
        // заполняем заголовки таблицы
        for (String line : List.of("Помещение №", "Тип отопления")) {
            Cell cell = new Cell();
            cell.add(line).setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFont(font);
            table1.addCell(cell);
        }
        // заполняем строки таблицы
        list.forEach(
                el -> {
                    for (String line : List.of(el.getApartment(), el.getHeatSupply())) {
                        Cell cell = new Cell();
                        cell.add(line).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(9);
                        table1.addCell(cell);
                    }
                }
        );
        doc.add(table1);
    }

    // font
    private PdfFont createFont() {
        String methodName = "createFont";
        log.info(messageEnter(methodName));
        try {
            return PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
        } catch (IOException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

    private String dateTimeNow() {
        String time = LocalDateTime.now().toString().replace("T", ", текущее время :  ");
        return "Текущая дата : " + time.substring(0, time.indexOf("."));
    }


//    Paragraph p = new Paragraph();
//p.add("The beginning of the line ");
//p.add(new Text("          (fill in your name)          ").setTextRise(-10).setUnderline().setFontSize(8));
//p.add(" end of the line");

}
