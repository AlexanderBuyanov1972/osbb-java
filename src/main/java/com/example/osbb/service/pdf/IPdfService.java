package com.example.osbb.service.pdf;


import com.example.osbb.dto.ApartmentBillFullNamePhoneNumber;
import com.example.osbb.dto.DebtDetails;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;

import java.util.List;

public interface IPdfService {
    // debt ---------------------------
    public Object printDebt(DebtDetails debt);

    public Object printAllDebt();

    public Object printAllInOneDebtAllApartment();

    // debt details -----------------------
    public Object printDebtDetails(Long id);

    public Object printAllDebtDetails();

     // result survey -------------
    public Object printResultSurvey(String title);

    public void printQueryListApartmentBillFullNamePhoneNumber(List<ApartmentBillFullNamePhoneNumber> list);

    public void createAppealListText(List<String> appealToTheResidentsList, Document doc, PdfFont font);
}
