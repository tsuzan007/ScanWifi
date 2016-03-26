package com.updatingdatabase.tsuzan.broadcasttest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by macbookpro on 3/18/16.
 */
public class MyActivity extends Activity {
    Handler handler;
    ArrayList<Object> scanlist;
    ListView listView;
    WifiManager w;
    WifiInfo wifiInfo;
    TextView textView;
    SensorManager sensorManager;

    //MyownBroadCast myownBroadCast;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        w = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        listView = (ListView) findViewById(R.id.listView);
        scanlist = new ArrayList<>();
        textView= (TextView) findViewById(R.id.textView);
        MySensorListenener mySensorListenener=new MySensorListenener(handler);
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(mySensorListenener,
                sensorManager.getDefaultSensor(Sensor.REPORTING_MODE_ON_CHANGE),
                SensorManager.SENSOR_DELAY_UI);

      /*  handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                scanlist.clear();
                scanlist.add( msg.obj);
                Log.i("sdaaf", scanlist.toString());
                ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(getApplication(), android.R.layout.simple_list_item_1, scanlist);
                listView.setAdapter(arrayAdapter);

              
            }
        }; */
      //  myownBroadCast = new MyownBroadCast(handler);


    }

    public void click(View view) {
        /*registerReceiver(myownBroadCast, new IntentFilter("android.sensor.accelerometer"));
        Intent i = new Intent();
        i.setAction("com.updatingdatabase.tsuzan.broadcasttest");
        // i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(i);*/

    }

    @Override
    protected void onPause() {
        //unregisterReceiver(myownBroadCast);
        super.onPause();
    }

    public class MySensorListenener implements SensorEventListener{
        Handler handler;
        ArrayList<String> list=new ArrayList<>();



        ArrayList<ScanResult> arrayList = new ArrayList<>();
        public MySensorListenener(Handler handler){
            this.handler=handler;

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    MyActivity.this.runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void run() {
                            wifiInfo=w.getConnectionInfo();
                            list.clear();
                            w.startScan();
                            arrayList = (ArrayList<ScanResult>) w.getScanResults();
                            for (ScanResult r:arrayList){
                                double exp = (27.55 - (20 * Math.log10(r.frequency)) + Math.abs(Integer.valueOf(r.level))) / 20.0;
                                double value=Math.pow(10.0, exp);
                                if(!(r.SSID.equals("Consoles"))) {
                                    list.add("BSSID= " + r.BSSID + "\nSSID=  " + r.SSID + " \nLevel  " + r.level + " \ntimestamp=  "+r.timestamp+" \nDistance= "+value);
                                }}
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(arrayAdapter);

                            textView.setText("This wifi is connected to "+ wifiInfo.getSSID()+" and its BSSID is "+wifiInfo.getBSSID()+"\n"+
                                    "the Rssi value is "+wifiInfo.getRssi());




                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            };
            new Thread(runnable).start();


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

  /*  public class MyownBroadCast extends BroadcastReceiver {

        Handler handler;
        ArrayList<String> list=new ArrayList<>();


        ArrayList<ScanResult> arrayList = new ArrayList<>();

        public MyownBroadCast(Handler handler) {

            this.handler = handler;


        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "new wifi found", Toast.LENGTH_SHORT).show();
            this.thread();

        }


        public void thread() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    MyActivity.this.runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void run() {
                            wifiInfo=w.getConnectionInfo();
                            list.clear();
                            w.startScan();
                            arrayList = (ArrayList<ScanResult>) w.getScanResults();
                            for (ScanResult r:arrayList){
                                double exp = (27.55 - (20 * Math.log10(r.frequency)) + Math.abs(Integer.valueOf(r.level))) / 20.0;
                                double value=Math.pow(10.0, exp);
                                if(!(r.SSID.equals("Consoles"))) {
                                list.add("BSSID= " + r.BSSID + "\nSSID=  " + r.SSID + " \nLevel  " + r.level + " \ntimestamp=  "+r.timestamp+" \nDistance= "+value);
                            }}
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(arrayAdapter);

                            textView.setText("This wifi is connected to "+ wifiInfo.getSSID()+" and its BSSID is "+wifiInfo.getBSSID()+"\n"+
                            "the Rssi value is "+wifiInfo.getRssi());




                        }
                   });
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            };
            new Thread(runnable).start();




        }

       /* public void mergesort(ArrayList<ScanResult> array){
            while(array.size())


        }//asdfasdfasdff


    }*/
}
