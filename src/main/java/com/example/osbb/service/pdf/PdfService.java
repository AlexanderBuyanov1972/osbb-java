package com.example.osbb.service.pdf;

import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderInvoiceNotification;
import com.example.osbb.dto.ResultQuestionnaire;
import com.example.osbb.dto.response.EntryBalanceHouse;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.enums.TypeOfAnswer;
import com.example.osbb.service.payment.IPaymentService;
import com.example.osbb.service.questionnaire.IQuestionnaireService;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PdfService implements IPdfService {
    private static final Logger log = Logger.getLogger(PdfService.class);
    @Autowired
    IPaymentService iPaymentService;
    @Autowired
    IQuestionnaireService iQuestionnaireService;
    @Autowired
    OwnershipDAO ownershipDAO;

    // debt -----------------------------------------------------------------------------------------------
    // печатать задолженность за последний календарный месяц по номеру помещения в pdf файл
    @Override
    public Object printPdfDebtByApartment(InvoiceNotification in) {
        log.info("Method printPdfDebtByApartment : enter");
        try {
            printPdfFile(in);
            log.info("Method printPdfDebtByApartment : exit");
            return Response
                    .builder()
                    .data(null)
                    .messages(List.of("PDF файл напечатан успешно."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в pdf файл каждый отдельно
    @Override
    public Object printListPdfDebtAllApartment() {
        log.info("Method printListPdfDebtAllApartment : enter");
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtInvoiceNotificationByApartment(
                            el.getAddress().getApartment()))
                    .forEach(this::printPdfFile);
            log.info("Method printListPdfDebtAllApartment : exit");
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    private void printPdfFile(InvoiceNotification in) {
        try {
            checkDir("D:/pdf/payments");
            PdfWriter writer = new PdfWriter("D:/pdf/payments/payment_" + in.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObject(in, doc);
            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в один pdf файл
    @Override
    public Object printAllTInOnePdfDebtAllApartment() {
        log.info("Method printAllTInOnePdfDebtAllApartment : enter");
        try {
            checkDir("D:/pdf/allInOne");
            AtomicInteger count = new AtomicInteger(1);
            PdfWriter writer = new PdfWriter("D:/pdf/allInOne/paymentAllInOne.pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtInvoiceNotificationByApartment(
                            el.getAddress().getApartment()))
                    .sorted(comparatorByApartment())
                    .forEach(el -> {
                        writeOnePdfObject(el, doc);
                        if (count.get() % 4 == 0)
                            doc.add(new AreaBreak());
                        count.getAndIncrement();
                    });
            doc.close();
            log.info("Все файлы распечатаны успешно");
            log.info("Method printAllTInOnePdfDebtAllApartment : exit");
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // debt details -------------------------------------------------------------------------------------------
    // печатать по номеру помещения детализированный долг от начальной точки до текущего месяца один pdf файл
    @Override
    public Object printPdfDebtDetailsByApartment(String apartment) {
        log.info("Method printPdfDebtDetailsByApartment : enter");
        try {
            DebtDetails debtDetails = iPaymentService.getDetailsDebtInvoiceNotificationByApartment(apartment);
            printPdfDetailsFile(debtDetails);
            log.info("Все файлы распечатаны успешно");
            log.info("Method printPdfDebtDetailsByApartment : exit");
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // печатать детализированный долг от начальной точки до текущего месяца по каждому помещению в отдельный файл
    @Override
    public Object printPdfDebtDetailsAllApartment() {
        log.info("Method printPdfDebtDetailsAllApartment : enter");
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDetailsDebtInvoiceNotificationByApartment(
                            el.getAddress().getApartment()))
                    .forEach(this::printPdfDetailsFile);
            log.info("Все файлы распечатаны успешно");
            log.info("Method printPdfDebtDetailsAllApartment : exit");
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    private void printPdfDetailsFile(DebtDetails details) {
        log.info("Method printPdfDetailsFile : enter");
        try {
            checkDir("D:/pdf/payments_details");
            PdfWriter writer = new PdfWriter("D:/pdf/payments_details/payment_details_" + details.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObjectDetails(details, doc);
            doc.close();
            log.info("Все файлы распечатаны успешно");
            log.info("Method printPdfDetailsFile : exit");
        } catch (FileNotFoundException error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // balance ------------------------------------------------------------------------------------------------
    // печатать баланс дома по помещениям (задолженность/переплата)
    @Override
    public Object printPdfBalanceHouse() {
        log.info("Method printPdfBalanceHouse : enter");
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
            log.info("Все файлы распечатаны успешно");
            log.info("Method printPdfBalanceHouse : exit");
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // questionnaire ------------------------------------------------------------------------------------------
    // questionnaire result print -------------------
    @Override
    public Object printResultQuestionnaire(String title) {
        log.info("Method printResultQuestionnaire : enter");
        try {
            ResultQuestionnaire result = iQuestionnaireService.getResultPoolByTitle(title);
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
            log.info("Все файлы распечатаны успешно");
            log.info("Method printResultQuestionnaire : exit");
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // формирование объекта задолженности
    private void writeOnePdfObject(InvoiceNotification in, Document doc) {
        try {
            log.info("Method writeOnePdfObject : enter");
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
            log.info("Method writeOnePdfObject : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // формирование детализации объекта задолженность
    public void writeOnePdfObjectDetails(DebtDetails debtDetails, Document doc) {
        log.info("Method writeOnePdfObjectDetails : enter");
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
            log.info("Method writeOnePdfObjectDetails : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // отдельные элементы для конструирования файла pdf
    // шапка для квитанций
    private void fillHeaderPdfFile(HeaderInvoiceNotification header, Document doc, PdfFont font) {
        log.info("Method fillHeaderPdfFile : enter");
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
            log.info("Method fillHeaderPdfFile : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // заголовок
    private void createHeader(String text, Document doc, PdfFont font) {
        log.info("Method fillHeaderPdfFile : enter");
        try {
            Paragraph header = new Paragraph(text);
            header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(13);
            doc.add(header);
            log.info("Method fillHeaderPdfFile : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private void createHeaderBlueviolet(String text, Document doc, PdfFont font) {
        log.info("Method createHeaderBlueviolet : enter");
        try {
            Color blueviolet = new DeviceRgb(138, 43, 226);
            Paragraph header = new Paragraph(text);
            header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(13).setFontColor(blueviolet);
            doc.add(header);
            log.info("Method createHeaderBlueviolet : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // заголовки в таблице debt
    private void fillListCellFirstRowDebt(Table table, PdfFont font, String beginDate, String finalDate) {
        log.info("Method fillListCellFirstRowDebt : enter");
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
        log.info("Method fillListCellFirstRowDebt : exit");
    }

    // заголовки в таблице debt details
    private void fillListCellFirstRowDebtDetails(Table table, PdfFont font) {
        log.info("Method fillListCellFirstRowDebtDetails : enter");
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
        log.info("Method fillListCellFirstRowDebtDetails : exit");
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
        try {
            log.info("Method checkDir : enter");
            String[] lines = str.split("/");
            String path = lines[0];
            for (int i = 1; i < lines.length; i++) {
                path = path + "/" + lines[i];
                File folder = new File(path);
                if (!folder.exists())
                    folder.mkdir();
            }
            log.info("Method checkDir : exit");
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    @Override
    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    public Object fillPdfNewBillForPayServiceOSBB() {
        try {
            log.info("Method fillPdfNewBillForPayServiceOSBB : enter");
            printPdfNewBillForPayServiceOSBB();
            log.info("Method fillPdfNewBillForPayServiceOSBB : exit");
            return Response
                    .builder()
                    .data(null)
                    .messages(List.of("PDF файл напечатан успешно."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    private void printPdfNewBillForPayServiceOSBB() {
        try {
            log.info("Method printPdfNewBillForPayServiceOSBB : enter");
            checkDir("D:/pdf/for_all");
            PdfWriter writer = new PdfWriter("D:/pdf/for_all/" + "new_bill" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            List<Paragraph> list = new ArrayList<>();
            list.add(new Paragraph("Голова правління ОСББ \"Свободи 51\" ___________________________ Олена ПАСЬКО"));
            list.add(new Paragraph("М.П.")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20)
                    .setMarginRight(30)
                    .setMarginBottom(10)
                    .setFontSize(12));
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
            log.info("Method printPdfNewBillForPayServiceOSBB : exit");
        } catch (FileNotFoundException error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    // font
    private PdfFont createFont() {
        log.info("Method createFont : enter");
        try {
            return PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
        } catch (IOException error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

//    Paragraph p = new Paragraph();
//p.add("The beginning of the line ");
//p.add(new Text("          (fill in your name)          ").setTextRise(-10).setUnderline().setFontSize(8));
//p.add(" end of the line");

}
