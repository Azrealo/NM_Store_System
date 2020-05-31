package com.example.nm_store_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class activity_orders extends AppCompatActivity {
    public String account= "";
    private RecyclerView recyclerView;
    public MysqlCon con = new MysqlCon();
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private Myadapter myadapter;
    private final int[] A = {0};
    private String[][] OrdersData = new String[][]{};
    public String customesID;




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        recyclerView = findViewById(R.id.Recycle);
        Thread get_ordersdata = new get_ordersdata();
        get_ordersdata.start();
        try {
            get_ordersdata.join();
            for(int i = 0; i < A[0]; i ++){
                HashMap<String,String> row = new HashMap<>();
                row.put("customerID",OrdersData[i][0]);
                row.put("orders",OrdersData[i][1]);
                row.put("total_price",OrdersData[i][2]);
                row.put("is_check",OrdersData[i][3]);
                row.put("is_done",OrdersData[i][4]);
                row.put("is_cancel",OrdersData[i][5]);
                row.put("is_over",OrdersData[i][6]);

                data.add(row);
            }
            myadapter = new Myadapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(myadapter);
        } catch (Exception e){
            Log.e("Error",e.toString());
        }
        Thread relord_every_min = new relord_every_min();
        relord_every_min.start();


    }
    public class relord_every_min extends Thread{
        public void run(){
            while(!isInterrupted()){
                try {
                    Thread.sleep(30000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Thread get_ordersdata = new get_ordersdata();
                            Thread adaptert_lord = new adaptert_lord();
                            get_ordersdata.start();
                            try{
                                get_ordersdata.join();
                                adaptert_lord.start();
                                try{
                                    adaptert_lord.join();
                                    myadapter = new activity_orders.Myadapter();
                                    recyclerView.setAdapter(myadapter);
                                }catch (Exception e){

                                }
                            }catch (Exception e){

                            }
                        }
                    });
                } catch (Exception e){

                }
            }
        }
    }
    public class get_ordersdata extends Thread{
        public void run(){
            A[0] = con.get_orderslenght(account);
            OrdersData = con.get_ordersdata(account,A[0]);
        }

    }
    public class adaptert_lord extends Thread{
        public void run(){
            data.clear();
            for(int i = 0; i < A[0]; i ++){
                HashMap<String,String> row = new HashMap<>();
                row.put("customerID",OrdersData[i][0]);
                row.put("orders",OrdersData[i][1]);
                row.put("total_price",OrdersData[i][2]);
                row.put("is_check",OrdersData[i][3]);
                row.put("is_done",OrdersData[i][4]);
                row.put("is_cancel",OrdersData[i][5]);
                row.put("is_over",OrdersData[i][6]);
                data.add(row);
            }

        }
    }

    private class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View orders_list;
            public TextView textView_CustomerName;
            public TextView textView_OrdersInfo;
            public TextView textView_OrdersState;
            public Button button_accept;
            public Button button_cancel;
            public Button buttom_OrdersDone;
            public LinearLayout layout;
            public TextView textView_price;
            public Button button_over;
            public ScrollView scroll;
            public String[] orders_splite  ;
            public String final_info;
            public int i ;



            public MyViewHolder(View v){
                super(v);
                orders_list = v;
                buttom_OrdersDone = orders_list.findViewById(R.id.buttom_OrdersDone);
                textView_CustomerName = orders_list.findViewById(R.id.textView_CustomerName);
                textView_OrdersInfo = orders_list.findViewById(R.id.textView_OrdersInfo);
                textView_OrdersState = orders_list.findViewById(R.id.textView_OrdersState);
                button_accept=orders_list.findViewById(R.id.button_accept);
                button_cancel=orders_list.findViewById(R.id.button_cancel);
                textView_price = orders_list.findViewById(R.id.textView_price);
                layout = orders_list.findViewById(R.id.layout);
                button_over = orders_list.findViewById(R.id.button_over);
                scroll= orders_list.findViewById(R.id.scroll);

                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        findViewById(R.id.scroll).getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                });
                scroll.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
            }
        }

        @NonNull
        @Override
        public activity_orders.Myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View orders_list = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list,parent,false);
            activity_orders.Myadapter.MyViewHolder vh = new activity_orders.Myadapter.MyViewHolder(orders_list);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final Myadapter.MyViewHolder holder, final int position) {
            holder.orders_splite = new String[]{};
            holder.final_info = "";
            holder.orders_splite = data.get(position).get("orders").split(",");
            holder.final_info = holder.orders_splite[0];
            for(holder.i =1 ; holder.i <holder.orders_splite.length;holder.i++){
                holder.final_info = holder.final_info + "\n" + holder.orders_splite[holder.i];
            }
            holder.textView_CustomerName.setText(data.get(position).get("customerID"));
            holder.textView_OrdersInfo.setText(holder.final_info);
            holder.textView_price.setText(data.get(position).get("total_price"));

            if (A[0] > 0) {
                if (data.get(position).get("is_done").equals("0")) {
                    holder.textView_OrdersState.setText("未完成");
                    holder.button_over.setVisibility(View.INVISIBLE);
                } else {
                    holder.textView_OrdersState.setText("已完成");
                    holder.button_over.setVisibility(View.VISIBLE);
                }
                if (data.get(position).get("is_check").equals("1")){
                    holder.layout.setVisibility(View.VISIBLE);
                } else {
                    holder.layout.setVisibility(View.INVISIBLE);
                }
            }

            holder.button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread get_ordersdata = new get_ordersdata();
                    Thread adaptert_lord = new adaptert_lord();
                    customesID = holder.textView_CustomerName.getText().toString();

                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                        con.cancel(account,customesID);
                        }
                    });
                    a.start();
                    try{
                        a.join();

                        get_ordersdata.start();
                        try{
                            get_ordersdata.join();

                            adaptert_lord.start();
                            try{
                                adaptert_lord.join();
                                myadapter = new activity_orders.Myadapter();
                                recyclerView.setAdapter(myadapter);
                            }catch (Exception e){
                                Log.e("ERROR",e.toString());
                            }
                        } catch (Exception e){
                            Log.e("ERROR",e.toString());
                        }
                    }catch (Exception e){
                        Log.e("ERROR",e.toString());
                    }
                }
            });

            holder.buttom_OrdersDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customesID = holder.textView_CustomerName.getText().toString();
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            con.done(account,customesID);
                        }
                    });
                    holder.textView_OrdersState.setText("已完成");
                    a.start();
                    try{
                        a.join();
                        holder.button_over.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        Log.e("ERROR",e.toString());
                    }
                }

            });
            holder.button_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customesID = holder.textView_CustomerName.getText().toString();
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            con.accept(account,customesID);
                        }
                    });
                    a.start();
                    try {
                        a.join();
                        holder.layout.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }

                }
            });
            holder.button_over.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread get_ordersdata = new get_ordersdata();
                    Thread adaptert_lord = new adaptert_lord();
                    customesID = holder.textView_CustomerName.getText().toString();
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            con.over(account,customesID);
                        }
                    });
                    a.start();
                    try {
                        a.join();
                        get_ordersdata.start();
                        try{
                            get_ordersdata.join();

                            adaptert_lord.start();
                            try{
                                adaptert_lord.join();
                                myadapter = new activity_orders.Myadapter();
                                recyclerView.setAdapter(myadapter);
                            }catch (Exception e){
                                Log.e("ERROR",e.toString());
                            }
                        } catch (Exception e){
                            Log.e("ERROR",e.toString());
                        }
                    } catch (Exception e) {

                    }
                }
            });


        }



        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public void button_BackToMain(View view){
        activity_orders.this.finish();
    }
}
