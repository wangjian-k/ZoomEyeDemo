package com.simple.zoom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.volley.utils.logger.Logger;
import com.simple.zoom.R;
import com.simple.zoom.vo.HostQueryResult;

import java.util.ArrayList;

/**
 * Created by kart0l on 2016/4/20.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter {

    private static final String TAG = SearchRecyclerAdapter.class.getSimpleName();
    protected Context mContext;
    protected ArrayList<HostQueryResult.result> mData;

    public SearchRecyclerAdapter(Context context,ArrayList<HostQueryResult.result> data){
        this.mData = data;
        this.mContext = context;
        Logger.d(TAG,"size : " + mData.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout,parent,false);
        RecyclerView.ViewHolder holder = new SearchViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder searchViewHolder = (SearchViewHolder)holder;
        HostQueryResult.result info = mData.get(position);
        searchViewHolder.ip.setText(info.ip);
        if(info.portinfo != null) {
            searchViewHolder.banner.setText(info.portinfo.banner);
            searchViewHolder.port.setText(info.portinfo.port + "");
            searchViewHolder.product.setText(info.portinfo.product);
        }

        if(info.geoinfo != null && info.geoinfo.country != null && info.geoinfo.country.names != null) {
            searchViewHolder.country.setText(info.geoinfo.country.names.en);
        }
        searchViewHolder.timestamp.setText(info.timestamp);

    }

    @Override
    public int getItemCount() {
        if(mData != null) {
            return mData.size();
        }
        return 0;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView ip;
        public TextView country;
        public TextView port;  //地区
        public TextView product;  //星座
        public TextView timestamp;  //职业
        public TextView banner;

        public SearchViewHolder(View view) {
            super(view);
            ip = (TextView)view.findViewById(R.id.ip);
            country = (TextView)view.findViewById(R.id.country);
            port = (TextView)view.findViewById(R.id.port);
            product = (TextView)view.findViewById(R.id.product);
            timestamp = (TextView)view.findViewById(R.id.timestamp);
            banner = (TextView)view.findViewById(R.id.banner);
        }
    }
}
