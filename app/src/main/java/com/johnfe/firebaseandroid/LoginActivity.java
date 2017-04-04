package com.johnfe.firebaseandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity  {



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText txtEmail;
    EditText txtClave;
    Button btnRegistrar;
    Button btnSignIn;
    Button btnRecordarClave;
    ProgressDialog progreso;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner= (Spinner) findViewById(R.id.tipoUsuario) ;
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtClave= (EditText) findViewById(R.id.txtClave);
        btnRegistrar= (Button) findViewById(R.id.btnRegistrar);
        btnSignIn= (Button) findViewById(R.id.btnSignIn);
        btnRecordarClave= (Button) findViewById(R.id.btnRecodarClave);





        mAuth = FirebaseAuth.getInstance();

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


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(intent);


            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progreso = new ProgressDialog(LoginActivity.this);
                progreso.setMessage("Iniciando sesion...");
                progreso.setCancelable(false);
                progreso.show();
                mAuth.signInWithEmailAndPassword(txtEmail.getText().toString().trim(), txtClave.getText().toString().trim())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("[Usuario logueado] ", ""+ task.isSuccessful());
                                progreso.dismiss();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.d("[Usuario logueado] ", ""+ task.isSuccessful());
                                    Toast.makeText(LoginActivity.this, "Usuario no logueado",
                                            Toast.LENGTH_SHORT).show();
                                }else{

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

                                   // startActivity(intent);
                                    if (user.isEmailVerified()) {
                                        Intent intent2 = new Intent(LoginActivity.this,HomeActivity.class);

                                        startActivity(intent2);

                                    } else {
                                        // User is signed out
                                        Log.d("[Usuario login]: ", "El Email no esta verificado");
                                        user.sendEmailVerification();
                                        Log.d("[Usuario]: ", "Email enviado:" );
                                    }





                                }

                                // ...
                            }
                        });
            }
        });

        btnRecordarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(txtEmail.getText().toString().trim());
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
