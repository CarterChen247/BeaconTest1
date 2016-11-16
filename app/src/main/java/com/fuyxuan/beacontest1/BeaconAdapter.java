package com.fuyxuan.beacontest1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Allison on 2016/11/16.
 */

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.ViewHolder> {
    private List<String> mData;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_beacon_info_item_beacon);
        }
    }

    public BeaconAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public BeaconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beacon, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
