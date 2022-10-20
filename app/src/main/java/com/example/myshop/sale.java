package com.example.myshop;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class sale extends AppCompatActivity {
    FirebaseFirestore myStore = FirebaseFirestore.getInstance();
    String idSeller;
    String sellerEmail;
    String name;
    String phone;
    String comision2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        EditText date = findViewById(R.id.etdate);
        EditText email = findViewById(R.id.etemail);
        EditText sales = findViewById(R.id.etsalevalue);
        Button add = findViewById(R.id.btnadd);
        Button search = findViewById(R.id.btnsearch);
        Button sellersView = findViewById(R.id.btnsellers);






        sellersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSale(date.getText().toString(),email.getText().toString(),sales.getText().toString());

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
                                            sellerEmail = document.getString("email");
                                            name = document.getString("name");
                                            phone = document.getString("phone");
                                            comision2 = document.getString("comision");

                                            Toast.makeText(sale.this,"1",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            }
                        });

            }
        });


    }

    private void saveSale(String sDate, String sEmail, String sSales) {
        myStore.collection("sales")
                .whereEqualTo("email", sEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(parseInt(sSales) >= 10000000) {
                            if (task.isSuccessful()) {
                                    Map<String, Object> sales = new HashMap<>();
                                    sales.put("email", sEmail);
                                    sales.put("date", sDate);
                                    sales.put("salevalue", sSales);

                                    myStore.collection("sales")
                                            .add(sales)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                    Toast.makeText(getApplicationContext(), "Sale added sucessufuly...", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Log.w(TAG, "Error adding document", e);
                                                    Toast.makeText(getApplicationContext(), "Error adding customer, please check if the internet conection is stable or if the service is running correctly", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    String comision = String.valueOf(parseInt(sSales)*2/100);
                                    Map<String, Object> seller = new HashMap<>();
                                    seller.put("email", sellerEmail);
                                    seller.put("name", name);
                                    seller.put("phone", phone);

                                    if(task.isSuccessful()){
                                        if(comision2 == null){
                                            seller.put("comision", comision);
                                            myStore.collection("seller").document(idSeller)
                                                    .set(seller)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //Log.d("cliente", "DocumentSnapshot successfully written!");
                                                            Toast.makeText(sale.this,"Has conseguido una comision :D...",Toast.LENGTH_SHORT).show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("cliente", "Error writing document", e);
                                                        }
                                                    });

                                        }else{
                                            comision = String.valueOf( parseInt(comision2)+parseInt(comision));
                                            seller.put("comision", comision);
                                            myStore.collection("seller").document(idSeller)
                                                    .set(seller)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //Log.d("cliente", "DocumentSnapshot successfully written!");
                                                            Toast.makeText(sale.this,"Este valor se ha sumado a tu comision total :D...",Toast.LENGTH_SHORT).show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("cliente", "Error writing document", e);
                                                        }
                                                    });


                                        }



                                    }




                                }else{
                                Toast.makeText(getApplicationContext(),"Error ading sale",Toast.LENGTH_SHORT).show();
                            }




                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"The sale must be equal or higher than 10000000",Toast.LENGTH_SHORT).show();


                            }




                        }

                });
    }
}