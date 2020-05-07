//package com.slotbooker.Payment;
//
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
//
//public class payment_login {
//    OkHttpClient client = new OkHttpClient().Builder()
//            .build();
//    MediaType mediaType = MediaType.parse("application/json");
//    RequestBody body = RequestBody.create(mediaType, "{\n  \"login\": \"\",\n  \"password\": \"\"\n}");
//    Request request = new Request.Builder()
//            .url("{{Base URL}}/login")
//            .method("POST", body)
//            .addHeader("Content-Type", "application/json")
//            .build();
//    Response response = client.newCall(request).execute();
//}
