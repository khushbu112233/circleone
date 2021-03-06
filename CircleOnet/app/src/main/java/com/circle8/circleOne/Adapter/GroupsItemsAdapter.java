package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupDetailModel;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 9/11/2017.
 */

public class GroupsItemsAdapter extends BaseAdapter
{
    Context context ;
    ArrayList<GroupModel> groupModelsList ;

    LoginSession loginSession;
    String profileId = "", groupID = "";

    ArrayList<GroupDetailModel> groupDetailModelArrayList = new ArrayList<>();

    public GroupsItemsAdapter(Context applicationContext, ArrayList<GroupModel> groupName)
    {
        this.context = applicationContext ;
        this.groupModelsList = groupName ;
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

    static class ViewHolder
    {
        CircleImageView imgGroup, imgProfile1, imgProfile2, imgProfile3 ;
        TextView tvGroupName, tvPersonName1, tvPersonName2, tvPersonName3 ;
        TextView tvDesignation1, tvDesignation2, tvDesignation3 ;
        TextView tvDetail1, tvDetail2, tvDetail3 ;
        RelativeLayout llOneUser, llSecondUser, llThirdUser ;
        View Line1, Line2 ;
        TextView tvMemberInfo ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder = null;

        if( row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.groups_items, null);
            holder = new ViewHolder();

            holder.imgGroup = (CircleImageView)row.findViewById(R.id.imgGroup);
            holder.imgProfile1 = (CircleImageView)row.findViewById(R.id.imgProfile1);
            holder.imgProfile2 = (CircleImageView)row.findViewById(R.id.imgProfile2);
            holder.imgProfile3 = (CircleImageView)row.findViewById(R.id.imgProfile3);

            holder.tvGroupName = (TextView)row.findViewById(R.id.tvGroupName);
            holder.tvPersonName1 = (TextView)row.findViewById(R.id.tvPersonName1);
            holder.tvPersonName2 = (TextView)row.findViewById(R.id.tvPersonName2);
            holder.tvPersonName3 = (TextView)row.findViewById(R.id.tvPersonName3);

            holder.tvDesignation1 = (TextView)row.findViewById(R.id.tvDesignation1);
            holder.tvDesignation2 = (TextView)row.findViewById(R.id.tvDesignation2);
            holder.tvDesignation3 = (TextView)row.findViewById(R.id.tvDesignation3);

            holder.tvDetail1 = (TextView)row.findViewById(R.id.tvPersonDetail1);
            holder.tvDetail2 = (TextView)row.findViewById(R.id.tvPersonDetail2);
            holder.tvDetail3 = (TextView)row.findViewById(R.id.tvPersonDetail3);

            holder.llOneUser = (RelativeLayout) row.findViewById(R.id.llOneUser);
            holder.llSecondUser = (RelativeLayout)row.findViewById(R.id.llSecondUser);
            holder.llThirdUser = (RelativeLayout)row.findViewById(R.id.llThirdUser);

            holder.Line1 = (View)row.findViewById(R.id.Line1);
            holder.Line2 = (View)row.findViewById(R.id.Line2);

            holder.tvMemberInfo = (TextView)row.findViewById(R.id.tvMemberInfo);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

            loginSession = new LoginSession(context);
            HashMap<String, String> user = loginSession.getUserDetails();
            profileId = user.get(LoginSession.KEY_PROFILEID);
            groupID = groupModelsList.get(position).getGroup_ID();

            //set values here!
             holder.tvGroupName.setText(groupModelsList.get(position).getGroup_Name());

            if (groupModelsList.get(position).getGroup_Photo().equals(""))
            {
                holder.imgGroup.setImageResource(R.drawable.usr_1);
            }
            else
            {
                Picasso.with(context).load(Utility.BASE_IMAGE_URL+"Group/"+groupModelsList.get(position).getGroup_Photo())
                        .skipMemoryCache().placeholder(R.drawable.usr_1).into(holder.imgGroup);
            }

            /*tvPersonName1.setText(groupModelsList.get(position).getFirstName1()+" "+groupModelsList.get(position).getLastName1());
            tvPersonName2.setText(groupModelsList.get(position).getFirstName2()+" "+groupModelsList.get(position).getLastName2());
            tvPersonName3.setText(groupModelsList.get(position).getFirstName3()+" "+groupModelsList.get(position).getLastName3());

            tvDesignation1.setText(groupModelsList.get(position).getDesignation1());
            tvDesignation2.setText(groupModelsList.get(position).getDesignation2());
            tvDesignation3.setText(groupModelsList.get(position).getDesignation3());

            String address1 = groupModelsList.get(position).getAddress1();
            String company1 = groupModelsList.get(position).getCompanyName1();
            String website1 = groupModelsList.get(position).getWebsite1();
            String email1 = groupModelsList.get(position).getEmail1();
            String phone1 = groupModelsList.get(position).getPhone1();
            tvDetail1.setText(company1);

            String address2 = groupModelsList.get(position).getAddress2();
            String company2 = groupModelsList.get(position).getCompanyName2();
            String website2 = groupModelsList.get(position).getWebsite2();
            String email2 = groupModelsList.get(position).getEmail2();
            String phone2 = groupModelsList.get(position).getPhone2();
            tvDetail2.setText(company2);

            String address3 = groupModelsList.get(position).getAddress3();
            String company3 = groupModelsList.get(position).getCompanyName3();
            String website3 = groupModelsList.get(position).getWebsite3();
            String email3 = groupModelsList.get(position).getEmail3();
            String phone3 = groupModelsList.get(position).getPhone3();
            tvDetail3.setText(company3);*/

            if (groupModelsList.get(position).getMemberArrays().equals("3"))
            {
                if (groupModelsList.get(position).getProfileId1().isEmpty())
                {
                    holder.llOneUser.setVisibility(View.GONE);
                    holder.Line1.setVisibility(View.GONE);
                }
                else
                {
                    if (groupModelsList.get(position).getUserPhoto1().isEmpty())
                    {
                        holder.imgProfile1.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + groupModelsList.get(position).getUserPhoto1())
                                .skipMemoryCache().into(holder.imgProfile1);
                    }
                    holder.tvPersonName1.setText(groupModelsList.get(position).getFirstName1()+" "+groupModelsList.get(position).getLastName1());
                    holder.tvDesignation1.setText(groupModelsList.get(position).getDesignation1());
                    String address1 = groupModelsList.get(position).getAddress1();
                    String company1 = groupModelsList.get(position).getCompanyName1();
                    String website1 = groupModelsList.get(position).getWebsite1();
                    String email1 = groupModelsList.get(position).getEmail1();
                    String phone1 = groupModelsList.get(position).getPhone1();
                    holder.tvDetail1.setText(company1);
                }
                if (groupModelsList.get(position).getProfileId2().isEmpty())
                {
                    holder.llSecondUser.setVisibility(View.GONE);
                    holder.Line2.setVisibility(View.GONE);
                }
                else
                {
                    if (groupModelsList.get(position).getUserPhoto2().isEmpty())
                    {
                        holder.imgProfile2.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + groupModelsList.get(position).getUserPhoto2())
                                .skipMemoryCache().into(holder.imgProfile2);
                    }
                    holder.tvPersonName2.setText(groupModelsList.get(position).getFirstName2()+" "+groupModelsList.get(position).getLastName2());
                    holder.tvDesignation2.setText(groupModelsList.get(position).getDesignation2());
                    String address2 = groupModelsList.get(position).getAddress2();
                    String company2 = groupModelsList.get(position).getCompanyName2();
                    String website2 = groupModelsList.get(position).getWebsite2();
                    String email2 = groupModelsList.get(position).getEmail2();
                    String phone2 = groupModelsList.get(position).getPhone2();
                    holder.tvDetail2.setText(company2);
                }
                if (groupModelsList.get(position).getProfileId3().isEmpty())
                {
                    holder.llThirdUser.setVisibility(View.GONE);
                    holder.Line2.setVisibility(View.GONE);
                }
                else
                {
                    if (groupModelsList.get(position).getUserPhoto3().isEmpty())
                    {
                        holder.imgProfile3.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + groupModelsList.get(position).getUserPhoto3())
                                .skipMemoryCache().into(holder.imgProfile3);
                    }
                    holder.tvPersonName3.setText(groupModelsList.get(position).getFirstName3()+" "+groupModelsList.get(position).getLastName3());
                    holder.tvDesignation3.setText(groupModelsList.get(position).getDesignation3());
                    String address3 = groupModelsList.get(position).getAddress3();
                    String company3 = groupModelsList.get(position).getCompanyName3();
                    String website3 = groupModelsList.get(position).getWebsite3();
                    String email3 = groupModelsList.get(position).getEmail3();
                    String phone3 = groupModelsList.get(position).getPhone3();
                    holder.tvDetail3.setText(company3);
                }
            }
            else if (groupModelsList.get(position).getMemberArrays().equals("2"))
            {
                if (groupModelsList.get(position).getProfileId1().isEmpty())
                {
                    holder.llOneUser.setVisibility(View.GONE);
                    holder.Line1.setVisibility(View.GONE);
                }
                else
                {
                    if (groupModelsList.get(position).getUserPhoto1().isEmpty())
                    {
                        holder.imgProfile1.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + groupModelsList.get(position).getUserPhoto1())
                                .skipMemoryCache().into(holder.imgProfile1);
                    }
                    holder.tvPersonName1.setText(groupModelsList.get(position).getFirstName1()+" "+groupModelsList.get(position).getLastName1());
                    holder.tvDesignation1.setText(groupModelsList.get(position).getDesignation1());
                    String address1 = groupModelsList.get(position).getAddress1();
                    String company1 = groupModelsList.get(position).getCompanyName1();
                    String website1 = groupModelsList.get(position).getWebsite1();
                    String email1 = groupModelsList.get(position).getEmail1();
                    String phone1 = groupModelsList.get(position).getPhone1();
                    holder.tvDetail1.setText(company1);
                }
                if (groupModelsList.get(position).getProfileId2().isEmpty())
                {
                    holder.llSecondUser.setVisibility(View.GONE);
                    holder.Line2.setVisibility(View.GONE);
                }
                else
                {
                    if (groupModelsList.get(position).getUserPhoto2().isEmpty())
                    {
                        holder.imgProfile2.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + groupModelsList.get(position).getUserPhoto2())
                                .skipMemoryCache().into(holder.imgProfile2);
                    }
                    holder.tvPersonName2.setText(groupModelsList.get(position).getFirstName2()+" "+groupModelsList.get(position).getLastName2());
                    holder.tvDesignation2.setText(groupModelsList.get(position).getDesignation2());
                    String address2 = groupModelsList.get(position).getAddress2();
                    String company2 = groupModelsList.get(position).getCompanyName2();
                    String website2 = groupModelsList.get(position).getWebsite2();
                    String email2 = groupModelsList.get(position).getEmail2();
                    String phone2 = groupModelsList.get(position).getPhone2();
                    holder.tvDetail2.setText(company2);
                }

                holder.llThirdUser.setVisibility(View.GONE);
            }
            else if (groupModelsList.get(position).getMemberArrays().equals("1"))
            {
                if (groupModelsList.get(position).getProfileId1().isEmpty())
                {
                    holder.llOneUser.setVisibility(View.GONE);
                    holder.Line1.setVisibility(View.GONE);
                }
                else
                {
                    if (groupModelsList.get(position).getUserPhoto1().isEmpty())
                    {
                        holder.imgProfile1.setImageResource(R.drawable.usr_1);
                    }
                    else
                    {
                        Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + groupModelsList.get(position).getUserPhoto1())
                                .skipMemoryCache().into(holder.imgProfile1);
                    }
                    holder.tvPersonName1.setText(groupModelsList.get(position).getFirstName1()+" "+groupModelsList.get(position).getLastName1());
                    holder.tvDesignation1.setText(groupModelsList.get(position).getDesignation1());
                    String address1 = groupModelsList.get(position).getAddress1();
                    String company1 = groupModelsList.get(position).getCompanyName1();
                    String website1 = groupModelsList.get(position).getWebsite1();
                    String email1 = groupModelsList.get(position).getEmail1();
                    String phone1 = groupModelsList.get(position).getPhone1();
                    holder.tvDetail1.setText(company1);
                }

                holder.Line1.setVisibility(View.GONE);
                holder.llSecondUser.setVisibility(View.GONE);
                holder.Line2.setVisibility(View.GONE);
                holder.llThirdUser.setVisibility(View.GONE);
            }
            else if (groupModelsList.get(position).getMemberArrays().equals("0"))
            {
                holder.tvMemberInfo.setVisibility(View.VISIBLE);
                holder.llOneUser.setVisibility(View.GONE);
                holder.llSecondUser.setVisibility(View.GONE);
                holder.llThirdUser.setVisibility(View.GONE);
                holder.Line1.setVisibility(View.GONE);
                holder.Line2.setVisibility(View.GONE);
            }
            else
            {

            }

