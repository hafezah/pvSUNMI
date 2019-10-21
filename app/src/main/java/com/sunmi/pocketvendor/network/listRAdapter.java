package com.sunmi.pocketvendor.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunmi.pocketvendor.R;

public class listRAdapter extends BaseAdapter {

    private static String[] result;
    private LayoutInflater inflater;

    public listRAdapter (Context context, String[] listing){
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
        ViewHolderR holderR;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_listright, null);
            holderR = new ViewHolderR();
            holderR.title = convertView.findViewById(R.id.tv_title);
            holderR.content = convertView.findViewById(R.id.tv_content);

            convertView.setTag(holderR);
        } else {
            holderR = (ViewHolderR) convertView.getTag();
        }

        if ((position % 2) == 0){
            //even
            holderR.title.setVisibility(View.GONE);
            holderR.content.setVisibility(View.VISIBLE);
            holderR.content.setText(result[position]);

        } else {
            //odd
            holderR.title.setVisibility(View.VISIBLE);
            holderR.content.setVisibility(View.GONE);
            holderR.title.setText(result[position]);

        }

        return convertView;
    }

    public class ViewHolderR{
        TextView title, content;
    }
}
