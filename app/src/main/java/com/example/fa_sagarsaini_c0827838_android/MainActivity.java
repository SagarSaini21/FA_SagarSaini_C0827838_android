package com.example.fa_sagarsaini_c0827838_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add, mapbtn;
    MyDatabaseHelper myDB;
    ArrayList<String> id, name, lati, longi;
    RVAdapter rvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        add = findViewById(R.id.btnadd);
        mapbtn = findViewById(R.id.btnmap);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);

            }
        });
        myDB = new MyDatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        lati = new ArrayList<>();
        longi = new ArrayList<>();
        dispalydata();
        rvAdapter = new RVAdapter(MainActivity.this, MainActivity.this, id, name, lati, longi);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void dispalydata() {
        Cursor cursor = myDB.readdata();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Products Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                lati.add(cursor.getString(2));
                longi.add(cursor.getString(3));


            }
        }
    }



}


