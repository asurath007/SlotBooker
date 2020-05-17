package com.slotbooker.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.slotbooker.HomeActivity;
import com.slotbooker.R;
import com.slotbooker.UI.AddPlayerList;
import com.slotbooker.UI.AdminAddMatch;
import com.slotbooker.Util.*;

public class LoginActivity extends AppCompatActivity {

    EditText login_email;
    EditText login_password;
    Button btn_login;
    Button btn_create_account;
    ProgressBar login_progressBar;
    TextView host_link;
    SharedPreferences sp;

//    //Google SignIn method
//    private static final String TAG = "SignInActivity";
//    private static final int RC_SIGN_IN = 9001;
//
//    private GoogleSignInClient mGoogleSignInClient;
//    private TextView mStatusTextView;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private CollectionReference googleCR = db.collection("Google Users");

//    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("login", MODE_PRIVATE);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.btn_login);
        host_link = findViewById(R.id.host_link);
        login_progressBar = findViewById(R.id.login_progressBar);

        btn_create_account = findViewById(R.id.btn_create_account);

        /**
         Views
         //        mStatusTextView = findViewById(R.id.status);
         //
         //        // Button listeners
         //        findViewById(R.id.sign_in_button).setOnClickListener(this);
         //        findViewById(R.id.sign_out_button).setOnClickListener(this);
         //        findViewById(R.id.disconnect_button).setOnClickListener(this);

         //        // [START configure_signin]
         //        // Configure sign-in to request the user's ID, email address, and basic
         //        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         //        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
         //                .requestEmail()
         //                .build();
         //        // [END configure_signin]
         //
         //        // [START build_client]
         //        // Build a GoogleSignInClient with the options specified by gso.
         //        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         //        // [END build_client]
         //
         //        // [START customize_button]
         //        // Set the dimensions of the sign-in button.
         //        SignInButton signInButton = findViewById(R.id.sign_in_button);
         //        signInButton.setSize(SignInButton.SIZE_STANDARD);
         //        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
         //        // [END customize_button]
         **/

        //check if previously logged
        if (sp.getBoolean("logged", false)) {
            skipLogin();
        }

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEmailPasswordUser(login_email.getText().toString().trim(),
                        login_password.getText().toString().trim());

                //hide keyboard
//                InputMethodManager inputManager = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                assert inputManager != null;
//                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);

                //skip login condition
                sp.edit().putBoolean("logged", true).apply();
            }
        });

        //host page linking
        host_link.setClickable(true);
        host_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AdminAddMatch.class));
            }
        });

    }

    private void skipLogin() {
        Intent prevLogIn = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(prevLogIn);
        finish();
    }

    private void loginEmailPasswordUser(String email, String pwd) {

        login_progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
        }

//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){
        {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();

                            collectionReference.whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                            login_progressBar.setVisibility(View.INVISIBLE);

                                            if (e != null) {

                                            }
                                            assert queryDocumentSnapshots != null;
                                            if (!queryDocumentSnapshots.isEmpty()) {

                                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                                                    UserDetail ud = UserDetail.getInstance();
                                                    ud.setEmail(snapshot.getString("userEmail"));
                                                    ud.setUserName(snapshot.getString("userName"));
                                                    ud.setId(snapshot.getId());
                                                    Log.d("KEY","ID:"+snapshot.getId());
                                                    //pass id to fetch prof info
                                                    SharedPreferences sharedPref = getSharedPreferences("loginKey", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("value", snapshot.getId());
                                                    editor.apply();
//                                                    Log.d("signinID", "id:"+currentUserId);

                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //next activity
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                    login_progressBar.setVisibility(View.INVISIBLE);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "User not found",
                                    Toast.LENGTH_LONG).show();

                            login_progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
//        }else {
//            Toast.makeText(LoginActivity.this, "Please fill all fields",
//                    Toast.LENGTH_LONG).show();
        }

    }
}


/**Google SignIn method
*/
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // [START on_start_sign_in]
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//        // [END on_start_sign_in]
//    }
//
//    // [START onActivityResult]
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//
//            /**save to db*/
//
//
//            startActivity(new Intent(LoginActivity.this, Main2Activity.class));
//
//        }
//    }
//    // [END onActivityResult]
//
//    // [START handleSignInResult]
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//
//
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//        /**go to next activity*/
//         }
//    // [END handleSignInResult]
//
//    // [START signIn]
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//
//    }
//    // [END signIn]
//
//    // [START signOut]
//    private void signOut() {
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//    // [END signOut]
//
//    // [START revokeAccess]
//    private void revokeAccess() {
//        mGoogleSignInClient.revokeAccess()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//    // [END revokeAccess]
//    private void updateUI(@Nullable GoogleSignInAccount account) {
//        if (account != null) {
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//        }
//    }
//    @Override         //add implements ViewOnClickListener when adding this section
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.sign_in_button:
//                signIn();
//                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
//        }
//    }

