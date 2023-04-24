package ie.ul.alcoguardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class EditUsers extends AppCompatActivity {

    private Button confirm;
    private Button returnBtn;
    TextView fname, lname;
    EditText fnameUPD, lnameUPD;

    private FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_users);

        confirm = findViewById(R.id.confirmBtn);
        fnameUPD = findViewById(R.id.user_fname_upd);
        lnameUPD = findViewById(R.id.user_lname_upd);

        fstore = FirebaseFirestore.getInstance();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=fnameUPD.getText().toString();
                String lname=lnameUPD.getText().toString();

                Map<String,String>Users=new HashMap<>();
                Users.put("fnameUPD", fname);
                Users.put("lnameUPD", lname);

                fstore.collection("Users").add(Users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(EditUsers.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error=e.getMessage();

                        Toast.makeText(EditUsers.this, "Error :"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        returnBtn = findViewById(R.id.returnBTN2);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfile();
            }
        });

    }
    private void openEditProfile() {
        Intent intend = new Intent(this, Profile.class);
        startActivity(intend);
    }

}