package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editUserName, editPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.editEmailSignUP);
        editPassword = findViewById(R.id.editPasswordSignUp);
        editUserName = findViewById(R.id.editUserNameSignUp);
        btnLogin = findViewById(R.id.btnLoginSignUp);
        btnSignUp = findViewById(R.id.btnSignUPActivity);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            transitionWhatsapp();
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSignUPActivity:
                ParseUser user = new ParseUser();
                user.setEmail(editEmail.getText().toString());
                user.setUsername(editUserName.getText().toString());
                user.setPassword(editPassword.getText().toString());
                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(MainActivity.this, "SignUp Successfully",
                                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            transitionWhatsapp();
                        } else {
                            FancyToast.makeText(MainActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.btnLoginSignUp:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void transitionWhatsapp(){
        Intent intent = new Intent(MainActivity.this, WhatsAppActivity.class);
        startActivity(intent);
    }
}
