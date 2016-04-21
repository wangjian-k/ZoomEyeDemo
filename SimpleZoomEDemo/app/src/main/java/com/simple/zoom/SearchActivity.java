package com.simple.zoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.base.volley.network.HttpRequestManager;
import com.base.volley.network.IHttpRequest;
import com.base.volley.utils.logger.Logger;
import com.simple.zoom.adapter.SearchRecyclerAdapter;
import com.simple.zoom.vo.HostQueryResult;
import com.simple.zoom.widget.VerticalDecoration;

import java.util.ArrayList;

/**
 * Created by kart0l on 2016/4/20.
 */
public class SearchActivity extends Activity {

    private final String TAG = SearchActivity.class.getSimpleName();
    private Spinner typeSpinner;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView mSearchContentView;
    private View mLoadingView;
    private Button searchButton;

    private RecyclerView mRecyclerView;
    private SearchRecyclerAdapter recyclerAdapter;

    private ArrayList<String> searchTypeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mLoadingView = findViewById(R.id.search_loading);
        typeSpinner = (Spinner) findViewById(R.id.spinner);
        mSearchContentView = (AutoCompleteTextView) findViewById(R.id.search_content);
        searchButton = (Button) findViewById(R.id.search_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayout.VERTICAL);//默认是LinearLayout.VERTICAL
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration decoration = new VerticalDecoration(getApplicationContext(),R.drawable.recycleview_vertical_divider_bg);
        mRecyclerView.addItemDecoration(decoration);

        searchTypeList.add("主机");
        searchTypeList.add("网站");

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, searchTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
//        typeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                /* 将所选typeSpinner 的值带入myTextView 中*/
//                myTextView.setText("您选择的是："+ adapter.getItem(arg2));
//                /* 将typeSpinner 显示*/
//                arg0.setVisibility(View.VISIBLE);
//            }
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//                myTextView.setText("NONE");
//                arg0.setVisibility(View.VISIBLE);
//            }
//        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        typeSpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub    
                /**
                 *
                 */
                return false;
            }
        });    
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        typeSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub    

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for a valid email address.
                if (TextUtils.isEmpty(mSearchContentView.getText().toString())) {
                    mSearchContentView.setError(getString(R.string.error_field_required));
                    return;
                }

                showLoading(true);
                NetworkUtils.searchWithZoomEye(searchTypeList.get(typeSpinner.getSelectedItemPosition()),
                        mSearchContentView.getText().toString(), 1, "", new IHttpRequest.IHttpRequestCallBack<HostQueryResult>() {
                            @Override
                            public void onSuccess(HttpRequestManager<HostQueryResult> httpRequestManager) {
                                Logger.d(TAG,"success : " + httpRequestManager.getDataString());
                                HostQueryResult result = httpRequestManager.getDataObject();
                                if(result != null && result.matches != null && result.matches.size() > 0) {
                                    int size = result.matches.size();
                                    for(int i=0;i<size;i++) {
                                        HostQueryResult.result infoResult = result.matches.get(i);
                                        Logger.d(TAG,"ip : " + infoResult.ip);
                                    }
                                    mRecyclerView.removeAllViews();
                                    recyclerAdapter = null;
                                    recyclerAdapter = new SearchRecyclerAdapter(SearchActivity.this,result.matches);
                                    mRecyclerView.setAdapter(recyclerAdapter);
                                }
                            }

                            @Override
                            public void onFailed(String s) {
                                Logger.e(TAG,"search error : " + s);
                            }
                        });
                showLoading(false);
            }
        });
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showLoading(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
