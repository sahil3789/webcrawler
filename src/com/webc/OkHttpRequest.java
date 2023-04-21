package com.webc;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OkHttpRequest {

    final OkHttpClient client = new OkHttpClient();

    Map<String, String> get(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {

            Map<String, String> responseData = new HashMap<>();

            responseData.put("code", String.valueOf(response.code()));
            responseData.put("body", response.body().string());

            return responseData;
        }
    }
}
