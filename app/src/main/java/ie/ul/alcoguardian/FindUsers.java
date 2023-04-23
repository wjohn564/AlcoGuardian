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

public class FindUsers extends AppCompatActivity {

    private EditText searchEmail;
    private Button searchButton;
    private TextView resultTextView;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        searchEmail = findViewById(R.id.email_edit_text);
        searchButton = findViewById(R.id.search_button);
        resultTextView = findViewById(R.id.results_list_view);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
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
    }

    private void searchForUsers(String email) {
        collectionReference.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String result = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                result = document.getString("email");
                            }
                            if (TextUtils.isEmpty(result)) {
                                resultTextView.setText("No users found with this email address");
                            } else {
                                resultTextView.setText("User found: " + result);
                            }
                        } else {
                            resultTextView.setText("Error searching for users");
                        }
                    }
                });
    }
}
