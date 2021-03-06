package com.circle8.circleOne.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.TestimonialActivity;
import com.circle8.circleOne.Activity.TestimonialRequest;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

/**
 * Created by admin on 09/01/2017.
 */

public class TestimonialRequestAdapter extends BaseSwipeAdapter
{
    public static Context context;
    private int layoutResourceId;
    Button delete;

    String user_id, testimonial_id ;
    LoginSession session;
    static String friendprofileID = "", profileID = "";
    static ViewHolder holder = null;
    //newly added

    ArrayList<TestimonialModel> testimonialModels = new ArrayList<>();

    private int posi ;

    public TestimonialRequestAdapter(Context context, int grid_list3_layout, ArrayList<TestimonialModel> testimonialModels)
    {
        this.context = context;
        this.layoutResourceId = grid_list3_layout;
        this.testimonialModels = testimonialModels;

        session = new LoginSession(context);
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        profileID = user.get(LoginSession.KEY_PROFILEID);
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.full_testimonial_row, null);

        delete = (Button) v.findViewById(R.id.delete);
        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

        testimonial_id = testimonialModels.get(position).getTestimonial_ID();

        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                //  Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                db.DeactiveCards(nfcModelList.get(position).getId());
//                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                swipeLayout.close();
                posi  = position ;
               // new HttpAsyncTask().execute("http://circle8.asia:8999/Onet.svc/Testimonial/Delete");
            }
        });

        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void fillValues(int position, View convertView)
    {
        View row = convertView;

        holder = new ViewHolder();
        holder.circleImageView = (CircleImageView) row.findViewById(R.id.imgUser);
        holder.txtName = (TextView) row.findViewById(R.id.txtName);
        holder.txtTestimonial = (TextView) row.findViewById(R.id.txtTestimonial);
        holder.txtRequest = (TextView) row.findViewById(R.id.txtRequest);
        row.setTag(holder);

        /* holder.imageDesc.setText(data.get(position));
        holder.imageName.setText(name.get(position));
        try {
            if (!designation.get(position).equalsIgnoreCase("")) {
                holder.imageDesignation.setText(designation.get(position));
            }
        }catch (Exception e){

        }
        Bitmap bmp = BitmapFactory.decodeByteArray(image.get(position), 0, image.get(position).length);
        // ImageView image = (ImageView) findViewById(R.id.imageView1);
        holder.image.setImageBitmap(bmp); */

        String name = testimonialModels.get(position).getFirstName() + " " + testimonialModels.get(position).getLastName();
        String testimonial = testimonialModels.get(position).getDesignation();
        holder.txtName.setText(name);
        holder.txtTestimonial.setText(testimonial);
        friendprofileID = testimonialModels.get(position).getFriendProfileID();
//        holder.txtRequest.setVisibility(View.VISIBLE);
        if (testimonialModels.get(position).getPurpose().equals("0")){
            holder.txtRequest.setVisibility(View.VISIBLE);
        }else {
            holder.txtRequest.setVisibility(View.GONE);
        }

        if (testimonialModels.get(position).getUserPhoto().equals(""))
        {
            holder.circleImageView.setImageResource(R.drawable.usr);
        }
        else
        {
            Picasso.with(context).load(Utility.BASE_IMAGE_URL+"UserProfile/" + testimonialModels.get(position).getUserPhoto())
                    .resize(300,300).onlyScaleDown().skipMemoryCache().into(holder.circleImageView);
        }
        //Picasso.with(context).load("http://circle8.asia/App_ImgLib/Cards/" + nfcModelList1.get(position).getCard_front()).into(holder.image);

        holder.txtRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncTaskTestimonialRequest().execute(Utility.BASE_URL+"Testimonial/Request");
            }
        });

    }


    private static class HttpAsyncTaskTestimonialRequest extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;
        private TestimonialRequestAdapter adapter;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(context);
            dialog.setMessage("Requesting...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Requesting";
            TestimonialRequest.CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST2(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            TestimonialRequest.rlProgressDialog.setVisibility(View.GONE);
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("Success");
                    String message = jsonObject.getString("Message");

                    if (success.equals("1"))
                    {
                        Toast.makeText(context, "Request sent..", Toast.LENGTH_LONG).show();
                        holder.txtRequest.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(context, "Request has not been sent..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String POST2(String url)
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
            jsonObject.accumulate("friendprofileID",  friendprofileID);
            jsonObject.accumulate("myprofileID", profileID );

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
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    @Override
    public int getCount() {
        return testimonialModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder
    {
        CircleImageView circleImageView;
        TextView txtName;
        TextView txtTestimonial;
        TextView txtRequest;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(context);
//            dialog.setMessage("Deleting Records...");
//            //dialog.setTitle("Saving Reminder");
//            dialog.show();
//            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Deleting records";
            TestimonialRequest.CustomProgressDialog(loading);

        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            TestimonialRequest.rlProgressDialog.setVisibility(View.GONE);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if(result == "")
                {
                    Toast.makeText(context, "Slow Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");

                    if(success.equals("1"))
                    {
                        Toast.makeText(context, "Delete Successfully", Toast.LENGTH_LONG).show();
                        TestimonialActivity.webCall();
                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public  String POST(String url)
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
            jsonObject.accumulate("TestimonialId", testimonial_id);
            jsonObject.accumulate("userId", user_id);

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
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }


}
