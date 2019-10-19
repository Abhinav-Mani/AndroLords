package com.androlord.prayaas.DataClass;



public class uploadPDF {
    public String name;
    public String url;
    public String Author;

    public uploadPDF() {
    }

    public uploadPDF(String name,String Author, String url) {
        this.Author=Author;
        this.name = name;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

