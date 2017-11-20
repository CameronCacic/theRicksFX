package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import edu.gatech.cs2340.thericks.R;

/**
 * Created by Ben Lashley on 9/20/2017.
 *
 * Screen that appears when app starts. Allows user to login or register for an account.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button login = findViewById(R.id.login_button);
        Button register = findViewById(R.id.register_button);

        login.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        });

        register.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        });
    }
}
