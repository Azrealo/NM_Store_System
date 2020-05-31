package com.example.nm_store_system;

import android.util.Log;
import android.view.View;

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
            ResultSet respond = st.executeQuery("select count(customerID) from " +account+"_orders where is_check = '1' and is_over = '0';");
            while (respond.next()){
                Log.v("shop_stop_R","respond有資料");
            if(Integer.valueOf(respond.getString("count(customerID)"))<1){

                result = true;
                Log.v("shop_stop_R","無資料");

            } else {

                result = false;
                Log.v("shop_stop_R","有資料");
                break;
            }
                st.close();
            }
        } catch (Exception e){
                Log.e("shop_stop_R",e.toString());
        }
        try{
            if(result){
                Connection con =  DriverManager.getConnection(url02, db_user, db_password);
                Statement st = con.createStatement();
                st.executeUpdate("update " + account+ "_orders set is_check = '1',is_cancel = '1',is_over = '1' where is_over = '0';");
                st.close();
            }
        }catch (Exception e){

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
        String sql = "update " + account+"_orders set is_check = '1',is_cancel = '1',is_over = '1' where is_over = '0';";
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
    public String[][] get_goodsdata(String account,int A){
        String[][] data = new String[A][2];
        int i = 0;
        try {
            Connection con = DriverManager.getConnection(url02, db_user, db_password);
            String sql = "select name,quantity from " + account + "_goods ;";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String name = rs.getString("name");
                String quantity = rs.getString("quantity");
                int ii = 0;
                while (ii < 2){
                    switch (ii){
                        case 0:
                            data[i][0]=name;
                            break;
                        case 1:
                            data[i][1]=quantity;
                            break;
                    }
                    ii++;
                }
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e("get_goodsdata","get_goodsdata failed");
            Log.e("get_goodsdata",e.toString());
        }
        return data;
    }
    public int get_goodslenght(String account){
        int result = 0;
        String a = "";
        try{
            Connection con = DriverManager.getConnection(url02, db_user, db_password);
            String sql = "select count(name) from " + account + "_goods;";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                try {
                    a =rs.getString("count(name)");
                    result = Integer.valueOf(a);
                } catch (Exception e){
                    Log.e("get_goodslenght",e.toString());
                }
            }
            st.close();
        }catch (Exception e){

        }
        return result;
    }
    public void change_quantity(String account,String name,int n){
        try {
            Connection con = DriverManager.getConnection(url02, db_user, db_password);
            String sql = "update "+account + "_goods set quantity = " + n +" where name = '" + name+ "';";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
        } catch (Exception e){

        }
    }
    public int get_orderslenght(String account){
        int result = 0;
        String a = "";
        try{
            Connection con = DriverManager.getConnection(url02, db_user, db_password);
            String sql = "select count(is_check) from " + account + "_orders where is_over = '0';";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                try {
                    a =rs.getString("count(is_check)");
                    result = Integer.valueOf(a);
                } catch (Exception e){
                    Log.e("get_orderslenght",e.toString());
                }
            }
            st.close();
        }catch (Exception e){

        }
        return result;
    }
    public String[][] get_ordersdata(String account,int A){

        String[][] data = new String[A][7];
        int i = 0;
        try {
            Connection con = DriverManager.getConnection(url02, db_user, db_password);
            String sql = "select * from " + account + "_orders where is_over = '0';";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String customerID = rs.getString("customerID");
                String orders = rs.getString("orders");
                String total_price = rs.getString("total_price");
                String is_check = rs.getString("is_check");
                String is_done = rs.getString("is_done");
                String is_over = rs.getString("is_over");
                String is_cancel = rs.getString("is_cancel");

                int ii = 0;
                while (ii < 7){
                    switch (ii){
                        case 0:
                            data[i][0]=customerID;
                            break;
                        case 1:
                            data[i][1]=orders;
                            break;
                        case 2:
                            data[i][2]=total_price;
                            break;
                        case 3:
                            data[i][3]=is_check;
                            break;
                        case 4:
                            data[i][4]=is_done;
                            break;
                        case 5:
                            data[i][5]=is_cancel;
                            break;
                        case 6:
                            data[i][6]=is_over;
                            break;
                    }
                    ii++;
                }
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e("get_goodsdata","get_goodsdata failed");
            Log.e("get_goodsdata",e.toString());
        }
        return data;
    }
    public void accept(String account,String customesID){
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate("update " + account +"_orders set is_check = '1' where customerID = '" + customesID  +"';");

            Log.v("accept","accept成功執行");

        }catch (Exception e) {
            Log.e("accept", e.toString());

        }

    }
    public void cancel(String account,String customerID){
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate("update " + account +"_orders set is_check = '0',is_cancel = '1',is_over = '1' where customerID = '" + customerID  +"';");

            Log.v("cancel","canceled");

        }catch (Exception e) {
            Log.e("accept", e.toString());

        }

    }
    public  void done(String account,String customerID){
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate("update " + account +"_orders set is_done = '1'  where customerID = '" + customerID  +"';");

            Log.v("done","done");

        }catch (Exception e) {
            Log.e("done", e.toString());

        }
    }
    public void over(String account,String customerID){
        try {
            Connection con =  DriverManager.getConnection(url02, db_user, db_password);
            Statement st = con.createStatement();
            st.executeUpdate("update " + account +"_orders set is_check = '1',is_done = '1',is_cancel = '0',is_over = '1'  where customerID = '" + customerID  +"';");
            Log.v("over","over");

        }catch (Exception e) {
            Log.e("over", e.toString());

        }
    }


}
