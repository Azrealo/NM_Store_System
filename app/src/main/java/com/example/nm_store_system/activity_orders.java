package com.example.nm_store_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;

public class activity_orders extends AppCompatActivity {
    public String account = "";
    private RecyclerView recyclerView;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private Myadapter myadapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        recyclerView = findViewById(R.id.RecyclerView);
    }
    private class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View edit_list;
            public TextView textview_goods_name;
            public EditText editText_quantity;

            public MyViewHolder(View v){
                super(v);
                edit_list = v;
                textview_goods_name = edit_list.findViewById(R.id.textview_goods_name);
                editText_quantity = edit_list.findViewById(R.id.editText_quantity);

            }
        }

        @NonNull
        @Override
        public Myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View edit_list = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_list,parent,false);
            MyViewHolder vh = new MyViewHolder(edit_list);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull Myadapter.MyViewHolder holder, final int position) {
            holder.textview_goods_name.setText(data.get(position).get("name"));
            holder.editText_quantity.setText(data.get(position).get("quantity"));





                }



        @Override
        public int getItemCount() {
        return data.size();
        }
    }
}
