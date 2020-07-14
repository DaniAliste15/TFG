package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText textName;
    private EditText textEmail;
    private EditText textPassword;
    private Button buttonRegistrar;
    private Button buttonLogin;
    private ProgressDialog barra;
    private int nIncencidios = 0;

    //variables de los datos a registrar
    private String name = "";
    private String email = "";
    private String password = ""; //firebase exige que tenga al menos 6 caracteres la pass

    FirebaseAuth Auth;
    DatabaseReference Database;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance().getReference();

        barra = new ProgressDialog(this);

        textName = (EditText) findViewById(R.id.editTextName);
        textEmail = (EditText) findViewById(R.id.editTextEmail);
        textPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegistrar = (Button) findViewById(R.id.btnRegistrar);
        buttonLogin = (Button) findViewById(R.id.btnSendLogin);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = textName.getText().toString();
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if (password.length() >= 6) {
                        barra.setTitle("Registrando");
                        barra.setMessage("Espere...");
                        barra.setCanceledOnTouchOutside(false);
                        barra.show();
                        registrerUser();
                    } else {
                        Toast.makeText(MainActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

    }


    private void registrerUser() {
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                /*if (!task.isSuccessful()) {
                    Log.d("respueta:_:", "onComplete: Failed=" + task.getException().getMessage()); //ADD THIS


                }*/ //Log para comprobar porque la tarea es falsa
                if (task.isSuccessful()) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);
                    map.put("numeroincendios",nIncencidios);


                    String id = Auth.getCurrentUser().getUid(); //obtener el id del usuario

                    Database.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, MapaActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo crear correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "No se ha podido registar dicho usuario." +
                            "Asegurese de que el EMAIL cumple con toda su sintaxis" + "¡CUIDADO CON LOS ESPACIOS AL FINAL!", Toast.LENGTH_LONG).show();
                }
            }

        });

    }


   @Override
    protected void onStart() {
        super.onStart();

        if(Auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this,MapaActivity.class));
            finish();
        }
    }
}
