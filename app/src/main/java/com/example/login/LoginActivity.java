package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText textEmail;
    private EditText textPassword;
    private Button buttonLogin;
    private Button buttonOlvidar;
    private Button buttonRegistrar;

    private String email = "";
    private String password = "";

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.setTitle("Iniciar sesion");

        mAuth = FirebaseAuth.getInstance();

        textEmail = (EditText) findViewById(R.id.editTextEmail);
        textPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonOlvidar = (Button) findViewById(R.id.btnOlvidar);
        buttonRegistrar = (Button) findViewById(R.id.btnRegistrar);

        dialog = new ProgressDialog(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = textEmail.getText().toString();
                password = textPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty() && password.length() >= 6){
                    dialog.setTitle("Iniciando sesion");
                    dialog.setMessage("Espere...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    loginUser();
                }
                else {
                    //Toast.makeText(LoginActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();

                    if(email.isEmpty()) {
                        textEmail.setError("Debe rellenar este campo");
                    }
                    if(password.isEmpty()) {
                        textPassword.setError("Debe rellenar este campo");
                    }

                    if(!password.isEmpty()) {
                        textPassword.setError("Numero insuficiente de caracteres");
                    }
                }
            }
        });

        buttonOlvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetContraActivity.class));
            }
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });
    }

    private void loginUser () {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //distingir admin de usuariosnormales
                    startActivity(new Intent(LoginActivity.this,MapaActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesion,compruebe los datos" +
                            "¡CUIDADO CON LOS ESPACIOS AL FINAL!", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this,MapaActivity.class));
            finish();
        }
    }
}
