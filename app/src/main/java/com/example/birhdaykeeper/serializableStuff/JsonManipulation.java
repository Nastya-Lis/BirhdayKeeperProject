package com.example.birhdaykeeper.serializableStuff;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonManipulation {

    ObjectMapper objectMapper;
    StringForJson templateList = new StringForJson();


    public boolean isFileExists(File file) {
        if(file!=null) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    createDefaultTemplate(file);
                    Log.i("Log_json", "File has been just created");
                    return true;
                } catch (IOException e) {
                    Log.i("Log_json", "File is not created. Lox");
                    return false;
                }
            }
            else {
              //  file.delete();
                Log.i("Log_json", "File exists");
                return true;
            }
        }
        else{
            Log.i("Log_json","File == null");
            return false;
        }

    }



    public void serializationToJson(File file, String template) {
        if(isFileExists(file)){
            objectMapper = new ObjectMapper();

            try {
                templateList = getFromFileList(file);
                if(templateList == null){
                    templateList = new StringForJson();
                    templateList.listTemplate = new ArrayList<>();
                }
                templateList.listTemplate.add(template);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,templateList);
            }
            catch (IOException e) {
                Log.i("Log_json","Oops, your serialization doesn't work");
            }
        }
    }



    private void serializationToJsonForRemove(File file, StringForJson forJson){
        if(isFileExists(file)){
            objectMapper = new ObjectMapper();
            try{
                templateList = getFromFileList(file);
                if(templateList == null){
                    templateList = new StringForJson();
                }
                templateList.listTemplate = forJson.listTemplate;
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,templateList);
            }
            catch(IOException e){
                Log.i("Log_json","Oops, your serialization doesn't work");
            }
        }
    }


    public void removeElementV2(String stringSend,File file){
        List<String> listString = deserializationFromJson(file).listTemplate;
        listString.removeIf(str ->str.equals(stringSend));
        serializationToJsonForRemove(file,new StringForJson(listString));
    }

    public void serializationToJsonForUpdate(File file,StringForJson forJson){
        if(isFileExists(file)){
            objectMapper = new ObjectMapper();
            try{
                templateList = getFromFileList(file);
                if(templateList == null){
                    templateList = new StringForJson();
                }
                templateList.listTemplate = forJson.listTemplate;
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,templateList);
            }
            catch(IOException e){
                Log.i("Log_json","Oops, your serialization doesn't work");
            }
        }
    }



    private StringForJson getFromFileList(File file){
        if(file!=null){
            try {
                objectMapper = new ObjectMapper();
                templateList = objectMapper.readValue(file,StringForJson.class);
                return templateList;
            } catch (IOException e) {
                Log.i("Log_json","Couldn't read file");
                return null;
            }
        }
        else{
            return null;
        }
    }



    public StringForJson deserializationFromJson(File file) {
        return getFromFileList(file);
    }


    void createDefaultTemplate(File file){
        String congratulation1 = "Глубоко уважаемый, товарищ. " +
                "С этим замечательным днем спешу поздравить тебя";
        String congratulation2 = "Лучшему начальнику этой вселенной!" +
                " Спасибо, что ты есть, иначе бы я умер от голода";
        String congratulation3 = "Братишка, я тебе поздравление принес";

        this.serializationToJson(file,congratulation1);
        this.serializationToJson(file,congratulation2);
        this.serializationToJson(file,congratulation3);
    }
}

