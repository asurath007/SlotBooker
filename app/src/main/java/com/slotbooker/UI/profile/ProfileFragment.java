package com.slotbooker.UI.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.slotbooker.Login.LoginActivity;
import com.slotbooker.R;
import com.slotbooker.Util.BookAPI;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel ProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference userRef = db.collection("Users");


        final Button btn_discord = root.findViewById(R.id.btn_discord);
        final Button btn_whatsapp = root.findViewById(R.id.btn_whatsapp);
        final Button btn_prof_edit = root.findViewById(R.id.btn_prof_edit);
        final Button btn_prof_logout = root.findViewById(R.id.btn_prof_logout);
        final FloatingActionButton btn_prof_save = root.findViewById(R.id.btn_prof_save);

        final EditText firstName = root.findViewById(R.id.prof_first_name);
        final EditText lastName = root.findViewById(R.id.prof_last_name);
        final EditText pubgUsername = root.findViewById(R.id.pubg_username);

        final TextView email = root.findViewById(R.id.prof_email);
        final TextView userName = root.findViewById(R.id.prof_username);

        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    //make this to get from database
                    BookAPI api = BookAPI.getInstance();
                    email.setText(api.getUserEmail());
                    userName.setText(api.getUsername());
                }else{
                    Toast.makeText(getContext(),"Error Getting Profile Information", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Profile Information not found", Toast.LENGTH_LONG).show();
            }
        });

        ProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                btn_discord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDiscord = new Intent(Intent.ACTION_VIEW);
                        String url = "https://discord.gg/rkfmJD";
                        intentDiscord.setData(Uri.parse(url));
                        intentDiscord.setPackage("com.discord");
                        startActivity(intentDiscord);
                    }
                });

                btn_whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                        String url = "https://chat.whatsapp.com/Ga8LZnS6yHAJ0cO7KlZML0";
                        intentWhatsapp.setData(Uri.parse(url));
                        intentWhatsapp.setPackage("com.whatsapp");
                        startActivity(intentWhatsapp);
                    }
                });

                btn_prof_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //setting fields editable on button click and display save button
                        firstName.setFocusableInTouchMode(true);
                        lastName.setFocusableInTouchMode(true);
                        pubgUsername.setFocusableInTouchMode(true);
                        btn_prof_save.setVisibility(View.VISIBLE);
                    }
                });

                btn_prof_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //save to db
                    }
                });

                btn_prof_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // log out user from app & show login screen
                        SharedPreferences sp = getContext().getSharedPreferences("login",MODE_PRIVATE);
                        sp.edit().putBoolean("logged",false).apply();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                        intent.putExtra("logged",false);
                        startActivity(intent);
                    }
                });
            }
        });
        return root;

    }
}
