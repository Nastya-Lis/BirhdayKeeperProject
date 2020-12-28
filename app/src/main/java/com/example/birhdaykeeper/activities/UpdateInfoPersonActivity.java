package com.example.birhdaykeeper.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class UpdateInfoPersonActivity extends AppCompatActivity {

    BirthDayMan currentBirthDayMan = new BirthDayMan();
    BirthDayMan oldBirthDayMan = new BirthDayMan();
    EditText name,surname,email,phone,dateBirth;

    BirthdayManSQLiteDataBase sqLiteDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_person);

        name = findViewById(R.id.nameUpdateId);
        surname = findViewById(R.id.surnameUpdateId);
        email = findViewById(R.id.emailUpdateId);
        phone = findViewById(R.id.phoneUpdateId);
        dateBirth = findViewById(R.id.birthUpdateId);

        getFromActivity();

        resForCategoryView();
    }


    private void getFromActivity(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            oldBirthDayMan = (BirthDayMan) bundle.getSerializable(BirthDayMan.class.getSimpleName());

            name.setText(oldBirthDayMan.getName());
            surname.setText(oldBirthDayMan.getSurname());
            email.setText(oldBirthDayMan.getEmail());
            phone.setText(oldBirthDayMan.getPhone());
            dateBirth.setText(oldBirthDayMan.getBirthData());


            Spinner spinner = (Spinner) findViewById(R.id.categoryUpdateSpinner);
            ArrayList<String> categories = new ArrayList<>();
            int position = -1;
            for (Category category : Category.values()) {
                categories.add(category.toString());
            }
            for (int i = 0; i < categories.size(); i++) {
                String comparing = oldBirthDayMan.getCategory().toString();
                if (categories.get(i).equals(comparing)) {
                    position = i;
                    break;
                }
            }
            if (position >= 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, categories);
                spinner.setAdapter(adapter);
                int positionElement = position;

            }

        }
    }

    private Integer createRecipe(){
        Integer countException = 0;
        currentBirthDayMan.setId(oldBirthDayMan.getId());
        currentBirthDayMan.setName(name.getText().toString());
        currentBirthDayMan.setSurname(surname.getText().toString());
        currentBirthDayMan.setBirthData(dateBirth.getText().toString());
        try {
            currentBirthDayMan.setEmail(email.getText().toString());
            currentBirthDayMan.setPhone(phone.getText().toString());
        } catch (Exception exceptionBirth) {
            exceptionBirth.printStackTrace();
            countException++;
        }
        return countException;
    }


    public void updateOldPerson(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Предупреждение!").setMessage("Вы хотите изменить данные?").
                setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(createRecipe() == 0) {
                            sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(UpdateInfoPersonActivity.this);
                            sqLiteDataBase.updateBirthManInDb(currentBirthDayMan);
                            Toast.makeText(UpdateInfoPersonActivity.this,
                                    "изменение прошло успешно", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(UpdateInfoPersonActivity.this,
                                    "Неверный формат данных", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


    private void resForCategoryView() {
        ArrayList<String> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(category.toString());
        }

        if(categories.size() != 0 ){
            Spinner categorySpinner = (Spinner) findViewById(R.id.categoryUpdateSpinner);
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
}