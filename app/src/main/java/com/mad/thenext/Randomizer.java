package com.mad.thenext;

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
    List<String> nouns;
    Button randomize_button;
    //String noun;
    TextView noun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);

        randomize_button = findViewById(R.id.randomize);
        noun = findViewById(R.id.noun);

        //this will be string resources
        nouns = new ArrayList<>(Arrays.asList("Green", "yellow", "blue"));

        final Random rand_noun = new Random();

        randomize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noun.setText(nouns.get(rand_noun.nextInt(nouns.size())));

            }
        });


    }
}
