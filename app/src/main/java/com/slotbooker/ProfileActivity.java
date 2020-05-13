package com.slotbooker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.slotbooker.Adapter.AdminMapAdapter;
import com.slotbooker.Adapter.Model.MapList;
import com.slotbooker.Login.LoginActivity;
import com.slotbooker.UI.AdminAddMatch;
import com.slotbooker.Util.UserDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage;
    private Context ctx;

    private EditText firstName, lastName, pubgUsername, codUsername, game3Username;
    private Button btn_prof_edit, btn_prof_logout;
    private FloatingActionButton btn_prof_save;
    private TextView email,userName, tv_fn,tv_ln,tv_pun,tv_cun,tv_gun;
    private ProgressBar prof_progress;
    private ImageView img_prof;


    private CollectionReference userRef = firestoreDB.collection("Users");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        firstName = findViewById(R.id.prof_first_name);
        lastName = findViewById(R.id.prof_last_name);
        pubgUsername = findViewById(R.id.pubg_username);
        codUsername = findViewById(R.id.cod_username);
        game3Username = findViewById(R.id.game3_username);
        btn_prof_edit = findViewById(R.id.btn_prof_edit);
        btn_prof_logout = findViewById(R.id.btn_prof_logout);
        btn_prof_save = findViewById(R.id.btn_prof_save);

        ImageView iv_prof = findViewById(R.id.iv_prof);

        email = findViewById(R.id.prof_email);
        userName = findViewById(R.id.prof_username);
        tv_fn = findViewById(R.id.tv_first_name);
        tv_ln = findViewById(R.id.tv_last_name);
        tv_pun = findViewById(R.id.tv_pubg_username);
        tv_cun = findViewById(R.id.tv_cod_username);
        tv_gun = findViewById(R.id.tv_game3_username);
        prof_progress = findViewById(R.id.prof_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        final CollectionReference userRef = firestoreDB.collection("Users");
        
        loadUserDetails();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
//        String uid = user.getUid();
        //getting doc id
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        final String uID = sharedPreferences.getString("value","");
        Log.d("Prof","id: "+uID);


        btn_prof_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting fields editable on button click and display save button
                editDetails();
                btn_prof_save.setVisibility(View.VISIBLE);
                btn_prof_edit.setVisibility(View.INVISIBLE);
            }
        });

        btn_prof_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log out user from app & show login screen
                SharedPreferences sp = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("logged",false).apply();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
//                        intent.putExtra("logged",false);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        btn_prof_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_prof_edit.setVisibility(View.VISIBLE);
                //UPDATE & save to db
                saveDetails();
//                refreshDetails();
                prof_progress.setVisibility(View.VISIBLE);

                //update
                String nfn= firstName.getText().toString().trim();
                String nln= lastName.getText().toString().trim();
                String pun = pubgUsername.getText().toString().trim();
                String cun = codUsername.getText().toString().trim();

                final Map<String,Object>updateUser = new UserDetail(nfn,nln,pun,cun).newUser();
                userRef.document(uID).set(updateUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Profile Details Updated", Toast.LENGTH_SHORT).show();
                                prof_progress.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Updating Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void loadUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String uID = sharedPreferences.getString("value","");
        userRef.document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    assert doc != null;
                    userName.setText(doc.getString("userName"));
                    email.setText(doc.getString("userEmail"));
                    tv_fn.setText(doc.getString("FirstName"));
                    tv_ln.setText(doc.getString("LastName"));
                    tv_pun.setText(doc.getString("PUBG"));
                    tv_cun.setText(doc.getString("COD"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Failed getting User Data",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDetails() {
        loadUserDetails();
        firstName.setVisibility(View.GONE);
        tv_fn.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.GONE);
        tv_ln.setVisibility(View.VISIBLE);
        pubgUsername.setVisibility(View.GONE);
        tv_pun.setVisibility(View.VISIBLE);
        codUsername.setVisibility(View.GONE);
        tv_cun.setVisibility(View.VISIBLE);
        game3Username.setVisibility(View.GONE);
        tv_gun.setVisibility(View.VISIBLE);
        btn_prof_save.setVisibility(View.GONE);
    }

    private void editDetails() {
        firstName.setVisibility(View.VISIBLE);
        tv_fn.setVisibility(View.INVISIBLE);
        lastName.setVisibility(View.VISIBLE);
        tv_ln.setVisibility(View.INVISIBLE);
        pubgUsername.setVisibility(View.VISIBLE);
        tv_pun.setVisibility(View.INVISIBLE);
        codUsername.setVisibility(View.VISIBLE);
        tv_cun.setVisibility(View.INVISIBLE);
        game3Username.setVisibility(View.VISIBLE);
        tv_gun.setVisibility(View.INVISIBLE);
        btn_prof_save.setVisibility(View.VISIBLE);
    }
}
