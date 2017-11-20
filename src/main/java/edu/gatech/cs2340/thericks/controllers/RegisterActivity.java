package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.UserDatabase;
import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Created by Ben Lashley on 9/19/2017.
 * Provides a window for the user to enter their data into to
 * create a new User. If username and password entered do not
 * meet the standards imposed by Security, a new account cannot
 * be created the user is prompted with what field is insufficient
 * and what they need to add to fix it
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText usernameEntry;
    private EditText passwordEntry;
    private EditText passwordReentry;
    private RadioGroup privilege;

    private TextView invalidUsername;
    private TextView invalidPassword;
    private TextView passwordMismatch;

    private UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = new UserDatabase();

        // Connect widgets
        usernameEntry   = findViewById(R.id.registration_create_username_entry);
        passwordEntry   = findViewById(R.id.registration_create_password_entry);
        passwordReentry = findViewById(R.id.registration_reenter_password_entry);

        privilege = findViewById(R.id.register_radio_group);

        Button createAccount = findViewById(R.id.register_button);

        invalidUsername  = findViewById(R.id.registration_invalid_username);
        invalidPassword  = findViewById(R.id.registration_invalid_password);
        passwordMismatch = findViewById(R.id.registration_password_mismatch);

        // Hide warning messages
        invalidUsername.setVisibility(View.GONE);
        invalidPassword.setVisibility(View.GONE);
        passwordMismatch.setVisibility(View.GONE);

        createAccount.setOnClickListener(v -> {
            Log.d(TAG, "Attempting to create new user account");
            String username = usernameEntry.getText().toString();
            String password = passwordEntry.getText().toString();
            String reenteredPassword = passwordReentry.getText().toString();
            Privilege user_privilege = null;
            boolean usernameTaken = (db.getUserByUsername(username) != null);
            boolean validEntries = true;

            if (!Security.validateUsername(username) || usernameTaken) {
                Log.d(TAG, "Username not valid");
                invalidUsername.setVisibility(View.VISIBLE);
                validEntries = false;
            } else {
                invalidUsername.setVisibility(View.GONE);
            }

            if (!Security.validatePassword(password)) {
                invalidPassword.setVisibility(View.VISIBLE);
                validEntries = false;
            } else {
                invalidPassword.setVisibility(View.GONE);
            }

            if (!password.equals(reenteredPassword)) {
                passwordMismatch.setVisibility(View.VISIBLE);
                validEntries = false;
            } else {
                passwordMismatch.setVisibility(View.GONE);
            }

            if (privilege.getCheckedRadioButtonId() == R.id.admin_radio) {
                user_privilege = Privilege.ADMIN;
            } else if (privilege.getCheckedRadioButtonId() == R.id.normal_radio) {
                user_privilege = Privilege.NORMAL;
            } else {
                // No privilege button selected
                validEntries = false;
            }

            if (validEntries) {
                db.createUser(username, password, user_privilege);

                // User object to pass to dashboard activity.
                Parcelable u = new User(username, password, user_privilege);

                Context context = v.getContext();
                Intent intent = new Intent(context, DashMapActivity.class);
                intent.putExtra("edu.gatech.cs2340.thericks.User", u);
                context.startActivity(intent);
                finish();
            }
        });
    }
}
