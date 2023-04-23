package ie.ul.alcoguardian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SipWatch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DrinkAdapter adapter;
    private List<Drink> drinkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_watch);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drinkList = new ArrayList<>();
        adapter = new DrinkAdapter(drinkList);
        recyclerView.setAdapter(adapter);

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDrinkList();
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDrinkDialog();
            }
        });

        // Add sample drinks to the list
        Drink drink1 = new Drink("Beer", "8:30 PM");
        Drink drink2 = new Drink("Wine", "9:00 PM");
        Drink drink3 = new Drink("Cocktail", "10:00 PM");
        drinkList.add(drink1);
        drinkList.add(drink2);
        drinkList.add(drink3);
        adapter.notifyDataSetChanged();
    }

    private void clearDrinkList() {
        drinkList.clear();
        adapter.notifyDataSetChanged();
    }

    private void openAddDrinkDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_drink, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final EditText drinkNameEditText = view.findViewById(R.id.drinkNameEditText);
        final EditText drinkTimeEditText = view.findViewById(R.id.drinkTimeEditText);

        builder.setTitle("Add Drink");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String drinkName = drinkNameEditText.getText().toString();
                String drinkTime = drinkTimeEditText.getText().toString();
                Drink newDrink = new Drink(drinkName, drinkTime);
                drinkList.add(newDrink);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class Drink {
        private String name;
        private String time;

        public Drink(String name, String time) {
            this.name = name;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }
    }

    public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

        private List<Drink> drinkList;

        public DrinkAdapter(List<Drink> drinkList) {
            this.drinkList = drinkList;
        }

        public void setDrinkList(List<Drink> drinkList) {
            this.drinkList = drinkList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_item, parent, false);
            return new DrinkViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
            Drink drink = drinkList.get(position);
            holder.drinkNameTextView.setText(drink.getName());
            holder.drinkTimeTextView.setText(drink.getTime());
        }

        @Override
        public int getItemCount() {
            return drinkList.size();
        }

        public void addDrink(Drink drink) {
            drinkList.add(drink);
            notifyItemInserted(drinkList.size() - 1);
        }

        public void clearDrinks() {
            int size = drinkList.size();
            drinkList.clear();
            notifyItemRangeRemoved(0, size);
        }

        public class DrinkViewHolder extends RecyclerView.ViewHolder {

            public TextView drinkNameTextView;
            public TextView drinkTimeTextView;

            public DrinkViewHolder(View itemView) {
                super(itemView);
                drinkNameTextView = itemView.findViewById(R.id.drinkNameTextView);
                drinkTimeTextView = itemView.findViewById(R.id.drinkTimeTextView);
            }
        }
    }
}
