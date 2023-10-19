package com.example.osbb.service.pdf;

import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.InvoiceNotification;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.service.payment.IPaymentService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService implements IPdfService {
    @Autowired
    IPaymentService iPaymentService;
    @Autowired
    OwnershipDAO ownershipDAO;

    @Override
    public Object printPdfDebtByApartment(InvoiceNotification invoiceNotification) {
        printPdfFile(invoiceNotification);
        return Response
                .builder()
                .data(null)
                .messages(List.of("PDF файл напечатан успешно."))
                .build();
    }

    private void printPdfFile(InvoiceNotification invoiceNotification) {
        // Creating a PdfWriter object
        String dest = "D:/pdf/payment_" + invoiceNotification.getHeader().getBill() + ".pdf";
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(dest);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfDocument pdfDoc = new PdfDocument(writer);
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Document doc = new Document(pdfDoc);
        // заголовок -----------------------
        String text1 = "Задолженость по оплате за услуги по управлению ОСББ";
        Paragraph paragraph1 = new Paragraph(text1);
        paragraph1.setFont(font);
        doc.add(paragraph1);

        String text2 = "51931, Украина, Днепропетровская область, Каменское";
        Paragraph paragraph2 = new Paragraph(text2);
        paragraph2.setFont(font);
        doc.add(paragraph2);

        String text23 = "Свободы, дом № 51, кв. № " + invoiceNotification.getHeader().getAddress().getApartment();
        Paragraph paragraph23 = new Paragraph(text23);
        paragraph23.setFont(font);
        doc.add(paragraph23);

        String text3 = "Лицевой счёт № " + invoiceNotification.getHeader().getBill();
        Paragraph paragraph3 = new Paragraph(text3);
        paragraph3.setFont(font);
        doc.add(paragraph3);

        String text4 = "Общая площадь помещения, на которую начисляется оплата: "
                + invoiceNotification.getHeader().getArea() + " м2.";
        Paragraph paragraph4 = new Paragraph(text4);
        paragraph4.setFont(font);
        doc.add(paragraph4);

        String str1 = invoiceNotification.getHeader().getCurrentTime().toString();
        String text5 = " Текущая дата: " + str1.substring(0, str1.indexOf("T"));
        Paragraph paragraph5 = new Paragraph(text5);
        paragraph5.setFont(font);
        doc.add(paragraph5);

        String str2 = invoiceNotification.getHeader().getCurrentTime().toString();
        String text6 = " Текущее время: " + str2.substring(str2.indexOf("T") + 1, str2.indexOf('.'));
        Paragraph paragraph6 = new Paragraph(text6);
        paragraph6.setFont(font);
        doc.add(paragraph6);

        // таблица ----------------------------

        float[] pointColumnWidths = {40F, 40F, 40F, 40F, 40F, 40F, 40F, 40F};
        Table table = new Table(pointColumnWidths);
        // первая линия --------------------
        Cell cell1 = new Cell();
        cell1.add("Долг на " + invoiceNotification.getBody().getBeginningPeriod() + " составляет, грн");
        table.addCell(cell1.setFont(font));

        Cell cell2 = new Cell();
        cell2.add("Текущий тариф, грн/м2");
        table.addCell(cell2.setFont(font));

        Cell cell3 = new Cell();
        cell3.add("Начислено, грн");
        table.addCell(cell3.setFont(font));

        Cell cell4 = new Cell();
        cell4.add("Перерасчёт за недополу- ченные услуги, грн");
        table.addCell(cell4.setFont(font));

        Cell cell5 = new Cell();
        cell5.add("Монетиза- ция субсидий, грн");
        table.addCell(cell5.setFont(font));

        Cell cell6 = new Cell();
        cell6.add("Монетиза- ция льгот, грн");
        table.addCell(cell6.setFont(font));

        Cell cell7 = new Cell();
        cell7.add("Оплачено, грн");
        table.addCell(cell7.setFont(font));

        Cell cell8 = new Cell();
        cell8.add("Долг на " + invoiceNotification.getBody().getFinalizingPeriod() + " составляет, грн");
        table.addCell(cell8.setFont(font));
        // вторая линия -----------------------------
        Cell cell9 = new Cell();
        cell9.add(invoiceNotification.getBody().getDebtAtBeginningPeriod().toString());
        table.addCell(cell9.setFont(font));

        Cell cell10 = new Cell();
        cell10.add(invoiceNotification.getBody().getRate().toString());
        table.addCell(cell10.setFont(font));

        Cell cell11 = new Cell();
        cell11.add(invoiceNotification.getBody().getAccrued().toString());
        table.addCell(cell11.setFont(font));

        Cell cell12 = new Cell();
        cell12.add(invoiceNotification.getBody().getRecalculationForServicesNotReceived().toString());
        table.addCell(cell12.setFont(font));

        Cell cell13 = new Cell();
        cell13.add(invoiceNotification.getBody().getSubsidyMonetization().toString());
        table.addCell(cell13.setFont(font));

        Cell cell14 = new Cell();
        cell14.add(invoiceNotification.getBody().getMonetizationBenefits().toString());
        table.addCell(cell14.setFont(font));

        Cell cell15 = new Cell();
        cell15.add(invoiceNotification.getBody().getPaid().toString());
        table.addCell(cell15.setFont(font));

        Cell cell16 = new Cell();
        cell16.add(invoiceNotification.getBody().getDebtAtFinalizingPeriod().toString());
        table.addCell(cell16.setFont(font));

        doc.add(table);
        doc.close();
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
}
