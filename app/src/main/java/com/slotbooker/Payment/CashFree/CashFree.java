//package com.slotbooker.Payment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.gocashfree.cashfreesdk.CFPaymentService;
//import com.gocashfree.cashfreesdk.ui.gpay.GooglePayStatusListener;
//import com.slotbooker.R;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
//import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
//
//public class CashFree extends AppCompatActivity {
//
//    private static final String TAG = "Cash Free";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.cash_free);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //Same request code for all payment APIs.
////        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
//        Log.d(TAG, "API Response : ");
//        //Prints all extras. Replace with app logic.
//        if (data != null) {
//            Bundle bundle = data.getExtras();
//            if (bundle != null)
//                for (String key : bundle.keySet()) {
//                    if (bundle.getString(key) != null) {
//                        Log.d(TAG, key + " : " + bundle.getString(key));
//                    }
//                }
//        }
//    }
//
//    public void onClick(View view) {
//
//
//        /*
//         * stage allows you to switch between sandboxed and production servers
//         * for CashFree Payment Gateway. The possible values are
//         *
//         * 1. TEST: Use the Test server. You can use this service while integrating
//         *      and testing the CashFree PG. No real money will be deducted from the
//         *      cards and bank accounts you use this stage. This mode is thus ideal
//         *      for use during the development. You can use the cards provided here
//         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
//         *
//         * 2. PROD: Once you have completed the testing and integration and successfully
//         *      integrated the CashFree PG, use this value for stage variable. This will
//         *      enable live transactions
//         */
//        String stage = "TEST";
//
//        //Show the UI for doGPayPayment and phonePePayment only after checking if the apps are ready for payment
//        if (view.getId() == R.id.phonePe_exists) {
//            Toast.makeText(
//                    CashFree.this,
//                    CFPaymentService.getCFPaymentServiceInstance().doesPhonePeExist(CashFree.this, stage) + "",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        } else if (view.getId() == R.id.gpay_ready) {
//            CFPaymentService.getCFPaymentServiceInstance().isGPayReadyForPayment(CashFree.this, new GooglePayStatusListener() {
//                @Override
//                public void isReady() {
//                    Toast.makeText(CashFree.this, "Ready", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void isNotReady() {
//                    Toast.makeText(CashFree.this, "Not Ready", Toast.LENGTH_SHORT).show();
//                }
//            });
//            return;
//        }
//
//        /*
//         * token can be generated from your backend by calling cashfree servers. Please
//         * check the documentation for details on generating the token.
//         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
//         */
//        String token = "TOKEN_DATA";
//
//
//        /*
//         * appId will be available to you at CashFree Dashboard. This is a unique
//         * identifier for your app. Please replace this appId with your appId.
//         * Also, as explained below you will need to change your appId to prod
//         * credentials before publishing your app.
//         */
//        String appId = "YOUR_APP_ID_HERE";
//        String orderId = "Order0001";
//        String orderAmount = "1";
//        String orderNote = "Test Order";
//        String customerName = "John Doe";
//        String customerPhone = "9900012345";
//        String customerEmail = "test@gmail.com";
//
//        Map<String, String> params = new HashMap<>();
//
//        params.put(PARAM_APP_ID, appId);
//        params.put(PARAM_ORDER_ID, orderId);
//        params.put(PARAM_ORDER_AMOUNT, orderAmount);
//        params.put(PARAM_ORDER_NOTE, orderNote);
//        params.put(PARAM_CUSTOMER_NAME, customerName);
//        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
//        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
//
//
//        for (Map.Entry entry : params.entrySet()) {
//            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
//        }
//
//        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
//        cfPaymentService.setOrientation(0);
//        switch (view.getId()) {
//
//            /***
//             * This method handles the payment gateway invocation (web flow).
//             *
//             * @param context Android context of the calling activity
//             * @param params HashMap containing all the parameters required for creating a payment order
//             * @param token Provide the token for the transaction
//             * @param stage Identifies if test or production service needs to be invoked. Possible values:
//             *              PROD for production, TEST for testing.
//             * @param color1 Background color of the toolbar
//             * @param color2 text color and icon color of toolbar
//             * @param hideOrderId If true hides order Id from the toolbar
//             */
//            case R.id.web: {
//                cfPaymentService.doPayment(CashFree.this, params, token, stage, "#784BD2", "#FFFFFF", false);
////                 cfPaymentService.doPayment(MainActivity.this, params, token, stage);
//                break;
//            }
//            /***
//             * Same for all payment modes below.
//             *
//             * @param context Android context of the calling activity
//             * @param params HashMap containing all the parameters required for creating a payment order
//             * @param token Provide the token for the transaction
//             * @param stage Identifies if test or production service needs to be invoked. Possible values:
//             *              PROD for production, TEST for testing.
//             */
//            case R.id.upi: {
////                                cfPaymentService.selectUpiClient("com.google.android.apps.nbu.paisa.user");
//                cfPaymentService.upiPayment(CashFree.this, params, token, stage);
//                break;
//            }
//            case R.id.amazon: {
//                cfPaymentService.doAmazonPayment(CashFree.this, params, token, stage);
//                break;
//            }
////            case R.id.gpay: {
////                cfPaymentService.gPayPayment(CashFree.this, params, token, stage);
////                break;
////            }
//            case R.id.phonePe: {
//                cfPaymentService.phonePePayment(CashFree.this, params, token, stage);
//                break;
//            }
//        }
//    }
//}
