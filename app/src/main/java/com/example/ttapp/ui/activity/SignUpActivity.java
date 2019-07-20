package com.example.ttapp.ui.activity;

import android.content.Intent;
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
import com.example.ttapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener authStateListener;
    boolean cancel;
    private ActivitySignUpBinding activitySignUpBinding;
    private FirebaseAuth firebaseAuth;
    public static final String TAG=SignUpActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        activitySignUpBinding.btnRegister.setOnClickListener(v -> {
            registerUser();
        });
        activitySignUpBinding.textViewClickableLogin.setOnClickListener(v->{
            Intent intent= new Intent(SignUpActivity.this,SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void registerUser() {

        activitySignUpBinding.etUsernameSignUp.setError(null);
        activitySignUpBinding.etPasswordSignUp.setError(null);
        String email, password;
        email = activitySignUpBinding.etUsernameSignUp.getText().toString();
        password = activitySignUpBinding.etPasswordSignUp.getText().toString();
        View focusView;

        if (TextUtils.isEmpty(email)) {
            activitySignUpBinding.etUsernameSignUp.setError(getString(R.string.email_alert));
             focusView = activitySignUpBinding.etUsernameSignUp;
            cancel = true;
            Log.d(TAG,"Entered in email view");
            Toast.makeText(getApplicationContext(), getString(R.string.email_alert), Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            activitySignUpBinding.etPasswordSignUp.setError(getString(R.string.password_alert));
            focusView = activitySignUpBinding.etPasswordSignUp;
            Log.d(TAG,"Entered in password view");
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
            activitySignUpBinding.progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendVerificattionEmail();
                                Toast.makeText(getApplicationContext(), getString(R.string.reg_sucessful_alert), Toast.LENGTH_SHORT).show();
                                //boolean result=verifyEmail();
                                activitySignUpBinding.progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.putExtra("VERIFICATION", "signup");
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.reg_unsuccessful_alert), Toast.LENGTH_SHORT).show();
                                activitySignUpBinding.progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void sendVerificattionEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this,
                            "Verification email sent to " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
