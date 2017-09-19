package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle8.circleOne.Activity.SortAndFilterOption;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/19/2017.
 */

public class SortAndFilterAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();

    ArrayList<GroupModel> groupModelsList ;

    public SortAndFilterAdapter(SortAndFilterOption sortAndFilterOption, ArrayList<GroupModel> groupModelArrayList)
    {
        this.context = sortAndFilterOption ;
        this.groupModelsList = groupModelArrayList ;
    }

    @Override
    public int getCount() {
        return groupModelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)

            vi = inflater.inflate(R.layout.sort_filter_groups, null);

        CircleImageView groupImg = (CircleImageView)vi.findViewById(R.id.imgProfile1);
        TextView groupName = (TextView)vi.findViewById(R.id.tvPersonName1);

        groupName.setText(groupModelsList.get(position).getGroup_Name());

        if (groupModelsList.get(position).getGroup_Photo().equals(""))
        {
            groupImg.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(context).load("http://circle8.asia/App_ImgLib/Group/"+groupModelsList.get(position).getGroup_Photo()).placeholder(R.drawable.usr_1).into(groupImg);
        }

        return vi;
    }
}