package com.example.nm_store_system;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
    String mysql_ip = "35.201.205.223";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name1 = "Night_Market_management";
    String db_name2 = "store_system";
    String url01 = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name1;
    String url02 = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name2;
    String db_user = "app-store";
    String db_password = "store";


    public Boolean connect_sql() {
        //載入驅動
        Boolean connect = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e) {
            Log.e("DB","加載驅動失敗");
        }
        Connection con = null;
        // 連接資料庫
        try {
            con = DriverManager.getConnection(url01,db_user,db_password);
            Log.v("DB","遠端連接成功");
            connect = true;
        }catch(SQLException e) {
            Log.e("DB","遠端連接失敗");
            Log.e("DB", e.toString());
        }
        return connect;
    }
    public boolean APD(String A,String P){
        boolean result = false;
        try {
            Connection con =  DriverManager.getConnection(url01, db_user, db_password);
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
            Log.v("APD","帳號驗證成功");
        } catch (Exception e){
            Log.e("APD","帳號驗證失敗");
            Log.e("APD",e.toString());
        }
        return result;
    }
    public boolean shop_check(String account){
        boolean re = false;
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet respond = st.executeQuery("select state from all_store where Account='" + account  +"';");
            while (respond.next()){
                if (respond.getString("state").equals("1")){
                    re = true;
                } else {
                    re = false;
                }
            }
            Log.v("shop_check","shop_check成功執行");
            return re;
        }catch (Exception e){
            Log.e("shop_check",e.toString());
            return false;
        }

    }
    public boolean shop_stop_R(String account){
        Boolean result = true;
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet respond = st.executeQuery("select customerID from " +account+"_orders where is_check = '1';");
            st.close();
            while (respond.next()){
                Log.v("shop_stop_R","respond有資料");
            if(respond.getString("customerID").equals("")){

                result = true;
                Log.v("shop_stop_R","無資料");
            } else {

                result = false;
                Log.v("shop_stop_R","有資料");
                break;
            }}
        } catch (Exception e){
                Log.e("shop_stop_R",e.toString());
        }
        return result;
    }
    public void stop(String account){
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate("update all_store set state = 0 where Account = '" + account  +"';");
            st.close();
        } catch (Exception e){
            Log.e("stop_fail",e.toString());
        }

    }
    public void stop_yes(String account){
        String sql = "TRUNCATE TABLE " + account+"_orders";
        try{
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
        }catch (Exception e){

        }
        Log.v("test","stop_yes成功執行");
    }
    public boolean exit(String account){
        Boolean re = false;
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet respond = st.executeQuery("select state from all_store where Account='" + account  +"';");
            while (respond.next()){
                if (respond.getString("state").equals("0")){
                    re = true;
                } else {
                    re = false;
                }
            }
            Log.v("shop_check","shop_check成功執行");
            return re;
        }catch (Exception e) {
            Log.e("shop_check", e.toString());
            return false;
        }
    }
    public void shop_open(String account){
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate("update all_store set state = 1 where Account = '" + account  +"';");

            Log.v("shop_check","shop_open成功執行");

        }catch (Exception e) {
            Log.e("shop_check", e.toString());

        }
    }
}
