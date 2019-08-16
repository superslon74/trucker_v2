package com.example.trucker;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trucker.adapter.DocumentsAdapter;
import com.example.trucker.model.DocumentModelAdapter;
import com.example.trucker.server.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static java.lang.System.in;

public class DownloadDocumentActivity extends AppCompatActivity {

    private static String asses_token;
    public ListView lvMain;
    public Button button;
    public DocumentsAdapter adapter;
    public EditText EditText01;

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    Uri  chosenImageUri = data.getData();


                    String file = getPath(DownloadDocumentActivity.this,chosenImageUri);

      Log.i("realpath", "onActivityResult: file path : "+file );


                    /*                  try {
       String file = null;
              file = FileUtil.from(DownloadDocumentActivity.this,chosenImageUri);
       String number_document = null;
              number_document = EditText01.getText().toString();

    //   Toast.makeText(getApplicationContext(), " "+file.getPath(), Toast.LENGTH_LONG).show();
       Log.i(" path ", " - " + " - "+file+" - "+number_document);


  //     OkHttpHandlerThree okHttpHandlerThree = new OkHttpHandlerThree();
  //     okHttpHandlerThree.execute("/storage/emulated/0/DCIM/Camera/20171201_145946.jpg","20171201_145946.jpg",number_document);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
*/



                    /*
/data/user/0/com.example.trucker/cache/20190803_150652.jpg
/data/user/0/com.example.trucker/cache/20190429_220040.jpg
/data/user/0/com.example.trucker/cache/20190715_090544.jpg

/storage/emulated/0/DCIM/Camera

*/
                }
                break;
            }
        }



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);

        EditText01 = (EditText) findViewById(R.id.EditText01);

        lvMain = (ListView) findViewById(R.id.lvMain);

        button = (Button) findViewById(R.id.button1);

        ArrayList<DocumentModelAdapter> arrayOfUsers = new ArrayList<DocumentModelAdapter>();
// Create the adapter to convert the array to views
        adapter = new DocumentsAdapter(this, arrayOfUsers);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

 //               Toast.makeText(getApplicationContext(), " id: -"+(position+1), Toast.LENGTH_LONG).show();
                EditText01.setText(""+(position+1));
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);



            }
        });


      OkHttpHandler okHttpHandler = new OkHttpHandler();
      okHttpHandler.execute();

    }
