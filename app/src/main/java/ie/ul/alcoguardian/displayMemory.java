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

public class displayMemory extends RecyclerView.Adapter<displayMemory.ViewHolder>{
    private List<MemoryObj> memory;
    private Context mContext;

    public displayMemory(Context context, List<MemoryObj> memory) {
        this.mContext = context;
        this.memory = memory;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_memory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.user.setText(memory.get(position).getUser());
        holder.memory.setText(memory.get(position).getMemory());
        holder.date.setText(memory.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return memory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user, memory, date;

        public ViewHolder(View itemView) {
            super(itemView);
            memory = itemView.findViewById(R.id.memory_text);
            date = itemView.findViewById(R.id.date);
            user = itemView.findViewById(R.id.user_reference);
        }
    }
}


