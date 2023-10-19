package com.example.osbb.service.pdf;

import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.DebtDetails;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.InvoiceNotification;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.service.payment.IPaymentService;
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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PdfService implements IPdfService {
    @Autowired
    IPaymentService iPaymentService;
    @Autowired
    OwnershipDAO ownershipDAO;

    // debt --------------------
    @Override
    public Object printPdfDebtByApartment(InvoiceNotification in) {
        printPdfFile(in);
        return Response
                .builder()
                .data(null)
                .messages(List.of("PDF файл напечатан успешно."))
                .build();
    }

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

    // debt details ---------------------------
    @Override
    public Object printPdfDebtDetailsByApartment(String apartment) {
        DebtDetails debtDetails = iPaymentService.getDetailsDebtInvoiceNotificationByApartment(apartment);
        PdfWriter writer = null;
        try {
            writer = new PdfWriter("D:/pdf/payment_details_" + apartment + ".pdf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        // заголовок -----------------------
        String text = "Детализация долга за услуги по управлению ОСББ по помещению № " + apartment;
        Paragraph header = new Paragraph(text);
        header.setTextAlignment(TextAlignment.CENTER).setFont(font);
        doc.add(header);
        // адресс -----------------------
        String str = debtDetails.getHeader().getCurrentTime().toString();
        List<String> listParagraph = List.of(
                "51931, Украина, Днепропетровская область, Каменское, Свободы, дом № 51, кв. № "
                        + apartment,
                "Лицевой счёт № " + debtDetails.getHeader().getBill(),
                "Общая площадь помещения, на которую начисляется оплата: "
                        + debtDetails.getHeader().getArea() + " м2.",
                "Текущая дата: " + str.substring(0, str.indexOf("T")),
                "Текущее время: " + str.substring(str.indexOf("T") + 1, str.indexOf('.'))
        );
        // параграфы -----------------------
        for (String line : listParagraph) {
            Paragraph paragraph = new Paragraph(line);
            paragraph.setFont(font).setFontSize(10).setMarginTop(0).setMarginBottom(0);
            doc.add(paragraph);
        }

        // таблица ----------------------------

        float[] pointColumnWidths = {40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F};
        Table table = new Table(pointColumnWidths);
        table.setMarginTop(8).setMarginBottom(8);

        // первая линия --------------------
        List<String> listCellOne = List.of(
                "Начальный период", "Долг на начало периода, грн", "Текущий тариф, грн/м2", "Начис- лено, грн",
                "Перерасчёт за недополучен- ные услуги, грн", "Монетиза- ция субсидий, грн", "Монетиза- ция льгот, грн",
                "Оплачено, грн", "Долг на конец период, грн", "  Конечный период  "
        );
        for (String line : listCellOne) {
            Cell cell = new Cell();
            cell.add(line).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font);
            table.addCell(cell);
        }

        // вторая линия и далее -----------------------------
        debtDetails.getList().forEach(
                el -> {
                    PdfFont font2 = null;
                    try {
                        font2 = PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    List<String> listCellTwo = List.of(
                            el.getBeginningPeriod().toString(),
                            el.getDebtAtBeginningPeriod().toString(),
                            el.getRate().toString(),
                            el.getAccrued().toString(),
                            el.getRecalculationForServicesNotReceived().toString(),
                            el.getSubsidyMonetization().toString(),
                            el.getMonetizationBenefits().toString(),
                            el.getPaid().toString(),
                            el.getDebtAtFinalizingPeriod().toString(),
                            el.getFinalizingPeriod().toString()
                    );
                    for (String line : listCellTwo) {
                        Cell cell = new Cell();
                        cell.add(line).setTextAlignment(TextAlignment.CENTER).setFont(font2).setFontSize(9);
                        table.addCell(cell);
                    }
                }
        );

        doc.add(table);


        doc.close();
        return new ResponseMessages(List.of("Все файлы распечатаны успешно"));
    }

    @Override
    public Object printPdfDebtDetailsAllApartment() {
        return null;
    }

    // private help functions -----------------
    private void printPdfFile(InvoiceNotification in) {
        try {
            PdfWriter writer = new PdfWriter("D:/pdf/payment_" + in.getHeader().getBill() + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            writeOnePdfObject(in, doc);
            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeOnePdfObject(InvoiceNotification in, Document doc) {
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // начало процесса -------------
        String str1 = in.getHeader().getCurrentTime().toString();
        String str2 = in.getHeader().getCurrentTime().toString();
        List<String> listParagraph = List.of(
                "51931, Украина, Днепропетровская область, Каменское, Свободы, дом № 51, кв. № "
                        + in.getHeader().getAddress().getApartment(),
                "Лицевой счёт № " + in.getHeader().getBill(),
                "Общая площадь помещения, на которую начисляется оплата: "
                        + in.getHeader().getArea() + " м2.",
                "Текущая дата: " + str1.substring(0, str1.indexOf("T")),
                "Текущее время: " + str2.substring(str2.indexOf("T") + 1, str2.indexOf('.'))
        );

        // заголовок -----------------------
        String text = "Текущее сальдо по оплате за услуги по управлению ОСББ";
        Paragraph header = new Paragraph(text);
        header.setTextAlignment(TextAlignment.CENTER).setFont(font);
        doc.add(header);

        // параграфы -----------------------
        for (String line : listParagraph) {
            Paragraph paragraph = new Paragraph(line);
            paragraph.setFont(font).setFontSize(10).setMarginTop(0).setMarginBottom(0);
            doc.add(paragraph);
        }
        // таблица ----------------------------

        float[] pointColumnWidths = {40F, 40F, 40F, 40F, 40F, 40F, 40F};
        Table table = new Table(pointColumnWidths);
        table.setMarginTop(8).setMarginBottom(8);

        // первая линия --------------------
        List<String> listCellOne = List.of(
                "Долг на \n " + in.getBody().getBeginningPeriod() + " составляет, грн",
                "Текущий тариф, грн/м2", "Начислено, грн", "Монетизация субсидий, грн", "Монетизация льгот, грн",
                "Оплачено, грн", "Долг на \n" + in.getBody().getFinalizingPeriod() + " составляет, грн"
        );
        for (String line : listCellOne) {
            Cell cell = new Cell();
            cell.add(line).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setFont(font);
            table.addCell(cell);
        }

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
        // окончание процесса ------------------
    }

    // sorted ----------
    private Comparator<InvoiceNotification> comparatorByApartment() {
        return (a, b) -> Integer.parseInt(a.getHeader().getAddress().getApartment())
                - Integer.parseInt(b.getHeader().getAddress().getApartment());
    }
}
