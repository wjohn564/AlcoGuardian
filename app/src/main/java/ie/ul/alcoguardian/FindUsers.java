package ie.ul.alcoguardian;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class FindUsers extends AppCompatActivity {

    private EditText mEmailEditText;
    private Button mSearchButton;
    private TextView mResultTextView;

    // Declare variables for Cloud Firestore
    private FirebaseFirestore mFirestore;
    private CollectionReference mUsersCollection;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        mEmailEditText = findViewById(R.id.email_edit_text);
        mSearchButton = findViewById(R.id.search_button);
        mResultTextView = findViewById(R.id.results_list_view);

        mFirestore = FirebaseFirestore.getInstance();
        mUsersCollection = mFirestore.collection("Users");

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mResultTextView.setText("Please enter an email address");
                } else {
                    searchForUsers(email);
                }
            }
        });
    }

    private void searchForUsers(String email) {
        mUsersCollection.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> results = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                results.add(document.getString("email"));
                            }
                            if (results.isEmpty()) {
                                mResultTextView.setText("No users found with this email address");
                            } else {
                                mResultTextView.setText("Users found: " + TextUtils.join(", ", results));
                            }
                        } else {
                            mResultTextView.setText("Error searching for users");
                        }
                    }
                });
    }
}
