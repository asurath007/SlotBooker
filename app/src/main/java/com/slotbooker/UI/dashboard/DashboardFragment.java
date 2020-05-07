package com.slotbooker.UI.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.slotbooker.Main2Activity;
import com.slotbooker.R;
import com.slotbooker.Registration.Duo;
import com.slotbooker.Registration.Squad;
import com.slotbooker.UI.DuoMapSelector;
import com.slotbooker.UI.SoloMapSelector;
import com.slotbooker.UI.SquadMapSelector;

public class DashboardFragment extends Fragment {

    private com.slotbooker.UI.dashboard.DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(com.slotbooker.UI.dashboard.DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
        final Button btn_solo = root.findViewById(R.id.btn_solo);
        final Button btn_duo = root.findViewById(R.id.btn_duo);
        final Button btn_squad = root.findViewById(R.id.btn_squad);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);




                btn_solo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                solo_count++;
//                Bundle keeper = new Bundle();
//                keeper.putInt("key", solo_count);
                        startActivity(new Intent(getActivity(), SoloMapSelector.class));

                    }
                });


                btn_duo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), DuoMapSelector.class));

                    }
                });

                btn_squad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), SquadMapSelector.class));
                    }
                });
            }
    });
        return root;
    }
}
