package com.example.birhdaykeeper.activities;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.birhdaykeeper.MainActivity;
import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManSQLiteDataBase;
import com.example.birhdaykeeper.dataBaseManager.SQLDBException;
import com.example.birhdaykeeper.recyclerViewPack.BirthdayManAdapter;
import com.example.birhdaykeeper.unit.BirthDayMan;
import com.example.birhdaykeeper.unit.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SortActivity extends AppCompatActivity {

    String categoryChoose;
    BirthdayManSQLiteDataBase sqLiteDataBase;
    RecyclerView recyclerView;
    BirthdayManAdapter birthdayManAdapter;

    PopupMenu popupMenu;
    Button buttonChoose,buttonDefault;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);


        buttonChoose = findViewById(R.id.buttonSpinner);
        buttonDefault = findViewById(R.id.buttonDefault);

        sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(this);
        List<BirthDayMan> birthDayManList =  new ArrayList<>();
        try {
            birthDayManList = sqLiteDataBase.takeAllBirthManFromDb();
        } catch (SQLDBException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.mySortRecycler);

        if(birthDayManList != null && birthDayManList.size() != 0){
            birthdayManAdapter = new BirthdayManAdapter(birthDayManList);
            recyclerView.setAdapter(birthdayManAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        resForCategoryView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        buttonChoose.setOnClickListener(view -> {
           /* if(categoryChoose!=null && !categoryChoose.isEmpty()){

                if(sqLiteDataBase == null)
                {
                    sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(SortActivity.this);
                }
                try {

                  List<BirthDayMan> birthDayMEN = sqLiteDataBase.takeAllBirthManFromDb();
                  List<BirthDayMan> birthSend = birthDayMEN.stream().
                          filter((birthMan1)-> birthMan1.getCategory().toString() == categoryChoose).
                          collect(Collectors.toList());

                  updateRecyclerData(birthSend);

                } catch (SQLDBException e) {
                    e.printStackTrace();
                }

            }*/
            categoryChoosed();

        });
        buttonDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sqLiteDataBase == null)
                {
                    sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(SortActivity.this);
                }
                try {

                    List<BirthDayMan> birthDayMEN = sqLiteDataBase.takeAllBirthManFromDb();
                    updateRecyclerData(birthDayMEN);


                } catch (SQLDBException e) {
                    e.printStackTrace();
                }
            }
        });

       creationOfPopupMenu();

    }


    private void categoryChoosed(){
        if(categoryChoose!=null && !categoryChoose.isEmpty()){

            if(sqLiteDataBase == null)
            {
                sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(SortActivity.this);
            }
            try {

                List<BirthDayMan> birthDayMEN = sqLiteDataBase.takeAllBirthManFromDb();
                List<BirthDayMan> birthSend = birthDayMEN.stream().
                        filter((birthMan1)-> birthMan1.getCategory().toString() == categoryChoose).
                        collect(Collectors.toList());

                updateRecyclerData(birthSend);

            } catch (SQLDBException e) {
                e.printStackTrace();
            }

        }
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
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SortActivity.this);
        alert.setTitle("Внимание!").
                setMessage("Вы действительно хотите удалить заметку?").
                setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            sqLiteDataBase.deleteRecipeFromDb(birthDayMan);
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


    private void updateRecyclerData(List<BirthDayMan> list){
        BirthdayManAdapter birthManAdapter = new BirthdayManAdapter(list);
        recyclerView.setAdapter(birthManAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //    creationOfPopupMenu();
    }

    private void resForCategoryView() {
        ArrayList<String> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(category.toString());
        }

        if(categories.size() != 0){
            Spinner categorySpinner = (Spinner) findViewById(R.id.categorySortSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,categories);
            categorySpinner.setAdapter(adapter);

            AdapterView.OnItemSelectedListener onItemSelectedListener =
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selectedItem = adapterView.getSelectedItem().toString();
                            categoryChoose = selectedItem;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            categoryChoose = Category.OTHER.toString();
                        }

                    };

            categorySpinner.setOnItemSelectedListener(onItemSelectedListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_bar_for_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.up:
                recyclerView.smoothScrollToPosition(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}