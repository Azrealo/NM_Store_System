package com.example.nm_store_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class activity_edit extends AppCompatActivity {
    public String account = "";
    private RecyclerView recyclerView;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private Myadapter myadapter;
    public MysqlCon con = new MysqlCon();
    public String[][] goodsdata = new String[][]{};
    public final int[] A = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        recyclerView = findViewById(R.id.RecyclerView);
        Thread get_goodsdata = new Thread(new Runnable() {
            @Override
            public void run() {
                A[0] = con.get_goodslenght(account);
                goodsdata = con.get_goodsdata(account,A[0]);
            }
        });
        get_goodsdata.start();
        try {
            get_goodsdata.join();
            for(int i = 0; i < A[0]; i ++){
                HashMap<String,String> row = new HashMap<>();
                row.put("name",goodsdata[i][0]);
                row.put("quantity",goodsdata[i][1]);

                data.add(row);
            }
            myadapter = new Myadapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(myadapter);
        } catch (Exception e){
            Log.e("Error",e.toString());
        }

    }
    private class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View edit_list;
            public TextView textview_goods_name;
            public EditText editText_quantity;
            public Button button_update;

            public MyViewHolder(View v){
                super(v);
                edit_list = v;
                textview_goods_name = edit_list.findViewById(R.id.textview_goods_name);
                editText_quantity = edit_list.findViewById(R.id.editText_quantity);
                button_update = edit_list.findViewById(R.id.button_update);


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
        public void onBindViewHolder(@NonNull final Myadapter.MyViewHolder holder, final int position) {
            holder.textview_goods_name.setText(data.get(position).get("name"));
            holder.editText_quantity.setText(data.get(position).get("quantity"));
            holder.button_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int n;
                    final String name;
                    name = holder.textview_goods_name.getText().toString();
                    n = Integer.valueOf(holder.editText_quantity.getText().toString());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            con.change_quantity(account,name,n);

                        }
                    });
                    thread.start();
                    Toast.makeText(activity_edit.this,"修改成功",Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            });

        }



        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public void buttom_done_code (View view){
        activity_edit.this.finish();
    }

}




