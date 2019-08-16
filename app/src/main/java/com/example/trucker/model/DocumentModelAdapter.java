package com.example.trucker.model;

public class DocumentModelAdapter {

    public String status;
    public String name;
    public String type;
    public String id;

    public DocumentModelAdapter(String status,String id,String name, String type) {
        this.status = status;
        this.id = id;
        this.name = name;
        this.type = type;
    }


}
