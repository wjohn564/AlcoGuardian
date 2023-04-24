//package ie.ul.alcoguardian;
//
//import static android.content.ContentValues.TAG;
//import static ie.ul.alcoguardian.Profile.db;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//public class Memory {
//
//    private String memory;
//    private String date;
//    private String user;
//
//    public Memory(String memory, String date, String user) {
//        this.memory = memory;
//        this.date = date;
//        this.user = user;
//    }
//
//    public String getMemory() {
//        return memory;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    //Memories
//    public void add_memory() {
//
//    }
//
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
////
////    public void display_memory() {
////
////    }
//
//}
