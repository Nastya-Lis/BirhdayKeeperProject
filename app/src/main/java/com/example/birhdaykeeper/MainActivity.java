package com.example.birhdaykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.birhdaykeeper.activities.TemplateCreateActivity;
import com.example.birhdaykeeper.activities.UpdateInfoPersonActivity;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManSQLiteDataBase;
import com.example.birhdaykeeper.dataBaseManager.SQLDBException;
import com.example.birhdaykeeper.recyclerViewPack.BirthdayManAdapter;
import com.example.birhdaykeeper.serializableStuff.JsonManager;
import com.example.birhdaykeeper.services.NotificationService;
import com.example.birhdaykeeper.unit.BirthDayMan;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG_BIRTH= "birthdayDate";
    CalendarView calendarView;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String dataPickFormat;
    //AsyncModeDb asyncModeDb = new AsyncModeDb();

    File file;
    JsonManager jsonManager = new JsonManager();
    String FILENAME = "templateCongratulation.json";

    RecyclerView recyclerView;
    BirthdayManAdapter birthdayManAdapter;

    BirthdayManSQLiteDataBase sqLiteDataBase;

//    List<BirthDayMan> listMen = new ArrayList<>();

    PopupMenu popupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();

        recyclerView  = findViewById(R.id.myRecycler);

//        asyncModeDb.execute(listMen);

        file = new File(super.getFilesDir(),FILENAME);
       // jsonManager.takeFileFromActivity(file);
        jsonManager.jsonManipulation.isFileExists(file);

    }



    @Override
    protected void onResume() {
        super.onResume();



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

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

        if(dataPickFormat!=null)
        activateSQLDb(dataPickFormat);

    }


    private void updatingData(){
        List<BirthDayMan> birthDayMEN = null;
        try {
            birthDayMEN = sqLiteDataBase.takeAllBirthManFromDb();
            birthdayManAdapter = new BirthdayManAdapter(birthDayMEN);
            recyclerView.setAdapter(birthdayManAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            creationOfPopupMenu();
        } catch (SQLDBException e) {
            e.printStackTrace();
        }

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
                Intent intent1 = new Intent(MainActivity.this, SortActivity.class);
                startActivity(intent1);
                break;
            case R.id.templateCongratulation:
                Intent intent2 = new Intent(MainActivity.this, TemplateCreateActivity.class);
                startActivity(intent2);
                break;
            case R.id.start_service:
                Intent intent = new Intent(this, NotificationService.class);
                intent.putExtra("Date", dataPickFormat);
                startService(intent);
                break;
            case R.id.stop_service:
                stopService(new Intent(this, NotificationService.class));
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
                       //     activateSQLDb(dataPickFormat);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
            return true;
        });

       // updatingData();
      //  listFragment.updateFragmentData();
    }

    private void editRecipe(BirthDayMan birthDayMan){
        Intent intent = new Intent(this, UpdateInfoPersonActivity.class);
        intent.putExtra(BirthDayMan.class.getSimpleName(),birthDayMan);
        startActivity(intent);
    }

    private void deleteRecipe(BirthDayMan birthDayMan){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Внимание!").
                setMessage("Вы действительно хотите удалить заметку?").
                setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            sqLiteDataBase.deleteBirthManFromDb(birthDayMan);
                            onResume();
                        }
                        catch (Exception e){

                        }
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startService(View view) {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra("Date", dataPickFormat);
        startService(intent);
        //нужно через стартфореграунд запускать
      //  ContextCompat.startForegroundService(this,intent);

    }

    public void stopService(View view) {
        stopService(new Intent(this, NotificationService.class));
    }


 /*   private class AsyncModeDb extends AsyncTask<List<BirthDayMan>,Void,List<BirthDayMan>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<BirthDayMan> birthDayMEN) {
            super.onPostExecute(birthDayMEN);
            updatingData(birthDayMEN);
        }

        @Override
        protected List<BirthDayMan> doInBackground(List<BirthDayMan>... lists) {
            BirthdayManSQLiteDataBase sqlDb = BirthdayManSQLiteDataBase.getInstance(MainActivity.this);
            //  List<Recipe> recList = new ArrayList<>();
            try {
                listMen = sqlDb.takeMenByBirth(dataPickFormat);
            } catch (SQLDBException e) {
                e.printStackTrace();
            }
            return listMen;
        }

     *//*   @Override
        protected Object doInBackground(Object[] objects) {
            RecipeSQLiteDataBase sqlDb = RecipeSQLiteDataBase.getInstance(MainActivity.this);
            List<Recipe> recList = new ArrayList<>();
            try {
               recList = sqlDb.takeAllRecipesFromDb();
               return recList;
            } catch (SQLDBException e) {
                e.printStackTrace();
                return null;
            }
       }*//*
    }*/


}