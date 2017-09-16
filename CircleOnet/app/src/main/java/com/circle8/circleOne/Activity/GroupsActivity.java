package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.GroupAdapter;
import com.circle8.circleOne.Adapter.GroupsItemsAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.circle8.circleOne.Activity.RegisterActivity.BitMapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.ConvertBitmapToString;
import static com.circle8.circleOne.Activity.RegisterActivity.rotateImage;

public class GroupsActivity extends AppCompatActivity
{
    ArrayList<String> groupName = new ArrayList<>();
    GroupsItemsAdapter groupsItemsAdapter ;

    ListView listView ;
    private LoginSession session;
    private String user_id ;

    public static ArrayList<GroupModel> groupModelArrayList;

    ImageView imgBack;
    RelativeLayout llBottom;
    String GroupName, GroupDesc;
    private String GroupImage = "";
    CharSequence[] items ;
    private String userChoosenTask ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String image;
    CircleImageView ivGroupImage;
    String final_ImgBase64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);

        groupModelArrayList = new ArrayList<>();

        listView = (ListView)findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        llBottom = (RelativeLayout) findViewById(R.id.llBottom);

        new HttpAsyncTaskGroup().execute("http://circle8.asia:8081/Onet.svc/Group/Fetch");
       /* groupName.add("Group 1");
        groupName.add("Group 2");
        groupName.add("Group 3");

        groupsItemsAdapter = new GroupsItemsAdapter(getApplicationContext(), groupName);
        listView.setAdapter(groupsItemsAdapter);
        groupsItemsAdapter.notifyDataSetChanged();
*/
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(GroupsActivity.this);

                LinearLayout layout = new LinearLayout(GroupsActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                ivGroupImage = new CircleImageView(GroupsActivity.this);
                ivGroupImage.setBorderColor(getResources().getColor(R.color.colorPrimary));
                ivGroupImage.setBorderWidth(1);
                ivGroupImage.setImageResource(R.drawable.usr_1);
                int width=200;
                int height=200;
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
                parms.gravity = Gravity.CENTER;
                ivGroupImage.setLayoutParams(parms);
                layout.addView(ivGroupImage);

                final EditText titleBox = new EditText(GroupsActivity.this);
                titleBox.setHint("Group Name");
                layout.addView(titleBox);

                final EditText descriptionBox = new EditText(GroupsActivity.this);
                descriptionBox.setHint("Description");
                layout.addView(descriptionBox);


                ivGroupImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });
                //   dialog.setView(layout);


