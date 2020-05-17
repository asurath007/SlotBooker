package com.slotbooker.UI.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.slotbooker.Login.LoginActivity;
import com.slotbooker.R;
import com.slotbooker.Util.BookAPI;
import com.slotbooker.Util.UserDetail;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

public class ProfileFragment extends Fragment {

    private static final String TAG = "Prof";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel ProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);



        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference userRef = db.collection("Users");


//        final String id = db.collection("Users").document().getId();
        final Button btn_prof_edit = root.findViewById(R.id.btn_prof_edit);
        final Button btn_prof_logout = root.findViewById(R.id.btn_prof_logout);
        final FloatingActionButton btn_prof_save = root.findViewById(R.id.btn_prof_save);

        final ImageView iv_prof = root.findViewById(R.id.iv_prof);
        final EditText firstName = root.findViewById(R.id.prof_first_name);
        final EditText lastName = root.findViewById(R.id.prof_last_name);
        final EditText pubgUsername = root.findViewById(R.id.pubg_username);
        final EditText codUsername = root.findViewById(R.id.cod_username);
        final EditText game3Username = root.findViewById(R.id.game3_username);

        final TextView userEmail = root.findViewById(R.id.prof_email);
        final TextView userName = root.findViewById(R.id.prof_username);
        final TextView tv_fn = root.findViewById(R.id.tv_first_name);
        final TextView tv_ln = root.findViewById(R.id.tv_last_name);
        final TextView tv_pun = root.findViewById(R.id.tv_pubg_username);
        final TextView tv_cun = root.findViewById(R.id.tv_cod_username);
        final TextView tv_gun = root.findViewById(R.id.tv_game3_username);
        final ProgressBar prof_progress = root.findViewById(R.id.prof_progressBar);

        //set user details

        ProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                loadUserDetails();
//                textView.setText(s);
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("myKey", MODE_PRIVATE);
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

                btn_prof_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_prof_edit.setVisibility(View.VISIBLE);
                        //UPDATE & save to db
                        saveDetails();
                        loadUserDetails();
                        prof_progress.setVisibility(View.VISIBLE);

                        //update
                        String nfn= firstName.getText().toString().trim();
                        String nln= lastName.getText().toString().trim();
                        String pun = pubgUsername.getText().toString().trim();
                        String cun = codUsername.getText().toString().trim();
                        String un = userName.getText().toString().trim();
                        String email = userEmail.getText().toString().trim();

                        final Map<String,Object>updateUser = new UserDetail(un,email,nfn,nln,pun,cun).newUser();
                        userRef.document(uID).set(updateUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Profile Details Updated", Toast.LENGTH_SHORT).show();
                                        prof_progress.setVisibility(View.INVISIBLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Updating Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                btn_prof_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // log out user from app & show login screen
                        SharedPreferences sp = getContext().getSharedPreferences("login",MODE_PRIVATE);
                        sp.edit().putBoolean("logged",false).apply();

                        //clear ids
                        SharedPreferences sp1 = getContext().getSharedPreferences("signupKey",MODE_PRIVATE);
                        SharedPreferences sp2 = getContext().getSharedPreferences("loginKey",MODE_PRIVATE);
                        sp1.edit().clear().apply();
                        sp2.edit().clear().apply();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                        intent.putExtra("logged",false);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void loadUserDetails() {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("signupKey", MODE_PRIVATE);
                String uID = sharedPreferences.getString("value", "");
                if (uID == null) {
                    userRef.document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                assert doc != null;
                                userName.setText(doc.getString("userName"));
                                userEmail.setText(doc.getString("userEmail"));
                                tv_fn.setText(doc.getString("FirstName"));
                                tv_ln.setText(doc.getString("LastName"));
                                tv_pun.setText(doc.getString("PUBG"));
                                tv_cun.setText(doc.getString("COD"));
                                firstName.setText(doc.getString("FirstName"));
                                lastName.setText(doc.getString("LastName"));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed getting User Data", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("loginKey",MODE_PRIVATE);
                    String ID = sharedPreferences1.getString("value","");
                    userRef.document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                assert doc != null;
                                userName.setText(doc.getString("userName"));
                                userEmail.setText(doc.getString("userEmail"));
                                tv_fn.setText(doc.getString("FirstName"));
                                tv_ln.setText(doc.getString("LastName"));
                                tv_pun.setText(doc.getString("PUBG"));
                                tv_cun.setText(doc.getString("COD"));
                                firstName.setText(doc.getString("FirstName"));
                                lastName.setText(doc.getString("LastName"));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed getting User Data", Toast.LENGTH_LONG).show();
                        }
                    });
                }

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
        });
        return root;

    }


}
