package com.example.trucker.server;

public class User {

    private   static final String device_token="kUgdIdZdIjBybnWpTd9ASo5MA3Quk5mNaRRHeUhc";
    private static final String device_type="android";
    private static final String device_id="f950260219bbd71f";

    private static final String GENDER_MALE="MALE";
    private static final String GENDER_FEMALE="FEMALE";

    private static final String URL = "https://api.trucker.host/api/";
    private static final String api_registration = "provider/register";
    private static final String api_ovtarization = "provider/oauth/token";
    private static final String api_verify = "provider/verify";
    private static final String api_documents = "provider/documents";



    private static String path_url;

   public static String getDevice_token()
   {
       return device_token;
   }

   public static String getDevice_type()
   {
       return device_type;
   }

   public static String getDevice_id()
   {
       return device_id;
   }

   public static String getUrl()
   {
       return URL;
   }

   public static String getApi_registration(){
       path_url = null;
       path_url = getUrl();
       path_url=path_url.concat(api_registration);
       return path_url;
   }

   public static String getApi_ovtarization(){
       path_url = null;
       path_url = getUrl();
       path_url=path_url.concat(api_ovtarization);
       return path_url;
   }

   public static String getApi_verify() {
       path_url = null;
       path_url = getUrl();
       path_url=path_url.concat(api_verify);
       return path_url;
      }

    public static String getApi_documents() {
        path_url = null;
        path_url = getUrl();
        path_url=path_url.concat(api_documents);
        return path_url;
    }




}
