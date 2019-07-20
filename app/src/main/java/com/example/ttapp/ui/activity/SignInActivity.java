package com.example.ttapp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.ttapp.R;
import com.example.ttapp.databinding.ActivityLoginBinding;
import com.example.ttapp.db.UserGame;
import com.example.ttapp.db.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    boolean cancel;
    private ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth firebaseAuth;
    private boolean status;
    private Bundle bundle;
    private String value;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReferenceProfile;
    private List<UserProfile> userProfileList;
    private static final String TAG=SignInActivity.class.getSimpleName();
    public static String KEY_USERNAME="USERNAME";
    private static final String KEY_PREF="KEY_PREF";
    private static final String KEY_LOGIN="KEY_LOGIN";
    private String passingUsername;
    private String keyValue,passingKeyValue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPreferences=getSharedPreferences(KEY_PREF,MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        userProfileList=new ArrayList<>();
        databaseReferenceProfile= FirebaseDatabase.getInstance().getReference("userProfile");
        activityLoginBinding.btnLogin.setOnClickListener(v -> {
            loginUser();
        });
        activityLoginBinding.textViewClickableSignup.setOnClickListener(v->{
            Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void loginUser() {
        activityLoginBinding.etUsernameSignIn.setError(null);
        activityLoginBinding.etPasswordSignIn.setError(null);
        String email, password;
        email = activityLoginBinding.etUsernameSignIn.getText().toString();
        password = activityLoginBinding.etPasswordSignIn.getText().toString();
        View focusView;


        if (TextUtils.isEmpty(email)) {
            activityLoginBinding.etUsernameSignIn.setError(getString(R.string.email_alert));
            focusView = activityLoginBinding.etUsernameSignIn;
            cancel = true;
            Toast.makeText(getApplicationContext(), getString(R.string.email_alert), Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            activityLoginBinding.etPasswordSignIn.setError(getString(R.string.password_alert));
            focusView = activityLoginBinding.etPasswordSignIn;
            cancel = true;
            Toast.makeText(getApplicationContext(), getString(R.string.password_alert), Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            focusView=null;
            cancel=false;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            activityLoginBinding.progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Login unsucesssful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()) {
                                    status = true;
                                }
                                if (status) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
                                        String username = firebaseUser1.getEmail();
                                        passingUsername = username;
                                        String id = databaseReferenceProfile.push().getKey();
                                        Log.d(TAG, "KEY DB= " + id);
                                        databaseReferenceProfile.child(id).getKey();
                                        UserGame userGame = new UserGame();
                                        UserProfile userProfile = new UserProfile(username, userGame);
                                        databaseReferenceProfile.child(id).setValue(userProfile);
                                        Log.d(TAG, "Database updated successfully");
                                        Toast.makeText(getApplicationContext(), getString(R.string.login_successful_alert), Toast.LENGTH_LONG).show();
                                        activityLoginBinding.progressBar.setVisibility(View.GONE);
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                                    databaseReferenceProfile.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                                                UserProfile userProfile=dataSnapshot1.getValue(UserProfile.class);
//                                                passingUsername= userProfile.getUserName();
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });


                                        //updateUi(firebaseUser);
                                        savePreference(id);
                                        Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                                        intent.putExtra(KEY_USERNAME, id);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), getString(R.string.login_unsuccessful_alert), Toast.LENGTH_LONG).show();
                                        activityLoginBinding.progressBar.setVisibility(View.GONE);
                                        //updateUi(null);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "email not verified", Toast.LENGTH_SHORT).show();
                                    activityLoginBinding.progressBar.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_LONG).show();
                                activityLoginBinding.progressBar.setVisibility(View.GONE);
                            }
                        }

                    });
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void savePreference(String id){
        sharedPreferences=getSharedPreferences(KEY_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        keyValue=id;
        editor.putString(KEY_LOGIN,keyValue);
        editor.apply();
    }

    private String loadPreference(){
        sharedPreferences=getSharedPreferences(KEY_PREF,MODE_PRIVATE);
        passingKeyValue=sharedPreferences.getString(KEY_LOGIN,keyValue);
        return passingKeyValue;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString("VERIFICATION");
            if (!value.equals("signup")) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.d("SIGN IN ACT", "signed out");
                }

            } else {
                super.onStart();
            }
        }
        else{

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                intent.putExtra(KEY_USERNAME,loadPreference());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Log.d("SIGN IN ACT", "signed out");
            }
        }
    }
}
