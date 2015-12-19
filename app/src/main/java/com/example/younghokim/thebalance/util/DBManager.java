package com.example.younghokim.thebalance.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.younghokim.thebalance.MainActivity;
import com.example.younghokim.thebalance.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by younghokim on 15. 12. 18.
 */
public class DBManager {
    final static String balanceDBName = "balanceRanking.db";
    final static String balanceTableName = "balanceRankingTable";
    final static int balanceDBMode = android.content.Context.MODE_PRIVATE;

    public static void createTable(Context ctx){
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        try{
            String sql = "create table " + balanceTableName + "(id integer primary key autoincrement, "
                    + "balance integer not null, "
                    + "latitude integer, "
                    + "longitude integer, "
                    + "time date, "
                    + "cursor integer"+ ")";
            balanceDB.execSQL(sql);
        }catch(android.database.sqlite.SQLiteException e){
            Log.d("Lab sqlite", "error: " + e);
        }
        balanceDB.close();
    }
    public static void removeTable(Context ctx){
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "drop table " + balanceTableName;
        balanceDB.execSQL(sql);
        balanceDB.close();
    }
    public static void insertData(Context ctx, int balance, double latitude, double longitude, int keyCursor) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "insert into " + balanceTableName + " values(NULL, " + balance + ", " + latitude + ", " + longitude + ", date('now'), " + keyCursor + ");";
        balanceDB.execSQL(sql);
        balanceDB.close();
    }
    public static void updateCursor(Context ctx, int index, int keyCursor) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "update " + balanceTableName + " set cursor = '" + keyCursor + "' where id = " + index + ";";
        balanceDB.execSQL(sql);
        balanceDB.close();
    }
    // Data 업데이트
    public static void updateData(Context ctx, int index, int balance) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "update " + balanceTableName + " set balance = '" + balance + "' where id = " + index + ";";
        //String secondSql = "update " + balanceTableName + " set cursor = '" + keyCursor + "' where id = " + index + ";";
        balanceDB.execSQL(sql);
        //balanceDB.execSQL(secondSql);
        balanceDB.close();
    }
    // Data 삭제
    public static void removeData(Context ctx, int index) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "delete from " + balanceTableName + " where id = " + index + ";";
        Log.i("removedata:", sql);
        balanceDB.execSQL(sql);
        balanceDB.close();
    }

    // Data 읽기(꺼내오기)
    public static int selectID(Context ctx, int keyCursor) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "select id from " + balanceTableName + " where cursor = " + keyCursor + ";";
        Cursor result = balanceDB.rawQuery(sql, null);
        int id = 0;
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            id = result.getInt(0);
        }
        result.close();
        balanceDB.close();
        return id;
    }
//    public static int getID(Context ctx, int keyCursor){
//        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
//        String sql = "select * from " + balanceTableName + " where time = date('" + selectDay + "');";
//        Cursor results = balanceDB.rawQuery(sql, null);
//        int id = results.getInt(0);
//        return id;
//    }
    // 모든 Data 읽기
    public static String selectAll(Context ctx, ArrayList nameList, String selectDay) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        //String sql = "select * from " + balanceTableName + " where time = date('now')" + ";"; //오늘것만 보고 싶을때 나중에 이걸로 바꿀것
        //String sql = "select * from " + balanceTableName + ";";
        String sql = "select * from " + balanceTableName + " where time = date('" + selectDay + "');";
        Cursor results = balanceDB.rawQuery(sql, null);
        results.moveToFirst();
        String buff = "";
        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            int balances = results.getInt(1);
            double latitude  = results.getDouble(2);
            double longitude = results.getDouble(3);
            String dateTime = results.getString(4);
            int cursors = results.getInt(5);
            buff = dateTime;
            String listString = id + ".    " + dateTime + "                " + balances + "원";//not accept \t
            Log.d("lab_sqlite", "id= " + id + " balnces=" + balances + " latitude=" + latitude + " longitude=" + longitude + " dateTime=" + dateTime + " cursors=" + cursors);
            nameList.add(listString);
            //showToadyMoneyMarker(latitude, longitude, balances, balanceMap);
            results.moveToNext();
        }
        results.close();
        balanceDB.close();
        return buff;
    }
    public static void selectAllMonthly(Context ctx, ArrayList monthBalanceList, String selectDay) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        //String sql = "select * from " + balanceTableName + " where time = date('now')" + ";"; //오늘것만 보고 싶을때 나중에 이걸로 바꿀것
        //String sql = "select * from " + balanceTableName + ";";
        String sql = "select * from " + balanceTableName + " where time LIKE '" + selectDay + "%';";
        Cursor results = balanceDB.rawQuery(sql, null);
        results.moveToFirst();
        int balanceSum=0;

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            int balances = results.getInt(1);
            double latitude  = results.getDouble(2);
            double longitude = results.getDouble(3);
            String dateTime = results.getString(4);
            int cursors = results.getInt(5);
            //String listString = "    " + dateTime + "                " + balances + "원";//not accept \t
            Log.d("lab_sqlite", "id= " + id + " balnces=" + balances + " latitude=" + latitude + " longitude=" + longitude + " dateTime=" + dateTime + " cursors=" + cursors);
            //nameList.add(listString);
            balanceSum += balances;
            results.moveToNext();
        }
        if (balanceSum != 0) {
            monthBalanceList.add(selectDay + " 월 총 지출 금액: " + balanceSum + "원");
        }else{
            monthBalanceList.add(selectDay + " 월 총 지출 금액: " + 0 + "원");
        }
        results.close();
        balanceDB.close();
    }
    public static void selectAllMarker(Context ctx, GoogleMap balanceMap) {
        SQLiteDatabase balanceDB = ctx.openOrCreateDatabase(balanceDBName, balanceDBMode, null);
        String sql = "select * from " + balanceTableName + " where time = date('now')" + ";"; //오늘것만 보고 싶을때 나중에 이걸로 바꿀것
        //String sql = "select * from " + balanceTableName + ";";
        Cursor results = balanceDB.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            int balances = results.getInt(1);
            double latitude  = results.getDouble(2);
            double longitude = results.getDouble(3);
            String dateTime = results.getString(4);
            int cursors = results.getInt(5);

            Log.d("lab_sqlite", "id= " + id + " balnces=" + balances + " latitude=" + latitude + " longitude=" + longitude + " dateTime=" + dateTime + " cursors=" + cursors);
            showToadyMoneyMarker(latitude, longitude, balances, balanceMap);
            results.moveToNext();
        }
        results.close();
        balanceDB.close();
    }
    public static void showToadyMoneyMarker(double latitude, double longitude, int balanceSum, GoogleMap balanceMap){
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions optFirst = new MarkerOptions();
        optFirst.position(latLng);// 위도 • 경도
        optFirst.title("Current Position : " + balanceSum);// 제목 미리보기
        optFirst.snippet("Snippet");
        optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_balancemap_70x70));
        balanceMap.addMarker(optFirst).showInfoWindow();
    }

}
