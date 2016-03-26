package com.updatingdatabase.tsuzan.broadcasttest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 3/18/16.
 */
public class MyBroadCast extends BroadcastReceiver{
    WifiManager w;
    Handler handler;
    MyActivity myActivity;


    ArrayList<ScanResult> arrayList=new ArrayList<>();

    public MyBroadCast(MyActivity myActivity,WifiManager w,Handler handler){
        this.w=w;
        this.handler=handler;
       this.myActivity=myActivity;

    }
    @Override
    public void onReceive(Context context, Intent intent) {
           Toast.makeText(context,"new wifi found", Toast.LENGTH_LONG).show();
        this.thread();

    }



            public void thread() {
                /*this.myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        w.startScan();
                        arrayList = (ArrayList<ScanResult>) w.getScanResults();
                        // Message message=Message.obtain();

                        //  message.obj=arrayList;
                        ArrayAdapter<ScanResult> arrayAdapter = new ArrayAdapter<ScanResult>(new MyActivity(), android.R.layout.simple_list_item_1, arrayList);
                        myActivity.listView.setAdapter(arrayAdapter);
               /* handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<ScanResult> arrayAdapter = new ArrayAdapter<ScanResult>(new MyActivity(), android.R.layout.simple_list_item_1, arrayList);
                        myActivity.listView.setAdapter(arrayAdapter);
                    }


                });


                    }
                });*/

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                w.startScan();
               arrayList= (ArrayList<ScanResult>) w.getScanResults();
               Message message=Message.obtain();

                   message.obj=arrayList;
                handler.sendMessage(message);
                  /* handler.post(new Runnable() {
                       @Override
                       public void run() {
                           ArrayAdapter<ScanResult> arrayAdapter=new ArrayAdapter<ScanResult>(new MyActivity(),android.R.layout.simple_list_item_1,arrayList);
                           listVi ew.setAdapter(arrayAdapter);

                       }
                   });
*/

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
       new Thread(runnable).start();

            }


        }

//Thread test

