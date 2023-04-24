package ie.ul.alcoguardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Friends extends AppCompatActivity {

    private FirebaseFirestore db;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<String> friendsList;
    private ListView friendsListView;

    Button backButton;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        searchButton = findViewById(R.id.search);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        friendsList = new ArrayList<>();
        friendsListView = findViewById(R.id.friendList);

        // Get the "Friends" subcollection for the current user's email
        CollectionReference friendsRef = db.collection("Users")
                .document(user.getEmail())
                .collection("Friends");

        friendsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String friendEmail = documentSnapshot.getId();
                    friendsList.add(friendEmail);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Friends.this,
                        android.R.layout.simple_list_item_1, friendsList);
                friendsListView.setAdapter(adapter);

                friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String friendEmail = (String) parent.getItemAtPosition(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Friends.this);
                        builder.setTitle("Delete Friend")
                                .setMessage("Are you sure you want to delete " + friendEmail + " from your friends list?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        friendsRef.document(friendEmail).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Friends.this, "Friend deleted successfully", Toast.LENGTH_SHORT).show();
                                                        friendsList.remove(friendEmail);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Friends.this, "Failed to delete friend", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create().show();
                    }
                });
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindUsers.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

