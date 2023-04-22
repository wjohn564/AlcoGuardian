package ie.ul.alcoguardian;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friends extends AppCompatActivity {

    private Button viewButton;
    private Button deleteFriendButton;
    String selectedFriendEmail;
    private ListView friendListView;
    private String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    Map<String, String> friends_list;

    FirebaseFirestore db;

    FirebaseAuth auth;

    FirebaseUser user;

    private DocumentReference doc;

    private boolean friendSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        viewButton = findViewById(R.id.viewProfileButton);
        deleteFriendButton = findViewById(R.id.deleteFriendButton);
        friendListView = findViewById(R.id.friendList);

        // Call the showFriendList() method with the current user's email
        showFriendList(userEmail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        doc = db.collection("Friends").document(user.getEmail());

        friends_list = new HashMap<>();

        friends_list.put("Friend", user.getEmail());

        doc.set(friends_list, SetOptions.merge());

        addFriend(user.getEmail(), selectedFriendEmail);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendSelected) {
                    viewFriendProfile(selectedFriendEmail);
                }
            }
        });

        deleteFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendSelected) {
                    //fill in
                   removeFriend(userEmail, selectedFriendEmail);
                }
            }
       });

    }

    public void showFriendList(String userEmail) {
        db.collection("Friends")
                .document(userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> friendEmails = (List<String>) document.get("friendEmails");
                                if (friendEmails != null && friendEmails.size() > 0) {
                                    // Create an ArrayAdapter for the list of friend emails
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Friends.this,
                                            android.R.layout.simple_list_item_1, friendEmails);
                                    // Set the adapter on the ListView
                                   friendListView.setAdapter(adapter);
                                } else {
                                    Log.d(TAG, "No friends found");
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });
    }


    public void friendSelected(String friendRef) {
        friendSelected = true;
        selectedFriendEmail = friendRef;
        viewButton.setEnabled(true);
        deleteFriendButton.setEnabled(true);
    }

    public void noFriendSelected() {
        friendSelected = false;
        String selectedFriendId = null;
        viewButton.setEnabled(false);
        deleteFriendButton.setEnabled(false);
    }


    public void addFriend(String userEmail, String friendEmail) {
        DocumentReference userRef = db.collection("Friends").document(userEmail);
        DocumentReference friendRef = db.collection("Friends").document(friendEmail);

        userRef.update("Friends" , FieldValue.arrayUnion(friendEmail))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Log.d(TAG, "Friend added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding friend", e);
                    }
                });

        friendRef.update("Friends" , FieldValue.arrayUnion(userEmail))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        Log.d(TAG, "Friend added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding friend", e);
                    }
                });
    }

    public void viewFriendProfile(String friendEmail) {
        db.collection("Friends")
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> friends = (List<String>) document.get("friends");
                                if (friends != null && friends.contains(friendEmail)) {
                                    db.collection("Users")
                                            .document("Friends")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                                            // Do something with the friend's profile data here
                                                        } else {
                                                            Log.d(TAG, "No such document");
                                                        }
                                                    } else {
                                                        Log.w(TAG, "Error getting document.", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Friend not found in Friends list");
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });
    }


    public void removeFriend(String userEmail, String friendEmail) {
        DocumentReference userRef = db.collection("Friends").document(userEmail);
        DocumentReference friendRef = db.collection("Friends").document(friendEmail);

        userRef.update("Friends" , FieldValue.arrayRemove(friendEmail))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void userRef) {
                        Log.d(TAG, "Friend removed successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error removing friend", e);
                    }
                });

        friendRef.update("Friends" , FieldValue.arrayRemove(userEmail))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void userRef) {
                        Log.d(TAG, "Friend removed successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error removing friend", e);
                    }
                });
    }
}