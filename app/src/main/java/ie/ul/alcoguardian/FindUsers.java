package ie.ul.alcoguardian;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FindUsers extends AppCompatActivity {

    private EditText searchEmail;
    private Button searchButton;
    Button backButton;
    private TextView resultTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        searchEmail = findViewById(R.id.email_edit_text);
        searchButton = findViewById(R.id.search_button);
        backButton = findViewById(R.id.back);
        resultTextView = findViewById(R.id.results_text_view);

        db = FirebaseFirestore.getInstance();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = searchEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    resultTextView.setText("Please enter an email address");
                } else {
                    searchForUsers(email);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Friends.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void searchForUsers(String email) {
        CollectionReference usersRef = db.collection("Users");
        DocumentReference userDoc = usersRef.document(email);

        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String result = document.getString("Email");
                        resultTextView.setText("User found: " + result);

                        // Add the searched user to the current user's Friends collection
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        String currentUserEmail = mAuth.getCurrentUser().getEmail();
                        CollectionReference currentUserFriendsRef = db.collection("Users")
                                .document(currentUserEmail)
                                .collection("Friends");
                        Map<String, String> friendData = new HashMap<>();
                        friendData.put("Email", result);
                        currentUserFriendsRef.document(email).set(friendData);

                    } else {
                        resultTextView.setText("No users found with this email address");
                    }
                } else {
                    resultTextView.setText("Error searching for users");
                }
            }
        });
    }
}