package ie.ul.alcoguardian;

import static android.content.ContentValues.TAG;

import static ie.ul.alcoguardian.Profile.db;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button logoutButton;

    Button sipWatchButton;

    Button showMap;
    Button reviewButton;
    Button friendButton;
    //    Button safetyButton;
    Button profileButton;
    TextView textView;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 123; //android:layout_height="83dp"

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        friendButton = findViewById(R.id.friends);


        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


//        showMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Map.class);
//                startActivity(intent);
//            }
//        });
//
//        reviewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Review.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Friends.class);
                startActivity(intent);
                finish();
            }
        });



//        profileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Profile.class);
//                startActivity(intent);
//                finish();
//            }
//        });

//        showMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SipWatch.class);
//                startActivity(intent);
//            }
//        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reviewsRef = database.getReference("Reviews");
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    String business = reviewSnapshot.child("business").getValue(String.class);
                    String address = reviewSnapshot.child("address").getValue(String.class);
                    String review = reviewSnapshot.child("review").getValue(String.class);
                    float stars = reviewSnapshot.child("stars").getValue(float.class);
                    String user = reviewSnapshot.child("user").getValue(String.class);
                    Review reviewObj = new Review(user, address, business, review, stars);
                    reviews.add(reviewObj);
                }
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(new displayReview(reviews));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read reviews", error.toException());
            }

    });
}
}