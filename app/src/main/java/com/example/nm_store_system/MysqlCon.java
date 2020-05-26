package com.example.nm_store_system;

import android.util.Log;
import android.util.LogPrinter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
    String mysql_ip = "35.201.205.223";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "Night_Market_management";
    String url = "jdbc:mysql://" + mysql_ip + ":" + mysql_port + "/" + db_name;
    String db_user = "app-management";
    String db_password = "management";


    public Boolean connect_sql() {
        //載入驅動
        Boolean connect = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB", "加載驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("DB", "加載驅動失敗");

        }
        Connection con = null;
        // 連接資料庫
        try {
            con = DriverManager.getConnection(url, db_user, db_password);
            Log.v("DB", "遠端連接成功");
            connect = true;
        } catch (SQLException e) {
            Log.e("DB", "遠端連接失敗");
            Log.e("DB", e.toString());
        }
        return connect;
    }
    public boolean APD(String A,String P){
        boolean result = false;
        try {
            Connection con =  DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet respond = st.executeQuery("select Account from Store_Account where Account='"+ A + "' and Password= '"+P+"'");
            while (respond.next()){
                String Z =respond.getString("Account");
                if(Z.equals(A)){
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (Exception e){

        }
        return result;
    }
}
