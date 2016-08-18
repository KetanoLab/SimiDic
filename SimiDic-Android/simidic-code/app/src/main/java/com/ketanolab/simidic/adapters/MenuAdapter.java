package com.ketanolab.simidic.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ketanolab.simidic.Constants.EMenuItems;
import com.ketanolab.simidic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pteran on 28-03-16.
 */
//UsersAssigmentAdapter extends RecyclerView.Adapter<UsersAssigmentAdapter.ViewHolder>
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<EMenuItems> mItems;
    public MenuAdapter(){
        mItems = new ArrayList<>();
        for(EMenuItems item : EMenuItems.values()){
            mItems.add(item);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(position);
        holder.text.setText(mItems.get(position).getText());
        holder.icon.setImageResource(mItems.get(position).getIcon());
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;
        public ViewHolder(View v) {
            super(v);
            icon  = (ImageView) v.findViewById(R.id.list_main_menu_icon);
            text = (TextView) v.findViewById(R.id.list_main_menu_text);
        }

        public void bindView(int position){

        }
    }
   }

