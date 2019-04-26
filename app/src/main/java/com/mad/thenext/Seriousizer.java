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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Seriousizer extends AppCompatActivity {
    ArrayList<String> big_tech_things;
    ArrayList<String> adjectives;
    ArrayList<String> buzzwords;
    ArrayList<String> nouns;
    ArrayList<String> languages;
    ArrayList<String> platforms;

    //For Silly Mode
    FirebaseFirestore mFirestore;
    Map data;

    Button serious_randomize_button;
    Button silly_button;
    TextView big_tech_thing_text;
    TextView the_thing;
    String message;
    String buzzword;

    final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seriousizer);

        Intent intent = getIntent();
        if (null != intent) {
            adjectives = intent.getStringArrayListExtra("adjectives");
            Log.v("Words", adjectives.toString());
            buzzwords = intent.getStringArrayListExtra("buzzwords");
            nouns = intent.getStringArrayListExtra("nouns");
            languages = intent.getStringArrayListExtra("languages");
            platforms = intent.getStringArrayListExtra("platforms");
            big_tech_things = intent.getStringArrayListExtra("big_tech_things");
        }


        big_tech_thing_text = findViewById(R.id.big_tech_thing);
        the_thing = findViewById(R.id.the_thing);
        serious_randomize_button = findViewById(R.id.seriousize);

//SILLY MODE ---------------------------------------------------------------------------------------
        FirebaseApp.initializeApp(getApplicationContext());
        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("serious_phrases")
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
                Toast.makeText(Seriousizer.this, "sIlLy MoDe: Re-EnGaGeD", Toast.LENGTH_SHORT).show();
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


        serious_randomize_button.setOnClickListener(new View.OnClickListener() {
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
