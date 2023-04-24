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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Review extends AppCompatActivity implements Serializable {
    // add review, delete review, display review
    //review needs to have - address, business name, reviewer name, review text,
    //form for adding review, needs at least 3 text boxes and an enter button.
    //Extra review - kind how the night went? reminder? memories?

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://alcoguardian-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_review();
            }
        });

    }

    public void add_review() {
        String uid = null;
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (db == null) {
            Log.e("Review", "FirebaseFirestore object is null");
            return;
        }

        CollectionReference reviewsRef = db.collection("Reviews");
        if (reviewsRef == null) {
            Log.e("Review", "CollectionReference object is null");
            return;
        }

        EditText businessEditText = findViewById(R.id.editTextTextPersonName);
        EditText addressEditText = findViewById(R.id.editTextTextPersonName2);
        EditText reviewEditText = findViewById(R.id.editTextTextPersonName3);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        String business = businessEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String review = reviewEditText.getText().toString().trim();
        float stars = ratingBar.getRating();

        ReviewObj newReview = new ReviewObj(uid, address, business, review, stars);
        reviewsRef.add(newReview)
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


    //TODO
//    public void delete_review() {
//        CollectionReference citiesRef = db.collection("Reviews");
//
//// Create a query against the collection.
//        Query query = citiesRef.whereEqualTo("state", "CA");
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
//
//    public void display_review() {
//        // Create a reference to the cities collection
//        CollectionReference citiesRef = db.collection("Reviews");
//
//// Create a query against the collection.
//        Query query = citiesRef.whereEqualTo("state", "CA");
//    }
}

