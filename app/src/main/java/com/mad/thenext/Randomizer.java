package com.mad.thenext;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Randomizer extends AppCompatActivity {
    String[] big_tech_things;
    String[] adjectives;
    String[] buzzwords;
    String[] nouns;
    String[] languages;
    String[] platforms;

    Button randomize_button;
    TextView big_tech_thing_text;
    TextView the_thing;
    String message;
    String buzzword;

    final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);

        big_tech_thing_text = findViewById(R.id.big_tech_thing);
        the_thing = findViewById(R.id.the_thing);
        randomize_button = findViewById(R.id.randomize);

        big_tech_things = getResources().getStringArray(R.array.big_tech_things);
        adjectives = getResources().getStringArray(R.array.adjectives);
        buzzwords = getResources().getStringArray(R.array.buzzwords);
        nouns = getResources().getStringArray(R.array.nouns);
        languages = getResources().getStringArray(R.array.languages);
        platforms = getResources().getStringArray(R.array.platforms);


        randomize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adjective = adjectives[rand.nextInt(adjectives.length)];
                buzzword = buzzwords[rand.nextInt(buzzwords.length)];
                String noun = nouns[rand.nextInt(nouns.length)];
                String language = languages[rand.nextInt(languages.length)];
                String platform = platforms[rand.nextInt(platforms.length)];

                String big_tech_thing = big_tech_things[rand.nextInt(big_tech_things.length)];
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
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//                startActivity(browserIntent);
            }
        });
    }
}
