package ie.ul.alcoguardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class displayReview extends RecyclerView.Adapter<displayReview.ViewHolder>{
    private List<ReviewObj> reviews;
    private Context mContext;

    public displayReview(Context context, List<ReviewObj> reviews) {
        this.mContext = context;
        this.reviews = reviews;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.business.setText(reviews.get(position).getBusiness());
        holder.address.setText(reviews.get(position).getAddress());
        holder.review.setText(reviews.get(position).getReview());
        holder.stars.setRating(reviews.get(position).getStars());
        holder.user.setText(reviews.get(position).getUser());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView business, address, review, user;
        public RatingBar stars;

        public ViewHolder(View itemView) {
            super(itemView);
            business = itemView.findViewById(R.id.business_name);
            address = itemView.findViewById(R.id.business_address);
            review = itemView.findViewById(R.id.review_text);
            stars = itemView.findViewById(R.id.rating_bar);
            user = itemView.findViewById(R.id.user_reference);
        }
    }
}


