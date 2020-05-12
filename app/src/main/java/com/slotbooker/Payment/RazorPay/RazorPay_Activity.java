package com.slotbooker.Payment.RazorPay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.slotbooker.R;

import org.json.JSONObject;

public class RazorPay_Activity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "RazorPay" ;
    private Button buttonConfirmOrder;
    private EditText editTextPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.razor_pay);

//        Button btn_buy = findViewById(R.id.buttonConfirmOrder);
//        btn_buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startPayment();
//            }
//        });
//    }
//
//    private void startPayment() {
//        Checkout checkout = null;
//        checkout.setKeyID("NV4QX0OSE3NAl6ZNdny9iWMT");
//        /**
//         * Instantiate Checkout
//         */
//        checkout = new Checkout();
//
//        /**
//         * Set your logo here
//         */
//        checkout.setImage(R.drawable.app_icon);
//
//        /**
//         * Reference to current activity
//         */
//        final Activity activity = this;
//
//        /**
//         * Pass your payment options to the Razorpay Checkout as a JSONObject
//         */
//        try {
//            JSONObject options = new JSONObject();
//
//            /**
//             * Merchant Name
//             * eg: ACME Corp || HasGeek etc.
//             */
//            options.put("name", "Merchant Name");
//
//            /**
//             * Description can be anything
//             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
//             *     Invoice Payment
//             *     etc.
//             */
//            options.put("description", "Reference No. #123456");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_9A33XWu170gUtm");
//            options.put("currency", "INR");
//
//            /**
//             * Amount is always passed in currency subunits
//             * Eg: "500" = INR 5.00
//             */
//            options.put("amount", "500");
//
//            checkout.open(activity, options);
//        } catch (Exception e) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e);
//        }
//    }
//
//    @Override
//    public void onPaymentSuccess(String s) {
//
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//
//    }
//}
        findViews();
        listeners();
    }

    public void findViews() {
        buttonConfirmOrder = findViewById(R.id.buttonConfirmOrder);
        editTextPayment = findViewById(R.id.editTextPayment);
    }

    public void listeners() {


        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextPayment.getText().toString().equals(""))
                {
                    Toast.makeText(RazorPay_Activity.this, "Please fill payment", Toast.LENGTH_LONG).show();
                    return;
                }
                startPayment();
            }
        });
    }


    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyKnot Gaming");
            options.put("description", "Registration Fee");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

            String payment = editTextPayment.getText().toString();

            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
//            preFill.put("email", "axe.nexas@gmail.com");
//            preFill.put("contact", "9853837232");
//
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
//        Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Payment successfully done! ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}