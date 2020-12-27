package com.example.birhdaykeeper.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.unit.BirthDayMan;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class InfoFragment extends Fragment {

    public TextView name,surname,category,email,phone,dateBirth;
    public Button buttonEmail, buttonPhone;
    public Calendar calendar = Calendar.getInstance();
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public String currentDateFormat, birthDateFormat;



    final String BIRTH_ARG = "birthArg";

    View InfoFragmentView;

    Context context;

 /*   // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/

    public BirthDayMan birthDayMan = new BirthDayMan();
    public int notificationId;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            birthDayMan = (BirthDayMan) getArguments().getSerializable(BIRTH_ARG);
        /*    mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }


/*
    private BirthDayMan getDataCurrentBirthMan(){
        Bundle bundle = getActivity().getIntent().getExtras();

        BirthDayMan getBirthMan = (BirthDayMan) bundle.getSerializable(BirthDayMan.class.getSimpleName());

        try {
            int notificationId = (int) bundle.get("NotificationID");
            NotificationManager notificationManager =
                    (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }
        catch (Exception e){

        }

        return  getBirthMan;
    }*/

    private void setDataInCurrentRecipe(){
        if(birthDayMan!=null) {
            BirthDayMan currentBirthMan = birthDayMan;
            name.setText(currentBirthMan.getName());
            surname.setText(currentBirthMan.getSurname());
            email.setText(currentBirthMan.getEmail());
            phone.setText(currentBirthMan.getPhone());
            category.setText(currentBirthMan.getCategory().toString());
            //image.setImageURI(Uri.parse(currentRecipe.getPhoto()));
            dateBirth.setText(String.valueOf(currentBirthMan.getBirthData()));
        }
    }

/*

    public void emailCross(View view) {
        currentDateFormat = simpleDateFormat.format(calendar.getTime());
        currentDateFormat = currentDateFormat.substring(0,5);
        birthDateFormat = dateBirth.getText().toString();
        birthDateFormat = birthDateFormat.substring(0,5);

        if(currentDateFormat.equals(birthDateFormat)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
                    + birthDayMan.getEmail()));
            startActivity(intent);}
        else{
            Toast.makeText(getActivity(),"Терпение, мой друг, время еще не пришло...",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void phoneCross(View view) {
        currentDateFormat = simpleDateFormat.format(calendar.getTime());
        currentDateFormat = currentDateFormat.substring(0,5);
        birthDateFormat = dateBirth.getText().toString();
        birthDateFormat = birthDateFormat.substring(0,5);

        if(currentDateFormat.equals(birthDateFormat)){
            String telephone = birthDayMan.getPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+ telephone));
            startActivity(intent);
        }
        else{
            Toast.makeText(getActivity(),"Терпение, мой друг, время еще не пришло...",
                    Toast.LENGTH_LONG).show();
        }
    }
*/

    @Override
    public void onStart() {
        super.onStart();
       setDataInCurrentRecipe();
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   buttonEmail.setOnClickListener(view -> {
            currentDateFormat = simpleDateFormat.format(calendar.getTime());
            currentDateFormat = currentDateFormat.substring(0,5);
            birthDateFormat = dateBirth.getText().toString();
            birthDateFormat = birthDateFormat.substring(0,5);

            if(currentDateFormat.equals(birthDateFormat)){

               *//* Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
                        + birthDayMan.getEmail()));
                startActivity(intent);*//*
            }
            else{
                Toast.makeText(getActivity(),"Терпение, мой друг, время еще не пришло...",
                        Toast.LENGTH_LONG).show();
            }
        });*/
        buttonPhone.setOnClickListener(view -> {
            currentDateFormat = simpleDateFormat.format(calendar.getTime());
            currentDateFormat = currentDateFormat.substring(0,5);
            birthDateFormat = dateBirth.getText().toString();
            birthDateFormat = birthDateFormat.substring(0,5);

            if(currentDateFormat.equals(birthDateFormat)){
                String telephone = birthDayMan.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ telephone));
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(),"Терпение, мой друг, время еще не пришло...",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InfoFragmentView = inflater.inflate(R.layout.fragment_info, container, false);

        name = InfoFragmentView.findViewById(R.id.nameCurrentPersonId);
        surname =  InfoFragmentView.findViewById(R.id.surnameCurrentPersonId);
        email =  InfoFragmentView.findViewById(R.id.emailCurrentPersonId);
        phone = InfoFragmentView.findViewById(R.id.phoneCurrentPersonId);
        category = InfoFragmentView.findViewById(R.id.categoryCurrentPersonId);
        dateBirth = InfoFragmentView.findViewById(R.id.birthdayCurrentPersonId);

        buttonEmail = InfoFragmentView.findViewById(R.id.emailIdCurrent);
        buttonPhone = InfoFragmentView.findViewById(R.id.phoneIdCurrent);

        return InfoFragmentView;
    }
}