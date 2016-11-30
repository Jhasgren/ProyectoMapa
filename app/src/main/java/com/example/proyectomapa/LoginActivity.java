package com.example.proyectomapa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView email;
    private EditText clave;
    private EnlaceBD enlaceBD;
    private Button btn;
    private LoginButton btnFB;
    private CallbackManager callbackManager;
    private SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        clave = (EditText) findViewById(R.id.password);
        btn = (Button) findViewById(R.id.email_sign_in_button);
        btnFB = (LoginButton) findViewById(R.id.login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autentificarse();
            }
        });
        btnFB.setReadPermissions("email");
        enlaceBD = new EnlaceBD(this);
        sh = getPreferences(0);
        if (sh.getBoolean("logueado", false)) {
            startActivity(new Intent(this, MapaActivity.class));
        }
        callbackManager = CallbackManager.Factory.create();
        btnFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sh.edit().putBoolean("logueado", true).commit();
                startActivity(new Intent(LoginActivity.this, MapaActivity.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                msgError();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void autentificarse() {
        Cursor c = enlaceBD.consulta("select * from usuarios where usuario='" + email.getEditableText().toString() + "'");
        if (c.moveToNext()) {
            if (clave.getEditableText().toString().equals(c.getString(c.getColumnIndex("clave")))) {
                sh.edit().putBoolean("logueado", true).commit();
                startActivity(new Intent(this, MapaActivity.class));
            } else {
                msgError();
            }
        } else {
            msgError();
        }
    }

    public void msgError() {
        Toast.makeText(this, "Usuario o clave invalidos", Toast.LENGTH_SHORT).show();
    }
}

