package com.example.younghokim.thebalance;

import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import com.example.younghokim.thebalance.util.DBManager;
import com.github.clans.fab.FloatingActionButton;
//import android.support.design.widget.FloatingActionButton;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener{

    //   static final LatLng SEOUL = new LatLng( 37.56, 126.97);
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    static final int REQUEST_PERM_LOCATION = 2;
    private GoogleMap balanceMap;
    private MapFragment mf;
    int balanceSum = 0;
    long inputStart = 0;
    long inputEnd = 0;
    int keyCursor = 0;
    int id = 0;
    double latitude = 0;
    double longitude = 0;

    DrawerLayout drawerLayout;
    NavigationView navigation;
    Toolbar toolbar;
    FloatingActionMenu menu1;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    Handler mUiHandler = new Handler();

    CalendarActivity calendarActivity;
    ChartActivity chartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //if permission not have
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // 권한 필요 설명 UI 노출
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERM_LOCATION);
            } else {
                //do something when satisfied condition (permission)
            }

        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //if permission not have
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // 권한 필요 설명 UI 노출
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_PERM_LOCATION);
            } else {
                //do something when satisfied condition (permission)
            }
        }

        setupFragments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        setupNavigationView();
        menu1 = (FloatingActionMenu) findViewById(R.id.menu1);
//        setupFab();
        setupToolbar();

        MapsInitializer.initialize(getApplicationContext());
        //DBManager.createTable(this);
        init();

        menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu1.isOpened()) {
                    Toast.makeText(MainActivity.this, menu1.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menu1.toggle(true);
            }
        });
        menu1.hideMenuButton(false);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menu1.showMenuButton(true);
            }
        }, 150);
        menu1.setClosedOnTouchOutside(true);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        //fab1.setEnabled(false);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mf).commit();
    }
    private void setupFragments(){
        calendarActivity = new CalendarActivity();
        chartActivity = new ChartActivity();
        mf = new MapFragment();
    }
    private void setupNavigationView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView)findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemid = menuItem.getItemId();
                switch (itemid) {
                    case R.id.navigation_item_0:
                        //Toast.makeText(MainActivity.this, "BalanceMap", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        //getFragmentManager().beginTransaction().replace(R.id.mainfc, mf).commit();
                        break;
                    case R.id.navigation_item_1:
                        //Toast.makeText(MainActivity.this, "Calendar", Toast.LENGTH_SHORT).show();
                        ((RelativeLayout) findViewById(R.id.mainfc)).removeAllViews();

//                        getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().getFragments().get(1));
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, calendarActivity).commit();
                        break;
                    case R.id.navigation_item_2:
                        //Toast.makeText(MainActivity.this, "DailyBalanceChart", Toast.LENGTH_SHORT).show();
                        ((RelativeLayout) findViewById(R.id.mainfc)).removeAllViews();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, chartActivity).commit();
                        /*Snackbar
                                .make(findViewById(R.id.coordinatelayout), "another snacbar test", Snackbar.LENGTH_LONG)
                                .setAction("Action", MainActivity.this)
                                .show(); // Don’t forget to show!*/
                        break;
