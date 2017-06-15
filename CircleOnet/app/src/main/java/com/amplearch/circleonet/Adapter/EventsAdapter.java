package com.amplearch.circleonet.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.circleonet.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 06/09/2017.
 */

public class EventsAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Integer> image = new ArrayList();
    private ArrayList<String> title = new ArrayList();
    private ArrayList<String> desc = new ArrayList();

    public EventsAdapter(Context context, int layoutResourceId, ArrayList<Integer> image, ArrayList<String> title, ArrayList<String> desc) {
        super(context, layoutResourceId, title);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtEventTitle);
            holder.txtDesc = (TextView) row.findViewById(R.id.txtEventDetail);
            holder.image = (ImageView) row.findViewById(R.id.imgEvents);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.txtTitle.setText(title.get(position));
        holder.txtDesc.setText(desc.get(position));

        holder.image.setImageResource(image.get(position));
        return row;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
        ImageView image;
    }
}