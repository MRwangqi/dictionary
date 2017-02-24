package com.dictionary.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/2/22.
 */

public class HttpUtls {

    public static final String WORDS = "https://api.shanbay.com/bdc/search/?word=";

    public static final String YOUDAO = "http://fanyi.youdao.com/openapi.do?keyfrom=dictionary3&key=973644381&type=data&doctype=json&version=1.1&q=";

    public static String getResult(String path) {
        String result = "";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String len = "";

            while ((len = br.readLine()) != null) {
                result += len;
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
