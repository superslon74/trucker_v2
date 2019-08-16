package com.example.trucker;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trucker.server.User;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.AccountPreferences;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.CountryCodeSpinner;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegistrationClientActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 99;

    public EditText editText1;
    public EditText editText2;
    public EditText editText3;
    public EditText editText4;
    public EditText editText5;
    public Button buttonone;
    public Button buttontwo;
    public RadioButton redRadioButton;
    public RadioButton greenRadioButton;

    String editText1value = null;
    String editText2value = null;
    String editText3value = null;
    String editText4value = null;
    String editText5value = null;


    private void getCurrentAccount() {
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            //Handle Returning User
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {

                @Override
                public void onSuccess(final Account account) {

                    String accountKitId = account.getId();
                    if (account.getPhoneNumber() != null) {
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();
                    }
                }

                @Override
                public void onError(final AccountKitError error) {
                }
            });
        } else {
        }
    }

    public void phoneLogin(@Nullable View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.CODE
        UIManager uiManager = new SkinManager(
                LoginType.PHONE,
                SkinManager.Skin.TRANSLUCENT,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? getResources().getColor(R.color.colorPrimary, null) : getResources().getColor(R.color.colorPrimary)),
                R.drawable.ic_arrow_back_white,
                SkinManager.Tint.WHITE,
                0.55
        );

        configurationBuilder.setUIManager(uiManager);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE && resultCode == RESULT_OK) {
            getCurrentAccount();

    //  Toast.makeText(getApplicationContext(), " Autorization : -"+ User.getApi_ovtarization(), Toast.LENGTH_LONG).show();

            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(editText1value,editText2value,editText3value,editText4value,editText5value);

        }
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_registration_client);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);


         redRadioButton = (RadioButton) findViewById(R.id.radioButtonHusband);
        redRadioButton.setOnClickListener(radioButtonClickListener);

        greenRadioButton = (RadioButton) findViewById(R.id.radioButtonKitten);
        greenRadioButton.setOnClickListener(radioButtonClickListener);

        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);

        buttonone = (Button) findViewById(R.id.buttonone);
         buttontwo = (Button) findViewById(R.id.buttontwo);
         buttontwo.setVisibility(View.GONE);

         getCurrentAccount();



        buttonone.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    editText1value = editText1.getText().toString();
                    editText2value = editText2.getText().toString();
                    editText3value = editText3.getText().toString();
                    editText4value = editText4.getText().toString();
                    editText5value = editText5.getText().toString();

                    if(editText1value.length()<1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.input_name_value), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    if(editText2value.length()<1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.input_last_name_value), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    if(editText3value.length()<1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.input_email_value), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    if(editText4value.length()<1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.input_password_value), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    if(editText5value.length()<1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.input_password_repeat_value), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    if(!editText5value.equals(editText4value)) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.input_password_repeat_error_value), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        buttonone.setVisibility(View.GONE);
                        redRadioButton.setVisibility(View.GONE);
                        greenRadioButton.setVisibility(View.GONE);
                        buttontwo.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });

    }



    public class OkHttpHandler extends AsyncTask<String, Void, String> {
     //   OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

   /*         RequestBody formBody = new FormBody.Builder()
                    .add("first_name", params[0])
                    .add("last_name", params[1])
                    .add("email", params[2])
                    .add("mobile", "+380935767412")
                    .add("password", params[3])
                    .add("password_confirmation", params[4])
                    .add("device_token", User.getDevice_token())
                    .add("device_type", User.getDevice_type())
                    .add("device_id", User.getDevice_id())
                    .build();
            Request request = new Request.Builder()
                    .url(User.getApi_registration())
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return   response.body().string();
                //   Log.i("SQL_result_to_server", " - " + response.body().string());
                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }
 */

/*
          RequestBody formBody = new FormBody.Builder()
            //        .add("first_name", "Gennadiy")
            //        .add("last_name", "Shamanskij")
                    .add("email", "superslon74@gmail.com")
            //        .add("mobile", "+380935767412")
                    .add("password", "Rambunas74")
            //        .add("password_confirmation", "Rambunas74")
                    .add("device_token", "xWwo6bPJcYCMKaMNBOjEIGrFNggKnF14dNQtctgv")
                    .add("device_type", "android")
                    .add("device_id", "f950260219bbd71f")
                    .build();
            Request request = new Request.Builder()
              //      .url("https://api.holler.taxi/api/provider/register")
                    .url("https://api.holler.taxi/api/provider/oauth/token")
                    .post(formBody)
                    .build();

     longitude 46.96797572
     latitude 32.0156461


    Bearer
    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM0MSwiaXNzIjoiaHR0cHM6Ly9hcGkuaG9sbGVyLnRheGkvYXBpL3Byb3ZpZGVyL29hdXRoL3Rva2VuIiwiaWF0IjoxNTY1MjY4Mjk3LCJleHAiOjE1NjU2MjgyOTcsIm5iZiI6MTU2NTI2ODI5NywianRpIjoiQ1FoVTk0THVpOXgxeTBTciJ9.Z8lWlXDUx8gYDGDsW1qFgb_RPtSFPmc3hn2ln2U5Wzs

            RequestBody formBody = new FormBody.Builder()
                    //        .add("first_name", "Gennadiy")
                    //        .add("last_name", "Shamanskij")
                    .add("email", "superslon74@gmail.com")
                    //        .add("mobile", "+380935767412")
                  //  .add("password", "Rambunas74")
                    //        .add("password_confirmation", "Rambunas74")
                //    .add("device_token", "xWwo6bPJcYCMKaMNBOjEIGrFNggKnF14dNQtctgv")
                //    .add("device_type", "android")
               //     .add("device_id", "f950260219bbd71f")
                    .build();
            Request request = new Request.Builder()
                    //      .url("https://api.holler.taxi/api/provider/register")
                    .url("https://api.holler.taxi/api/provider/verify")
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return   response.body().string();
             //   Log.i("SQL_result_to_server", " - " + response.body().string());
                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
  //          Log.i("SQL_result_to_server", " - " + s);

            Intent intent = new Intent(getApplicationContext(), DownloadDocumentActivity.class);
            startActivity(intent);
            finish();
/*
 SQL_result_to_server:
 -{"first_name":"\u0433\u0435\u043d\u043d\u0430\u0434\u0438\u0439",
   "last_name":"\u0448\u0430\u043c\u0430\u043d\u0441\u043a\u0438\u0439",
   "email":"superslon74@gmail.com",
   "mobile":"+380935767412",
   "updated_at":"2019-08-14 13:40:03",
   "created_at":"2019-08-14 13:40:03",
   "id":1}
*/

        }
    }





    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.radioButtonHusband:

                    break;
                case R.id.radioButtonKitten:

                    break;

                default:
                    break;
            }
        }
    };
}
