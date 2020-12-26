package com.example.birhdaykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.birhdaykeeper.activities.AddHappyEventActivity;
import com.example.birhdaykeeper.activities.ShowInfoPersonActivity;
import com.example.birhdaykeeper.activities.SortActivity;
import com.example.birhdaykeeper.activities.UpdateInfoPersonActivity;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManDataBaseContract;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManSQLiteDataBase;
import com.example.birhdaykeeper.dataBaseManager.SQLDBException;
import com.example.birhdaykeeper.recyclerViewPack.BirthdayManAdapter;
import com.example.birhdaykeeper.unit.BirthDayMan;
import com.example.birhdaykeeper.unit.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG_BIRTH= "birthdayDate";
    CalendarView calendarView;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String dataPickFormat;

    RecyclerView recyclerView;
    BirthdayManAdapter birthdayManAdapter;

    BirthdayManSQLiteDataBase sqLiteDataBase;

    PopupMenu popupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();

        /*BirthDayMan b1 = new BirthDayMan();
        b1.setName("lola");
        b1.setSurname("kekeke");
        b1.setCategory(Category.COLLEAGUES);
        b1.setBirthData("20.11.1990");

        BirthDayMan b2 = new BirthDayMan();
        b2.setName("petro");
        b2.setSurname("bubeb");
        b2.setCategory(Category.FRIENDS);
        b2.setBirthData("13.04.1987");


        ArrayList<BirthDayMan> birthDayManArrayList = new ArrayList<>();
        birthDayManArrayList.add(b1);
        birthDayManArrayList.add(b2);*/


        /*recyclerView  = findViewById(R.id.myRecycler);
        birthdayManAdapter = new BirthdayManAdapter(birthDayManArrayList);
        recyclerView.setAdapter(birthdayManAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/


      //  this.deleteDatabase(BirthdayManDataBaseContract.DATABASE_NAME);
       // BirthdayManSQLiteDataBase sql = BirthdayManSQLiteDataBase.getInstance(this);

        recyclerView  = findViewById(R.id.myRecycler);

    }



    @Override
    protected void onResume() {
        super.onResume();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //СДЕЛАТЬ ТАК, ЧТОБЫ ВЫВОДИЛ RECYCLERVIEW В ЗАВИСИМОСТИ ОТ ВЫБРАННОЙ ДАТЫ
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;

                //очистка ресайклавью от данных
                //немного шакальный
                //но пока сгодится
                recyclerView.removeAllViewsInLayout();

                simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                calendar.set(mYear,mMonth,mDay);
                dataPickFormat = simpleDateFormat.format(calendar.getTime());

                activateSQLDb(dataPickFormat);

                //   Toast.makeText(getApplicationContext(),dataPickFormat, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void activateSQLDb(String date){
        if(sqLiteDataBase == null){
            sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(this);
        }
        try {
          List<BirthDayMan> arrayList = sqLiteDataBase.takeMenByBirth(date);
          if(arrayList.size() != 0 ) {
              birthdayManAdapter = new BirthdayManAdapter(arrayList);
              recyclerView.setAdapter(birthdayManAdapter);
              creationOfPopupMenu();
          }
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (SQLDBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.sort:
                Intent intent = new Intent(MainActivity.this, SortActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }




    private void creationOfPopupMenu() {

        birthdayManAdapter.setOnBirthManClickListener(birthdayMan -> {
                Intent intent = new Intent(this, ShowInfoPersonActivity.class);
                intent.putExtra(BirthDayMan.class.getSimpleName(), birthdayMan);
                startActivity(intent);
            });
         //   listFragment.updateFragmentData();


        birthdayManAdapter.setOnBirthManLongClickListener((birthDayMan, view) -> {
            popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.context_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.editId:
                            editRecipe(birthDayMan);
                            break;
                        case R.id.deleteId:
                            deleteRecipe(birthDayMan);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
            return true;
        });

      //  listFragment.updateFragmentData();
    }

    private void editRecipe(BirthDayMan birthDayMan){
        Intent intent = new Intent(this, UpdateInfoPersonActivity.class);
       /* String currentKey = "";
        for (String key: listFragment.forListManager.keySet()) {
            if( recipe == listFragment.forListManager.get(key))
            currentKey = key;
        }*/
        intent.putExtra(BirthDayMan.class.getSimpleName(),birthDayMan);
        /*   intent.putExtra(nameUser,userId);*/
        // intent.putExtra("currentKey",currentKey);
        startActivity(intent);
    }

    private void deleteRecipe(BirthDayMan birthDayMan){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Warning!").
                setMessage("Are you really want to delete this element?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            /*ListExistingRecipesManager listExistingRecipesManager =
                                    new ListExistingRecipesManager(recipesFromDb,listFragment.forListManager);
                            listExistingRecipesManager.removeElementV2(recipe);*/
                            sqLiteDataBase.deleteRecipeFromDb(birthDayMan);
                            onResume();
                        }
                        catch (Exception e){

                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        android.app.AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }


    public void addEvent(View view) {
        if(dataPickFormat!=null){
        Intent intent = new Intent(MainActivity.this, AddHappyEventActivity.class);
        intent.putExtra(TAG_BIRTH,dataPickFormat);
        startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"Не выбрана дата",Toast.LENGTH_SHORT);

    }
}