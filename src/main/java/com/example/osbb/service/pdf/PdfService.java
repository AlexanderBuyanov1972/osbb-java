package com.example.osbb.service.pdf;

import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.HeaderInvoiceNotification;
import com.example.osbb.dto.response.EntryBalanceHouse;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.service.payment.IPaymentService;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PdfService implements IPdfService {
    @Autowired
    IPaymentService iPaymentService;
    @Autowired
    OwnershipDAO ownershipDAO;

    // debt -----------------------------------------------------------------------------------------------
    // печатать задолженность за последний календарный месяц по номеру помещения в pdf файл
    @Override
    public Object printPdfDebtByApartment(InvoiceNotification in) {
        printPdfFile(in);
        return Response
                .builder()
                .data(null)
                .messages(List.of("PDF файл напечатан успешно."))
                .build();
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в pdf файл каждый отдельно
    @Override
    public Object printListPdfDebtAllApartment() {
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDebtInvoiceNotificationByApartment(
                            el.getAddress().getApartment()))
                    .forEach(this::printPdfFile);
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // печатать задолженности за последний календарный месяц по всем номерам помещений в один pdf файл
    @Override
    public Object printAllTInOnePdfDebtAllApartment() {
        try {
            AtomicInteger count = new AtomicInteger(1);
            PdfWriter writer = new PdfWriter("D:/pdf/paymentAllToOne.pdf");
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
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // debt details -------------------------------------------------------------------------------------------
    // печатать по номеру помещения детализированный долг от начальной точки до текущего месяца один pdf файл
    @Override
    public Object printPdfDebtDetailsByApartment(String apartment) {
        DebtDetails debtDetails = iPaymentService.getDetailsDebtInvoiceNotificationByApartment(apartment);
        printPdfDetailsFile(debtDetails);
        return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
    }

    // печатать детализированный долг от начальной точки до текущего месяца по каждому помещению в отдельный файл
    @Override
    public Object printPdfDebtDetailsAllApartment() {
        try {
            ownershipDAO.findAll()
                    .stream()
                    .map(el -> iPaymentService.getDetailsDebtInvoiceNotificationByApartment(
                            el.getAddress().getApartment()))
                    .forEach(this::printPdfDetailsFile);
            return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
    }

    // печатать баланс дома по помещениям (задолженность/переплата)
    @Override
    public Object printPdfBalanceHouse() {
        try {
            List<EntryBalanceHouse> list = iPaymentService.getListEntryBalanceHouse();
            Double summa = formatDoubleValue(list.stream().mapToDouble(EntryBalanceHouse::getSumma).reduce(0, (a, b) -> a + b));
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
        } catch (Exception e) {
            return new ErrorResponseMessages(List.of(e.getMessage()));
        }
        return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
    }

    // private help functions -----------------
    private void printPdfFile(InvoiceNotification in) {
        try {
            PdfWriter writer = new PdfWriter("D:/pdf/payments/payment_" + in.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObject(in, doc);
            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void printPdfDetailsFile(DebtDetails details) {
        try {
            PdfWriter writer = new PdfWriter("D:/pdf/payments_details/payment_details_" + details.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObjectDetails(details, doc);
            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // формирование объекта задолженности
    private void writeOnePdfObject(InvoiceNotification in, Document doc) {
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
    }

    // формирование детализации объекта задолженность
    public void writeOnePdfObjectDetails(DebtDetails debtDetails, Document doc) {
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
    }

    // отдельные элементы для конструирования файла pdf
    // шапка для квитанций
    private void fillHeaderPdfFile(HeaderInvoiceNotification header, Document doc, PdfFont font) {
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
    }

    // заголовок
    private void createHeader(String text, Document doc, PdfFont font) {
        Paragraph header = new Paragraph(text);
        header.setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(13);
        doc.add(header);
    }

    // font
    private PdfFont createFont() {
        try {
            return PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // заголовки в таблице debt
    private void fillListCellFirstRowDebt(Table table, PdfFont font, String beginDate, String finalDate) {
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
    }

    // заголовки в таблице debt details
    private void fillListCellFirstRowDebtDetails(Table table, PdfFont font) {
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
}
