package com.example.birhdaykeeper.unit;

import android.util.Patterns;
import android.widget.Toast;

import java.io.Serializable;
import java.util.regex.Pattern;

public class BirthDayMan implements Serializable {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private Category category;
    private String birthData;

    public BirthDayMan(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ExceptionBirth{
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches() == true)
        {
            this.email = email;
        }
        else throw  new ExceptionBirth("Неверный формат записи почты.");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone)throws ExceptionBirth {
        Pattern pattern = Pattern.compile("^((8|\\+374|\\+994|\\+995|\\+375|\\+7|\\+380|\\+38|\\+996|\\+998|\\+993)[\\- ]?)?" +
                "\\(?\\d{3,5}\\)?[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?\\d{1}[\\- ]?" +
                "\\d{1}[\\- ]?\\d{1}(([\\- ]?\\d{1})?[\\- ]?\\d{1})?$");
        if(pattern.matcher(phone).matches() == true){
           this.phone = phone;
        }
        else throw new ExceptionBirth("Неверный формат записи телефона.");
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBirthData() {
        return birthData;
    }

    public void setBirthData(String birthData) {
        this.birthData = birthData;
    }

}
