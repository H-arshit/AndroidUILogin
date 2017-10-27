package com.example.harshit.apiuse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class Festivals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festivals);


        ArrayList<FestivalModel> myList = (ArrayList<FestivalModel>) getIntent().getSerializableExtra("festival_list");

    }
}
