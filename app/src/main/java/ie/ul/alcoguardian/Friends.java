package ie.ul.alcoguardian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        friendsList = new ArrayList<>();
        friendsListView = findViewById(R.id.friendList);

        // Get the "Friends" collection for the current user's email
        DocumentReference friendsListRef = db.collection("Friends")
                .document(user.getEmail());

        friendsListRef.collection("Friends").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String friendEmail = documentSnapshot.getId();
                    friendsList.add(friendEmail);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Friends.this,
                        android.R.layout.simple_list_item_1, friendsList);
                friendsListView.setAdapter(adapter);
            }
        });
    }
}


/*public class Friends extends AppCompatActivity {

    private Button viewButton;
    private Button deleteFriendButton;
    String selectedFriendEmail = "";
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

       friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String friendEmail = adapterView.getItemAtPosition(position).toString();
                friendSelected(friendEmail);
            }
        });

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
        })

        deleteFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendSelected) {
                   removeFriend(userEmail, selectedFriendEmail);
                }
            }
       });

    }

   public void getFriends(final OnCompleteListener<QuerySnapshot> listener) {
        DocumentReference userRef = db.collection("Friends").document(userEmail);

        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<String> friends = (List<String>) documentSnapshot.get("Friends");
                            if (friends != null) {
                                Query query = db.collection("Friends").document(userEmail).getParent().whereIn("Email", friends);
                                query.get().addOnCompleteListener(listener);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting friends", e);
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


    public String friendSelected(String friendRef) {
        friendSelected = true;
        selectedFriendEmail = friendRef;
        viewButton.setEnabled(true);
        deleteFriendButton.setEnabled(true);
        return selectedFriendEmail;
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
}*/