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

import com.slotbooker.AfterPayment.AFSolo;
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

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                Intent intent = new Intent(getActivity(), AFSolo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
    });
        return root;
    }
}
