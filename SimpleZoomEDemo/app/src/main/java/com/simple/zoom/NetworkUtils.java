package com.simple.zoom;

import com.base.volley.Request;
import com.base.volley.Response;
import com.base.volley.network.HttpIntent;
import com.base.volley.network.HttpRequestManager;
import com.base.volley.network.IHttpRequest;
import com.base.volley.toolbox.JsonObjectRequest;
import com.base.volley.toolbox.JsonRequest;
import com.base.volley.utils.logger.Logger;
import com.simple.zoom.vo.HostQueryResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kart0l on 2016/4/20.
 */
public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String LOGIN_ADDR = "http://api.zoomeye.org/user/login";

    public static final String HOSTDOMAIN = "http://api.zoomeye.org/";
    public static final String SEARCH_WITH_QUERY = "/search?query=";
    public static final String WEB = "web";
    public static final String HOST = "host";
    public static final String AUTH_JWT = "JWT ";

    public static void login(String username, String password, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        JSONObject jsonObject = new JSONObject(map);
        HttpRequestManager requestManager = new HttpRequestManager();
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_ADDR, jsonObject,
                listener,errorListener){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        requestManager.request(jsonRequest,2);
    }

    public static void searchWithZoomEye(String type,String searchContent,int page,String facets,
                                         IHttpRequest.IHttpRequestCallBack<HostQueryResult> callback) {
        Logger.d(TAG,"searchWithZoomEye, type : " + type + ", content : " + searchContent);
        String typeString = "";
        if(type.equals("主机")) {
            typeString = HOST;
            //因为host和web两种方式返回的数据结果不同，所以暂且停用web方式，后续时间足够时再考虑添加web对应的数据结构。
//        } else {
//            typeString = WEB;
        }
        String url = new StringBuilder(HOSTDOMAIN + typeString + SEARCH_WITH_QUERY + searchContent).toString();

        HttpRequestManager<HostQueryResult> httpRequestTask = new HttpRequestManager<HostQueryResult>();
        httpRequestTask.setRetryTimes(2);
        httpRequestTask.setUseEtagCache(false);
        HttpIntent httpIntent = new HttpIntent(url, false);
        httpIntent.setHeader("Authorization", new StringBuilder(AUTH_JWT + ZoomEApplication.getAccess_token()).toString());


        httpRequestTask.request(httpIntent, callback,HostQueryResult.class);
    }
}
