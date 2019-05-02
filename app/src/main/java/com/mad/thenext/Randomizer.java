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

public class Randomizer extends AppCompatActivity {
    ArrayList<String> big_tech_things;
    ArrayList<String> adjectives;
    ArrayList<String> buzzwords;
    ArrayList<String> nouns;
    ArrayList<String> languages;
    ArrayList<String> platforms;

    //For Serious Mode
    FirebaseFirestore mFirestore;
    Map seriousData;
    ArrayList<String> serious_big_tech_things;
    ArrayList<String> serious_adjectives;
    ArrayList<String> serious_buzzwords;
    ArrayList<String> serious_nouns;
    ArrayList<String> serious_languages;
    ArrayList<String> serious_platforms;

    Button randomize_button;
    Button serious_button;
    Button save_button;
    Button i_button;
    TextView big_tech_thing_text;
    TextView the_thing;
    String message;
    String buzzword;

    final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);

        Intent intent = getIntent();
        if (null != intent) {
            adjectives = intent.getStringArrayListExtra("adjectives");
            //Log.v("Words", adjectives.toString());
            buzzwords = intent.getStringArrayListExtra("buzzwords");
            nouns = intent.getStringArrayListExtra("nouns");
            languages = intent.getStringArrayListExtra("languages");
            platforms = intent.getStringArrayListExtra("platforms");
            big_tech_things = intent.getStringArrayListExtra("big_tech_things");
        }


        big_tech_thing_text = findViewById(R.id.big_tech_thing);
        the_thing = findViewById(R.id.the_thing);
        randomize_button = findViewById(R.id.randomize);
//SERIOUS MODE -------------------------------------------------------------------------------------
        FirebaseApp.initializeApp(getApplicationContext());
        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("serious_phrases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                seriousData = document.getData();

                                try {
                                    serious_adjectives = (ArrayList<String>) seriousData.get("serious_adjectives");
                                    serious_buzzwords = (ArrayList<String>) seriousData.get("serious_buzzwords");
                                    serious_nouns = (ArrayList<String>) seriousData.get("serious_nouns");
                                    serious_languages = (ArrayList<String>) seriousData.get("serious_languages");
                                    serious_platforms = (ArrayList<String>) seriousData.get("serious_platforms");
                                    serious_big_tech_things = (ArrayList<String>) seriousData.get("serious_big_tech_things");
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


        serious_button = findViewById(R.id.serious);
        serious_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Randomizer.this, "SERIOUS MODE: E N G A G E D", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Seriousizer.class);
                i.putStringArrayListExtra("serious_adjectives", serious_adjectives);
                i.putStringArrayListExtra("serious_buzzwords", serious_buzzwords);
                i.putStringArrayListExtra("serious_nouns", serious_nouns);
                i.putStringArrayListExtra("serious_languages", serious_languages);
                i.putStringArrayListExtra("serious_platforms", serious_platforms);
                i.putStringArrayListExtra("serious_big_tech_things", serious_big_tech_things);

                startActivity(i);
            }
        });
//END SERIOUS MODE ---------------------------------------------------------------------------------

//info button
        i_button = findViewById(R.id.i);
        i_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Randomizer.this, "Tap on 'The Next __' to search related news", Toast.LENGTH_LONG).show();
                Toast.makeText(Randomizer.this, "Tap on the buzzword to search for some related spicy memes", Toast.LENGTH_LONG).show();
                Toast.makeText(Randomizer.this, "Long press on the buzzword for related jobs", Toast.LENGTH_LONG).show();

            }
        });

        randomize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adjective = adjectives.get(rand.nextInt(adjectives.size()));
                buzzword = buzzwords.get(rand.nextInt(buzzwords.size()));
                String noun = nouns.get(rand.nextInt(nouns.size()));
                String language = languages.get(rand.nextInt(languages.size()));
                String platform = platforms.get(rand.nextInt(platforms.size()));

                String big_tech_thing = big_tech_things.get(rand.nextInt(big_tech_things.size()));
                big_tech_thing_text.setText(big_tech_thing + getResources().getString(R.string.colon));

                message = getResources().getString(R.string.the_thing, adjective, buzzword, noun, language, platform);

                the_thing.setText(message);

            }
        });
        //SAVE BUTTON --------------------------------------------------------------------------------------
        save_button = findViewById(R.id.save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message != null){
                    Map<String, String> toSave = new HashMap<>();
                    toSave.put(message, "savedMeme");
                    mFirestore.collection("saved").document("saved")
                            .set(toSave, SetOptions.merge());
                }
                Toast.makeText(Randomizer.this, "Saved!", Toast.LENGTH_SHORT).show();

            }
        });
//END SAVE BUTTON ----------------------------------------------------------------------------------
        the_thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getResources().getString(R.string.memes, buzzword)); // query contains search string
                startActivity(intent);
            }
        });
        the_thing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getResources().getString(R.string.jobs, buzzword)); // query contains search string
                startActivity(intent);
                return false;
            }
        });
        big_tech_thing_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getResources().getString(R.string.news, big_tech_thing_text.getText().toString())); // query contains search string
                startActivity(intent);
            }
        });
    }
}
