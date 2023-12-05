package com.example.osbb.service.pdf;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderDebt;
import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.dto.queries.ApartmentHeatSupply;
import com.example.osbb.dto.response.EntryBalanceHouse;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.Debt;
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
    public Object printDebt(Debt in) {
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
    public Object printAllDebt() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtByBill(
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

    private void printPdfFile(Debt in) {
        try {
            checkDir("D:/pdf/payments");
            PdfWriter writer = new PdfWriter("D:/pdf/payments/payment_"
                    + in.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObject(in, doc);
            doc.close();
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в один pdf файл
    @Override
    public Object printAllInOneDebtAllApartment() {
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
                    .map(el -> iPaymentService.getDebtByBill(
                            el.getBill())).sorted(comparatorByBill())
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
    public Object printDebtDetails(Long id) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            Ownership ownership = ownershipDAO.findById(id).orElse(null);
            if (ownership == null) {
                String messageResponse = "Помещение с ID : " + id + " не найдено," +
                        " распечатать детализированный долг не представляется возможным";
                log.info(messageResponse);
                log.info(messageExit(methodName));
                return new Response(List.of(messageResponse));
            }
            DebtDetails dd = iPaymentService.getDetailsDebtByBill(ownership.getBill());
            printDetailsFile(dd);
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // печатать детализированный долг от начальной точки до текущего месяца по каждому помещению в отдельный файл
    @Override
    public Object printAllDebtDetails() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            ownershipDAO.findAll().stream().map(el -> iPaymentService.getDetailsDebtByBill(
                    el.getBill())).forEach(this::printDetailsFile);
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of(PRINT_SUCCESSFULLY, "Распечатка детализированого долга по каждому помещению"));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private void printDetailsFile(DebtDetails details) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/payments_details");
            PdfWriter writer = new PdfWriter("D:/pdf/payments_details/payment_details_"
                    + details.getHeader().getBill() + ".pdf");
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
    public Object printBalanceHouse() {
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
    public Object printResultSurvey(String title) {
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
            createHeaderBlueViolet("Тема опроса : " + title, doc, font);
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

                Paragraph line = new Paragraph("ЗА : "
                        + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.BEHIND)));
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                line = new Paragraph("ПРОТИВ : "
                        + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.AGAINST)));
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);
                line = new Paragraph("ВОЗДЕРЖАЛИСЬ : "
                        + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.ABSTAINED)));
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

                Paragraph line = new Paragraph("ЗА : " + result.getMapVotedOwner().get(key).get(TypeOfAnswer.BEHIND));
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                line = new Paragraph("ПРОТИВ : " + result.getMapVotedOwner().get(key).get(TypeOfAnswer.AGAINST));
                line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10);
                doc.add(line);

                line = new Paragraph("ВОЗДЕРЖАЛИСЬ : " + result.getMapVotedOwner().get(key).get(TypeOfAnswer.ABSTAINED));
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
    private void writeOnePdfObject(Debt in, Document doc) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            PdfFont font = createFont();
            // заголовок -----------------------
            createHeader("Счёт-уведомление по оплате за услуги по управлению ОСББ", doc, font);
            // шапка -> параграфы
            fillHeaderFile(in.getHeader(), doc, font);
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
            fillHeaderFile(debtDetails.getHeader(), doc, font);
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
    private void fillHeaderFile(HeaderDebt header, Document doc, PdfFont font) {
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


    @Override
    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    public Object printNewBillForPayServiceOSBB() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            log.info(messageEnter(methodName));
            checkDir("D:/pdf/queries");
            PdfWriter writer = new PdfWriter("D:/pdf/queries/" + "Платёжные реквизиты ОСББ" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            // --- начало ----------
            dateTimeNow(doc, font);
            bankAccountForPayment(doc, font);
            numberPhoneEmergencyService(doc, font);
            // --- конец -----------
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of("Распечатать платёжные реквизиты ОСББ", PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public void printQueryReport_2023_11() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/queries");
            String text = "Отчёт о деятельности ОСББ \"Cвободы - 51\" за ноябрь 2023 года.";
            PdfWriter writer = new PdfWriter("D:/pdf/queries/" + "отчётЗаНоябрь2023" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            // старт -------------------------------
            dateTimeNow(doc, font);
            createHeader(text, doc, font);
            createListText(TextsAndLists.report_2023_11, doc, font);
            appealToTheResidentsOfTheHouse(TextsAndLists.appealToTheResidents, doc, font);
            bankAccountForPayment(doc, font);
            // финиш ----------------------------------
            doc.close();
            log.info(messageExit(methodName));
        } catch (FileNotFoundException error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // *************************** вспомогательные функции ******************************
    private void createHeaderBlueViolet(String text, Document doc, PdfFont font) {
        Color blueViolet = new DeviceRgb(138, 43, 226);
        Paragraph header = new Paragraph(text);
        header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(13).setFontColor(blueViolet);
        doc.add(header);
    }

    // создание директории, например ("D:/pdf/queries")  ------------------------
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

    // создание таблицы для поквартирного учёта типизации систем отопления
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

    // заголовок ---------------------------------
    private void createHeader(String text, Document doc, PdfFont font) {
        Paragraph header = new Paragraph(text);
        header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14).setBold();
        doc.add(header);
    }

    // лист сообщений - перечень ----------------------------------------
    private void createListText(List<String> list, Document doc, PdfFont font) {
        for (String line : list) {
            Paragraph paragraph = new Paragraph();
            paragraph.setFont(font);
            paragraph.add(line);
            doc.add(paragraph);
        }
    }

    // font -----------------------------------
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

    // расчётный счёт для оплаты за услуги ОСББ ------------------
    private void bankAccountForPayment(Document doc, PdfFont font) {
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

        list2.add(new Paragraph("При оплате обязательно указывайте номер квартиры и за какой месяц. Спасибо.")
                .setFontSize(12).setMarginBottom(0).setBold().setTextAlignment(TextAlignment.CENTER));
        for (Paragraph paragraph : list2) {
            paragraph.setFont(font);
            doc.add(paragraph);
        }
    }

    // номер телефона аварийной службы ---------------
    private void numberPhoneEmergencyService(Document doc, PdfFont font) {
        Paragraph p1 = new Paragraph("Телефон цілодобової аварійної служби ОСББ \"Свободи 51\": ");
        p1.setFontSize(12).setMarginBottom(0).setFont(font);
        doc.add(p1);
        Paragraph p2 = new Paragraph("097-659-29-10");
        p2.setFontSize(14).setUnderline().setMarginTop(0).setBold().setFont(font);
        doc.add(p2);
    }

    // обращение к жильцам дома ----------------
    private void appealToTheResidentsOfTheHouse(String text, Document doc, PdfFont font) {
        Paragraph pf = new Paragraph(text);
        pf.setFont(font);
        pf.setTextAlignment(TextAlignment.CENTER).setFontSize(14).setBold().setMarginBottom(20).setMarginTop(20);
        doc.add(pf);
    }

    // заголовки в таблице debt details --------------------------
    private void fillListCellFirstRowDebtDetails(Table table, PdfFont font) {
        for (String line : TextsAndLists.forTableDebtDetails) {
            Cell cell = new Cell();
            cell.add(line).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font);
            table.addCell(cell);
        }
    }


    // sorted ----------
    private Comparator<Debt> comparatorByBill() {
        return (a, b) -> Integer.parseInt(a.getHeader().getBill())
                - Integer.parseInt(b.getHeader().getBill());
    }

    //Округление дробной части до 2-х запятых
    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    private String getString(Double summa) {
        return summa > 0 ? "Задолженость : " + summa : "Переплата : " + summa;
    }


    //    Paragraph p = new Paragraph();
//p.add("The beginning of the line ");
//p.add(new Text("          (fill in your name)          ").setTextRise(-10).setUnderline().setFontSize(8));
//p.add(" end of the line");
    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

    private void dateTimeNow(Document doc, PdfFont font) {
        String time = LocalDateTime.now().toString().replace("T", ", текущее время :  ");
        time = "Текущая дата : " + time.substring(0, time.indexOf("."));
        Paragraph p = new Paragraph(time);
        doc.add(p.setFont(font).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
    }
}
