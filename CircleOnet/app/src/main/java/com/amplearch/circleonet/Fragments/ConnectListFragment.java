package com.amplearch.circleonet.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amplearch.circleonet.Activity.CardDetail;
import com.amplearch.circleonet.Activity.ConnectActivity;
import com.amplearch.circleonet.Adapter.List4Adapter;
import com.amplearch.circleonet.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectListFragment extends Fragment {

    private ListView listView;
    private List4Adapter gridAdapter;
    public ConnectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect_list, container, false);

        ArrayList<Integer> image = new ArrayList<Integer>();
        image.add(R.drawable.profile1);
        image.add(R.drawable.profile2);
        image.add(R.drawable.profile3);
        image.add(R.drawable.profile4);
        image.add(R.drawable.profile5);

        ArrayList<String> name = new ArrayList<>();
        name.add("Physician Yong");
        name.add("Justin Yuan fel");
        name.add("Physician Yong");
        name.add("Justin Yuan fel");
        name.add("Physician Yong");

        ArrayList<String> desc = new ArrayList<>();
        desc.add("TCMOng Medicare pvt ltd\nTCMOng@tcmong.com.sg\nwww.tcmong.com.sg\n+65 68426188");
        desc.add("TCMOng Medicare pvt ltd\nTCMOng@tcmong.com.sg\nwww.tcmong.com.sg\n+65 68426188");
        desc.add("TCMOng Medicare pvt ltd\nTCMOng@tcmong.com.sg\nwww.tcmong.com.sg\n+65 68426188");
        desc.add("TCMOng Medicare pvt ltd\nTCMOng@tcmong.com.sg\nwww.tcmong.com.sg\n+65 68426188");
        desc.add("TCMOng Medicare pvt ltd\nTCMOng@tcmong.com.sg\nwww.tcmong.com.sg\n+65 68426188");

        ArrayList<String> designation = new ArrayList<>();
        designation.add("");
        designation.add("General Manager");
        designation.add("");
        designation.add("");
        designation.add("General Manager");

        listView = (ListView) view.findViewById(R.id.listViewType4);
        gridAdapter = new List4Adapter(getContext(), R.layout.grid_list4_layout, image, desc, name, designation);
        listView.setAdapter(gridAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ConnectActivity.class);
                getContext().startActivity(intent);
            }
        });

        return view;
    }

}
