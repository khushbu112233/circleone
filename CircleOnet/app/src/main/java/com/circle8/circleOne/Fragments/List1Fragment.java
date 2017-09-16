package com.circle8.circleOne.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Activity.CardsActivity;
import com.circle8.circleOne.Activity.SortAndFilterOption;
import com.circle8.circleOne.Adapter.GalleryAdapter;
import com.circle8.circleOne.Adapter.GalleryAdapter1;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.daimajia.swipe.util.Attributes;

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
import java.util.List;
import java.util.Locale;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class List1Fragment extends Fragment
{
    // private ArrayList<Integer> imageFront = new ArrayList<>();
    // private ArrayList<Integer> imageBack = new ArrayList<>();
    //  public static MyPager myPager ;
    DatabaseHelper db ;
    private GestureDetector gestureDetector1;
    FrameLayout frameList1;
    LinearLayout lnrList;
    /*ArrayList<byte[]> imgf;
    ArrayList<byte[]> imgb;
    ArrayList<String> tag_id;*/
    public static RelativeLayout lnrSearch;
    public static View line;
    private String DEBUG_TAG = "gesture";
    //  private GestureDetector gestureDetector;
    //  private View.OnTouchListener gestureListener;
    public static List<FriendConnection> allTags ;
    //new asign value
    AutoCompleteTextView searchText ;
    public static ArrayList<FriendConnection> nfcModel ;
    ViewConfiguration vc;
    private int mTouchSlop;
    FrameLayout frame, frame1;

    public static int pageno = 1;

    View view;

    private String TAG = CardsActivity.class.getSimpleName();
    public static ArrayList<Integer> images;
    public static ArrayList<Integer> images1;
    public static GalleryAdapter mAdapter;
    public static GalleryAdapter1 mAdapter1;
    public static RecyclerView recyclerView1, recyclerView2;
    public static CarouselLayoutManager manager1, manager2;
    private int draggingView = -1;
    RelativeLayout rlt;
    public static TextView txtNoCard1;
    LoginSession session;

    private static ProgressBar progressBar1, progressBar2  ;
    private static RelativeLayout rlLoadMore1, rlLoadMore2  ;

    static String UserId = "";

    public static Context mContext ;

    static String comeAtTime = "FIRST" ;

    static int number_cards = 0 ;

    static int numberCount, recycleSize  ;

    public List1Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_list1, container, false);
        vc = ViewConfiguration.get(view.getContext());

       ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        // frameList1 = (FrameLayout) view.findViewById(R.id.frameList1);
        mTouchSlop = vc.getScaledTouchSlop();

        mContext = List1Fragment.this.getContext() ;
        pageno = 1;

        recyclerView1 = (RecyclerView) view.findViewById(R.id.list_horizontal1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.list_horizontal2);
        txtNoCard1 = (TextView) view.findViewById(R.id.txtNoCard1);
        lnrList = (LinearLayout) view.findViewById(R.id.lnrList);
        frame = (FrameLayout) view.findViewById(R.id.frame);
        frame1 = (FrameLayout) view.findViewById(R.id.frame1);
        progressBar1 = (ProgressBar)view.findViewById(R.id.more_progress1);
        rlLoadMore1 = (RelativeLayout)view.findViewById(R.id.rlLoadMore1);
        progressBar2 = (ProgressBar)view.findViewById(R.id.more_progress2);
        rlLoadMore2 = (RelativeLayout)view.findViewById(R.id.rlLoadMore2);

        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        // name
        UserId = user.get(LoginSession.KEY_USERID);

        db = new DatabaseHelper(getContext());
        //viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        lnrSearch = (RelativeLayout) view.findViewById(R.id.lnrSearch);
        line = view.findViewById(R.id.view);
        initRecyclerView1(recyclerView1,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter ) ;
        initRecyclerView2(recyclerView2,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1 ) ;

        searchText = (AutoCompleteTextView)view.findViewById(R.id.searchView);
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

        lnrSearch.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        CardsFragment.tabLayout.setVisibility(View.VISIBLE);

       /* GestureDetector.OnGestureListener gestureListener = new MyOnGestureListener();
        GestureDetector.OnDoubleTapListener doubleTapListener = new MyOnDoubleTapListener();
        gestureDetector1= new GestureDetector(getContext(), gestureListener);
        gestureDetector1.setOnDoubleTapListener(doubleTapListener);*/

        nfcModel = new ArrayList<>();
        allTags = new ArrayList<>();

        // new LoadDataForActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetFriendConnection");

        pageno = 1;
        callFirst();

        recyclerView1.addOnScrollListener(scrollListener);
        recyclerView2.addOnScrollListener(scrollListener);
        ViewTreeObserver vto = lnrList.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    lnrList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    lnrList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = lnrList.getMeasuredWidth();
                int height = lnrList.getMeasuredHeight();

                View view_instance = (View)view.findViewById(R.id.list_horizontal1);
                ViewGroup.LayoutParams params=view_instance.getLayoutParams();
                params.height=(height/(29/10))-10;
                view_instance.setLayoutParams(params);

                View view_instance1 = (View)view.findViewById(R.id.list_horizontal2);
                ViewGroup.LayoutParams params1=view_instance1.getLayoutParams();
                params1.height=(height/(29/10))-10;
                view_instance1.setLayoutParams(params1);
            }
        });

       /* frame.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                gestureDetector1.onTouchEvent(me);
                recyclerView1.requestFocus();
                recyclerView1.dispatchTouchEvent(me); // don't cause scrolling
                //recyclerView1.dispatchTouchEvent(me); // don't cause scrolling? Alternative solutoin?

                // recyclerView2.requestFocus();
                // recyclerView2.dispatchTouchEvent(mBackupTouchDownEvent); // don't cause scrolling
                //recyclerView2.dispatchTouchEvent(me);
                return true;
            }
        });*/

       /* frame1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {
                gestureDetector1.onTouchEvent(me);
                recyclerView2.requestFocus();
                recyclerView2.dispatchTouchEvent(me); // don't cause scrolling
                //recyclerView1.dispatchTouchEvent(me); // don't cause scrolling? Alternative solutoin?
                //recyclerView2.requestFocus();
                // recyclerView2.dispatchTouchEvent(mBackupTouchDownEvent); // don't cause scrolling
                //recyclerView2.dispatchTouchEvent(me);
                return true;
            }
        });*/


        /*frame.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                recyclerView1.addOnScrollListener(scrollListener);
                recyclerView2.addOnScrollListener(scrollListener);
            }
        });*/
       /* recyclerView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector1.onTouchEvent(event);
            }
        });*/
        /*recyclerView1.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector1.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });*/


        /*recyclerView2.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return gestureDetector1.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
*/
        /*view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

// Start the animation
        view.animate()
                .translationY(view.getHeight())
                .alpha(1.0f);

        view.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });*/

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gestureDetector.onTouchEvent(event);

            }
        });*/

       /* imgf = new ArrayList<byte[]>();
        imgb = new ArrayList<byte[]>();
        tag_id = new ArrayList<String>();*/

        //myPager = new MyPager(getContext(), imgf, imgb, tag_id);
        //viewPager.setAdapter(myPager);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                    if(s.length() <= 0)
                    {
                        // allTags = db.getActiveNFC();
//                    GetData(getContext());
//                        nfcModel.clear();
                        pageno = 1;
                        allTags.clear();
                        try
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){}
                        try
                        {
                            mAdapter1.notifyDataSetChanged();
                        }
                        catch (Exception e){}
                        callFirst();
                    }
                    else if( s.length() > 0 )
                    {
                        String text = searchText.getText().toString().toLowerCase(Locale.getDefault());

//                        nfcModel.clear();
                        allTags.clear();
                        try
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){}
                        try
                        {
                            mAdapter1.notifyDataSetChanged();
                        }
                        catch (Exception e){}
                        new HttpAsyncTaskSearch().execute("http://circle8.asia:8081/Onet.svc/SearchConnect");
                    }
                }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void callFirst()
    {
       // tvNoCard.setVisibility(View.GONE);
        nfcModel.clear();
        pageno = 1;
        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetFriendConnection");
    }

    public static void webCall()
    {

        allTags.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter1.notifyDataSetChanged();
        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetFriendConnection");
    }

    public static String POST(String url)
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
            jsonObject.accumulate("Type", SortAndFilterOption.SortType);
            jsonObject.accumulate("numofrecords", "3" );
            jsonObject.accumulate("pageno", pageno );
            jsonObject.accumulate("userid", UserId );

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

        pageno++;
        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private static class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.setCancelable(false);
            dialog.show();
           /* if(comeAtTime.equalsIgnoreCase("FIRST"))
            {
                dialog.show();
                comeAtTime = "SECOND";
            }
            else
            {
                dialog.dismiss();
            }*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
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
            dialog.dismiss();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);

                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    String count = jsonObject.getString("count");
                    if(count.equals("") || count.equals("null"))
                    {
                        numberCount = 0 ;
                    }
                    else
                    {
                        numberCount = Integer.parseInt(count);
                    }

//                    nfcModel.clear();
//                    allTags.clear();
                    try
                    {
                        mAdapter.notifyDataSetChanged();
                        mAdapter1.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("connection");

                    number_cards = jsonArray.length();
                    rlLoadMore1.setVisibility(View.GONE);
                    rlLoadMore2.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite("");
                        nfcModelTag.setMob_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));
                        nfcModelTag.setUser_image(object.getString("UserPhoto"));
                        nfcModelTag.setProfile_id(object.getString("ProfileId"));
//                        Toast.makeText(getActivity(),"Profile_id"+object.getString("ProfileId"),Toast.LENGTH_SHORT).show();
                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);

                        GetData(mContext);
                    }

                    recycleSize = allTags.size() ;

                    final CarouselLayoutManager linearLayoutManager1 = (CarouselLayoutManager) recyclerView1.getLayoutManager();

                    recyclerView1.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int scrollState)
                        {
                            super.onScrollStateChanged(recyclerView, scrollState);
                            int threshold = 1;
                            int count = linearLayoutManager1.getItemCount();
                            int lastVisibleItem, totalItemCount;

                            totalItemCount = linearLayoutManager1.getItemCount();
                            lastVisibleItem = linearLayoutManager1.getMaxVisibleItems();

                            if (scrollState == SCROLL_STATE_IDLE)
                            {
                                if(recycleSize <= numberCount)
                                {
                                    if(lastVisibleItem >= (count - threshold))
                                    {
                                        rlLoadMore1.setVisibility(View.VISIBLE);
                                        rlLoadMore2.setVisibility(View.VISIBLE);
                                        new HttpAsyncTask().execute("http://circle8.asia:8081/Onet.svc/GetFriendConnection");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });

                }
                else
                {
                    Toast.makeText(mContext, "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadDataForActivity extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressBar;
        @Override
        protected void onPreExecute() {

            progressBar = new ProgressDialog(getContext());
            progressBar.setMessage("Loading Data..");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.setCancelable(false);
            progressBar.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            db = new DatabaseHelper(getContext());
            // allTags = db.getActiveNFC();

            images = new ArrayList<>();
            images1 = new ArrayList<>();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            GetData(getContext());
            progressBar.dismiss();
        }

    }

    public static void initRecyclerView1(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, GalleryAdapter mAdapter)
    {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);
        manager1 = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

    public static void initRecyclerView2(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, GalleryAdapter1 mAdapter)
    {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);
        manager2 = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

   /* class MyOnGestureListener implements GestureDetector.OnGestureListener  {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1000;

        @Override
        public boolean onDown(MotionEvent e) {
            //  Toast.makeText(getContext(), "onDown", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // Toast.makeText(context, "onShowPress", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Toast.makeText(getContext(), "onSingleTap", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //   Log.e(TAG, "onSingleTapUp");

            // final_position = List1Fragment.viewPager.getCurrentItem();
           *//* Intent intent = new Intent(getContext(), CardDetail.class);
            intent.putExtra("tag_id", GalleryAdapter.nfcModelList.get(GalleryAdapter.posi).getNfc_tag());
            getContext().startActivity(intent);
*//*            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Toast.makeText(getContext(), "onScroll", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e1.getX()+":"+ e1.getY() +"  "+ e2.getX()+":"+ e2.getY());
            //Log.e(TAG, "onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // Toast.makeText(context, "onLongPress", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Toast.makeText(getContext(), "onFling", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e1.getX() + ":" + e1.getY() + "  " + e2.getX() + ":" + e2.getY());
            boolean result = true;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // onSwipeRight();
                        } else {
                            // onSwipeLeft();
                        }
                       *//* recyclerView1.addOnScrollListener(scrollListener);
                        recyclerView2.addOnScrollListener(scrollListener);*//*
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            //Toast.makeText(getContext(), "Down", Toast.LENGTH_LONG).show();
                            List1Fragment.lnrSearch.setVisibility(View.VISIBLE);
                            List1Fragment.line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        } else {
                            //  Toast.makeText(getContext(), "Up", Toast.LENGTH_LONG).show();
                            lnrSearch.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }


    }
*/
   /* class MyOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //  Toast.makeText(getContext(), "onSingleTapConfirmed", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            //  Log.e(TAG, "onSingleTapConfirmed");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //  Toast.makeText(context, "onDoubleTap", Toast.LENGTH_LONG).show();
            //textEvt2.setText(e.getX()+":"+ e.getY());
            // Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //  Toast.makeText(context, "onDoubleTapEvent", Toast.LENGTH_LONG).show();
            // textEvt2.setText(e.getX() + ":" + e.getY());
            //  Log.e(TAG, "onDoubleTapEvent");
            return true;
        }
    }*/

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        public int y=0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView1 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                draggingView = 1;
                // GalleryAdapter.position = Integer.parseInt(GalleryAdapter.imageView.getTag().toString());
            } else if (recyclerView2 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                draggingView = 2;
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // y=dy;
            if (draggingView == 1 && recyclerView == recyclerView1) {
                recyclerView2.scrollBy(dx, dy);
            } else if (draggingView == 2 && recyclerView == recyclerView2) {
                recyclerView1.scrollBy(dx, dy);
            }
        }
    };

   /* GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 50;

            // TODO Auto-generated method stub
            try {
                // TODO Auto-generated method stub
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    swipe += "Swipe Left\n";
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    swipe += "Swipe Right\n";
                } else {
                    swipe += "\n";
                }

                if ((e1.getY() - e2.getY()) > sensitvity) {
                    swipe += "Swipe Up\n";
                    lnrSearch.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.GONE);
                    CardsFragment.tabLayout.setVisibility(View.GONE);
                } else if ((e2.getY() - e1.getY()) > sensitvity) {
                    swipe += "Swipe Down\n";
                    lnrSearch.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    CardsFragment.tabLayout.setVisibility(View.VISIBLE);
                } else {
                    swipe += "\n";
                }

                //  Toast.makeText(getContext(), swipe, Toast.LENGTH_LONG).show();

                return super.onFling(e1, e2, velocityX, velocityY);
            }
            catch (Exception e ){
                return true;
            }
        }
    };*/

    /*GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);*/

   /* @Override
    public void onResume()
    {
        super.onResume();
        callFirst();
//        nfcModel.clear();
//        GetData(getContext());
    }*/

    private class HttpAsyncTaskSearch extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Searching Records...");
            //dialog.setTitle("Saving Reminder");
          //  dialog.show();
          //  dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            return POSTSearch(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
           // dialog.dismiss();
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("success");
                    String findBy = response.getString("FindBy");
                    String search = response.getString("Search");
                    String count = response.getString("count");
                    String pageno = response.getString("pageno");
                    String recordno = response.getString("numofrecords");

                    JSONArray connect = response.getJSONArray("connect");

                    allTags.clear();
                    mAdapter.notifyDataSetChanged();
                    mAdapter1.notifyDataSetChanged();

                    if(connect.length() == 0)
                    {
                      //  tvNoCard.setVisibility(View.VISIBLE);
                        allTags.clear();
                        mAdapter.notifyDataSetChanged();
                        mAdapter1.notifyDataSetChanged();
                    }
                    else
                    {
                        //  tvNoCard.setVisibility(View.GONE);

                        for(int i = 0 ; i <= connect.length() ; i++ )
                        {
                            JSONObject iCon = connect.getJSONObject(i);
                            FriendConnection connectModel = new FriendConnection();
                            connectModel.setUserID(iCon.getString("UserID"));
                            connectModel.setFirstName(iCon.getString("FirstName"));
                            connectModel.setLastName(iCon.getString("LastName"));
                            connectModel.setName(iCon.getString("FirstName") + " " + iCon.getString("LastName"));
                            connectModel.setUser_image(iCon.getString("UserPhoto"));
                            connectModel.setCard_front(iCon.getString("Card_Front"));
                            connectModel.setCard_back(iCon.getString("Card_Back"));
                            connectModel.setProfile_id(iCon.getString("ProfileId"));
                            connectModel.setPh_no(iCon.getString("Phone"));
                            connectModel.setCompany(iCon.getString("CompanyName"));
                            connectModel.setDesignation(iCon.getString("Designation"));
                            connectModel.setFb_id(iCon.getString("Facebook"));
                            connectModel.setTwitter_id(iCon.getString("Twitter"));
                            connectModel.setGoogle_id(iCon.getString("Google"));
                            connectModel.setLinkedin_id(iCon.getString("LinkedIn"));
                            connectModel.setWebsite(iCon.getString("Website"));
                            allTags.add(connectModel);

                            GetData(mContext);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public  String POSTSearch(String url)
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
            jsonObject.accumulate("FindBy", "NAME" );
            jsonObject.accumulate("Search", searchText.getText().toString() );
            jsonObject.accumulate("UserID", UserId);
            jsonObject.accumulate("numofrecords", "30" );
            jsonObject.accumulate("pageno", "1" );

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

    public static void GetData(Context context)
    {
        images = new ArrayList<>();
        images1 = new ArrayList<>();
        //newly added
        nfcModel.clear();
        for(FriendConnection reTag : allTags)
        {
            FriendConnection nfcModelTag = new FriendConnection();
            // nfcModelTag.setId(reTag.getId());
            nfcModelTag.setName(reTag.getName());
            nfcModelTag.setCompany(reTag.getCompany());
            nfcModelTag.setEmail(reTag.getEmail());
            nfcModelTag.setWebsite(reTag.getWebsite());
            nfcModelTag.setMob_no(reTag.getMob_no());
            nfcModelTag.setDesignation(reTag.getDesignation());
            nfcModelTag.setCard_front(reTag.getCard_front());
            nfcModelTag.setCard_back(reTag.getCard_back());
            nfcModelTag.setNfc_tag(reTag.getNfc_tag());
            nfcModelTag.setProfile_id(reTag.getProfile_id());
            nfcModel.add(nfcModelTag);
        }

        mAdapter = new GalleryAdapter(context, nfcModel);
        mAdapter1 = new GalleryAdapter1(context, nfcModel);
        mAdapter.notifyDataSetChanged();
        mAdapter1.notifyDataSetChanged();

        if (nfcModel.size() == 0)
        {
            txtNoCard1.setVisibility(View.VISIBLE);
        }
        else
        {
            txtNoCard1.setVisibility(View.GONE);
        }

        CardsActivity.setActionBarTitle("Cards - "+nfcModel.size());
//        CardsActivity.setActionBarTitle("Cards - "+number_cards);
        initRecyclerView1(recyclerView1,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter ) ;
        initRecyclerView2(recyclerView2,new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), mAdapter1 ) ;

        // myPager = new MyPager(context, R.layout.cardview_list, nfcModel);
        //viewPager.setAdapter(myPager);
        //  myPager.notifyDataSetChanged();

        //gridAdapter.setMode(Attributes.Mode.Single);
    }

}