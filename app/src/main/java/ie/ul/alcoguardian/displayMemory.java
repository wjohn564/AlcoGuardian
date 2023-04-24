//package ie.ul.alcoguardian;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class displayMemory extends RecyclerView.Adapter<displayMemory.ViewHolder>{
//    private List<Review> reviews;
//
//    public displayMemory(List<Review> reviews) {
//        this.reviews = reviews;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.review_item_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Review review = reviews.get(position);
//        holder.businessTextView.setText(review.getBusiness());
//        holder.addressTextView.setText(review.getAddress());
//        holder.reviewTextView.setText(review.getReview());
//        holder.starsRatingBar.setRating(review.getStars());
//        holder.userTextView.setText(review.getUser());
//    }
//
//    @Override
//    public int getItemCount() {
//        return reviews.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView businessTextView;
//        public TextView addressTextView;
//        public TextView reviewTextView;
//        public RatingBar starsRatingBar;
//        public TextView userTextView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            businessTextView = itemView.findViewById(R.id.businessTextView);
//            addressTextView = itemView.findViewById(R.id.addressTextView);
//            reviewTextView = itemView.findViewById(R.id.reviewTextView);
//            starsRatingBar = itemView.findViewById(R.id.starsRatingBar);
//            userTextView = itemView.findViewById(R.id.userTextView);
//        }
//    }
//}
