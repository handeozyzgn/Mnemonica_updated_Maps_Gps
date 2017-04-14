package com.example.Mnemonica;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Bengu on 6.4.2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class AddActToSchedule extends AppCompatActivity {


    private FirebaseAuth mAuth;


    private DatabaseReference dataRef;

    private FirebaseDatabase myRef;
    private String userID;

    String key;
    String key2;
    ArrayAdapter<String> arrAdap;
    ArrayList<Integer> h;
    ArrayList<Integer> min;
    ArrayList<Integer> d;
    ArrayList<Integer> m;
    ArrayList<Integer> y;
    ArrayList<String> name;
    ArrayList<String> sss;
    boolean check =true;
    String value;
    int num=0;
    int no;
    int newNum;
    int a;
    int b;
    Button alrmTry;

    private ListView mListView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addschedule);
        alrmTry = (Button)findViewById(R.id.alrmTry);



        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myRef =FirebaseDatabase.getInstance();
        dataRef = myRef.getReference("Users");



        h = new ArrayList<>();
        min = new ArrayList<>();
        d = new ArrayList<>();
        m = new ArrayList<>();
        y = new ArrayList<>();
        name = new ArrayList<>();
        sss = new ArrayList<>();


      /*  dataRef.child(userID).child("Number of Activities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                no = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        dataRef.child(userID).child("activities").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    value = child.getValue(String.class);
                  //  key2 = child.getKey();
                    sss.add(value);
                 /*   if(key2.equals("Activity Hour")){
                        a = Integer.valueOf(value);
                        h.add(a);
                    }
                    else if(key2.equals("Activity Minute")){
                        b = Integer.valueOf(value);
                        m.add(b);
                    }*/

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        alrmTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al();
            }
        });

    }
    private void al(){

        for (int k=0; k<sss.size(); k=k+6){
            h.add(Integer.valueOf(sss.get(k)));
            name.add(sss.get(k+1));
            d.add(Integer.valueOf(sss.get(k+2)));
            min.add(Integer.valueOf(sss.get(k+3)));
            m.add(Integer.valueOf(sss.get(k+4)));
            y.add(Integer.valueOf(sss.get(k+5)));
        }


        //Calendar calendar[] = new Calendar[3];

        Calendar cal[] = new Calendar[m.size()];


        for (int i = 0; i < m.size(); i++) {

            cal[i] = Calendar.getInstance();
            cal[i].set(Calendar.HOUR_OF_DAY, h.get(i));
            cal[i].set(Calendar.MINUTE, min.get(i));
            cal[i].set(Calendar.SECOND, 0);
            cal[i].set(Calendar.DAY_OF_MONTH, d.get(i));
            cal[i].set(Calendar.MONTH, m.get(i));
            cal[i].set(Calendar.YEAR, y.get(i));
        }

        AlarmManager[] alarmManager = new AlarmManager[3];
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        for (int f = 0; f < cal.length; f++) {
            Intent intent = new Intent(AddActToSchedule.this,
                    AlarmReceiver.class);

            PendingIntent pi = PendingIntent.getBroadcast(
                    AddActToSchedule.this, f, intent, 0);

            alarmManager[f] = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager[f].set(AlarmManager.RTC_WAKEUP,
                    cal[f].getTimeInMillis(), pi);

            intentArray.add(pi);

        }
    }
}