//text_entry is an Layout XML file containing two text field to display in alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(GroupsActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Create Group").setView(layout).setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                GroupName = titleBox.getText().toString();
                                GroupDesc = descriptionBox.getText().toString();

                                if (GroupName.equals("")){
                                    Toast.makeText(getApplicationContext(), "Enter Group Name", Toast.LENGTH_LONG).show();
                                }
                                else if (GroupDesc.equals("")){
                                    Toast.makeText(getApplicationContext(), "Enter Group Description", Toast.LENGTH_LONG).show();
                                }
                                else if (final_ImgBase64.equals("")){
                                    Toast.makeText(getApplicationContext(), "Upload Group Image", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    new HttpAsyncTaskPhotoUpload().execute("http://circle8.asia:8081/Onet.svc/ImgUpload");
                                   // new HttpAsyncTaskGroupCreate().execute("http://circle8.asia:8081/Onet.svc/Group/Create");
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
     /*
     * User clicked cancel so do some stuff
     */
                                dialog.dismiss();

                            }
                        });
                alert.show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
                intent.putExtra("group_id", groupModelArrayList.get(position).getGroup_ID());
                intent.putExtra("groupImg", groupModelArrayList.get(position).getGroup_Photo());
                intent.putExtra("groupName", groupModelArrayList.get(position).getGroup_Name());
                intent.putExtra("groupDesc", groupModelArrayList.get(position).getGroup_Desc());
                startActivity(intent);
            }
        });
    }

    public  String POST1(String url)
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
            jsonObject.accumulate("ImgBase64", final_ImgBase64 );
            jsonObject.accumulate("classification", "group" );

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

    private class HttpAsyncTaskPhotoUpload extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupsActivity.this);
            dialog.setMessage("Uploading...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POST1(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String ImgName = jsonObject.getString("ImgName").toString();
                    String success = jsonObject.getString("success").toString();

                    if (success.equals("1") && ImgName!=null) {
                        /*Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();*/
                        //   Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                        GroupImage = ImgName;
                        new HttpAsyncTaskGroupCreate().execute("http://circle8.asia:8081/Onet.svc/Group/Create");

                    } else {
                        Toast.makeText(getBaseContext(), "Error While Uploading Image..", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Not able to Register..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private void selectImage()
    {
        items = new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GroupsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkStoragePermission(GroupsActivity.this);
                boolean result1 = Utility.checkCameraPermission(GroupsActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result1)
//                        activeTakePhoto();
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
//                        activeGallery();
                        galleryIntent();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        final_ImgBase64 = BitMapToString(thumbnail);
      //  Upload();
        ivGroupImage.setImageBitmap(thumbnail);
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
//        imagepath = getPath(selectedImageUri);

        Bitmap bm = null;
        if (data != null)
        {
            Uri targetUri = data.getData();

            String photoPath = getPath(targetUri);

            ExifInterface ei = null;
            Bitmap bitmap = null;
            Bitmap rotatedBitmap = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                try
                {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    image = ConvertBitmapToString(resizedBitmap);
                    final_ImgBase64 = BitMapToString(resizedBitmap);
                    // final_ImgBase64 = resizeBase64Image(s);
                    Log.d("base64string ", final_ImgBase64);
//                  Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
                   // Upload();
                    ivGroupImage.setImageBitmap(resizedBitmap);
                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    ei = new ExifInterface(photoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                    switch (orientation)
                    {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                           // Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                           // Upload();
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                          //  Upload();
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                            ivGroupImage.setImageBitmap(rotatedBitmap);
                            final_ImgBase64 = BitMapToString(rotatedBitmap);
                          //  Upload();
                            break;
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

           /* try
            {
                ei = new ExifInterface(photoPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/

//            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

//            Bitmap bitmap = null;
//            Bitmap rotatedBitmap = null;

           /* try
            {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
//                image = ConvertBitmapToString(resizedBitmap);
//                final_ImgBase64 = BitMapToString(resizedBitmap);
               // final_ImgBase64 = resizeBase64Image(s);
                Log.d("base64string ", final_ImgBase64);
//                Toast.makeText(getApplicationContext(), final_ImgBase64, Toast.LENGTH_LONG).show();
//                Upload();
//                civProfilePic.setImageBitmap(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

/*
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
                    civProfilePic.setImageBitmap(rotatedBitmap);
                    final_ImgBase64 = BitMapToString(rotatedBitmap);
                    Upload();
            }
*/

        }
//        BmToString(bm);
    }


    public String POST(String url) {
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
            jsonObject.accumulate("GroupDesc", GroupDesc);
            jsonObject.accumulate("GroupName", GroupName);
            jsonObject.accumulate("GroupPhoto", GroupImage);
            jsonObject.accumulate("UserId", user_id);

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

    private class HttpAsyncTaskGroupCreate extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupsActivity.this);
            dialog.setMessage("Creating Group...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
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
                    String Success = jsonObject.getString("Success").toString();
                    if (Success.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Group Created..", Toast.LENGTH_LONG).show();
                        groupModelArrayList.clear();
                        new HttpAsyncTaskGroup().execute("http://circle8.asia:8081/Onet.svc/Group/Fetch");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Group not Created..", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to create Group..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupsActivity.this);
            dialog.setMessage("Fetching Groups...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
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

                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        listView.setVisibility(View.GONE);
                        //txtGroup.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        listView.setVisibility(View.VISIBLE);
                       // txtGroup.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length() ; i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        GroupModel nfcModelTag = new GroupModel();
                        nfcModelTag.setGroup_ID(object.getString("group_ID"));
                        nfcModelTag.setGroup_Name(object.getString("group_Name"));
                        nfcModelTag.setGroup_Desc(object.getString("group_desc"));
                        nfcModelTag.setGroup_Photo(object.getString("group_photo"));

                        JSONArray memberArray = object.getJSONArray("Members");

                        if (memberArray.length() == 3)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));

                            //for 1st member details
                            JSONObject memberObject = memberArray.getJSONObject(0);
                            nfcModelTag.setProfileId1(memberObject.getString("ProfileId"));
                            nfcModelTag.setFirstName1(memberObject.getString("FirstName"));
                            nfcModelTag.setLastName1(memberObject.getString("LastName"));
                            nfcModelTag.setUserPhoto1(memberObject.getString("UserPhoto"));
                            nfcModelTag.setCompanyName1(memberObject.getString("CompanyName"));
                            nfcModelTag.setDesignation1(memberObject.getString("Designation"));

                            //for 2nd member details
                            JSONObject memberObject1 = memberArray.getJSONObject(1);
                            nfcModelTag.setProfileId2(memberObject1.getString("ProfileId"));
                            nfcModelTag.setFirstName2(memberObject1.getString("FirstName"));
                            nfcModelTag.setLastName2(memberObject1.getString("LastName"));
                            nfcModelTag.setUserPhoto2(memberObject1.getString("UserPhoto"));
                            nfcModelTag.setCompanyName2(memberObject1.getString("CompanyName"));
                            nfcModelTag.setDesignation2(memberObject1.getString("Designation"));

                            //for 3rd member details
                            JSONObject memberObject2 = memberArray.getJSONObject(2);
                            nfcModelTag.setProfileId3(memberObject2.getString("ProfileId"));
                            nfcModelTag.setFirstName3(memberObject2.getString("FirstName"));
                            nfcModelTag.setLastName3(memberObject2.getString("LastName"));
                            nfcModelTag.setUserPhoto3(memberObject2.getString("UserPhoto"));
                            nfcModelTag.setCompanyName3(memberObject2.getString("CompanyName"));
                            nfcModelTag.setDesignation3(memberObject2.getString("Designation"));
                        }
                        else if (memberArray.length() == 2)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));

                            //for 1st member details
                            JSONObject memberObject = memberArray.getJSONObject(0);
                            nfcModelTag.setProfileId1(memberObject.getString("ProfileId"));
                            nfcModelTag.setFirstName1(memberObject.getString("FirstName"));
                            nfcModelTag.setLastName1(memberObject.getString("LastName"));
                            nfcModelTag.setUserPhoto1(memberObject.getString("UserPhoto"));
                            nfcModelTag.setCompanyName1(memberObject.getString("CompanyName"));
                            nfcModelTag.setDesignation1(memberObject.getString("Designation"));

                            //for 2nd member details
                            JSONObject memberObject1 = memberArray.getJSONObject(1);
                            nfcModelTag.setProfileId2(memberObject1.getString("ProfileId"));
                            nfcModelTag.setFirstName2(memberObject1.getString("FirstName"));
                            nfcModelTag.setLastName2(memberObject1.getString("LastName"));
                            nfcModelTag.setUserPhoto2(memberObject1.getString("UserPhoto"));
                            nfcModelTag.setCompanyName2(memberObject1.getString("CompanyName"));
                            nfcModelTag.setDesignation2(memberObject1.getString("Designation"));
                        }
                        else if (memberArray.length() == 1)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));

                            //for 1st member details
                            JSONObject memberObject = memberArray.getJSONObject(0);
                            nfcModelTag.setProfileId1(memberObject.getString("ProfileId"));
                            nfcModelTag.setFirstName1(memberObject.getString("FirstName"));
                            nfcModelTag.setLastName1(memberObject.getString("LastName"));
                            nfcModelTag.setUserPhoto1(memberObject.getString("UserPhoto"));
                            nfcModelTag.setCompanyName1(memberObject.getString("CompanyName"));
                            nfcModelTag.setDesignation1(memberObject.getString("Designation"));
                        }
                        else if (memberArray.length() == 0)
                        {
                            nfcModelTag.setMemberArrays(String.valueOf(memberArray.length()));
                        }
                        else
                        {

                        }

                        groupModelArrayList.add(nfcModelTag);
                    }

                    groupsItemsAdapter = new GroupsItemsAdapter(getApplicationContext(), groupModelArrayList);
                    listView.setAdapter(groupsItemsAdapter);
                    groupsItemsAdapter.notifyDataSetChanged();

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
            jsonObject.accumulate("UserId", user_id);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}