//                    case R.id.navigation_item_3:
//                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                        //getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, fragmentSetting).commit();
//                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //    private void setupFab(){
//        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
//        if(fab1 != null)
//            fab1.setOnClickListener(this);
//    }
    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null)
            setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void onMapClick(LatLng point) {

        // 현재 위도와 경도에서 화면 포인트를 알려준다
        Point screenPt = balanceMap.getProjection().toScreenLocation(point);

        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
        LatLng latLng = balanceMap.getProjection().fromScreenLocation(screenPt);

        Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도("
                + String.valueOf(point.longitude) + ")");
        Log.d("화면좌표", "화면좌표: X(" + String.valueOf(screenPt.x) + "), Y("
                + String.valueOf(screenPt.y) + ")");
    }

    public void init(){

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        mf = (MapFragment)getFragmentManager().findFragmentById(R.id.frag_map);
        balanceMap = mf.getMap();
        //((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.frag_map)).getMap();
        GPSInfo gps = new GPSInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            // Showing the current location in Google Map
            DBManager.createTable(this);
            DBManager.selectAllMarker(this, balanceMap);
            balanceMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Map 을 zoom 합니다.
            balanceMap.animateCamera(CameraUpdateFactory.zoomTo(14)); //숫자가 클수록 줌을 더 함.
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";

            switch (v.getId()) {
                case R.id.fab1:
                    inputEnd = System.currentTimeMillis();
                    //init();
                    if(inputEnd - inputStart >= 1000*30){
                        inputStart = 0;
                        inputEnd = 0;
                        keyCursor = 0;
                        balanceSum = 0;
                    }
                    balanceSum += 100;
                    if(inputStart == 0){
                        inputStart = System.currentTimeMillis();
                        keyCursor = 1;
                        DBManager.insertData(getApplicationContext(), balanceSum, latitude, longitude, keyCursor);
                        id = DBManager.selectID(getApplicationContext(), keyCursor);
                        keyCursor = 0;
                        DBManager.updateCursor(getApplicationContext(), id, keyCursor);
                        Log.e("FirstCheck", "balanceSum=" + balanceSum + " latitude=" + latitude + " longitude=" + longitude + " keyCursor=" + keyCursor);
                    }
                    else{
                        DBManager.updateData(getApplicationContext(), id, balanceSum);  //key값인 id값을 바로 위에 if문에서 받아서 여기서 넣으면 될듯
                        Log.e("SecondCheck", "balanceSum=" + balanceSum + " keyCursor=" + keyCursor);
                    }
                    DBManager.selectAllMarker(getApplicationContext(), balanceMap);
                    text = ""+balanceSum;
                    break;
                case R.id.fab2:
                    inputEnd = System.currentTimeMillis();
                    //init();
                    if(inputEnd - inputStart >= 1000*30){
                        inputStart = 0;
                        inputEnd = 0;
                        keyCursor = 0;
                        balanceSum = 0;
                    }
                    balanceSum += 1000;
                    if(inputStart == 0){
                        inputStart = System.currentTimeMillis();
                        keyCursor = 1;
                        DBManager.insertData(getApplicationContext(), balanceSum, latitude, longitude, keyCursor);
                        //showMoneyMarker(latLng);
                        id = DBManager.selectID(getApplicationContext(), keyCursor);
                        keyCursor = 0;
                        DBManager.updateCursor(getApplicationContext(), id, keyCursor);
                        Log.e("FirstCheck", "balanceSum=" + balanceSum + " latitude=" + latitude + " longitude=" + longitude + " keyCursor=" + keyCursor);
                    }
                    else{
                        DBManager.updateData(getApplicationContext(), id, balanceSum);  //key값인 id값을 바로 위에 if문에서 받아서 여기서 넣으면 될듯
                        Log.e("SecondCheck", "balanceSum=" + balanceSum + " keyCursor=" + keyCursor);
                    }
                    //insertData(balanceSum);
                    //showMoneyMarker(latLng);
                    DBManager.selectAllMarker(getApplicationContext(), balanceMap);
                    text = ""+balanceSum;
                    break;
                case R.id.fab3:
                    inputEnd = System.currentTimeMillis();
                    if(inputEnd - inputStart >= 1000*30){
                        inputStart = 0;
                        inputEnd = 0;
                        keyCursor = 0;
                        balanceSum = 0;
                    }
                    balanceSum += 10000;
                    if(inputStart == 0){
                        inputStart = System.currentTimeMillis();
                        keyCursor = 1;
                        DBManager.insertData(getApplicationContext(), balanceSum, latitude, longitude, keyCursor);
                        id = DBManager.selectID(getApplicationContext(), keyCursor);
                        keyCursor = 0;
                        DBManager.updateCursor(getApplicationContext(), id, keyCursor);
                        Log.e("FirstCheck", "balanceSum=" + balanceSum + " latitude=" + latitude + " longitude=" + longitude + " keyCursor=" + keyCursor);
                    }
                    else{
                        DBManager.updateData(getApplicationContext(), id, balanceSum);  //key값인 id값을 바로 위에 if문에서 받아서 여기서 넣으면 될듯
                        Log.e("SecondCheck", "balanceSum=" + balanceSum + " keyCursor=" + keyCursor);
                    }
                    DBManager.selectAllMarker(getApplicationContext(), balanceMap);
                    text = ""+balanceSum;
                    break;
            }
            Snackbar
                    .make(findViewById(R.id.coordinatelayout), balanceSum + " Won register", Snackbar.LENGTH_SHORT)
                    .setAction("Action", this)
                    .show(); // Don’t forget to show!
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            init();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_PERM_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "allow permission for access storage",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "not allow permission for access storage",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        init();
    }
}