/*
    {"id":1,
    "first_name":"\u0433\u0435\u043d\u043d\u0430\u0434\u0438\u0439",
    "last_name":"\u0448\u0430\u043c\u0430\u043d\u0441\u043a\u0438\u0439",
    "email":"superslon74@gmail.com",
    "gender":"MALE",
    "mobile":"+380935767412",
    "avatar":null,
    "rating":"5.00",
    "status":"onboarding",
    "fleet":0,
    "latitude":null,
    "longitude":null,
    "otp":0,
    "created_at":"2019-08-14 13:40:03",
    "updated_at":"2019-08-14 13:40:03",
    "request_at":null,
    "login_by":"manual",
    "social_unique_id":null,
    "automatic_approval":0,
    "access_token":"
    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHBzOi8vYXBpLnRydWNrZXIuaG9zdC9hcGkvcHJvdmlkZXIvb2F1dGgvdG9rZW4iLCJpYXQiOjE1NjU3ODA1NzYsImV4cCI6MTU2NjE0MDU3NiwibmJmIjoxNTY1NzgwNTc2LCJqdGkiOiJoZE95QnVWMFFReXFYOEJaIn0.1whPVApistNnUrVN_hPWh_AlZgUTocAszEkmifuCa80
    ",
    "currency":"$",
    "sos":"911",
    "service":null,
    "device":{"id":1,
              "provider_id":1,
              "udid":"f950260219bbd71f",
              "token":"kUgdIdZdIjBybnWpTd9ASo5MA3Quk5mNaRRHeUhc",
              "sns_arn":null,
              "type":"android"}}
*/

    public class OkHttpHandler extends AsyncTask<String, Void, String> {
           OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {



          RequestBody formBody = new FormBody.Builder()
               .add("email", "superslon74@gmail.com")
               .add("password", "Rambunas74")
                  .add("device_token", User.getDevice_token())
                  .add("device_type", User.getDevice_type())
                  .add("device_id", User.getDevice_id())
                    .build();
            Request request = new Request.Builder()
                    .url(User.getApi_ovtarization())
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

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject reader = null;
            try {
                reader = new JSONObject(s);
            } catch (JSONException e) {
             //   e.printStackTrace();
            }

           String sys  = null;
            try {
          //      Log.i("SQL_result_to_server", " - " + reader.getString("access_token"));
                sys  = reader.getString("access_token");
            } catch (JSONException e) {
        //        e.printStackTrace();
            }
            asses_token = sys.toString();
            Log.i("asses_token", " - " + asses_token);


            OkHttpHandlerTwo okHttpHandlerTwo = new OkHttpHandlerTwo();
            okHttpHandlerTwo.execute();

                 }
    }

  /*-------------------------*/
  public class OkHttpHandlerTwo extends AsyncTask<String, Void, String> {
      OkHttpClient client = new OkHttpClient();

      @Override
      protected String doInBackground(String... params) {


          Request request = new Request.Builder()
          .header("Content-Type", "application/x-www-form-urlencoded")
          .header("Authorization", "Bearer "+asses_token)
          .header("X-Requested-With", "XMLHttpRequest")
          .url(User.getApi_documents())
          .build();
          try {
              Response response = client.newCall(request).execute();
              return   response.body().string();
              //   Log.i("SQL_result_to_server", " - " + response.body().string());
              // Do something with the response.
          } catch (IOException e) {
              e.printStackTrace();
          }

          return null;
      }


      @Override
      protected void onPostExecute(String s) {
          super.onPostExecute(s);

          adapter.clear();

          JSONArray jsonArray = null;
          try {
              jsonArray = new JSONArray(s);
          } catch (JSONException e) {
              e.printStackTrace();
          }

          for(int i=0; i<jsonArray .length(); i++) {


              JSONObject reader = null;
              try {
                  reader = new JSONObject(jsonArray.get(i).toString());

      DocumentModelAdapter newUser1 = null;
      newUser1 = new DocumentModelAdapter(
              reader.getString("status").toString(),
              reader.getString("id").toString(),
              reader.getString("name").toString(),
              reader.getString("type").toString());
      adapter.add(newUser1);
     /*
  Log.i("Result - ", " id:" + reader.getString("id")+" name:"+reader.getString("name")+
          " type:"+reader.getString("type")+" url:"+reader.getString("url")+
          " status:"+reader.getString("status"));
     */
              } catch (JSONException e) {
                  //   e.printStackTrace();
              }
          }

          lvMain.setAdapter(adapter);



      }
  }

/*
   [{"id":1,"name":"Driving Licence","type":"DRIVER","url":null,"status":null}
   ,{"id":2,"name":"Bank Passbook","type":"DRIVER","url":null,"status":null},
    {"id":3,"name":"Joining Form","type":"DRIVER","url":null,"status":null},
    {"id":4,"name":"Work Permit","type":"DRIVER","url":null,"status":null},
    {"id":5,"name":"Registration Certificate","type":"VEHICLE","url":null,"status":null},
    {"id":6,"name":"Insurance Certificate","type":"VEHICLE","url":null,"status":null},
    {"id":7,"name":"Fitness Certificate","type":"VEHICLE","url":null,"status":null}]
*/




    public class OkHttpHandlerThree extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Log.i("download_file", " - " +params[0]+" - "+params[1]+" - "+params[2]+" "+asses_token);
            File file = new File(params[0]);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                   .addFormDataPart("document", params[1],
                            RequestBody.create(MediaType.parse("image/jpeg"), file))
                    .build();


            Request request = new Request.Builder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Bearer "+asses_token)
                    .header("X-Requested-With", "XMLHttpRequest")
                    .url(User.getApi_documents()+"/3")//+params[2])
                //    .url(User.getApi_documents())
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return   response.body().string();
                //   Log.i("SQL_result_to_server", " - " + response.body().string());
                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

  Log.i("Result_Update_Document", " " + s);

      /*      OkHttpHandlerTwo okHttpHandlerTwo = new OkHttpHandlerTwo();
            okHttpHandlerTwo.execute();
    */    }
    }
/*
    I/download_file:  - /storage/emulated/0/DCIM/Camera/20190803_150652.jpg - 20190803_150652.jpg - 1 eyJ0eXAiOiJKV1QiLCJhbGciOiJIU

*/

/*
 I/Result_Update_Document:
 {"message":"The given data was invalid.",
 "errors":{"document":["The document must be a file of type: jpg, jpeg, png, pdf."]}}
*/
}
