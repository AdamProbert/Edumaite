package com.edumaite.adam.edumiate_poc.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.edumaite.adam.edumiate_poc.R;
import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    Context context;
    private final LayoutInflater mInflater;
    private List<App> mApps; // Cached copy of words

    public AppAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        private final TextView wordItemView;
        private final Switch appToggle;
        private final ImageView appImage;


        private AppViewHolder(View itemView) {
            super(itemView);
            appImage = itemView.findViewById(R.id.app_icon);
            appToggle = itemView.findViewById(R.id.app_switch);
            wordItemView = itemView.findViewById(R.id.app_name);
        }
    }





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

            try {
                Drawable appIcon = context.getPackageManager().getApplicationIcon(current.getAppPackage());
                holder.appImage.setImageDrawable(appIcon);
                Log.i("adam", "set app image");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log.i("adam", "couldnt set app image");
            }

        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Apps");
        }

        holder.appToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Log.i("adam", "toggle has been checked");
                } else {
                    Log.i("adam", "toggle has been unchecked");

                }
            }
        });
    }

    public void setApps(List<App> apps){
        Log.i("adam", "SetApps called form AppAdapter");
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

    public void app_toggle_handler(View v)
    {
        Log.i("adam", "app toggle handler called");
    }

}