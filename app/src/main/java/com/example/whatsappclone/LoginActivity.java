package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmailLogin);
        editPassword = findViewById(R.id.editPasswordLogin);
        btnLogin = findViewById(R.id.btnLoginActivity);
        btnSignUp = findViewById(R.id.btnSignUpLoginActivity);

        btnLogin.setOnClickListener(LoginActivity.this);
        btnSignUp.setOnClickListener(LoginActivity.this);

        if (ParseUser.getCurrentUser() != null){
                transitionWhatsapp();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLoginActivity:
                if (editEmail.getText().toString().equals("") || editPassword.getText().toString().equals("")){
                    FancyToast.makeText(LoginActivity.this, "Email and Password Required",
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                } else {
                    ParseUser.logInInBackground(editEmail.getText().toString(),
                            editPassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null){
                                        FancyToast.makeText(LoginActivity.this, "Login Successfully",
                                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                        transitionWhatsapp();
                                    } else {
                                        FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                                FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.btnSignUpLoginActivity:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void transitionWhatsapp(){
        Intent intent = new Intent(LoginActivity.this, WhatsAppActivity.class);
        startActivity(intent);
    }
}
