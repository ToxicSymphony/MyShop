package com.example.myshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    FirebaseFirestore myStore = FirebaseFirestore.getInstance();
    String idSeller;
    String totalcomision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText name = findViewById(R.id.etname);
        EditText email = findViewById(R.id.etemail);
        EditText phone = findViewById(R.id.etphone);
        TextView comision = findViewById(R.id.etcommision);
        Button salesView = findViewById(R.id.btnsales);
        Button add = findViewById(R.id.btnadd);
        Button search= findViewById(R.id.btnsearch);
        Button update = findViewById(R.id.btnupdate);
        Button delete = findViewById(R.id.btndelete);

       comision.setText(getIntent().getStringExtra("comision"));





        salesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),sale.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSeller(name.getText().toString(),email.getText().toString(),phone.getText().toString());

            }
        });


//
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStore.collection("seller")
                        .whereEqualTo("email", email.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            idSeller = document.getId();
                                            name.setText(document.getString("name"));
                                            phone.setText(document.getString("phone"));
                                            comision.setText(document.getString("comision"));


                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Este cliente no se encuentra en nuestra base de datos",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
//
//
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> seller = new HashMap<>();
                seller.put("email", email.getText().toString());
                seller.put("name", name.getText().toString());
                seller.put("phone", phone.getText().toString());
                seller.put("comision", comision.getText().toString());

                myStore.collection("seller").document(idSeller)
                        .set(seller)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d("cliente", "DocumentSnapshot successfully written!");
                                Toast.makeText(MainActivity.this,"Cliente actualizado correctmente...",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("cliente", "Error writing document", e);
                            }
                        });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Está seguro de eliminar el cliente con id "+email.getText().toString()+"?");
                alertDialogBuilder.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (comision.getText().toString().isEmpty()){
                                myStore.collection("seller").document(idSeller)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Cliente eliminado correctamente...", Toast.LENGTH_SHORT).show();
                                                name.setText("");
                                                email.setText("");
                                                phone.setText("");
                                                email.requestFocus();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("cliente", "Error deleting document", e);
                                            }
                                        });}
                                else{
                                    Toast.makeText(getApplicationContext(), "ya tienes comisiones", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


        });





    }

    private void saveSeller(String sName, String sEmail, String sPhone) {
        myStore.collection("seller")
                .whereEqualTo("email", sEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {


                                Map<String, Object> seller = new HashMap<>();
                                seller.put("email", sEmail);
                                seller.put("name", sName);
                                seller.put("phone", sPhone);

                                myStore.collection("seller")
                                        .add(seller)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                Toast.makeText(getApplicationContext(), "Seller added sucessufuly...", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Log.w(TAG, "Error adding document", e);
                                                Toast.makeText(getApplicationContext(), "Error adding customer, please check if the internet conection is stable or if the service is running correctly", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"This customer already exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }






}