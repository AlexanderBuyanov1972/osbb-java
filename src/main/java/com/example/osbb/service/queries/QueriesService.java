package com.example.osbb.service.queries;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dto.queries.ApartmentHeatSupply;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.service.pdf.IPdfService;
import com.example.osbb.service.pdf.PdfService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class QueriesService implements IQueriesService {
    private static final Logger log = Logger.getLogger(PdfService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    private final String PRINT_SUCCESSFULLY = "Все PDF файлы напечатаны успешно";
    @Autowired
    private OwnershipDAO ownershipDAO;
    @Autowired
    private IPdfService iPdfService;


    @Override
    public Object queryListHeatSupplyForApartment() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            checkDir("D:/pdf/queries");
            PdfWriter writer = new PdfWriter("D:/pdf/queries/" + "Поквартирная типизация отопления" + ".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            PdfFont font = createFont();
            // начало ------------------------
            Map<String, List<ApartmentHeatSupply>> map = ownershipDAO.findAll().stream().map(ApartmentHeatSupply::new)
                    .sorted(comparatorApartmentHeatSupply()).collect(groupingBy(ApartmentHeatSupply::getHeatSupply));
            doc.add(new Paragraph().add(dateTimeNow()).setFont(font));
            createTable(map.get("CENTER"), doc, font, "централизованное");
            createTable(map.get("AUTO_GAZ"), doc, font, "автономное (газовое)");
            createTable(map.get("AUTO_ELECTRO"), doc, font, "автономное (электрическое)");
            // финиш ---------------------------
            doc.close();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of("Наличие и типизация квартирного отопления в доме", PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }

    @Override
    public Object queryReport_2023_11() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            iPdfService.printQueryReport_2023_11();
            log.info(PRINT_SUCCESSFULLY);
            log.info(messageExit(methodName));
            return new Response(List.of("Отчёт о деятельности ОСББ за ноябрь 2023 года", PRINT_SUCCESSFULLY));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }

    }


    // ************** help function ************************

    private String dateTimeNow() {
        String time = LocalDateTime.now().toString().replace("T", ", текущее время :  ");
        return "Текущая дата : " + time.substring(0, time.indexOf("."));
    }

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

    // sorted ---------------------
    private Comparator<ApartmentHeatSupply> comparatorApartmentHeatSupply() {
        return (a, b) -> {
            return a.getHeatSupply().compareTo(b.getHeatSupply()) != 0 ?
                    a.getHeatSupply().compareTo(b.getHeatSupply()) :
                    Integer.parseInt(a.getApartment())
                            - Integer.parseInt(b.getApartment());
        };
    }

    // log services ----------------------
    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
