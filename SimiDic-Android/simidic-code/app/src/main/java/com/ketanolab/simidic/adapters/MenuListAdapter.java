package com.ketanolab.simidic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ketanolab.simidic.Constants.EMenuItems;
import com.ketanolab.simidic.R;


/**
 * Created by pteran on 05-02-16.
 */
public class MenuListAdapter extends BaseAdapter{
    private Context mCtx;
    public MenuListAdapter(Context ctx){
        this.mCtx = ctx;
    }
    @Override
    public int getCount() {
        int count = EMenuItems.values().length;
        return count;
    }

    @Override
    public Object getItem(int position) {
        return EMenuItems.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return  EMenuItems.values()[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.menu_list_item,null);
            holder.icon  = (ImageView) convertView.findViewById(R.id.list_main_menu_icon);
            holder.text = (TextView) convertView.findViewById(R.id.list_main_menu_text);
            convertView.setTag(holder);
        } else
            holder=(ViewHolder)convertView.getTag();

        holder.icon.setImageResource(EMenuItems.values()[position].getIcon());
        holder.text.setText(EMenuItems.values()[position].getText());
        return convertView;
    }


    static class ViewHolder {
         ImageView icon;
         TextView text;

    }
}
