package com.example.zhoukaoerlx;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by 索园 on 2017/10/16.
 */

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils;
    private final OkHttpClient client;
    private Handler handler=new Handler(Looper.getMainLooper());
    private OkHttpUtils(){
        client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor()).build();
    }

    public static OkHttpUtils getInstance(){
         if(okHttpUtils==null){
           synchronized (OkHttpUtils.class){
               if (okHttpUtils==null){
                   okHttpUtils=new OkHttpUtils();
               }
           }
         }
         return okHttpUtils;
    }
    //?method=baidu.ting.billboard.billList&type=1&size=10&offset=0
    public <T> void doGet(String url, Map<String,String> params, final Class<T> clazz, final OnNetListener<T> onNetListener){
        params.put("method","baidu.ting.billboard.billList");
        params.put("size","10");
        params.put("offset","0");
        StringBuilder sb=new StringBuilder();
        sb.append(url);
        sb.append("?");
        for(Map.Entry<String,String> entry:params.entrySet()){
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");

        }
        url=sb.toString();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {

         }

         @Override
         public void onResponse(Call call, final Response response) throws IOException {
             final String string= response.body().string();
             handler.post(new Runnable() {
                 @Override
                 public void run() {
                       T t=new Gson().fromJson(string,clazz);
                         onNetListener.onSuccess(t);
                 }
             });
         }
     });
    }
}
