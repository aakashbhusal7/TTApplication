package com.example.ttapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.example.ttapp.R;
import com.example.ttapp.databinding.LayoutMatchBinding;
import com.example.ttapp.db.UserGame;
import com.example.ttapp.db.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MatchActivity extends AppCompatActivity {

    private LayoutMatchBinding layoutMatchBinding;
    private boolean cancel;
    private DatabaseReference databaseReferenceGame;
    private List<UserGame> userGameList;
    private Map<String,UserGame>map;
    private Bundle bundle;
    private String userKey;
    public static final String KEY_DAY="KEY_DAY";
    public static final String KEY_WINS="KEY_WIN";
    public static final String KEY_MATCHES="KEY_MATCH";
    Toolbar toolbar;

    private static final String TAG=MatchActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutMatchBinding= DataBindingUtil.setContentView(this, R.layout.layout_match);
        userGameList=new ArrayList<>();
        map=new HashMap<>();
        bundle=getIntent().getExtras();
        toolbar=findViewById(R.id.toolbar);
        if(bundle!=null){
            userKey=bundle.getString(SignInActivity.KEY_USERNAME);
        }
        UserProfile userProfile=new UserProfile();
        databaseReferenceGame=FirebaseDatabase.getInstance().getReference();
        layoutMatchBinding.btnSave.setOnClickListener(v->{
            saveStat();
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void saveStat(){
        layoutMatchBinding.etDayNumber.setError(null);
        layoutMatchBinding.etMatchesPlayed.setError(null);
        layoutMatchBinding.etMatchesWon.setError(null);
        String day=layoutMatchBinding.etDayNumber.getText().toString();
        String matches=layoutMatchBinding.etMatchesPlayed.getText().toString();
        String wins=layoutMatchBinding.etMatchesWon.getText().toString();
        View focusView;

        if(TextUtils.isEmpty(day)){
            layoutMatchBinding.etDayNumber.setError("Enter day Number");
            focusView=layoutMatchBinding.etDayNumber;
            cancel=true;
            return;
        }
        else if(TextUtils.isEmpty(matches)){
            layoutMatchBinding.etMatchesPlayed.setError("Enter total number of matches");
            focusView=layoutMatchBinding.etMatchesPlayed;
            cancel=true;
            return;
        }
        else if(TextUtils.isEmpty(wins)){
            layoutMatchBinding.etMatchesWon.setError("Enter total nunber of wins");
            focusView=layoutMatchBinding.etMatchesWon;
            cancel=true;
            return;
        }
        else{
            focusView=null;
            cancel=false;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        else{
            if(bundle!=null) {
                 userKey = bundle.getString(SignInActivity.KEY_USERNAME);
                 Log.d(TAG,"USERKEY= "+userKey);
                 String id = databaseReferenceGame.push().getKey();
                 //databaseReferenceGame.child(id).child(username).getKey();
                UserGame userGame = new UserGame(day, matches, wins);
                UserProfile userProfile=new UserProfile();
                userProfile.setUserGame(userGame);
                //UserGame userGame1=new UserGame();
                //userGame1.setDay(day);
                //userGame1.setWins(wins);
                //userGame1.setMatches(matches);
                //map.put(username,new UserGame(day,matches,wins));
                //userGameList.add(userGame);
                String passedUserkey=userKey;

                //List<UserGame>userGameList1=new ArrayList<>();
                //userGameList1.add(userGame);
                //stringMap.put(username,userGame);
                try {
                    databaseReferenceGame.child("userProfile").child(userKey).child("userGame").push().setValue(userGame);
                    Intent returnIntent=getIntent();
                    returnIntent.putExtra(KEY_DAY,userGame.getDay());
                    Log.d(TAG,"DAY PASSED= "+userGame.getDay());
                    returnIntent.putExtra(KEY_MATCHES,userGame.getMatches());
                    returnIntent.putExtra(KEY_WINS,userGame.getWins());
                    setResult(RESULT_OK,returnIntent);
                    finish();
                    //databaseReferenceGame.child(id).setValue(map.get(username));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                Log.d(TAG,"Bundle less");
            }
        }
    }
}
