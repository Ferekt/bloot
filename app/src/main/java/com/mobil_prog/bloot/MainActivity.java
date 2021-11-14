package com.mobil_prog.bloot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String user=null;
    //public static String postUrl = "https://blootapi.herokuapp.com/";
    public static String postUrl = "http://167.86.96.99:5000";
    public static String group=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        user=null;
        group=null;
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

   /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("All data will be deleted from the phone \nit cannot be reversed");
                builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        FileOutputStream fileout= null;
        try {
        fileout = context.openFileOutput("Save.txt",MODE_PRIVATE);
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
        try {
        fileout.write("".getBytes());
        } catch (IOException e) {
        e.printStackTrace();
        }
        //Toast.makeText(this,"Saved to " + getFilesDir() + "/" + "Alcohol.txt",Toast.LENGTH_LONG).show();
        try {
        fileout.close();
        } catch (IOException e) {
        e.printStackTrace();
        }*/