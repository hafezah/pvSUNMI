package com.sunmi.pocketvendor.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.pocketvendor.R;

public class ReportAdapter extends BaseAdapter {

    private static String[][] result;
    private LayoutInflater inflater;

    public ReportAdapter(Context context, String[][] listing){
        result = listing;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int i) {
        return result[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int rowNo, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_list_view, null);
            holder = new ViewHolder();
            holder.tv1 = convertView.findViewById(R.id.tv_1);
            holder.tv2 = convertView.findViewById(R.id.tv_2);
            holder.tv3 = convertView.findViewById(R.id.tv_3);
            holder.img = convertView.findViewById(R.id.img_4);
            holder.tv5 = convertView.findViewById(R.id.tv_5);
            holder.tv6 = convertView.findViewById(R.id.tv_6);
            holder.img2 = convertView.findViewById(R.id.img_8);

            convertView.setTag(holder);
        } else {holder = (ViewHolder) convertView.getTag();}

        holder.tv1.setText(result[rowNo][0]);
        holder.tv2.setText(result[rowNo][1]);
        //
        String refno[] = result[rowNo][6].split("-");
        //holder.tv3.setText(result[rowNo][6].substring(0, 6));
        holder.tv3.setText(refno[0]);
        //

        if (result[rowNo][3] != null){
            if (result[rowNo][3].equals("SUCCESS")){
                holder.img.setImageResource(R.drawable.success);
            }
            else {
                holder.img.setImageResource(R.drawable.unsuccess);
            }
        }
        else {
            holder.img.setImageResource(R.drawable.cross);
        }
        holder.tv5.setText(result[rowNo][2]);

        result[rowNo][4] = result[rowNo][4].replace("$", "");
        holder.tv6.setText(result[rowNo][4]);

        if (result[rowNo][13].length() > 0){
            holder.img2.setVisibility(View.VISIBLE);
        } else {
            holder.img2.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class ViewHolder{
        TextView tv1, tv2, tv3, tv5, tv6;
        ImageView img, img2;
    }

}
