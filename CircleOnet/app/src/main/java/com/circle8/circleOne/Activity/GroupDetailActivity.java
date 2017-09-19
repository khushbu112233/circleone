package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.GroupDetailAdapter;
import com.circle8.circleOne.Adapter.GroupsItemsAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.ProfileModel;
import com.circle8.circleOne.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailActivity extends AppCompatActivity
{
    private ListView listView ;

    private CircleImageView imgProfile ;
    private ImageView ivChangeProf, ivBack, ivMenu, ivShare, ivEdit ;
    private TextView tvGroupName, tvGroupDesc, tvMemberInfo ;

    private GroupDetailAdapter groupDetailAdapter ;
    ImageView imgBack;
    private ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();

    private LoginSession session;
    private String profile_id ;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> designation = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> website = new ArrayList<>();
    private ArrayList<String> phone = new ArrayList<>();
    private ArrayList<String> mobile = new ArrayList<>();
    private ArrayList<String> address = new ArrayList<>();
    private ArrayList<String> imgprofile = new ArrayList<>();

    String group_id = "", group_Name, group_Desc, group_Img ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        listView = (ListView)findViewById(R.id.listView);
        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvGroupName = (TextView)findViewById(R.id.tvGroupName);
        tvGroupDesc = (TextView)findViewById(R.id.tvGroupPartner);
        tvMemberInfo = (TextView)findViewById(R.id.tvMemberInfo);

        ivMenu = (ImageView)findViewById(R.id.imgProfileMenu);
        ivChangeProf = (ImageView)findViewById(R.id.imgCamera);
        ivShare = (ImageView)findViewById(R.id.ivProfileShare);
        ivEdit = (ImageView)findViewById(R.id.ivEdit);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);

        Intent intent = getIntent();
        group_id = intent.getStringExtra("group_id");
        group_Name = intent.getStringExtra("groupName");
        group_Desc = intent.getStringExtra("groupDesc");
        group_Img = intent.getStringExtra("groupImg");

        tvGroupName.setText(group_Name);
        tvGroupDesc.setText(group_Desc);

        if (group_Img.equals(""))
        {
            imgProfile.setImageResource(R.drawable.usr_1);
        }
        else
        {
            Picasso.with(getApplicationContext()).load("http://circle8.asia/App_ImgLib/Group/"+group_Img).placeholder(R.drawable.usr_1).into(imgProfile);
        }

        new HttpAsyncTaskGroup().execute("http://circle8.asia:8999/Onet.svc/Group/FetchConnection");

        /*name.add("Kajal Patadia");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Jay Nagar");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Sameer Desai");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");

        name.add("Nagar Joy");
        designation.add("Software Developer");
        company.add("Ample Arch Infotech Pvt. Ltd.");
        phone.add("+79 9876 54321");
        mobile.add("+91 9876543210");
        website.add("www.circle8.com");
        email.add("ample@arch.org");
        address.add("F-507, Titanium City Center, Ahmedabad, India.");
        imgprofile.add("");
*/

      /*  groupDetailAdapter = new GroupDetailAdapter(getApplicationContext(), R.layout.group_detail_items,
                name,designation,company,website,email,phone,mobile,address,imgprofile);
        listView.setAdapter(groupDetailAdapter);
        groupDetailAdapter.notifyDataSetChanged();*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String POST4(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("group_ID", group_id);
            jsonObject.accumulate("profileId", profile_id);
            jsonObject.accumulate("pageno", "1");
            jsonObject.accumulate("numofrecords", "50");

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupDetailActivity.this);
            dialog.setMessage("Fetching Connections...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST4(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        tvMemberInfo.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvMemberInfo.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        GroupDetailModel groupDetailModel = new GroupDetailModel();
                        groupDetailModel.setFirstname(object.getString("FirstName"));
                        groupDetailModel.setLastname(object.getString("LastName"));
                        groupDetailModel.setDesignation(object.getString("Designation"));
                        groupDetailModel.setCompany(object.getString("CompanyName"));
                        groupDetailModel.setEmail(object.getString("UserName"));
                        groupDetailModel.setWebsite(object.getString("Website"));
                        groupDetailModel.setMobile(object.getString("Phone"));
                        groupDetailModel.setAddress1(object.getString("Address1"));
                        groupDetailModel.setAddress2(object.getString("Address2"));
                        groupDetailModel.setAddress3(object.getString("Address3"));
                        groupDetailModel.setAddress4(object.getString("Address4"));
                        groupDetailModel.setCity(object.getString("City"));
                        groupDetailModel.setState(object.getString("State"));
                        groupDetailModel.setCountry(object.getString("Country"));
                        groupDetailModel.setPostalcode(object.getString("Postalcode"));
                        groupDetailModel.setImgProfile(object.getString("UserPhoto"));
                        groupDetailModelArrayList.add(groupDetailModel);
                    }

                    groupDetailAdapter = new GroupDetailAdapter(getApplicationContext(), R.layout.group_detail_items, groupDetailModelArrayList);
                    listView.setAdapter(groupDetailAdapter);
                    groupDetailAdapter.notifyDataSetChanged();

                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


}
