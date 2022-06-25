package com.tabutech.eddysap.View.Activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityPhoneLoginBinding;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private ActivityPhoneLoginBinding binding;
    private EditText etPhone;

    private static final String TAG = "PhoneLoginActivity";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_phone_login);

        etPhone = findViewById(R.id.ed_phone);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progress = new ProgressDialog(this);

        binding.btnNext.setOnClickListener( v-> {

            if (binding.btnNext.getText().toString().equals("NEXT")) {
                progress.setMessage("Loading please wait ");
                progress.show();

                String number = binding.countryCodePicker.getSelectedCountryCodeWithPlus().trim();
                String phone = binding.edPhone.getText().toString().trim();
                String comb = number+phone;
                startPhoneNumberVerification(comb);
                //Toast.makeText(this, number, Toast.LENGTH_SHORT).show();

            }else {
                binding.btnNext.setText(R.string.confirm);
                progress.setMessage("verifying......");
                progress.show();
                verifyPhoneNumberWithCode(mVerificationId,binding.edCode.getText().toString());
            }
        });

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Log.w(TAG, "onVerificationFailed Completed");
                progress.dismiss();
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                progress.dismiss();

            }

            @Override
            public void onCodeSent(@NonNull String s,
                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                mVerificationId = s;
                mResendToken = forceResendingToken;

                //update UI
                binding.btnNext.setText(R.string.confirm);
                binding.edCode.setVisibility(View.VISIBLE);
                binding.edPhone.setEnabled(false);
                binding.countryCodePicker.setEnabled(false);
                progress.dismiss();
            }
        };
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        progress.dismiss();
                        FirebaseUser user = task.getResult().getUser();
                        startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        progress.dismiss();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if User is signed in (non-null) and update UI accordingly
        FirebaseUser  currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {

    }
}