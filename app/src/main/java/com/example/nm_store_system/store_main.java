package com.example.nm_store_system;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class store_main extends AppCompatActivity {
    MysqlCon con = new MysqlCon();
    protected String account;
    final protected Boolean[] result={false};
    public TextView shop_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
        TextView name_textview = findViewById(R.id.who_text);
         shop_state = findViewById(R.id.textview_shop_state);

        name_textview.setText(account);
        Thread shop_check = new shop_check();
        shop_check.start();
        try {
            shop_check.join();
            if (result[0]){
                shop_state.setText("營業中");
                shop_state.setTextColor(Color.GREEN);
            } else {
                shop_state.setText("休息中");
                shop_state.setTextColor(Color.RED);
            }

        }catch (Exception e){

        }
    }
    public class shop_check extends Thread{
        public void run(){
           result[0]=con.shop_check(account);
        }
    }
    public void orders(View view){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("account",account);
        intent.setClass(store_main.this,activity_orders.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    public void edit (View view){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("account",account);
        intent.setClass(store_main.this,activity_edit.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    public void shop_open(View view){
       Thread open = new Thread(new Runnable() {
            @Override
            public void run() {
            con.shop_open(account);

            }
        });
       open.start();
       try {
           open.join();
           shop_state.setText("營業中");
           shop_state.setTextColor(Color.GREEN);
       }catch (Exception e){
           Log.e("shop_open",e.toString());
       }
    }
    public void shop_shutdown(View view){
        final Boolean[] result = {false};
       Thread stop =new Thread(new Runnable() {
           @Override
           public void run() {
           result[0]=con.shop_stop_R(account);
           }
       });
       stop.start();
        try {
            stop.join();
            if (result[0]){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        con.stop(account);
                    }
                }).start();
                Toast.makeText(this,"已成功關閉商店",Toast.LENGTH_LONG);
                shop_state.setText("休息中");
                shop_state.setTextColor(Color.RED);
            }else {
                dialog();
            }
            Log.v("shop_shutdown","shop_shutdown執行成功");
        } catch (Exception e){
            Log.v("shop_shutdown","shop_shutdown執行失敗");
        }

    }
    public void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(store_main.this);
        builder.setTitle("警告")
                .setMessage("您還有訂單未完成,確定要關店嗎?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                con.stop_yes(account);
                            }
                        }).start();
                    }
                }).setNegativeButton("否", null);
        builder.create().show();
    }
    public void exit(View view){
        final Boolean[] result ={false} ;
        Thread exit_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                result[0]=con.exit(account);
            }
        });
        exit_thread.start();
        try {
            exit_thread.join();
            if (result[0]){
            Intent intent = new Intent();
            intent.setClass(store_main.this,MainActivity.class);
            store_main.this.finish();

            } else {
                Toast.makeText(store_main.this ,"請先關店在離開喔",Toast.LENGTH_LONG).show();

            }
            Log.v("Exit","exit執行成功");
        } catch (Exception e){
            Log.v("Exit","exit執行失敗");
        }

    }

}
