package com.slotbooker.UI.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.slotbooker.Main2Activity;
import com.slotbooker.R;

import org.json.JSONObject;

import java.util.List;

public class HomeFragment extends Fragment implements PaymentResultListener {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final Button btn_discord = root.findViewById(R.id.btn_home_discord);
        final Button btn_whatsapp = root.findViewById(R.id.btn_home_whatsapp);
        final Button btn_instagram = root.findViewById(R.id.btn_home_instagram);
        final Button btn_website = root.findViewById(R.id.btn_home_website);
        final Button btn_donate = root.findViewById(R.id.btn_home_donate);

        final ImageView iv1 = root.findViewById(R.id.iv_pubg);
        final ImageView iv2 = root.findViewById(R.id.iv_cod);

        final Context mContext = getContext();


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                btn_discord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDiscord = new Intent(Intent.ACTION_VIEW);
                        String url = "https://discord.gg/5VFDyHZ";
                        intentDiscord.setData(Uri.parse(url));
                        intentDiscord.setPackage("com.discord");
                        if (isIntentAvailable(mContext, intentDiscord)){
                            startActivity(intentDiscord);
                        } else{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/rkfmJD")));
                        }
                    }
                });

                btn_whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                        String url = "https://chat.whatsapp.com/Ga8LZnS6yHAJ0cO7KlZML0";
                        intentWhatsapp.setData(Uri.parse(url));
                        intentWhatsapp.setPackage("com.whatsapp");
                        if (isIntentAvailable(mContext, intentWhatsapp)){
                            startActivity(intentWhatsapp);
                        } else{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/Ga8LZnS6yHAJ0cO7KlZML0")));
                        }
                    }
                });

                btn_instagram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent insta = new Intent(Intent.ACTION_VIEW);
                        String url = "https://instagram.com/myknot_gaming?igshid=wuf0ppebeze1";
                        insta.setData(Uri.parse(url));
                        insta.setPackage("com.instagram.android");

                        if (isIntentAvailable(mContext, insta)){
                            startActivity(insta);
                        } else{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/myknot_gaming?igshid=wuf0ppebeze1")));
                        }
                    }
                });

                btn_website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent web = new Intent(Intent.ACTION_VIEW);
                        String url = "https://dragonballzs.000webhostapp.com/?name=Gowtham+kumar+D&mobile=9353690229";
                        web.setData(Uri.parse(url));
                        web.setPackage("com.android.chrome");

                        if (isIntentAvailable(mContext, web)){
                            startActivity(web);
                        } else{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://dragonballzs.000webhostapp.com/?name=Gowtham+kumar+D&mobile=9353690229")));
                        }
                    }
                });
                btn_donate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPayment();
                    }
                });

                iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Main2Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"No COD matches found", Toast.LENGTH_LONG).show();
                    }
                });
            }

            //check if app installed
            //if app not installed open in browser
            private boolean isIntentAvailable(Context ctx, Intent intent) {
                final PackageManager packageManager = ctx.getPackageManager();
                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                return list.size() > 0;
            }

            //card-view clicking

        });
        return root;


//        return inflater.inflate(R.layout.activity_main2,container,false);
    }

    private void startPayment() {
        final Activity activity = getActivity();

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyKnot Gaming");
            options.put("description", "Donation Fee");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

//            String payment = editTextPayment.getText().toString();
            String payment = "50";
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
//            preFill.put("email", "axe.nexas@gmail.com");
//            preFill.put("contact", "9853837232");
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getContext(), "Payment successfully done! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(getContext(), "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}
