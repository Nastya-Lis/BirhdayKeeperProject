package com.example.birhdaykeeper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.birhdaykeeper.MainActivity;
import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.serializableStuff.JsonManager;
import com.example.birhdaykeeper.serializableStuff.StringForJson;
import com.example.birhdaykeeper.unit.BirthDayMan;

import java.io.File;
import java.util.ArrayList;

public class TemplateCreateActivity extends AppCompatActivity {

    final int DIALOG_ADD = 1;
    final int DIALOG_UPDATE = 2;
    File file;
    String FILENAME = "templateCongratulation.json";
    JsonManager jsonManager = new JsonManager();
    StringForJson stringForJson;
    int itemSelectedPosition;

    PopupMenu popupMenu;

    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    EditText congratulationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_create);

        listView = findViewById(R.id.templateListView);


        file = new File(super.getFilesDir(),FILENAME);
        loadStuff();

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadStuff();
        creationOfPopupMenu();
    }

    void loadStuff(){
        if(file!=null) {
            stringForJson = jsonManager.jsonManipulation.deserializationFromJson(file);
            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,stringForJson.listTemplate);

            listView.setAdapter(arrayAdapter);
        }
    }


    private void creationOfPopupMenu() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                popupMenu = new PopupMenu(TemplateCreateActivity.this, itemClicked);
                popupMenu.inflate(R.menu.context_menu);
                StringForJson forJson = new StringForJson();
                forJson.listTemplate = new ArrayList<>();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.editId:

                                itemSelectedPosition = position;
                               // forJson.listTemplate.add();
                                editRecipe(stringForJson.listTemplate.get(position));
                                break;
                            case R.id.deleteId:
                                //forJson.listTemplate.add();
                                deleteRecipe(stringForJson.listTemplate.get(position));
                                //     activateSQLDb(dataPickFormat);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

     //   loadStuff();
        // updatingData();
        //  listFragment.updateFragmentData();
    }

    private void editRecipe(String forJson){
      /*  Intent intent = new Intent(this, UpdateInfoPersonActivity.class);
        intent.putExtra(BirthDayMan.class.getSimpleName(),birthDayMan);
        startActivity(intent);*/

        View view = getLayoutInflater().inflate(R.layout.layout_add_template,null);
        congratulationField = view.findViewById(R.id.dialog_view_addCongratulation);
        congratulationField.setText(forJson);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(view)
                .setPositiveButton("Изменить", (dialogInterface, i) -> {

                    int index = -1;
                    stringForJson = jsonManager.jsonManipulation.deserializationFromJson(file);
                    for(int h = 0;h < stringForJson.listTemplate.size();h++ ){
                        if(stringForJson.listTemplate.get(h).equals(forJson)
                        )
                        {
                            index = h;
                            break;
                        }
                    }
                    if(index != -1) {
                        stringForJson.listTemplate.set(index, congratulationField.getText().toString());

                        jsonManager.jsonManipulation.serializationToJsonForUpdate(file, stringForJson);
                        Toast.makeText(TemplateCreateActivity.this,
                                "успешно обновлено", Toast.LENGTH_SHORT).show();
                    }

                }).setNegativeButton("Нет",null);

        alertDialogBuilder.create().show();

    }


    private void deleteRecipe(String forJson){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder
                (TemplateCreateActivity.this);
        alert.setTitle("Внимание!").
                setMessage("Вы действительно хотите удалить шаблончик?").
                setPositiveButton("Да", (dialogInterface, i) -> {
                    jsonManager.jsonManipulation.removeElementV2(forJson,file);
                    Toast.makeText(TemplateCreateActivity.this,"успешно удалено",
                            Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Нет", (dialogInterface, i) -> {

                });
        android.app.AlertDialog alertDialog = alert.create();
        alertDialog.show();

/*
        if(file!=null) {
            stringForJson = jsonManager.jsonManipulation.deserializationFromJson(file);
            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,stringForJson.listTemplate);

            listView.setAdapter(arrayAdapter);
        }
*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.update:
                loadStuff();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if(id == DIALOG_ADD){
            View view = getLayoutInflater().inflate(R.layout.layout_add_template,null);

            alertDialogBuilder.setView(view)
                    .setPositiveButton("Добавить", (dialogInterface, i) -> {
                        if(congratulationField.getText()!=null){
                            jsonManager.jsonManipulation.serializationToJson(file,congratulationField.getText().
                                    toString());
                            Toast.makeText(TemplateCreateActivity.this,
                                    "успешно добавлено",Toast.LENGTH_SHORT).show();

                        }
                    }).setNegativeButton("Нет",null);

        }


    /*    if(id == DIALOG_UPDATE){
            View view = getLayoutInflater().inflate(R.layout.layout_add_template,null);
            alertDialogBuilder.setView(view)
                    .setPositiveButton("Изменить", (dialogInterface, i) -> {
                            congratulationField.setText(stringForJson.listTemplate.
                                    get(itemSelectedPosition));
                            jsonManager.jsonManipulation.serializationToJsonForUpdate(file,stringForJson);
                            Toast.makeText(TemplateCreateActivity.this,
                                    "успешно обновлено",Toast.LENGTH_SHORT).show();


                    }).setNegativeButton("Нет",null);
        }*/
        return alertDialogBuilder.create();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if(id == DIALOG_ADD ){
            congratulationField = dialog.getWindow().findViewById(R.id.dialog_view_addCongratulation);
        }
        if(id == DIALOG_UPDATE){
            congratulationField = dialog.getWindow().findViewById(R.id.dialog_view_addCongratulation);
        }

    }


    public void addTemplate(View view) {

        showDialog(DIALOG_ADD);
    }
}