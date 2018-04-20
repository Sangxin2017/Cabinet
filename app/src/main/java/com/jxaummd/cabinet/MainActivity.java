package com.jxaummd.cabinet;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView= null;
    boolean  isstart = false;
    Thread  checkThread;
    TextView  notifytext  ;
    private   boolean  shandong = false;

    public  static    Activity  activity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        imageView=findViewById(R.id.app_main_image);
        notifytext=findViewById(R.id.app_main_text);
        notifytext.setText("一切正常！");



        checkThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection=null;
                    ResultSet  resultSet= null;
                    Class.forName("com.mysql.jdbc.Driver");
                    connection= DriverManager.getConnection("jdbc:mysql://120.79.0.116:3306/ipark?characterEncoding=utf8&useSSL=false","dog","kejiyueyueping2017");
                    while (true) {
                        resultSet = connection.prepareStatement("SELECT statue FROM  parkstatue  WHERE number='10005'").executeQuery();

                        while (resultSet.next()) {

                            if(resultSet.getString(1).equals("0")){
                                MainActivity.activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(shandong)
                                        notifytext.setText("一切正常");
                                        else
                                            notifytext.setText("");

                                        shandong=!shandong;
                                    }
                                });
                            }else {
                                MainActivity.activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(shandong)
                                            notifytext.setText("发生了危险");
                                        else
                                            notifytext.setText("");

                                        shandong=!shandong;
                                    }
                                });
                            }

                        }
                        Thread.sleep(2000);

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isstart){
                    Toast.makeText(MainActivity.this,"开始监测",Toast.LENGTH_LONG).show();
                    checkThread.start();
                }else {
                    Toast.makeText(MainActivity.this,"已经停止监测",Toast.LENGTH_LONG).show();
//                    if(checkThread.isAlive())
//                        checkThread.destroy();
                }
                isstart=!isstart;
            }
        });
    }




}
