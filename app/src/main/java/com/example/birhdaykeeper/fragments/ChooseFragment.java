package com.example.birhdaykeeper.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.serializableStuff.JsonManager;
import com.example.birhdaykeeper.serializableStuff.StringForJson;
import com.example.birhdaykeeper.unit.BirthDayMan;

import java.io.File;
import java.util.List;


public class ChooseFragment extends Fragment {


    View ChooseFragmentView;
    ListView listView;
    Button justSendButton;
    String FILENAME = "templateCongratulation.json";
    ArrayAdapter<String> arrayAdapter;
    final String BIRTH_ARG = "birthArg";

    BirthDayMan birthDayMan;

    File file;
    JsonManager jsonManager = new JsonManager();
    StringForJson stringForJson = new StringForJson();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChooseFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ChooseFragment newInstance(String param1, String param2) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           birthDayMan =  (BirthDayMan) getArguments().getSerializable(BIRTH_ARG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String selected = stringForJson.listTemplate.get(position);
                Log.i("Log_frag", selected);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
                        + birthDayMan.getEmail()));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Прими мои поздравления, семпай");
                intent.putExtra(Intent.EXTRA_TEXT,selected);
                startActivity(intent);
            }
        });

        justSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"
                        + birthDayMan.getEmail()));
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ChooseFragmentView = inflater.inflate(R.layout.fragment_choose, container, false);
        listView = ChooseFragmentView.findViewById(R.id.mineListView);

        justSendButton = ChooseFragmentView.findViewById(R.id.justSendToEmail);

        file = new File(ChooseFragmentView.getContext().getFilesDir(),FILENAME);
        stringForJson = jsonManager.jsonManipulation.deserializationFromJson(file);

        arrayAdapter = new ArrayAdapter<String>(ChooseFragmentView.getContext(),
                android.R.layout.simple_list_item_1,stringForJson.listTemplate);

        listView.setAdapter(arrayAdapter);

        return ChooseFragmentView;
    }
}