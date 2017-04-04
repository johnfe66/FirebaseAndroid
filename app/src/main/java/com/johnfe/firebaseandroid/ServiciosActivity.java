package com.johnfe.firebaseandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiciosActivity extends AppCompatActivity {

    private TextView txtNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        txtNombre= (TextView) findViewById(R.id.tvNombre);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String cedula= "";
        DatabaseReference myRef = database.getReference("usuarios").child("94552499").child("nombre");
        //cedula=myRef.chil


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("TAG", "Valor es: " + value);
                txtNombre.setText(value);
            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
    }
}
