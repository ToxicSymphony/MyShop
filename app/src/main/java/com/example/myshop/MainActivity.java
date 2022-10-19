package com.example.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText name = findViewById(R.id.etname);
        EditText email = findViewById(R.id.etemail);
        EditText phone = findViewById(R.id.etphone);
        EditText comision = findViewById(R.id.etcommision);
        Button add = findViewById(R.id.btnadd);
        Button search= findViewById(R.id.btnsearch);
        Button update = findViewById(R.id.btnupdate);
        Button delete = findViewById(R.id.btndelete);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }
}