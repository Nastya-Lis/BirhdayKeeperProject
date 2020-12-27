package com.example.birhdaykeeper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.fragments.ChooseFragment;
import com.example.birhdaykeeper.fragments.InfoFragment;
import com.example.birhdaykeeper.services.NotificationService;
import com.example.birhdaykeeper.unit.BirthDayMan;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShowInfoPersonActivity extends AppCompatActivity {

    FrameLayout frameLayoutInfo;
    FrameLayout frameLayoutChoose;
    private InfoFragment infoFragment;
    private ChooseFragment chooseFragment;
    private FragmentManager fragmentManager;

    final String BIRTH_ARG = "birthArg";

 /*   TextView name,surname,category,email,phone,dateBirth;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    String currentDateFormat, birthDateFormat;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_person);

        frameLayoutInfo = findViewById(R.id.birthMan_info);
        frameLayoutChoose = findViewById(R.id.birth_template_choose);

        fragmentManager = getSupportFragmentManager();

        infoFragment = new InfoFragment();

        BirthDayMan sending = getDataCurrentBirthMan();
        Bundle bundleSend = new Bundle();
        bundleSend.putSerializable(BIRTH_ARG,sending);

        infoFragment.setArguments(bundleSend);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.birthMan_info,infoFragment);
        transaction.commit();

    /*    name = findViewById(R.id.nameCurrentPersonId);
        surname = findViewById(R.id.surnameCurrentPersonId);
        email = findViewById(R.id.emailCurrentPersonId);
        phone = findViewById(R.id.phoneCurrentPersonId);
        category = findViewById(R.id.categoryCurrentPersonId);
        dateBirth = findViewById(R.id.birthdayCurrentPersonId);

        setDataInCurrentRecipe();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        infoFragment.buttonEmail.setOnClickListener(view -> {
            infoFragment.currentDateFormat = infoFragment.simpleDateFormat.format(infoFragment.calendar.getTime());
            infoFragment.currentDateFormat = infoFragment.currentDateFormat.substring(0,5);
            infoFragment.birthDateFormat = infoFragment.dateBirth.getText().toString();
            infoFragment.birthDateFormat = infoFragment.birthDateFormat.substring(0,5);

            if(infoFragment.currentDateFormat.equals(infoFragment.birthDateFormat)){

                frameLayoutChoose.setVisibility(View.VISIBLE);
                chooseFragment = new ChooseFragment();

                BirthDayMan sending = getDataCurrentBirthMan();
                Bundle bundleSend = new Bundle();
                bundleSend.putSerializable(BIRTH_ARG,sending);

                chooseFragment.setArguments(bundleSend);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.birth_template_choose,chooseFragment).
                        addToBackStack(null);
                fragmentTransaction.commit();
            }
            else{
                Toast.makeText(ShowInfoPersonActivity.this,
                        "Терпение, мой друг, время еще не пришло...", Toast.LENGTH_LONG).show();
            }
        });

    }

    private BirthDayMan getDataCurrentBirthMan(){
        Bundle bundle = getIntent().getExtras();
        BirthDayMan getBirthMan = (BirthDayMan) bundle.getSerializable(BirthDayMan.class.getSimpleName());

        try {
            int notificationId = (int) bundle.get("NotificationID");
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }
        catch (Exception e){

        }

        return  getBirthMan;
    }


 /*   private BirthDayMan getDataCurrentBirthMan(){
        Bundle bundle = getIntent().getExtras();

        BirthDayMan getBirthMan = (BirthDayMan) bundle.getSerializable(BirthDayMan.class.getSimpleName());

        try {
            int notificationId = (int) bundle.get("NotificationID");
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }
        catch (Exception e){

        }


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

    public void emailCross(View view) {
        currentDateFormat = simpleDateFormat.format(calendar.getTime());
        currentDateFormat = currentDateFormat.substring(0,5);
        birthDateFormat = dateBirth.getText().toString();
        birthDateFormat = birthDateFormat.substring(0,5);

        if(currentDateFormat.equals(birthDateFormat)){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
                + getDataCurrentBirthMan().getEmail()));
        startActivity(intent);}
        else{
            Toast.makeText(this,"Терпение, мой друг, время еще не пришло...",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void phoneCross(View view) {
        currentDateFormat = simpleDateFormat.format(calendar.getTime());
        currentDateFormat = currentDateFormat.substring(0,5);
        birthDateFormat = dateBirth.getText().toString();
        birthDateFormat = birthDateFormat.substring(0,5);

        if(currentDateFormat.equals(birthDateFormat)){
        String telephone = getDataCurrentBirthMan().getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ telephone));
        startActivity(intent);
        }
        else{
            Toast.makeText(this,"Терпение, мой друг, время еще не пришло...",
                    Toast.LENGTH_LONG).show();
        }
    }*/
}