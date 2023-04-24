package ie.ul.alcoguardian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

    public class displayReview extends RecyclerView.Adapter<ie.ul.alcoguardian.displayReview.ViewHolder>{
        private List<Review> reviews;

        public displayReview(List<Review> reviews) {
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public ie.ul.alcoguardian.displayReview.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_display_review, parent, false);
            return new ie.ul.alcoguardian.displayReview.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ie.ul.alcoguardian.displayReview.ViewHolder holder, int position) {
            Review review = reviews.get(position);
            holder.businessTextView.setText(review.getBusiness());
            holder.addressTextView.setText(review.getAddress());
            holder.reviewTextView.setText(review.getReview());
            holder.starsRatingBar.setRating(review.getStars());
            holder.userTextView.setText(review.getUser());
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView businessTextView;
            public TextView addressTextView;
            public TextView reviewTextView;
            public RatingBar starsRatingBar;
            public TextView userTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                businessTextView = itemView.findViewById(R.id.business_name);
                addressTextView = itemView.findViewById(R.id.business_address);
                reviewTextView = itemView.findViewById(R.id.review_text);
                starsRatingBar = itemView.findViewById(R.id.rating_bar);
                userTextView = itemView.findViewById(R.id.user_reference);
            }
        }
    }