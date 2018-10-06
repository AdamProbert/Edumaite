package com.edumaite.adam.edumiate_poc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edumaite.adam.edumiate_poc.R;
import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    class AppViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private AppViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<App> mApps; // Cached copy of words

    public AppAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        if (mApps != null) {
            App current = mApps.get(position);
            holder.wordItemView.setText(current.getApp());
        } else {
            // Covers the case of data not being ready yet.
        holder.wordItemView.setText("No Apps");
        }
    }

    public void setApps(List<App> apps){
        Log.i("adam", "SetApps called from AppAdapter");
        mApps = apps;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mApps != null)
            return mApps.size();
        else return 0;
    }
}