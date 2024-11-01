package com.example.db_connection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etPhone;
    private Button btnRegister, btnVerifyUser, btnShowUsers;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnRegister = findViewById(R.id.btn_register);
        btnVerifyUser = findViewById(R.id.btn_verify_user);
        btnShowUsers = findViewById(R.id.btn_show_users);

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Set OnClickListener for Register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set OnClickListener for Verify User Button
        btnVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUserDialog();
            }
        });

        // Set OnClickListener for Show All Users Button
        btnShowUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllUsers();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUser(email)) {
            Toast.makeText(this, "User already exists with this email", Toast.LENGTH_SHORT).show();
        } else {
            boolean isAdded = databaseHelper.addUser(username, password, email, phone);
            if (isAdded) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                etUsername.setText("");
                etPassword.setText("");
                etEmail.setText("");
                etPhone.setText("");
            } else {
                Toast.makeText(this, "Registration failed. Try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void verifyUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify User");

        // Create an EditText to enter email
        final EditText input = new EditText(this);
        input.setHint("Enter Email");
        builder.setView(input);

        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                } else {
                    boolean userExists = databaseHelper.checkUser(email);
                    if (userExists) {
                        Toast.makeText(MainActivity.this, "User exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showAllUsers() {
        StringBuilder usersList = databaseHelper.getAllUsers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Registered Users");
        builder.setMessage(usersList.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
