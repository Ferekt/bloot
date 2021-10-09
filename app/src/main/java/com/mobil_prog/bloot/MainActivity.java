package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String user=null;
    public static String postUrl = "https://blootapi.herokuapp.com/";
    public static String group=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void register(View v){
        Intent intent = new Intent(this, Register_Activity.class);
        startActivity(intent);
    }
    public void login(View v) {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);
    }
}