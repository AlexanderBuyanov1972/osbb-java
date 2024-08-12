package com.example.osbb.service.pdf;

import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.ApartmentBillFullNamePhoneNumber;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderDebt;
import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.enums.TypeOfAnswer;
import com.example.osbb.service.payment.IPaymentService;
import com.example.osbb.service.survey.ISurveyService;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class PdfService implements IPdfService {

    private final String PRINT_SUCCESSFULLY = "Все PDF файлы напечатаны успешно, смотрите в папке D:/pdf";
    @Autowired
    IPaymentService iPaymentService;
    @Autowired
    ISurveyService iSurveyService;
    @Autowired
    OwnershipDAO ownershipDAO;

    // debt -----------------------------------------------------------------------------------------------
    // печатать задолженность за последний календарный месяц по номеру помещения в pdf файл
    @Override
    public ResponseEntity<?> printDebt(DebtDetails in) {
        try {
            printPdfFile(in);
            return ResponseEntity.ok(new Response(List.of(PRINT_SUCCESSFULLY + "/payments")));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
        }

    }

    // формирование объекта задолженности
    private void writeOnePdfObject(DebtDetails in, Document doc) {
        try {
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
                    in.getListBody().get(0).getBeginningPeriod().toString(),
                    in.getListBody().get(0).getFinalizingPeriod().toString());
            // вторая линия -----------------------------
            List<String> listCellTwo = List.of(
                    in.getListBody().get(0).getDebtAtBeginningPeriod().toString(),
                    in.getListBody().get(0).getRate().toString(),
                    in.getListBody().get(0).getAccrued().toString(),
                    in.getListBody().get(0).getSubsidyMonetization().toString(),
                    in.getListBody().get(0).getMonetizationBenefits().toString(),
                    in.getListBody().get(0).getPaid().toString(),
                    in.getListBody().get(0).getDebtAtFinalizingPeriod().toString()
            );

            for (String line : listCellTwo)
                table.addCell(new Cell().add(new Paragraph(line)).setTextAlignment(TextAlignment.CENTER).setFont(font));

            doc.add(table);
        } catch (Exception error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }


    // печатать задолженности за последний календарный месяц по всем номерам помещений в pdf файл каждый отдельно
    @Override
    public ResponseEntity<?> printAllDebt() {
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtByBill(
                            el.getBill()))
                    .forEach(this::printPdfFile);
            return ResponseEntity.ok(new Response(List.of(PRINT_SUCCESSFULLY + "/payments")));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }

    private void printPdfFile(DebtDetails in) {
        try {
            String path = "D:/pdf/payments";
            checkDir(path);
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/payment_"
                    + in.getHeader().getBill() + ".pdf")));
            writeOnePdfObject(in, doc);
            doc.close();
        } catch (FileNotFoundException error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в один pdf файл
    @Override
    public ResponseEntity<?> printAllInOneDebtAllApartment() {
        try {
            String path = "D:/pdf/allInOne";
            checkDir(path);
            AtomicInteger count = new AtomicInteger(1);
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/paymentAllInOne.pdf")));
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
            return ResponseEntity.ok(new Response(List.of(PRINT_SUCCESSFULLY + "/allInOne")));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }

    // debt details -------------------------------------------------------------------------------------------
    // печатать по номеру помещения детализированный долг от начальной точки до текущего месяца один pdf файл
    @Override
    public ResponseEntity<?> printDebtDetails(Long id) {
        try {
            Ownership ownership = ownershipDAO.findById(id).orElse(null);
            if (ownership == null) {
                String message = "Помещение с ID : " + id + " не найдено," +
                        " распечатать детализированный долг не представляется возможным";
                log.info(message);
                return ResponseEntity.badRequest().body(new Response(List.of(message)));
            }
            DebtDetails dd = iPaymentService.getDetailsDebtByBill(ownership.getBill());
            printDetailsFile(dd);
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of(PRINT_SUCCESSFULLY + "/payments_details")));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }

    // печатать детализированный долг от начальной точки до текущего месяца по каждому помещению в отдельный файл
    @Override
    public ResponseEntity<?> printAllDebtDetails() {
        try {
            ownershipDAO.findAll().stream().map(el -> iPaymentService.getDetailsDebtByBill(
                    el.getBill())).forEach(this::printDetailsFile);
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of(PRINT_SUCCESSFULLY + "/payments_details")));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }

    private void printDetailsFile(DebtDetails details) {
        try {
            String path = "D:/pdf/payments_details";
            checkDir(path);
            PdfWriter writer = new PdfWriter(path + "/payment_details_"
                    + details.getHeader().getBill() + ".pdf");
            Document doc = new Document(new PdfDocument(writer));
            writeOnePdfObjectDetails(details, doc);
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
        } catch (IOException error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // опросы ------------------------------------------------------------------------------------------
    // печатать результаты опросов по теме -------------------
    @Override
    public ResponseEntity<?> printResultSurvey(String title) {
        try {
            ResultSurvey result = iSurveyService.getResultSurveyByTitleForPrint(title);
            String path = "D:/pdf/survey_result";
            checkDir(path);
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/" + title + ".pdf")));
            PdfFont font = createFont();
            Color teal = new DeviceRgb(0, 128, 128);
            Color blueViolet = new DeviceRgb(138, 43, 226);
            // start ------------------
            createHeaderColor(blueViolet, "Тема опроса : " + title, doc, font);
            // 1 ---------------
            createHeader("Обработано " + result.getProcessingPercentageArea()
                    + "% в метрах квадратных", doc, font);
            createHeader("Всего проголосовало : " + result.getAreaVoted()
                    + " из " + result.getSummaArea()
                    + " метров квадратных", doc, font);
            List<String> listNames = List.of("ЗА : ", "ПРОТИВ : ", "ВОЗДЕРЖАЛИСЬ : ");
            for (String key : result.getMapVotedArea().keySet()) {
                doc.add(new Paragraph(key)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(teal));
                for (String text : listNames) {
                    Paragraph line = new Paragraph(text
                            + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.BEHIND)));
                    doc.add(line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10));
                }
            }
            // 2 ------------
            createHeader("Обработано " + result.getProcessingPercentageOwner()
                    + "% в голосах собственников", doc, font);
            createHeader("Всего проголосовало : " + result.getOwnerVoted()
                    + " из " + result.getCountOwner()
                    + " собственников", doc, font);
            for (String key : result.getMapVotedOwner().keySet()) {

                doc.add(new Paragraph(key)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFont(font)
                        .setFontSize(12)
                        .setFontColor(teal));

                for (String text : listNames) {
                    Paragraph line = new Paragraph(text
                            + formatDoubleValue(result.getMapVotedArea().get(key).get(TypeOfAnswer.BEHIND)));
                    doc.add(line.setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(10));
                }
            }
            // finish -----------------
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of(title, PRINT_SUCCESSFULLY + "/survey_result")));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }


    // формирование детализации объекта задолженность
    public void writeOnePdfObjectDetails(DebtDetails debtDetails, Document doc) throws IOException {
        String text = "Детализация долга за услуги по управлению ОСББ";
        PdfFont font = createFont();
        createHeader(text, doc, font);
        fillHeaderFile(debtDetails.getHeader(), doc, font);
        float[] pointColumnWidths = {40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F};
        Table table = new Table(pointColumnWidths);
        table.setMarginTop(8).setMarginBottom(8);
        fillListCellFirstRowDebtDetails(table, font);
        debtDetails.getListBody().forEach(
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
                    for (String line : listCellTwo)
                        table.addCell(new Cell().add(new Paragraph(line))
                                .setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(9));
                }
        );
        doc.add(table);
    }

    @Override
    public void printQueryListApartmentBillFullNamePhoneNumber(List<ApartmentBillFullNamePhoneNumber> list) {
        try {
            String path = "D:/pdf/queries";
            checkDir(path);
            String text = "Список собственников дома Свободы - 51";
            Document doc = new Document(new PdfDocument(new PdfWriter(path + text + ".pdf")));
            PdfFont font = createFont();
            dateTimeNow(doc, font);
            log.info("Создаём заголовок");
            createHeader(text, doc, font);
            log.info("Создаём таблицу");
            float[] pointColumnWidths = {50F, 100F, 400F, 100F};
            Table table = new Table(pointColumnWidths);
            table.setMarginTop(10).setMarginBottom(10);
            log.info("Заполняем заголовки таблицы");
            for (String line : List.of("Помещение №", "Лицевой счёт", "Ф.И.О.", "Номер телефона"))
                table.addCell(new Cell().add(new Paragraph(line)).setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFont(font));
            log.info("Заполняем строки таблицы");
            list.forEach(
                    el -> {
                        for (String line : List.of(el.getApartment(), el.getBill(), el.getFullName(), el.getPhoneNumber()))
                            table.addCell(new Cell().add(new Paragraph(line))
                                    .setTextAlignment(TextAlignment.CENTER)
                                    .setFont(font)
                                    .setFontSize(9));
                    }
            );
            log.info("Перед сохранением таблицы в DOC");
            doc.add(table);
            log.info("После сохранения таблицы в DOC");
            doc.close();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAppealListText(List<String> list, Document doc, PdfFont font) {
        for (String line : list)
            doc.add(new Paragraph().setFont(font).add(line).setBold().setFontSize(13));
    }

    // *************************** вспомогательные функции ******************************
    // отдельные элементы для конструирования файла pdf
    // шапка для квитанций
    private void fillHeaderFile(HeaderDebt header, Document doc, PdfFont font) {
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
        for (String line : list)
            doc.add(new Paragraph(line).setFont(font).setFontSize(10).setMarginTop(0).setMarginBottom(0));
    }

    // заголовки в таблице debt
    private void fillListCellFirstRowDebt(Table table, PdfFont font, String beginDate, String finalDate) {
        List<String> list = List.of(
                "Долг на \n " + beginDate + " составляет, грн",
                "Текущий тариф, грн/м2", "Начислено, грн", "Монетизация субсидий, грн", "Монетизация льгот, грн",
                "Оплачено, грн", "Долг на \n" + finalDate + " составляет, грн"
        );
        for (String line : list)
            table.addCell(new Cell().add(new Paragraph(line)).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font));
    }

    private void createHeaderColor(Color blueViolet, String text, Document doc, PdfFont font) {
        doc.add(new Paragraph(text).setTextAlignment(TextAlignment.CENTER)
                .setFont(font).setFontSize(13).setFontColor(blueViolet));
    }

    // создание директории, например ("D:/pdf/queries")  ------------------------
    private void checkDir(String str) {
        try {
            String[] lines = str.split("/");
            String path = lines[0];
            for (int i = 1; i < lines.length; i++) {
                path = path + "/" + lines[i];
                File folder = new File(path);
                if (!folder.exists())
                    folder.mkdir();
            }
        } catch (Exception error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    // заголовок ---------------------------------
    private void createHeader(String text, Document doc, PdfFont font) {
        doc.add(new Paragraph(text)
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(font)
                .setFontSize(14)
                .setBold());
    }

    // лист сообщений - перечень ----------------------------------------
    public void createListText(List<String> list, Document doc, PdfFont font) {
        for (String line : list)
            doc.add(new Paragraph().setFont(font).add(line));
    }

    // font -----------------------------------
    private PdfFont createFont() throws IOException {
        return PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251");
    }

    // расчётный счёт для оплаты за услуги ОСББ ------------------
    public void bankAccountForPayment(Document doc, PdfFont font) {
        List<Paragraph> list2 = new ArrayList<>();
        list2.add(new Paragraph("Реквізити:")
                .setFontSize(12)
                .setUnderline()
                .setMarginTop(20)
                .setBold());

        list2.add(new Paragraph().add(new Text("Отримувач: ").setFontSize(12).setBold()).add("ОСББ \"Свободи 51\""));
        list2.add(new Paragraph().add("Код ЄДРПОУ: ").add(new Text("44987443,").setUnderline()));
        list2.add(new Paragraph("МФО 305299"));
        list2.add(new Paragraph()
                .add("р/р ")
                .add(new Text("UA 9130 5299 0000 0260 0005 0586 588    в АТ КБ \"ПРИВАТ БАНК\"").setUnderline()));
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
        for (Paragraph paragraph : list2)
            doc.add(paragraph.setFont(font));
    }

    // номер телефона аварийной службы ---------------
    public void numberPhoneEmergencyService(Document doc, PdfFont font) {
        doc.add(new Paragraph("Телефон цілодобової аварійної служби ОСББ \"Свободи 51\": ")
                .setFontSize(12).setMarginBottom(0).setFont(font));
        doc.add(new Paragraph("097-659-29-10")
                .setFontSize(14).setUnderline().setMarginTop(0).setBold().setFont(font));
    }

    // обращение к жильцам дома ----------------
    public void appealToTheResidentsOfTheHouse(String text, Document doc, PdfFont font) {
        doc.add(new Paragraph(text)
                .setFont(font)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14)
                .setBold()
                .setMarginBottom(20)
                .setMarginTop(20));
    }

    // заголовки в таблице debt details --------------------------
    private void fillListCellFirstRowDebtDetails(Table table, PdfFont font) {
        for (String line : TextsAndLists.forTableDebtDetails)
            table.addCell(new Cell().add(new Paragraph(line)).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font));
    }

    // sorted ----------
    private Comparator<DebtDetails> comparatorByBill() {
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

    public void dateTimeNow(Document doc, PdfFont font) {
        String time = LocalDateTime.now().toString().replace("T", ", текущее время :  ");
        time = "Текущая дата : " + time.substring(0, time.indexOf("."));
        doc.add(new Paragraph(time).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
    }
}
