package com.example.birhdaykeeper.serializableStuff;

import java.io.Serializable;
import java.util.List;

public class StringForJson implements Serializable {
    public List<String> listTemplate;

    public StringForJson(){}

    public StringForJson(List<String> send){
        listTemplate = send;
    }
}
