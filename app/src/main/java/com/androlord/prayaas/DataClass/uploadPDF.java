package com.androlord.prayaas.DataClass;



public class uploadPDF {
    public String name;
    public String url;
    public String Author;
    public String Key;

    public uploadPDF() {
    }

    public uploadPDF(String name,String Author, String url,String Key) {
        this.Author=Author;
        this.name = name;
        this.url=url;
        this.Key=Key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

