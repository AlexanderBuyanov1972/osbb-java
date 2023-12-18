package com.example.osbb.dto;


import java.util.List;

public class DebtDetails {
    private HeaderDebt header;
    private List<BodyDebt> listBody;

    public DebtDetails() {
    }

    public DebtDetails(HeaderDebt header, List<BodyDebt> listBody) {
        this.header = header;
        this.listBody = listBody;
    }

    public HeaderDebt getHeader() {
        return header;
    }

    public List<BodyDebt> getListBody() {
        return listBody;
    }

    public DebtDetails setHeader(HeaderDebt header) {
        this.header = header;
        return this;
    }

    public DebtDetails setListBody(List<BodyDebt> listBody) {
        this.listBody = listBody;
        return this;
    }

    @Override
    public String toString() {
        return "DebtDetails = { header = " + header + ", listBody = " + listBody + "}";
    }
}


