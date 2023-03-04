package com.example.appprimera2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText videtnombre;
    private EditText videtcorreo;
    private EditText videtcontra;
    private Button vbotonregistrar;

    // Variable de los datos necesarios para registrar

    private String nombre = "";
    private String correo = "";
    private String contra = "";

    FirebaseAuth vAuth;
    DatabaseReference vBaseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vAuth = FirebaseAuth.getInstance();
        vBaseDatos = FirebaseDatabase.getInstance().getReference();


        videtnombre = (EditText) findViewById(R.id.editnombre);
        videtcorreo = (EditText) findViewById(R.id.editemail);
        videtcontra = (EditText) findViewById(R.id.editcontra);
        vbotonregistrar = (Button) findViewById(R.id.botonregistrar);


        vbotonregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nombre = videtnombre.getText().toString();
                correo = videtcorreo.getText().toString();
                contra = videtcontra.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contra.isEmpty()){
                    if (contra.length() >= 6){


                    }else {
                        Toast.makeText(MainActivity.this, "La contraseña debes tener al menos 6 caracteres ", Toast.LENGTH_SHORT).show();
                    }
                    regitrarusuario();
                }
                else {
                    Toast.makeText(MainActivity.this, "Debe de rellenar los campo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  regitrarusuario(){
        vAuth.createUserWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("correo", correo);
                    map.put("contra", contra);

                    String id = vAuth.getCurrentUser().getUid();
                    vBaseDatos.child("usuario").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(MainActivity.this, Perfil.class));
                                finish();
                            }

                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}