package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ModifDatosActivity extends AppCompatActivity {

    public EditText textPassword;
    public EditText textEmail;
    public Button buttonModificar;

    FirebaseAuth Auth;
    DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_datos);

        textPassword = (EditText) findViewById(R.id.editTextPassword);
        textEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonModificar = (Button) findViewById(R.id.btnModificar);

        buttonModificar.setOnClickListener(new View.OnClickListener() {
        
                String email2 = "";
                private String password2 = "";

                pass

    

                public void onClick(View v) {
                    if (!email.isEmpty() && !password.isEmpty()) {
                        if (password.length() >= 6) {
                            modificarDatos();
                        }
                        else {
                            Toast.makeText(ModifDatosActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                            
                    }
                    else {
                        Toast.makeText(ModifDatosActivity.this, "Debe rellenar los campos", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    private void modificarDatos() {
        String id = Auth.getCurrentUser().getUid(); //obtener el id del usuario
    }
}
