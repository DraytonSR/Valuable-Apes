package com.mad.thenext;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView swipe;

    FirebaseFirestore mFirestore;
    Map data;
    ArrayList<String> big_tech_things;
    ArrayList<String> adjectives;
    ArrayList<String> buzzwords;
    ArrayList<String> nouns;
    ArrayList<String> languages;
    ArrayList<String> platforms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = findViewById(R.id.swipe);

        swipe.setVisibility(View.INVISIBLE);

        FirebaseApp.initializeApp(getApplicationContext());
        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("phrases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data = document.getData();

                                try {
                                    adjectives = (ArrayList<String>) data.get("adjectives");
                                    buzzwords = (ArrayList<String>) data.get("buzzwords");
                                    nouns = (ArrayList<String>) data.get("nouns");
                                    languages = (ArrayList<String>) data.get("languages");
                                    platforms = (ArrayList<String>) data.get("platforms");
                                    big_tech_things = (ArrayList<String>) data.get("big_tech_things");
                                } catch (Exception e){
                                    Log.d("EXCEPTION", e.getMessage());
                                }

                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                swipe.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });


        swipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Randomizer.class);
                i.putStringArrayListExtra("adjectives", adjectives);
                i.putStringArrayListExtra("buzzwords", buzzwords);
                i.putStringArrayListExtra("nouns", nouns);
                i.putStringArrayListExtra("languages", languages);
                i.putStringArrayListExtra("platforms", platforms);
                i.putStringArrayListExtra("big_tech_things", big_tech_things);

                startActivity(i);

            }
        });

    }


}
