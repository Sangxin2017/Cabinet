package com.jxaummd.cabinet;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    ImageView imageView= null;
    boolean  isstart = false;
    Thread  checkThread;
    public  static    Activity  activity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        imageView=findViewById(R.id.app_main_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isstart){
                    Toast.makeText(MainActivity.this,"开始监测",Toast.LENGTH_LONG).show();
                    if(!checkThread.isAlive()){
                        checkThread.start();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"已经停止监测",Toast.LENGTH_LONG).show();
                    if(checkThread.isAlive())
                        checkThread.stop();
                }
                isstart=!isstart;
            }
        });


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
                                                Toast.makeText(MainActivity.activity,"发生了危险",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }else {
                                MainActivity.activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.activity,"一切正常，请不要担心！",Toast.LENGTH_SHORT).show();

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
    }




}