//            new HttpAsyncTaskGroup().execute("http://circle8.asia:8999/Onet.svc/Group/FetchConnection");
        return row;
    }

   /* private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            dialog = new ProgressDialog(context);
//            dialog.setMessage("Fetching Groups...");
//            dialog.show();
//            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                    String count = jsonObject.getString("count");

                    JSONArray jsonArray = jsonObject.getJSONArray("connection");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        llOneUser.setVisibility(View.GONE);
                        llSecondUser.setVisibility(View.GONE);
                        llThirdUser.setVisibility(View.GONE);
                        Line1.setVisibility(View.GONE);
                        Line2.setVisibility(View.GONE);
                    }
                    else if (jsonArray.length() == 1)
                    {
                        llSecondUser.setVisibility(View.GONE);
                        llThirdUser.setVisibility(View.GONE);
                        Line1.setVisibility(View.GONE);
                        Line2.setVisibility(View.GONE);
                    }
                    else if (jsonArray.length() == 2)
                    {
                        llThirdUser.setVisibility(View.GONE);
                        Line2.setVisibility(View.GONE);
                    }

                    // for OneUserData..
                    JSONObject object0 = jsonArray.getJSONObject(0);
                    if(object0.length() == 0)
                    {
                        llOneUser.setVisibility(View.GONE);
                    }
                    else
                    {
                        String profile = object0.getString("UserPhoto");
                        String name = object0.getString("FirstName")+" "+object0.getString("LastName") ;
                        String designation = object0.getString("Designation");
                        String address = object0.getString("Address1")+" "+object0.getString("Address2")+" "+
                                object0.getString("Address3")+" "+object0.getString("Address4")+" "+
                                object0.getString("City")+" "+object0.getString("State")+" "+
                                object0.getString("Country")+" "+object0.getString("Postalcode");
                        String company = object0.getString("CompanyName");
                        String email = object0.getString("UserName");
                        String website = object0.getString("Website");
                        String phone = object0.getString("Phone");

                        if (profile.equals(""))
                        {
                            imgProfile1.setImageResource(R.drawable.usr_1);
                        }
                        else
                        {
                            Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+profile).placeholder(R.drawable.usr_1).into(imgGroup);
                        }

                        if(name.equals(""))
                        {
                            tvPersonName1.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvPersonName1.setText(name);
                        }
                        if(designation.equals(""))
                        {
                            tvDesignation1.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvDesignation1.setText(designation);
                        }
                        tvDetail1.setText(address+"\n"+company+"\n"+email+"\n"+website+"\n"+phone);
                    }

                    // for SecondUserData..
                    JSONObject object1 = jsonArray.getJSONObject(1);
                    if(object1.length() == 0)
                    {
                        llSecondUser.setVisibility(View.GONE);
                    }
                    else
                    {
                        String profile = object1.getString("UserPhoto");
                        String name = object1.getString("FirstName")+" "+object1.getString("LastName") ;
                        String designation = object1.getString("Designation");
                        String address = object1.getString("Address1")+" "+object1.getString("Address2")+" "+
                                object1.getString("Address3")+" "+object1.getString("Address4")+" "+
                                object1.getString("City")+" "+object1.getString("State")+" "+
                                object1.getString("Country")+" "+object1.getString("Postalcode");
                        String company = object1.getString("CompanyName");
                        String email = object1.getString("UserName");
                        String website = object1.getString("Website");
                        String phone = object1.getString("Phone");

                        if (profile.equals(""))
                        {
                            imgProfile1.setImageResource(R.drawable.usr_1);
                        }
                        else
                        {
                            Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+profile).placeholder(R.drawable.usr_1).into(imgGroup);
                        }

                        if(name.equals(""))
                        {
                            tvPersonName1.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvPersonName1.setText(name);
                        }
                        if(designation.equals(""))
                        {
                            tvDesignation1.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvDesignation1.setText(designation);
                        }
                        tvDetail1.setText(address+"\n"+company+"\n"+email+"\n"+website+"\n"+phone);
                    }

                    // for SecondUserData..
                    JSONObject object2 = jsonArray.getJSONObject(1);
                    if(object2.length() == 0)
                    {
                        llSecondUser.setVisibility(View.GONE);
                    }
                    else
                    {
                        String profile = object2.getString("UserPhoto");
                        String name = object2.getString("FirstName")+" "+object2.getString("LastName") ;
                        String designation = object2.getString("Designation");
                        String address = object2.getString("Address1")+" "+object2.getString("Address2")+" "+
                                object2.getString("Address3")+" "+object2.getString("Address4")+" "+
                                object2.getString("City")+" "+object2.getString("State")+" "+
                                object2.getString("Country")+" "+object2.getString("Postalcode");
                        String company = object2.getString("CompanyName");
                        String email = object2.getString("UserName");
                        String website = object2.getString("Website");
                        String phone = object2.getString("Phone");

                        if (profile.equals(""))
                        {
                            imgProfile1.setImageResource(R.drawable.usr_1);
                        }
                        else
                        {
                            Picasso.with(context).load("http://circle8.asia/App_ImgLib/UserProfile/"+profile).placeholder(R.drawable.usr_1).into(imgGroup);
                        }

                        if(name.equals(""))
                        {
                            tvPersonName1.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvPersonName1.setText(name);
                        }
                        if(designation.equals(""))
                        {
                            tvDesignation1.setVisibility(View.GONE);
                        }
                        else
                        {
                            tvDesignation1.setText(designation);
                        }
                        tvDetail1.setText(address+"\n"+company+"\n"+email+"\n"+website+"\n"+phone);
                    }


                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        if(i < 3)
                        {
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
                        //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();

                    }
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(context, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String POST(String url)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("group_ID", groupID);
            jsonObject.accumulate("profileId", profileId);
            jsonObject.accumulate("numofrecords", "50");
            jsonObject.accumulate("pageno", "1");

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

 */
}
