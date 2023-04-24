package ie.ul.alcoguardian;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private Button register;

    static Map<String, String> userDetails;

    static FirebaseFirestore db;
    static FirebaseAuth auth;
    static FirebaseUser user;

    private static DocumentReference doc;

    private Button returnBtn;
    private Button editProfile;

    TextView user_email;
    TextView user_fname;
    TextView user_lname;

    public Profile() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        user_email = findViewById(R.id.user_email);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            user_email.setText(user.getEmail());
        }

        user_fname = findViewById(R.id.user_fname);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            user_fname.setText(user.getEmail());
        }

        user_lname = findViewById(R.id.user_lname);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            user_lname.setText(user.getEmail());
        }

        editProfile = findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProfile();
            }
        });


    }

    public static void addProfile(FirebaseUser user) {
        auth =  FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        doc = db.collection("Users").document(user.getEmail());

        userDetails = new HashMap<String, String>();

        userDetails.put("Email", user.getEmail());

        doc.set(userDetails, SetOptions.merge());
    }

    private void deleteProfile() {
        // Create an AlertDialog to prompt the user to confirm deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete your profile?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked "Yes", delete profile from Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // Delete user document from "users" collection in Firestore
                            db.collection("users").document(user.getUid()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Profile deleted successfully
                                            Toast.makeText(getApplicationContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show();
                                            // Sign out user and return to login activity
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Profile deletion failed
                                            Toast.makeText(getApplicationContext(), "Failed to delete profile", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked "No", dismiss dialog
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


    public void viewProfile() {
        DocumentReference profile = db.collection("Users").document();
        profile.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents." , task.getException());
                        }
                    }
                });
    }

    private void openMainActivity() {
        Intent intend = new Intent(this, MainActivity.class);
        startActivity(intend);
    }

    private void openEditProfile() {
        Intent intend = new Intent(this, EditUsers.class);
        startActivity(intend);
    }
}