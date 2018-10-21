package com.edumaite.adam.edumiate_poc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edumaite.adam.edumiate_poc.MainActivity;
import com.edumaite.adam.edumiate_poc.R;
import com.edumaite.adam.edumiate_poc.db.AppViewModel;
import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    Context context;
    private final LayoutInflater mInflater;
    private List<App> mApps; // Cached copy of words
    public AppViewModel mAppViewModel;
    public String user;

    public AppAdapter(Context context, AppViewModel mAppViewModel, String user) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mAppViewModel = mAppViewModel;
        this.user = user;

    }

    public interface ICheckChangeListener {
        void onItemChecked(int position, boolean value);
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        private final TextView wordItemView;
        private final SwitchCompat appToggle;
        private final ImageView appImage;
        private ICheckChangeListener iCheckChangeListener;


        private AppViewHolder(View itemView) {
            super(itemView);
            appImage = itemView.findViewById(R.id.app_icon);
            appToggle = itemView.findViewById(R.id.app_switch);
            wordItemView = itemView.findViewById(R.id.app_name);

            if(user == "student"){
                appToggle.setVisibility(View.INVISIBLE);
            } else {

                appToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.i("adam", "onCheckChanged call. Vars: " + buttonView + ", " + isChecked);
                        if (iCheckChangeListener != null) {
                            iCheckChangeListener.onItemChecked(getAdapterPosition(), isChecked);
                        }


                    }
                });
            }
        }

        void setICheckChangeListener(ICheckChangeListener iCheckChangeListener) {
            this.iCheckChangeListener = iCheckChangeListener;
        }

    }


    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        App current = mApps.get(position);
        if (mApps != null) {
            holder.wordItemView.setText(current.getApp());

            // Set blacklisted for app
            holder.appToggle.setChecked(current.blacklisted);


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

        holder.setICheckChangeListener(new ICheckChangeListener() {
            @Override
            public void onItemChecked(int position, boolean value) {
                Log.i("adam", "onBindViewHolder.onItemChecked called");
                mApps.get(position).setBlacklisted(value);
            }
        });


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

    public List<App> getApps(){
        return mApps;
    }


}