package com.example.rajajainofficalproject.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rajajainofficalproject.Activity.Chatting_Activity;
import com.example.rajajainofficalproject.Activity.DashBoardActivity;
import com.example.rajajainofficalproject.Activity.Knowledge_Transfer_Activity;
import com.example.rajajainofficalproject.Activity.LoginActivity;
import com.example.rajajainofficalproject.Activity.MapsActivity;
import com.example.rajajainofficalproject.Activity.QR_Code_Activity;
import com.example.rajajainofficalproject.Activity.SettingActivity;
import com.example.rajajainofficalproject.Activity.UPI_Payment_Activity;
import com.example.rajajainofficalproject.Activity.UserDetailsActivity;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firestore.v1.StructuredQuery;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements View.OnClickListener, PaymentResultListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Checkout checkout;
    Context context;

    public HomeFragment(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageButton imgNotes, imgChatting, imgPlayStore, imgPayUpi, imgSearching, imgLearn;
    EasyUpiPayment easyUpiPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dash_board, container, false);


        easyUpiPayment = new EasyUpiPayment.Builder().with((Activity) context)
                .setPayeeVpa("8889338396@ybl")
                .setPayeeName("Raja Jain")
                .setTransactionId("20190603022401")
                .setTransactionRefId("0120192019060302240")
                .setDescription("For Today's Food")
                .setAmount("90.00")
                .build();

        imgNotes = root.findViewById(R.id.img_notes);
        imgChatting = root.findViewById(R.id.img_chatting);
        imgPlayStore = root.findViewById(R.id.img_play_store);
        imgPayUpi = root.findViewById(R.id.img_pay_upi);
        imgSearching = root.findViewById(R.id.img_google_chrome);
        imgLearn = root.findViewById(R.id.img_knowledge_transfer);

        imgNotes.setOnClickListener(this);
        imgChatting.setOnClickListener(this);
        imgPlayStore.setOnClickListener(this);
        imgPayUpi.setOnClickListener(this);
        imgSearching.setOnClickListener(this);
        imgLearn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_notes:
                // startActivity(new Intent(context, QR_Code_Activity.class));
                startActivity(new Intent(context, UserDetailsActivity.class));
                break;
            case R.id.img_chatting:
                // startActivity(new Intent(context, Chatting_Activity.class));
                testOnlinePayment();
               // startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.img_play_store:
//                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
//                startActivity(launchIntent);
//                https://play.google.com/store/apps/details?id=com
//                view=(WebView) findViewById(R.id.w);
//                view.loadUrl("https://play.google.com/store/apps/details?id=com");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.theopen.android"));
                startActivity(intent);
                break;
            case R.id.img_pay_upi:
                //startActivity(new Intent( context, UPI_Payment_Activity.class));
                startActivity(new Intent(context, MapsActivity.class));
                break;
            case R.id.img_google_chrome:
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);
                break;
            case R.id.img_knowledge_transfer:

                userSignOut();
                //startActivity(new Intent(context, Knowledge_Transfer_Activity.class));
                break;
        }

    }
    public void testOnlinePayment(){

        checkout = new Checkout();
        checkout.setKeyID("<rzp_test_fKQEgqJu9l8wPm>");
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("name", "Razorpay Corp");
            orderRequest.put("description", "Demoing Charges");
            orderRequest.put("amount", 1); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("prefill.email", "rajajain8198@gmail.com");
            orderRequest.put("prefill.contact","8889338396");
            orderRequest.put("payment_capture", false);
            checkout.open(getActivity(), orderRequest);

        } catch (Exception e) {
            // Handle Exception
            Toast.makeText(context, " try error : "+ e, Toast.LENGTH_SHORT).show();
        }
    }

    public void userSignOut() {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_details", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor sh = sharedPreferences.edit();

                        UserDetailsRoomDatabase userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(context);
                        userDetailsRoomDatabase.productDao().deleteDetails(sharedPreferences.getString("user_unique_id",""));
                        sh.putString("user_unique_id", "");
                        sh.apply();
                        startActivity(new Intent(context, LoginActivity.class));
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(context, "Payment successfully done! " + s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(context, "Payment error ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "please try again : "+ e, Toast.LENGTH_SHORT).show();
        }
    }
}
