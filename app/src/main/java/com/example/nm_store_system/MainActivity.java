package com.example.nm_store_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public EditText editText_account;
    public EditText editText_passwd;
    String[] AP = new String[2];
    Boolean result = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_account = findViewById(R.id.editText_account);
        editText_passwd = findViewById(R.id.editText_passwd);

    }

    public void buttom_loging(View view) {
        AP[0] = String.valueOf(editText_account.getText());
        AP[1] = String.valueOf(editText_passwd.getText());
        if (AP[1].equals("") || AP[0].equals(""))
        {
            editText_account.setHint("請輸入帳號");
            editText_passwd.setHint("請輸入密碼");
            editText_account.setHintTextColor(Color.RED);
            editText_passwd.setHintTextColor(Color.RED);

        } else {
            Thread thread_login = new login();
            thread_login.start();
            try {
                thread_login.join();
                if (result){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,store_main.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("account",AP[0]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(MainActivity.this,"帳號或密碼錯誤，請重式", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e){
                Toast.makeText(MainActivity.this,"伺服器連接失敗,請確認網路狀態",Toast.LENGTH_LONG).show();
            }
        }

    }
    public class login extends Thread{
        public void run(){
            MysqlCon con = new MysqlCon();
            con.connect_sql();
            result = con.APD(AP[0],AP[1]);
        }
    }
}