package com.example.birhdaykeeper.serializableStuff;

import java.io.File;
import java.util.List;

public class JsonManager {
    File file;
    public JsonManipulation jsonManipulation = new JsonManipulation();

    public void serialize(String listTemplate){
        if(file!=null)
            jsonManipulation.serializationToJson(file,listTemplate);
    }

    public StringForJson deserialize(){
        StringForJson personReturn = jsonManipulation.deserializationFromJson(file);
        return personReturn;
    }

    public void takeFileFromActivity(File file){
        this.file = file;
    }

}
