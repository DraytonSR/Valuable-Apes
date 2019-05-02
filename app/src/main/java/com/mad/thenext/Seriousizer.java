package com.mad.thenext;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Seriousizer extends AppCompatActivity {
    ArrayList<String> serious_big_tech_things;
    ArrayList<String> serious_adjectives;
    ArrayList<String> serious_buzzwords;
    ArrayList<String> serious_nouns;
    ArrayList<String> serious_languages;
    ArrayList<String> serious_platforms;

    //For Silly Mode
    FirebaseFirestore mFirestore;
    Map data;
    ArrayList<String> big_tech_things;
    ArrayList<String> adjectives;
    ArrayList<String> buzzwords;
    ArrayList<String> nouns;
    ArrayList<String> languages;
    ArrayList<String> platforms;

    Button serious_randomize_button;
    Button silly_button;
    Button save_button;
    TextView serious_big_tech_thing_text;
    TextView serious_the_thing;
    String serious_message;
    String serious_buzzword;

    final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seriousizer);

        Intent intent = getIntent();
        if (null != intent) {
            serious_adjectives = intent.getStringArrayListExtra("serious_adjectives");
            //Log.v("Words", serious_adjectives.toString());
            serious_buzzwords = intent.getStringArrayListExtra("serious_buzzwords");
            serious_nouns = intent.getStringArrayListExtra("serious_nouns");
            serious_languages = intent.getStringArrayListExtra("serious_languages");
            serious_platforms = intent.getStringArrayListExtra("serious_platforms");
            serious_big_tech_things = intent.getStringArrayListExtra("serious_big_tech_things");
        }


        serious_big_tech_thing_text = findViewById(R.id.serious_big_tech_thing);
        serious_the_thing = findViewById(R.id.serious_the_thing);
        serious_randomize_button = findViewById(R.id.seriousize);

//SILLY MODE ---------------------------------------------------------------------------------------
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
                            }
                        } else {
                            Log.w("ERROR", "Error getting serious documents.", task.getException());
                        }
                    }
                });
        silly_button = findViewById(R.id.silly);
        silly_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Seriousizer.this, "sIlLy MoDe: Re-EnGaGeD", Toast.LENGTH_LONG).show();
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
//END SILLY MODE -----------------------------------------------------------------------------------
//SAVE BUTTON --------------------------------------------------------------------------------------
        save_button = findViewById(R.id.save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Seriousizer.this, "Saved!", Toast.LENGTH_SHORT).show();
                if (serious_message != null){
                    Map<String, String> toSave = new HashMap<>();
                    toSave.put(serious_message, "savedSRS");
                    mFirestore.collection("saved").document("saved")
                            .set(toSave, SetOptions.merge());
                }

            }
        });
//END SAVE BUTTON ----------------------------------------------------------------------------------

        serious_randomize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serious_adjective = serious_adjectives.get(rand.nextInt(serious_adjectives.size()));
                serious_buzzword = serious_buzzwords.get(rand.nextInt(serious_buzzwords.size()));
                String serious_noun = serious_nouns.get(rand.nextInt(serious_nouns.size()));
                String serious_language = serious_languages.get(rand.nextInt(serious_languages.size()));
                String serious_platform = serious_platforms.get(rand.nextInt(serious_platforms.size()));
                String serious_big_tech_thing = serious_big_tech_things.get(rand.nextInt(serious_big_tech_things.size()));
                serious_big_tech_thing_text.setText(serious_big_tech_thing + getResources().getString(R.string.colon));

                serious_message = getResources().getString(R.string.serious_the_thing, serious_adjective, serious_buzzword, serious_noun, serious_language, serious_platform);

                serious_the_thing.setText(serious_message);

            }
        });
        /*serious_the_thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getResources().getString(R.string.jobs, serious_buzzword)); // query contains search string
                startActivity(intent);
            }
        });
        serious_big_tech_thing_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getResources().getString(R.string.news, serious_big_tech_thing_text.getText().toString())); // query contains search string
                startActivity(intent);
            }
        });*/
    }
}
