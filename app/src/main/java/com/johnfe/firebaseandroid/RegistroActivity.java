package com.johnfe.firebaseandroid;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.johnfe.firebaseandroid.model.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;

    EditText txtEmail;
    EditText txtClave;
    EditText txtNombre;
    EditText txtDocumento;

    Spinner tipoDocumento;
    Spinner tipoUsuario;

    Usuario usuario = null;

    Button btnSignUp;
    ProgressDialog progreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("[Usuario]: ", "El usuario esta autenticado con el UID:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("[Usuario]: ", "El usuario no esta autenticado");
                }
                // ...
            }
        };

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtClave = (EditText) findViewById(R.id.txtEmail);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtDocumento = (EditText) findViewById(R.id.txtDocumento);
        tipoUsuario= (Spinner) findViewById(R.id.tipoUsuario);
        tipoDocumento= (Spinner) findViewById(R.id.tipoDocumento);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        usuario = new Usuario();


        List<String> tipoDocumentoList = new ArrayList<String>();
        tipoDocumentoList.add("- Tipo Documento -");
        tipoDocumentoList.add("Cedula");
        tipoDocumentoList.add("Cedula Extranjería");
        tipoDocumentoList.add("NIT");

        List<String> tipoUsuarioList = new ArrayList<String>();
        tipoUsuarioList.add("- Tipo Usuario -");
        tipoUsuarioList.add("Profesional");
        tipoUsuarioList.add("Usuario");

        ArrayAdapter<String> tipoDocumentoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipoDocumentoList);
        // Drop down layout style - list view with radio button
        tipoDocumentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tipoDocumento.setAdapter(tipoDocumentoAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> tipoUsuarioAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipoUsuarioList);
        // Drop down layout style - list view with radio button
        tipoUsuarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        tipoUsuario.setAdapter(tipoUsuarioAdapter);


        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    usuario.setTipoUsuario(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usuario.setTipoDocumento(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progreso = new ProgressDialog(RegistroActivity.this);
                progreso.setMessage("Creando cuenta...");
                progreso.setCancelable(false);
                progreso.show();


                if (txtDocumento.getText().toString().trim().isEmpty() ||
                        txtNombre.getText().toString().trim().isEmpty() ||
                        txtClave.getText().toString().trim().isEmpty() ||
                        txtEmail.getText().toString().trim().isEmpty() ||
                        tipoDocumento.getSelectedItemPosition() == 0 ||
                        tipoUsuario.getSelectedItemPosition() == 0) {
                    Toast.makeText(RegistroActivity.this, "Debe completar los campos!!!",
                            Toast.LENGTH_SHORT).show();

                } else {


                    usuario.setNombre(txtNombre.getText().toString().trim());
                    usuario.setEmail(txtEmail.getText().toString().trim());
                    usuario.setDocumento(txtDocumento.getText().toString().trim());



                    mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString().trim(), txtClave.getText().toString().trim())
                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("[Crear usuario]: ", "Usuario creado" + task.isSuccessful());
                                    progreso.dismiss();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {

                                        // User is signed in
                                        Log.d("[Usuario]: ", "El usuario esta autenticado con el UID:" + user.getUid());
                                        user.sendEmailVerification();
                                        Log.d("[Usuario]: ", "Email enviado:");
                                        usuario.setUid(user.getUid());

                                        DatabaseReference refRegistro = database.getReference("usuarios").child(txtDocumento.getText().toString().trim());
                                        refRegistro.setValue(usuario);



                                    } else {
                                        // User is signed out
                                        Log.d("[Usuario]: ", "El usuario no esta autenticado");
                                    }

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegistroActivity.this, "Usuario no creado!!!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
