package com.example.birhdaykeeper.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManSQLiteDataBase;
import com.example.birhdaykeeper.unit.BirthDayMan;
import com.example.birhdaykeeper.unit.Category;

import java.util.ArrayList;

public class AddHappyEventActivity extends AppCompatActivity {

    public static final String TAG_BIRTH= "birthdayDate";
    EditText name,surname,email,phone,dateBirth;


    BirthDayMan currentBirthDayMan = new BirthDayMan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_happy_event);


        name = findViewById(R.id.nameId);
        surname = findViewById(R.id.surnameId);
        email = findViewById(R.id.emailId);
        phone = findViewById(R.id.phoneId);
        dateBirth = findViewById(R.id.dateBirthId);

        getFromActivity();
        //createPerson();
        resForCategoryView();

    }

    private void getFromActivity(){
        Bundle bundle = getIntent().getExtras();
        String gettingDate = bundle.get(TAG_BIRTH).toString();
        dateBirth.setText(gettingDate);
    }

    public void createPerson(){
        currentBirthDayMan.setBirthData(dateBirth.getText().toString());
        currentBirthDayMan.setName(name.getText().toString());
        currentBirthDayMan.setSurname(surname.getText().toString());
      //  Toast.makeText(getApplicationContext(),getting, Toast.LENGTH_SHORT).show();
    }

    private void resForCategoryView() {
        ArrayList<String> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(category.toString());
        }

        if(categories.size() != 0 ){
            Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,categories);
            categorySpinner.setAdapter(adapter);


            AdapterView.OnItemSelectedListener onItemSelectedListener =
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selectedItem = adapterView.getSelectedItem().toString();
                            Category category = Category.valueOf(selectedItem);
                            currentBirthDayMan.setCategory(category);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            currentBirthDayMan.setCategory(Category.OTHER);
                        }

                    };

            categorySpinner.setOnItemSelectedListener(onItemSelectedListener);
        }
    }

    public void addNewPerson(View view) {
        createPerson();
        if(email.getText() != null && email.getText().toString()!= ""
                && phone.getText() != null && phone.getText().toString()!="")
        {
            try {
                currentBirthDayMan.setEmail(email.getText().toString());
                currentBirthDayMan.setPhone(phone.getText().toString());

                BirthdayManSQLiteDataBase birthDaySQLiteDataBase = BirthdayManSQLiteDataBase.getInstance(this);
                birthDaySQLiteDataBase =  BirthdayManSQLiteDataBase.getInstance(this);
                birthDaySQLiteDataBase.addBirthManToDb(currentBirthDayMan);
                Toast.makeText(this, "добавлено", Toast.LENGTH_LONG).show();

            } catch (Exception exceptionBirth) {
                Toast.makeText(getApplicationContext(),exceptionBirth.getMessage(),Toast.LENGTH_LONG).show();
            }

        }

    }
}