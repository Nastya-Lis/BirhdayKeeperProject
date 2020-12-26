package com.example.birhdaykeeper.recyclerViewPack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.unit.BirthDayMan;

import java.util.ArrayList;
import java.util.List;

public class BirthdayManAdapter extends RecyclerView.Adapter<BirthdayManAdapter.RecyclerBirth> {

    List<BirthDayMan> birthManList;
    List<BirthDayMan> birthManListCopy;

    public BirthdayManAdapter(List<BirthDayMan> recipeList){
        this.birthManList = recipeList;
        birthManListCopy = new ArrayList<>(birthManList);
    }

    public class RecyclerBirth extends RecyclerView.ViewHolder {
        TextView name,surname,birthday,category;
        public RecyclerBirth(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameElementId);
            surname = itemView.findViewById(R.id.surnameElementId);
            birthday = itemView.findViewById(R.id.dateBirthElementId);
            category = itemView.findViewById(R.id.categoryElementId);
        }
    }


    public interface OnBirthManClickListener {
        void onBirthManClick(BirthDayMan birthMan);
    }


    public interface OnBirthManLongClickListener{
        boolean onBirthManLongClick(BirthDayMan birthMan, View view);
    }

    public OnBirthManClickListener onBirthManClickListener;
    public OnBirthManLongClickListener onBirthManLongClickListener;



    public void setOnBirthManClickListener(OnBirthManClickListener onBirthManClickListener){
        this.onBirthManClickListener = onBirthManClickListener;
    }

    public void setOnBirthManLongClickListener(OnBirthManLongClickListener onBirthManLongClickListener){
        this.onBirthManLongClickListener = onBirthManLongClickListener;
    }


    @NonNull
    @Override
    public RecyclerBirth onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_template,
                parent,false);
        return new RecyclerBirth(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerBirth holder, int position) {
        ArrayList<BirthDayMan> birthManArrayList = (ArrayList<BirthDayMan>) birthManList;
        BirthDayMan birthMan = birthManArrayList.get(position);

        holder.name.setText(birthMan.getName());
        holder.surname.setText(birthMan.getSurname());
        holder.category.setText(birthMan.getCategory().toString());
        holder.birthday.setText(birthMan.getBirthData());

        if(onBirthManClickListener !=null){
            holder.itemView.setOnClickListener(view -> onBirthManClickListener.onBirthManClick(birthMan));
        }
        if(onBirthManLongClickListener != null){
            holder.itemView.setOnLongClickListener(view ->
                    onBirthManLongClickListener.onBirthManLongClick(birthMan,view));
        }

    }

    @Override
    public int getItemCount() {
        return  birthManList == null ? 0 : birthManList.size();
    }


}
