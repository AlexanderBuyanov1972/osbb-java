package com.example.osbb.service.queries;

import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.RecordDAO;
import com.example.osbb.dto.ApartmentBillFullNamePhoneNumber;
import com.example.osbb.dto.ApartmentHeatSupply;
import com.example.osbb.dto.EntryBalanceHouse;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.Record;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.service.payment.IPaymentService;
import com.example.osbb.service.pdf.IPdfService;
import com.example.osbb.service.pdf.PdfService;
import com.example.osbb.service.pdf.TextsAndLists;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class QueriesService implements IQueriesService {

    private final String PRINT_SUCCESSFULLY = "Все PDF файлы напечатаны успешно, смотрите в папке D:/pdf";
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private IPdfService iPdfService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private RecordDAO recordDAO;
    @Autowired
    private IPaymentService iPaymentService;

    // печатать объявление о новых реквизитах по оплате за услуги ОСББ
    @Override
    public ResponseEntity<?> queryNewBillForPayServiceOSBB() {
        try {
            String path = "D:/pdf/queries";
            checkDir(path);
            String text = "Платёжные реквизиты ОСББ";
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/" + text + ".pdf")));
            PdfFont font = createFont();
            pdfService.dateTimeNow(doc, font);
            pdfService.bankAccountForPayment(doc, font);
            pdfService.numberPhoneEmergencyService(doc, font);
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of(text, PRINT_SUCCESSFULLY + "/queries")));
        } catch (Exception error) {
            log.error(error.getMessage());
            return ResponseEntity.badRequest().body(new Response(List.of(error.getMessage())));
        }
    }

    @Override
    public ResponseEntity<?> queryListHeatSupplyForApartment() {
        try {
            String path = "D:/pdf/queries";
            checkDir(path);
            String text = "Поквартирная типизация отопления";
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/" + text + ".pdf")));
            PdfFont font = createFont();
            // начало ------------------------
            Map<String, List<ApartmentHeatSupply>> map = ownershipDAO.findAll().stream().map(ApartmentHeatSupply::new)
                    .sorted(comparatorApartmentHeatSupply()).collect(groupingBy(ApartmentHeatSupply::getHeatSupply));
            doc.add(new Paragraph().add(currentDate()).setFont(font));
            createTableForListApartmentBillFullNamePhoneNumber(map.get("CENTER"), doc, font, "централизованное");
            createTableForListApartmentBillFullNamePhoneNumber(map.get("AUTO_GAZ"), doc, font, "автономное (газовое)");
            createTableForListApartmentBillFullNamePhoneNumber(map.get("AUTO_ELECTRO"), doc, font, "автономное (электрическое)");
            // финиш ---------------------------
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of(text, PRINT_SUCCESSFULLY + "/queries")));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
        }
    }

    // !!! обратить внимание на не целевое мспользование
    @Override
    public ResponseEntity<?> queryReport_2023_11() {
        try {
            String path = "D:/pdf/queries";
            checkDir(path);
            String text = "Звіт ОСББ за зимовий період 2023-2024 роки.";
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/" + text + ".pdf")));
            PdfFont font = createFont();
            dateTimeNow(doc, font);
            // старт -------------------------------
            createHeader(text, doc, font);
            pdfService.createListText(TextsAndLists.report_2023_2024_winter, doc, font);
            pdfService.createAppealListText(TextsAndLists.appealToTheResidentsList, doc, font);
//            pdfService.appealToTheResidentsOfTheHouse(TextsAndLists.appealToTheResidents, doc, font);
//            pdfService.bankAccountForPayment(doc, font);
            // финиш ----------------------------------
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of(text, PRINT_SUCCESSFULLY + "/queries")));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> queryListApartmentBillFullNamePhoneNumber() {
        List<ApartmentBillFullNamePhoneNumber> listForPrint = new ArrayList<>();
        recordDAO.findAll().forEach(s -> listForPrint.add(createApartmentBillFullNamePhoneNumber(s)));
        List<ApartmentBillFullNamePhoneNumber> result = listForPrint.stream().sorted(comparatorApartment()).toList();
        result.forEach(System.out::println);
        iPdfService.printQueryListApartmentBillFullNamePhoneNumber(result);
        log.info(PRINT_SUCCESSFULLY);
        return ResponseEntity.ok(new Response(List.of("Список собственников дома", PRINT_SUCCESSFULLY + "/queries")));
    }

    @Override
    public ResponseEntity<?> queryBalanceHouse() {
        try {
            List<EntryBalanceHouse> list = iPaymentService.getListEntryBalanceHouse();
//            Double summa = formatDoubleValue(list.stream()
//                    .mapToDouble(EntryBalanceHouse::getSumma).reduce(0, (a, b) -> a + b));
            String path = "D:/pdf/balance";
            checkDir(path);
            String text = "Задолженость по оплате за услуги ОСББ по помещениям";
            Document doc = new Document(new PdfDocument(new PdfWriter(path + "/" + text + ".pdf")));
            PdfFont font = createFont();
            log.info("Текущая дата");
            dateTimeNow(doc, font);
            log.info("Создаём заголовок");
            createHeader(text, doc, font);
            log.info("Создаём таблицу");
            float[] pointColumnWidths = {200F, 200F, 300F};
            Table table = new Table(pointColumnWidths);
            table.setMarginTop(10).setMarginBottom(10);
            log.info("Заполняем заголовки таблицы");
            List<String> listHeader = List.of("Помещение №", "Лицевой счёт", "Задолженость/Переплата");
            createHeaderTable(listHeader, table, doc, font);
            log.info("Заполняем строки таблицы");
            list.forEach(
                    el -> {
                        for (String line : List.of(el.getApartment(), el.getBill(), el.getSumma().toString()))
                            table.addCell(new Cell().add(new Paragraph(line)).setTextAlignment(TextAlignment.CENTER)
                                    .setFont(font).setFontSize(9));
                    }
            );
            doc.add(table);
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            return ResponseEntity.ok(new Response(List.of("Задолженность по оплате за услуги ОСББ по помещениям",
                    PRINT_SUCCESSFULLY + "/balance")));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.badRequest().body(List.of(exception.getMessage()));
        }
    }

    private ApartmentBillFullNamePhoneNumber createApartmentBillFullNamePhoneNumber(Record record) {
        return new ApartmentBillFullNamePhoneNumber(record.getOwnership().getAddress().getApartment(),
                record.getOwnership().getBill(), record.getOwner().getPhoneNumber(),
                mapOwnerToFullName(record.getOwner()));
    }

    // ************** help function ************************

    private String currentDate() {
        String time = LocalDateTime.now().toString().replace("T", ", текущее время :  ");
        return "Текущая дата : " + time.substring(0, time.indexOf("."));
    }

    private PdfFont createFont() {
        try {
            return PdfFontFactory.createFont("C:\\Windows\\Fonts\\Arial.ttf", "CP1251");
        } catch (IOException error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

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
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    private void createHeader(String text, Document doc, PdfFont font) {
        doc.add(new Paragraph(text).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14).setBold());
    }

    private void createHeaderTable(List<String> list, Table table, Document doc, PdfFont font) {
        for (String line : list)
            table.addCell(new Cell().add(new Paragraph(line)).setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFont(font));
    }


    private void createTableForListApartmentBillFullNamePhoneNumber(
            List<ApartmentHeatSupply> list, Document doc, PdfFont font, String message) {
        doc.add(new Paragraph().add("Тип отопления - " + message + ", в количестве " + list.size() + " л.с.").setFont(font));
        float[] pointColumnWidths = {200F, 200F};
        Table table = new Table(pointColumnWidths);
        table.setMarginTop(7).setMarginBottom(7);
        List<String> listHeader = List.of("Помещение №", "Тип отопления");
        createHeaderTable(listHeader, table, doc, font);
        list.forEach(
                el -> {
                    for (String line : List.of(el.getApartment(), el.getHeatSupply()))
                        table.addCell(new Cell().add(new Paragraph(line)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(9));
                }
        );
        doc.add(table);
    }

    private String mapOwnerToFullName(Owner o) {
        return o.getLastName() + " " + o.getFirstName() + " " + o.getSecondName();
    }

    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }

    public void dateTimeNow(Document doc, PdfFont font) {
        String time = LocalDateTime.now().toString().replace("T", ", текущее время :  ");
        time = "Текущая дата : " + time.substring(0, time.indexOf("."));
        Paragraph p = new Paragraph(time);
        doc.add(p.setFont(font).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
    }

    // sorted ---------------------
    private Comparator<ApartmentHeatSupply> comparatorApartmentHeatSupply() {
        return (a, b) -> {
            return a.getHeatSupply().compareTo(b.getHeatSupply()) != 0 ?
                    a.getHeatSupply().compareTo(b.getHeatSupply()) :
                    Integer.parseInt(a.getApartment())
                            - Integer.parseInt(b.getApartment());
        };
    }

    private Comparator<ApartmentBillFullNamePhoneNumber> comparatorApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment());

    }
}
