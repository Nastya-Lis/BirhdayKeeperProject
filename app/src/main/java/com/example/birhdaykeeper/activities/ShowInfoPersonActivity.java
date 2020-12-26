package com.example.birhdaykeeper.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.unit.BirthDayMan;

public class ShowInfoPersonActivity extends AppCompatActivity {

    TextView name,surname,category,email,phone,dateBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_person);

        name = findViewById(R.id.nameCurrentPersonId);
        surname = findViewById(R.id.surnameCurrentPersonId);
        email = findViewById(R.id.emailCurrentPersonId);
        phone = findViewById(R.id.phoneCurrentPersonId);
        category = findViewById(R.id.categoryCurrentPersonId);
        dateBirth = findViewById(R.id.birthdayCurrentPersonId);

        setDataInCurrentRecipe();

    }

    private BirthDayMan getDataCurrentBirthMan(){
        Bundle bundle = getIntent().getExtras();
        BirthDayMan getBirthMan = (BirthDayMan) bundle.getSerializable(BirthDayMan.class.getSimpleName());
        return  getBirthMan;
    }

    private void setDataInCurrentRecipe(){
        BirthDayMan currentBirthMan = getDataCurrentBirthMan();
        name.setText(currentBirthMan.getName());
        surname.setText(currentBirthMan.getSurname());
        email.setText(currentBirthMan.getEmail());
        phone.setText(currentBirthMan.getPhone());
        category.setText(currentBirthMan.getCategory().toString());
        //image.setImageURI(Uri.parse(currentRecipe.getPhoto()));
        dateBirth.setText(String.valueOf(currentBirthMan.getBirthData()));

    }
    //отправка поздравления на почту и телефон
    //реализовать чуть позже.
    public void emailCross(View view) {
        //будет производиться проверка на текущую дату и дату рождения человека
        //если совпадают, то можно отправить поздравление
        //аналогично и с телефоном

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
                + getDataCurrentBirthMan().getEmail()));
        startActivity(intent);
    }

    public void phoneCross(View view) {
        String telephone = getDataCurrentBirthMan().getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ telephone));
        startActivity(intent);
    }
}