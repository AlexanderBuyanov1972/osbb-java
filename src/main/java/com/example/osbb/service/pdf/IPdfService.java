package com.example.osbb.service.pdf;


import com.example.osbb.dto.ApartmentBillFullNamePhoneNumber;
import com.example.osbb.dto.DebtDetails;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPdfService {
    // debt ---------------------------
    ResponseEntity<?> printDebt(DebtDetails debt);

    ResponseEntity<?> printAllDebt();

    ResponseEntity<?> printAllInOneDebtAllApartment();

    // debt details -----------------------
    ResponseEntity<?> printDebtDetails(Long id);

    ResponseEntity<?> printAllDebtDetails();

    // result survey -------------
    ResponseEntity<?> printResultSurvey(String title);

    void printQueryListApartmentBillFullNamePhoneNumber(List<ApartmentBillFullNamePhoneNumber> list);

    void createAppealListText(List<String> appealToTheResidentsList, Document doc, PdfFont font);
}
