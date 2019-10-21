package com.sunmi.pocketvendor.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.pocketvendor.R;

public class listLAdapter extends BaseAdapter {

    private static String[] result;
    private LayoutInflater inflater;

    public listLAdapter (Context context, String[] listing){
        result = listing;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return result[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderL holderL;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_listleft, null);
            holderL = new ViewHolderL();
            holderL.header = convertView.findViewById(R.id.tv_hstr);
            holderL.expandcollapse = convertView.findViewById(R.id.iv_expandcollapse);

            //convertView.setTag(holderL);
        } else {
            holderL = (ViewHolderL) convertView.getTag();
        }

        if (result[position].equals("Products")){
            holderL.expandcollapse.setVisibility(View.VISIBLE);
        } else {
            holderL.expandcollapse.setVisibility(View.GONE);
        }

        holderL.header.setText(result[position]);
        return convertView;
    }


    public class ViewHolderL{
        TextView header;
        ImageView expandcollapse;
    }

}
