package com.example.proyectomapa;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView email;
    private EditText clave;
    private EnlaceBD enlaceBD;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        clave = (EditText) findViewById(R.id.password);
        btn=(Button)findViewById(R.id.email_sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autentificarse();
            }
        });
        enlaceBD=new EnlaceBD(this);
    }

    public void autentificarse(){
        Cursor c=enlaceBD.consulta("select * from usuarios where usuario='"+email.getEditableText().toString()+"'");
        if(c.moveToNext()){
            if(clave.getEditableText().toString().equals(c.getString(c.getColumnIndex("clave")))){
                startActivity(new Intent(this, MapaActivity.class));
            }else {
                msgError();
            }
        }else {
            msgError();
        }
    }

    public void msgError(){
        Toast.makeText(this, "Usuario o clave invalidos", Toast.LENGTH_SHORT).show();
    }
}

