package ie.ul.alcoguardian;

import static android.content.ContentValues.TAG;
import static ie.ul.alcoguardian.Profile.db;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

public class Memory extends AppCompatActivity implements Serializable {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://alcoguardian-default-rtdb.europe-west1.firebasedatabase.app/");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_memory);

            Button submitButton = findViewById(R.id.button2);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_memory();
                }
            });

        }

    public void add_memory() {
            String uid = null;
            if (auth.getCurrentUser() != null) {
                uid = auth.getCurrentUser().getUid();
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (db == null) {
                Log.e("Memory", "FirebaseFirestore object is null");
                return;
            }

            CollectionReference memoryRef = db.collection("Memories");
            if (memoryRef == null) {
                Log.e("Memory", "CollectionReference object is null");
                return;
            }

            EditText memoryEditText = findViewById(R.id.editTextTextPersonName4);

            String memory = memoryEditText.getText().toString().trim();
            Calendar cal = Calendar.getInstance();
            String date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

            MemoryObj newMemory = new MemoryObj(uid, memory, date);
            memoryRef.add(newMemory)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

//    public void delete_memory() {
//
//        db.collection("cities").document("DC")
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error deleting document", e);
//                    }
//                });
//    }

}
