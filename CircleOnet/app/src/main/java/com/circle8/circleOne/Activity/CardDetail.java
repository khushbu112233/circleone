package com.circle8.circleOne.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.CardSwipe;
import com.circle8.circleOne.Adapter.CustomAdapter;
import com.circle8.circleOne.Adapter.EditGroupAdapter;
import com.circle8.circleOne.Adapter.GroupsRecyclerAdapter;
import com.circle8.circleOne.Helper.DatabaseHelper;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Helper.ReferralCodeSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.Model.NFCModel;
import com.circle8.circleOne.Model.TestimonialModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.GeocodingLocation;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.chat.ChatActivity;
import com.circle8.circleOne.chat.ChatHelper;
import com.circle8.circleOne.chat.DialogsAdapter;
import com.circle8.circleOne.chat.DialogsManager;
import com.circle8.circleOne.chat.qb.QbChatDialogMessageListenerImp;
import com.circle8.circleOne.chat.qb.QbDialogHolder;
import com.circle8.circleOne.databinding.ActivityCardDetailBinding;
import com.circle8.circleOne.databinding.EditGroupsPopupBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class CardDetail extends NfcActivity implements DialogsManager.ManagingDialogsCallbacks, View.OnClickListener, OnMapReadyCallback {

    private ArrayList<String> image = new ArrayList<>();
    private CardSwipe myPager;
    private static final String TAG = NFCDemo.class.getName();
    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    DatabaseHelper db;
    String user_id = "", profile_id, currentUser_ProfileId = "", DateInitiated = "";
    String recycle_image1, recycle_image2,userImg, frontCardImg, backCardImg, personName, personAddress;
    public static JSONArray selectedStrings = new JSONArray();
    List<CharSequence> list;
    ArrayList<String> listGroupId;
    LoginSession loginSession;
    String strfbUrl = "", strlinkedInUrl = "", strtwitterUrl = "", strgoogleUrl = "", stryoutubeUrl = "";
    String FirstName = "", LastName = "", UserPhoto = "", Phone1 = "", Phone2 = "", Mobile1 = "", Mobile2 = "", Fax1 = "",
            Fax2 = "", Email1 = "", Email2 = "", IndustryName = "", CompanyName = "", CompanyProfile = "", Designation = "",
            ProfileDesc = "";
    ArrayList<GroupModel> groupModelArrayList = new ArrayList<>();
    ArrayList<String> groupName = new ArrayList<>();
    ArrayList<String> groupPhoto = new ArrayList<>();
    ArrayList<String> ID_group = new ArrayList<>();
    ArrayList<String> groupDesc = new ArrayList<>();
    private String displayProfile;
    public static ArrayList<TestimonialModel> allTaggs;
    private CustomAdapter customAdapter;
    ReferralCodeSession referralCodeSession;
    String refer;
    double Latitude, Longitude;
    Boolean netCheck = false;
    private QBSystemMessagesManager systemMessagesManager;
    public static ArrayList<QBUser> selectedUsers = new ArrayList<QBUser>();
    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private DialogsAdapter dialogsAdapter;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private QBUser currentUser;
    public static String Q_ID = "", CurrentQ_ID = "";
    String CurrentUserEmail = "";
    public static int occupant_id = 0;
    GoogleMap googleMaps;
    ActivityCardDetailBinding mBinding;
    EditGroupsPopupBinding binding;
    public static CardDetail activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_card_detail);
        Utility.freeMemory();
        Utility.deleteCache(getApplicationContext());
        Log.e("act","click");
        referralCodeSession = new ReferralCodeSession(getApplicationContext());
        loginSession = new LoginSession(getApplicationContext());
        HashMap<String, String> user = loginSession.getUserDetails();
        user_id = user.get(LoginSession.KEY_USERID);
        currentUser_ProfileId = user.get(LoginSession.KEY_PROFILEID);
        CurrentQ_ID = user.get(LoginSession.KEY_QID);
        CurrentUserEmail = user.get(LoginSession.KEY_EMAIL);
        db = new DatabaseHelper(getApplicationContext());
        list = new ArrayList<CharSequence>();
        listGroupId = new ArrayList<String>();
        netCheck = Utility.isNetworkAvailable(getApplicationContext());
        final HashMap<String, String> referral = referralCodeSession.getReferralDetails();
        refer = referral.get(ReferralCodeSession.KEY_REFERRAL);
        Intent intent = getIntent();
        profile_id = intent.getStringExtra("profile_id");
        DateInitiated = intent.getStringExtra("DateInitiated");
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("long");

        activity = CardDetail.this;
        dialogsManager = new DialogsManager();
        SpannableString ss = new SpannableString("Ask your friends to write a Testimonial for you(100 words or less),Please choose from your CircleOne contacts and send a request.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 91, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // TextView textView = (TextView) findViewById(R.id.hello);
        mBinding.includeLayoutDetails.txtTestimonial.setText(ss);
        mBinding.includeLayoutDetails.txtTestimonial.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.includeLayoutDetails.txtTestimonial.setHighlightColor(getResources().getColor(R.color.colorPrimary));

        try {
            if ((lat.equals("") || lat.equals("null") || lat == null || lat.isEmpty()) && (lon.equals("") || lon.equals("null") || lon == null || lon.isEmpty())) {
                mBinding.includeLayoutDetails.lnrNfcLocation.setVisibility(View.GONE);
            } else {
                mBinding.includeLayoutDetails.lnrNfcLocation.setVisibility(View.VISIBLE);
                Latitude = Double.parseDouble(lat);
                Longitude = Double.parseDouble(lon);

                getAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Toast.makeText(getApplicationContext(), DateInitiated.toString(),Toast.LENGTH_SHORT).show();


        mBinding.includeLayoutDetails.tvDateInitiated.setText(DateInitiated);

        allTaggs = new ArrayList<>();

        if (netCheck == false) {
            Utility.freeMemory();
            Utility.deleteCache(getApplicationContext());
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_check), Toast.LENGTH_LONG).show();
        } else {

            //googlePlayServicesHelper = new GooglePlayServicesHelper();
            pushBroadcastReceiver = new PushBroadcastReceiver();
            allDialogsMessagesListener = new AllDialogsMessageListener();
            systemMessagesListener = new SystemMessagesListener();
            dialogsManager = new DialogsManager();
            currentUser = ChatHelper.getCurrentUser();
            incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
            systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

            if (incomingMessagesManager != null) {
                incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                        ? allDialogsMessagesListener : new AllDialogsMessageListener());
            }

            if (systemMessagesManager != null) {
                systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                        ? systemMessagesListener : new SystemMessagesListener());
            }

            dialogsManager.addManagingDialogsCallbackListener(this);
            dismissProgress();
            // new HttpAsyncTaskGroup().execute(Utility.BASE_URL+"Group/Fetch");
            if (profile_id.equals("")) {
                Toast.makeText(CardDetail.this, "Having no profile ID", Toast.LENGTH_LONG).show();
            } else {
                new HttpAsyncTask().execute(Utility.BASE_URL + "GetUserProfile");
            }

            new HttpAsyncTaskGroup().execute(Utility.BASE_URL + "Group/Fetch");

            new HttpAsyncTaskGroupsFetch().execute(Utility.BASE_URL + "Group/MyGroupsTaggedWithFriendProfile");

            new HttpAsyncTaskTestimonial().execute(Utility.BASE_URL + "Testimonial/Fetch");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // registerQbChatListeners();
        mBinding.viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                mBinding.viewPager.scrollTo(mBinding.viewPager1.getScrollX(), mBinding.viewPager1.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {
                // mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mBinding.viewPager.setCurrentItem(mBinding.viewPager1.getCurrentItem(), false);
                }
            }
        });
        mBinding.includeLayoutViepager.imgBack.setOnClickListener(this);
        mBinding.includeLayoutBottom.imgCards.setOnClickListener(this);
        mBinding.includeLayoutBottom.imgConnect.setOnClickListener(this);
        mBinding.includeLayoutBottom.imgEvents.setOnClickListener(this);
        mBinding.includeLayoutBottom.imgProfile.setOnClickListener(this);
        mBinding.tvSendMessage.setOnClickListener(this);
        mBinding.includeLayoutDetails.ivMap.setOnClickListener(this);
        mBinding.includeLayoutSocial.imgProfileShare.setOnClickListener(this);
        mBinding.includeLayoutDetails.txtMore.setOnClickListener(this);
        mBinding.includeLayoutDetails.txtAttachment.setOnClickListener(this);
        mBinding.includeLayoutViepager.imgProfileCard.setOnClickListener(this);
        mBinding.includeLayoutDetails.imgAddGroupFriend.setOnClickListener(this);
        mBinding.includeLayoutSocial.fbUrl.setOnClickListener(this);
        mBinding.includeLayoutSocial.googleUrl.setOnClickListener(this);
        mBinding.includeLayoutSocial.youtubeUrl.setOnClickListener(this);
        mBinding.includeLayoutSocial.twitterUrl.setOnClickListener(this);
        mBinding.includeLayoutSocial.linkedInUrl.setOnClickListener(this);
        mBinding.includeLayoutSocial.imgMail.setOnClickListener(this);
        mBinding.includeLayoutSocial.imgSMS.setOnClickListener(this);
        mBinding.includeLayoutSocial.imgCall.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id) {
            case R.id.txtMore:
                Intent intent = new Intent(getApplicationContext(), TestimonialCardDetail.class);
                intent.putExtra("ProfileId", profile_id);
                startActivity(intent);
                break;
            case R.id.txtAttachment:
                Intent intent1 = new Intent(getApplicationContext(), AttachmentDisplay.class);
                intent1.putExtra("url", Utility.BASE_IMAGE_URL + "Other_doc/" + mBinding.includeLayoutDetails.txtAttachment.getText().toString());
                startActivity(intent1);
                break;
            case R.id.imgProfileCard:
                final Dialog dialog = new Dialog(CardDetail.this);
                dialog.setContentView(R.layout.imageview_popup);

                ImageView ivViewImage = (ImageView) dialog.findViewById(R.id.ivViewImage);
                if (displayProfile.equals("")) {
                    ivViewImage.setImageResource(R.drawable.usr_1);
                } else {
                    Picasso.with(getApplicationContext()).load(Utility.BASE_IMAGE_URL + "UserProfile/" + displayProfile).skipMemoryCache().placeholder(R.drawable.usr_1)
                            .resize(500, 500)
                            .onlyScaleDown()
                            .into(ivViewImage);
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ivViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ImageZoom.class);
                        intent.putExtra("displayProfile", Utility.BASE_IMAGE_URL + "UserProfile/" + displayProfile);
                        startActivity(intent);
                    }
                });

               /* WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                wmlp.y = 300;*/   //y position
                dialog.show();
                break;
            case R.id.imgAddGroupFriend:
                final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(CardDetail.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                binding =DataBindingUtil.inflate(inflater, R.layout.edit_groups_popup, null, false);
// get the root view
                View dialogView = binding.getRoot();
                int count = dialogList.length;
                boolean[] is_checked = new boolean[count];

                final AlertDialog alertDialog = builderDialog.create();
                alertDialog.setCancelable(false);
                if (groupName.size() == 0) {
                    binding.tvGroupInfo.setVisibility(View.VISIBLE);
                } else {
                    binding.tvGroupInfo.setVisibility(View.GONE);
                    ArrayList<GroupModel> groupModelArrayList1 = new ArrayList<GroupModel>();
                    groupModelArrayList1 = groupModelArrayList;

//                EditGroupAdapter editGroupAdapter = new EditGroupAdapter(CardDetail.this, groupModelArrayList1);
                    EditGroupAdapter editGroupAdapter = new EditGroupAdapter(CardDetail.this, groupName, groupPhoto, listGroupId);
                    binding.listView.setAdapter(editGroupAdapter);
                    editGroupAdapter.notifyDataSetChanged();
                }

                binding.btnCancelGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                binding.btnAddToGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        // make selected item in the comma seprated string
                        //  Toast.makeText(getApplicationContext(), selectedStrings.toString(), Toast.LENGTH_LONG).show();
                        if (selectedStrings.length() == 0){
                            Toast.makeText(getApplicationContext(), "Please select any circle", Toast.LENGTH_LONG).show();
                        }else {
                            new HttpAsyncTaskGroupAddFriend().execute(Utility.BASE_URL + "AddMemberToGroups");
                        }
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;
            case R.id.fbUrl:
                if (strfbUrl != null) {
                    if (!strfbUrl.startsWith("http://") && !strfbUrl.startsWith("https://"))
                        strfbUrl = "http://" + strfbUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strfbUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.imgProfileShare:
                Intent intent_share = new Intent(getApplicationContext(), SearchGroupMembers.class);
                intent_share.putExtra("from", "cardDetail");
                intent_share.putExtra("ProfileId", profile_id);
                startActivity(intent_share);
                break;
            case R.id.googleUrl:
                if (strgoogleUrl != null) {
                    if (!strgoogleUrl.startsWith("http://") && !strgoogleUrl.startsWith("https://"))
                        strgoogleUrl = "http://" + strgoogleUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strgoogleUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.youtubeUrl:
                if (stryoutubeUrl != null) {
                    if (!stryoutubeUrl.startsWith("http://") && !stryoutubeUrl.startsWith("https://"))
                        stryoutubeUrl = "http://" + stryoutubeUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stryoutubeUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.twitterUrl:
                if (strtwitterUrl != null) {
                    if (!strtwitterUrl.startsWith("http://") && !strtwitterUrl.startsWith("https://"))
                        strtwitterUrl = "http://" + strtwitterUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strtwitterUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.linkedInUrl:
                if (strlinkedInUrl != null) {
                    if (!strlinkedInUrl.startsWith("http://") && !strlinkedInUrl.startsWith("https://"))
                        strlinkedInUrl = "http://" + strlinkedInUrl;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strlinkedInUrl));
                    startActivity(browserIntent);
                }
                break;
            case R.id.imgMail:
                if (mBinding.includeLayoutDetails.txtEmail.getText().toString().equals("")) {

                } else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);
                    builder.setTitle("Mail to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                            .setMessage("Are you sure you want to drop Mail ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mBinding.includeLayoutDetails.txtEmail.getText().toString()));
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Sorry...you don't have any mail app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
                break;
            case R.id.imgSMS:
                boolean result = Utility.checkSMSPermission(CardDetail.this);
                if (result) {
                    if (mBinding.includeLayoutDetails.txtMob.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "You are not having contact to SMS..", Toast.LENGTH_LONG).show();
                    } else {
                        Uri uri = Uri.parse("smsto:" + mBinding.includeLayoutDetails.txtMob.getText().toString());
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "");
                        startActivity(it);
                       /* Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", txtMob.getText().toString());
                        smsIntent.putExtra("sms_body", "");
                        startActivity(smsIntent);*/
                    }
                }
                break;
            case R.id.imgCall:
                if (!mBinding.includeLayoutDetails.txtMob.getText().toString().equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

                    builder.setTitle("Call to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + mBinding.includeLayoutDetails.txtMob.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                } else if (!mBinding.includeLayoutDetails.txtWork.getText().toString().equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

                    builder.setTitle("Call to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + mBinding.includeLayoutDetails.txtWork.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                } else if (!mBinding.includeLayoutDetails.txtPH.getText().toString().equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

                    builder.setTitle("Call to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                            .setMessage("Are you sure you want to make a Call ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + mBinding.includeLayoutDetails.txtPH.getText().toString()));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_call)
                            .show();
                }
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgCards:
                CardsActivity.mViewPager.setCurrentItem(0);
                finish();
                break;
            case R.id.imgConnect:
                CardsActivity.mViewPager.setCurrentItem(1);
                finish();
                break;
            case  R.id.imgEvents:
                CardsActivity.mViewPager.setCurrentItem(2);
                finish();
                break;
            case R.id.imgProfile:
                CardsActivity.mViewPager.setCurrentItem(3);
                finish();
                break;
            case R.id.tvSendMessage:
                registerQbChatListeners();

                QBChatService.setDebugEnabled(true); // enable chat logging

                QBChatService.setDefaultPacketReplyTimeout(10000);
                QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
                chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
                chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
                chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
                chatServiceConfigurationBuilder.setAutojoinEnabled(true);
                QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);

                try {

                    QBUser currentUser = getUserFromSession();
                    //loginToChat(currentUser);
                    Boolean aBoolean = SharedPrefsHelper.getInstance().hasQbUser();
                    // Toast.makeText(getApplicationContext(), aBoolean.toString(), Toast.LENGTH_LONG).show();
                    ChatHelper.getInstance().loginToChat(currentUser, new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void result, Bundle bundle) {
                            Log.v(TAG, "Chat login onSuccess()");

                            ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
                            occupantIdsList.add(Integer.parseInt(CurrentQ_ID));
                            occupantIdsList.add(occupant_id);
                            QBChatDialog dialog = DialogUtils.buildDialog("Chat with Garry and John", QBDialogType.PRIVATE, occupantIdsList);

                            QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                                @Override
                                public void onSuccess(QBChatDialog result, Bundle params) {
                                    //ChatActivity.startForResult(CardDetail.this, 165, result);
                                    ChatHelper.getInstance();
                                    Intent intent = new Intent(CardDetail.this, ChatActivity.class);
                                    intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, result);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(QBResponseException responseException) {

                                }
                            });
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            //  ProgressDialogFragment.hide(getSupportFragmentManager());
                            Log.w(TAG, "Chat login onError(): " + e);

                        }
                    });

                }catch (Exception e){

                }
                break;
            case R.id.ivMap:

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

                builder.setTitle("Google Map")
                        .setMessage("Are you sure you want to redirect to Google Map ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                String map = "http://maps.google.co.in/maps?q=" + mBinding.includeLayoutDetails.txtAddress.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_map)
                        .show();
                break;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps = googleMap;

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
        boolean result = Utility.checkLocationPermission(CardDetail.this);
        if(result)
        {

        }

    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address == null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }

    private void createMarker(Double Lat, Double Lang)
    {
        LatLng location = new LatLng(Lat,Lang);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        googleMaps.addMarker(new MarkerOptions().position(location).title(""));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMaps.animateCamera(zoom);
    }

    private void createMarker1(String fullAddress, String addressTitle)
    {
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        LatLng address = getLocationFromAddress(CardDetail.this, fullAddress);
        googleMaps.addMarker(new MarkerOptions().position(address).title(addressTitle));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(address));
        googleMaps.animateCamera(zoom);
    }


    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
            //  requestBuilder.setSkip(skipRecords = 0);
            //  loadDialogsFromQb(true, true);
        }
    }

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        //isProcessingResultInProgress = false;
                        // dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                        //  ChatActivity.startForResult(CardDetail.this, 165, dialog);
                        // finish();
                        //   ProgressDialogFragment.hide(getSupportFragmentManager());
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        //isProcessingResultInProgress = false;
                        //ProgressDialogFragment.hide(getSupportFragmentManager());
                        //showErrorSnackbar(R.string.dialogs_creation_error, null, null);
                    }
                }
        );
    }

    private void registerQbChatListeners() {
        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                    ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : new SystemMessagesListener());
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }


    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private QBUser getUserFromSession()
    {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        Log.e("user",""+user);
        try {
            user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        }catch (Exception e){

        }
        return user;
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
        Utility.deleteCache(getApplicationContext());

    }


    public void openWebPage(View v)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

        builder.setTitle("Redirect to Web Browser")
                .setMessage("Are you sure you want to redirect to Web Browser ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        String url = mBinding.includeLayoutDetails.txtWebsite.getText().toString();
                        if (url != null) {
                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://" + url;
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_set_as)
                .show();
    }

    public void openGMail(View v)
    {
        if (mBinding.includeLayoutDetails.txtEmail.getText().toString().equals("")) {

        } else {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);
            builder.setTitle("Mail to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                    .setMessage("Are you sure you want to drop Mail ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mBinding.includeLayoutDetails.txtEmail.getText().toString()));
                                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                                intent.putExtra(Intent.EXTRA_TEXT, "");
                                startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Sorry...you don't have any mail app", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();
        }
    }

    public void openTele(View v)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

        builder.setTitle("Call to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                .setMessage("Are you sure you want to make a Call ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mBinding.includeLayoutDetails.txtPH.getText().toString()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_call)
                .show();
    }

    public void openMobile(View v)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(CardDetail.this, R.style.Blue_AlertDialog);

        builder.setTitle("Call to " + mBinding.includeLayoutViepager.txtName.getText().toString())
                .setMessage("Are you sure you want to make a Call ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mBinding.includeLayoutDetails.txtMob.getText().toString()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_call)
                .show();
    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null) {
                try {
                    return addresses.get(0);
                }
                catch (Exception e){
                    mBinding.includeLayoutDetails.lnrNfcLocation.setVisibility(View.GONE);
                }
            }
            else {
                mBinding.includeLayoutDetails.lnrNfcLocation.setVisibility(View.GONE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public void getAddress()
    {
        Address locationAddress = getAddress(Latitude,Longitude);

        if(locationAddress != null)
        {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+=" "+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+=" "+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" "+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+=" "+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+=" "+country;

                mBinding.includeLayoutDetails.txtRemark.setText(currentLocation);
            }
        }
    }

    /*public String POST2(String url)
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
            jsonObject.accumulate("ProfileId", profile_id);
            jsonObject.accumulate("numofrecords", "10" );
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
    }*/

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {

    }

    @Override
    public void onDialogUpdated(String chatDialog) {

    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {

    }

    private class HttpAsyncTaskTestimonial extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching testimonials...");
            //dialog.setTitle("Saving Reminder");
            // dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.accumulate("ProfileId", profile_id);
                jsonObject.accumulate("numofrecords", "10" );
                jsonObject.accumulate("pageno", "1" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            // dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Testimonials");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0)
                    {
                        mBinding.includeLayoutDetails.rltTestimonialList.setVisibility(View.GONE);
                        mBinding.includeLayoutDetails.rltTestimonial.setVisibility(View.GONE);
                        mBinding.includeLayoutDetails.lstTestimonial.setVisibility(View.GONE);
                        mBinding.includeLayoutDetails.txtMore.setVisibility(View.GONE);
                        mBinding.includeLayoutDetails.txtTestimonial.setVisibility(View.GONE);
                    }
                    else
                    {
                        mBinding.includeLayoutDetails.rltTestimonialList.setVisibility(View.VISIBLE);
                        mBinding.includeLayoutDetails.rltTestimonial.setVisibility(View.VISIBLE);
                        mBinding.includeLayoutDetails.lstTestimonial.setVisibility(View.VISIBLE);
                        mBinding.includeLayoutDetails.txtMore.setVisibility(View.VISIBLE);
                        mBinding.includeLayoutDetails.txtTestimonial.setVisibility(View.GONE);
                    }
                    allTaggs.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        if (i < 3)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                            TestimonialModel nfcModelTag = new TestimonialModel();
                            nfcModelTag.setCompanyName(object.getString("CompanyName"));
                            nfcModelTag.setDesignation(object.getString("Designation"));
                            nfcModelTag.setFirstName(object.getString("FirstName"));
                            nfcModelTag.setFriendProfileID(object.getString("FriendProfileID"));
                            nfcModelTag.setLastName(object.getString("LastName"));
                            nfcModelTag.setPurpose(object.getString("Purpose"));
                            nfcModelTag.setStatus(object.getString("Status"));
                            nfcModelTag.setTestimonial_Text(object.getString("Testimonial_Text"));
                            nfcModelTag.setUserPhoto(object.getString("UserPhoto"));
                            nfcModelTag.setTestimonial_ID(object.getString("Testimonial_ID"));
//                        Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                            allTaggs.add(nfcModelTag);
                        }
                    }
                    customAdapter = new CustomAdapter(CardDetail.this, allTaggs);
                    mBinding.includeLayoutDetails.lstTestimonial.setAdapter(customAdapter);
                    mBinding.includeLayoutDetails.lstTestimonial.setExpanded(true);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = binding.listView.getCheckedItemPositions();
        int itemCount = binding.listView.getCount();

        for(int i=0;i<itemCount;i++){
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }

   /* public String POST4(String url) {
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
            jsonObject.accumulate("numofrecords", "10");
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
    /*public String POST5(String url) {
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
            jsonObject.accumulate("GroupIDs", selectedStrings);
            jsonObject.accumulate("ProfileId", profile_id);
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
*/


    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching circles...");
            //dialog.show();
            dialog.setCancelable(false);

            /*String loading = "Fetching Circles" ;
            CustomProgressDialog(loading);*/
        }

        @Override
        protected String doInBackground(String... urls) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("UserId", user_id);
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            // dialog.dismiss();
//            rlProgressDialog.setVisibility(View.GONE);
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                    groupModelArrayList.clear();
                    list.clear();
                    listGroupId.clear();
                    groupName.clear();
                    groupPhoto.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        GroupModel nfcModelTag = new GroupModel();
                        nfcModelTag.setGroup_ID(object.getString("group_ID"));
                        nfcModelTag.setGroup_Name(object.getString("group_Name"));
                        nfcModelTag.setGroup_Desc(object.getString("group_desc"));
                        nfcModelTag.setGroup_Photo(object.getString("group_photo"));
                        groupModelArrayList.add(nfcModelTag);
                        //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                        list.add(object.getString("group_Name"));
                        listGroupId.add(object.getString("group_ID"));

                        groupName.add(object.getString("group_Name"));
                        groupPhoto.add(object.getString("group_photo"));
                    }
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskGroupAddFriend extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Adding Friend...");
            //dialog.setTitle("Saving Reminder");
            //dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
            String loading = "Adding friend" ;
            CustomProgressDialog(loading,activity);
        }

        @Override
        protected String doInBackground(String... urls) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("GroupIDs", selectedStrings);
                jsonObject.accumulate("ProfileId", profile_id);
                jsonObject.accumulate("UserId", user_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // dialog.dismiss();
            dismissProgress();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success");
                    String Message = jsonObject.getString("Message");
                    if (Success.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Member added into circle successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Member not added", Toast.LENGTH_LONG).show();
                    }
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to add friend in circle", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching Cards...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();

            String loading = "Fetching Connection Data" ;
            CustomProgressDialog(loading,activity);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("profileid", profile_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            dismissProgress();

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    FirstName = jsonObject.getString("FirstName");
                    LastName = jsonObject.getString("LastName");
                    userImg = jsonObject.getString("UserPhoto");
                    Phone1 = jsonObject.getString("Phone1");
                    Phone2 = jsonObject.getString("Phone2");
                    Mobile1 = jsonObject.getString("Mobile1");
                    Mobile2 = jsonObject.getString("Mobile2");
                    Fax1 = jsonObject.getString("Fax1");
                    Fax2 = jsonObject.getString("Fax2");
                    Email1 = jsonObject.getString("Email1");
                    Email2 = jsonObject.getString("Email2");
                    stryoutubeUrl = jsonObject.getString("Youtube");
                    IndustryName = jsonObject.getString("IndustryName");
                    CompanyName = jsonObject.getString("CompanyName");
                    CompanyProfile = jsonObject.getString("CompanyProfile");
                    Designation = jsonObject.getString("Designation");
                    ProfileDesc = jsonObject.getString("ProfileDesc");
                    strlinkedInUrl = jsonObject.getString("LinkedIn");
                    strfbUrl = jsonObject.getString("Facebook");
                    strtwitterUrl = jsonObject.getString("Twitter");
                    stryoutubeUrl = jsonObject.getString("Youtube");
                    strgoogleUrl = jsonObject.getString("Google");
                    frontCardImg = jsonObject.getString("Card_Front");
                    backCardImg = jsonObject.getString("Card_Back");
                    Q_ID = jsonObject.getString("Q_ID");

                    if (Q_ID.equals("") || Q_ID == null || Q_ID.equals("")){
//                        imgChat.setVisibility(View.GONE);
                        mBinding.tvSendMessage.setVisibility(View.GONE);
                    }
                    else if (CurrentQ_ID.equals("")){
//                        imgChat.setVisibility(View.GONE);
                        mBinding.tvSendMessage.setVisibility(View.GONE);
                    }
                    else {
//                        imgChat.setVisibility(View.VISIBLE);
                        mBinding.tvSendMessage.setVisibility(View.VISIBLE);
                        occupant_id = Integer.parseInt(Q_ID);

                        List<Integer> tags = new ArrayList<>();
                        tags.add(Integer.parseInt(CurrentQ_ID));
                        tags.add(occupant_id);
                        // tags.add(App.getSampleConfigs().getUsersTag());

                        QBUsers.getUsersByIDs(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                            @Override
                            public void onSuccess(ArrayList<QBUser> result, Bundle params) {
                                //  QBChatDialog dialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_QB_DIALOG);
                                selectedUsers.addAll(result);
                            }

                            @Override
                            public void onError(QBResponseException e) {
                      /*  showErrorSnackbar(R.string.select_users_get_users_error, e,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loadUsersFromQb();
                                    }
                                });
                        progressBar.setVisibility(View.GONE);*/
                            }
                        });

                    }

                    if (jsonObject.getString("Attachment_FileName").toString().equals("") || jsonObject.getString("Attachment_FileName").toString() == null ||
                            jsonObject.getString("Attachment_FileName").toString().equals("null")) {

                        mBinding.includeLayoutDetails.txtAttachment.setVisibility(View.GONE);
                        mBinding.includeLayoutDetails.lblAttachment.setVisibility(View.GONE);
                    }
                    else {
                        mBinding.includeLayoutDetails.txtAttachment.setVisibility(View.VISIBLE);
                        mBinding.includeLayoutDetails.lblAttachment.setVisibility(View.VISIBLE);

                        mBinding.includeLayoutDetails.txtAttachment.setText(jsonObject.getString("Attachment_FileName").toString());
                    }

                    if (strfbUrl.equals("") || strfbUrl.equals(null))
                    {
                        mBinding.includeLayoutSocial.fbUrl.setImageResource(R.drawable.ic_fb_gray);
                        mBinding.includeLayoutSocial.fbUrl.setEnabled(false);
                    }
                    else {
                        mBinding.includeLayoutSocial.fbUrl.setImageResource(R.drawable.icon_fb);
                        mBinding.includeLayoutSocial.fbUrl.setEnabled(true);
                    }

                    if (stryoutubeUrl.equals("") || stryoutubeUrl.equals(null))
                    {
                        mBinding.includeLayoutSocial.youtubeUrl.setImageResource(R.drawable.icon_utube_gray);
                        mBinding.includeLayoutSocial.youtubeUrl.setEnabled(false);
                    }
                    else {
                        mBinding.includeLayoutSocial.youtubeUrl.setImageResource(R.drawable.icon_utube_red);
                        mBinding.includeLayoutSocial.youtubeUrl.setEnabled(true);
                    }

                    if (strgoogleUrl.equals("") || strgoogleUrl.equals(null))
                    {
                        mBinding.includeLayoutSocial.googleUrl.setImageResource(R.drawable.ic_google_gray);
                        mBinding.includeLayoutSocial.googleUrl.setEnabled(false);
                    }
                    else {
                        mBinding.includeLayoutSocial.googleUrl.setImageResource(R.drawable.icon_google);
                        mBinding.includeLayoutSocial.googleUrl.setEnabled(true);
                    }

                    if (strtwitterUrl.equals("") || strtwitterUrl.equals(null))
                    {
                        mBinding.includeLayoutSocial.twitterUrl.setImageResource(R.drawable.icon_twitter_gray);
                        mBinding.includeLayoutSocial.twitterUrl.setEnabled(false);
                    }
                    else {
                        mBinding.includeLayoutSocial.twitterUrl.setImageResource(R.drawable.icon_twitter);
                        mBinding.includeLayoutSocial.twitterUrl.setEnabled(true);
                    }

                    if (stryoutubeUrl.equals("") || stryoutubeUrl.equals(null))
                    {
                        mBinding.includeLayoutSocial.youtubeUrl.setImageResource(R.drawable.icon_utube_gray);
                        mBinding.includeLayoutSocial.youtubeUrl.setEnabled(false);
                    }
                    else {
                        mBinding.includeLayoutSocial.youtubeUrl.setImageResource(R.drawable.icon_utube_red);
                        mBinding.includeLayoutSocial.youtubeUrl.setEnabled(true);
                    }


                    if (strlinkedInUrl.equals("") || strlinkedInUrl.equals(null))
                    {
                        mBinding.includeLayoutSocial.linkedInUrl.setImageResource(R.drawable.icon_linkedin_gray);
                        mBinding.includeLayoutSocial.linkedInUrl.setEnabled(false);
                    }
                    else {
                        mBinding.includeLayoutSocial.linkedInUrl.setImageResource(R.drawable.icon_linkedin);
                        mBinding.includeLayoutSocial.linkedInUrl.setEnabled(true);
                    }


//                        txtName.setText(jsonObject.getString("FirstName")+" "+jsonObject.getString("LastName"));
//                        txtDesi.setText(jsonObject.getString("Designation"));
//                        txtCompany.setText(jsonObject.getString("CompanyName"));
//                        txtEmail.setText(jsonObject.getString("Emailid"));
//                        txtMob.setText(jsonObject.getString("PrimaryPhone"));
//                        txtPH.setText(jsonObject.getString("OfficePhone"));
                        /*txtAddress.setText(jsonObject.getString("Address1") + " " + jsonObject.getString("Address2")
                            + " " + jsonObject.getString("Address3") + " " + jsonObject.getString("Address4")
                            + " " + jsonObject.getString("City")  + " " + jsonObject.getString("State")
                            + " " + jsonObject.getString("Country") + " " + jsonObject.getString("Postalcode"));*/
//                    txtWebsite.setText(jsonObject.getString("Website"));

                    personName = jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName");

                    if (personName.equalsIgnoreCase("") || personName.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutViepager.txtName.setText("Person");
                    } else {
                        mBinding.includeLayoutViepager.txtName.setText(personName);
                    }

                    if (Designation.equalsIgnoreCase("")
                            || Designation.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutViepager.txtDesi.setText("Designation");
                        mBinding.includeLayoutViepager.txtDesi.setVisibility(View.GONE);
                    } else {
                        mBinding.includeLayoutViepager.txtDesi.setText(Designation);
                    }

                    if (IndustryName.equalsIgnoreCase("") || IndustryName == null)
                    {
                        mBinding.includeLayoutDetails.llIndustryBox.setVisibility(View.GONE);
                    } else {
                        mBinding.includeLayoutDetails.txtIndustry.setText(IndustryName.trim().toString());
                    }

                    if (CompanyName.equalsIgnoreCase("")
                            || CompanyName.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutViepager.txtCompany.setText("Company");
                        mBinding.includeLayoutViepager.txtCompany.setVisibility(View.GONE);
                    } else {
                        mBinding.includeLayoutViepager.txtCompany.setText(CompanyName);
                    }

                    if (jsonObject.getString("Website").equalsIgnoreCase("")
                            || jsonObject.getString("Website").equalsIgnoreCase(null)) {
                        mBinding.includeLayoutDetails.txtWebsite.setText("Website");
                        mBinding.includeLayoutDetails.llWebsiteBox.setVisibility(View.GONE);
                    } else {
                        mBinding.includeLayoutDetails.txtWebsite.setText(jsonObject.getString("Website"));
                    }

                    if (Email1.equalsIgnoreCase("")
                            || Email1.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutDetails.txtEmail.setText("Email Address");
                        mBinding.includeLayoutDetails.llEmailBox.setVisibility(View.GONE);
                    } else {
                        mBinding.includeLayoutDetails.txtEmail.setText(Email1);
                    }

                    if (Phone1.equalsIgnoreCase("")
                            || Phone1.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutDetails.txtPH.setText("Phone No.");
                        mBinding.includeLayoutDetails.llTeleBox.setVisibility(View.GONE);
                    } else {
                        //                   Phone1.trim();
//                        Phone1 = Phone1.trim();
                        Phone1 = Phone1.replaceAll("\\s+", "").trim();
                        /*int number = Integer.parseInt(Phone1);
//                        Phone1 = Phone1.replaceAll("\\s++$", "");
                        String number1 = String.valueOf(number);
                        txtPH.setText(String.valueOf(number));*/
                        mBinding.includeLayoutDetails.txtPH.setText(Phone1);
                    }

                    if (Mobile1.equalsIgnoreCase("")
                            || Mobile1.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutDetails.txtMob.setText("Mobile No.");
                        mBinding.includeLayoutDetails.llMobileBox.setVisibility(View.GONE);
                    } else {
                        Mobile1.trim();
                        mBinding.includeLayoutDetails.txtMob.setText(Mobile1);
                    }

                    if (Fax1.equalsIgnoreCase("")
                            || Fax1.equalsIgnoreCase(null)) {
                        mBinding.includeLayoutDetails.txtWork.setText("Fax");
                        mBinding.includeLayoutDetails.llFaxBox.setVisibility(View.GONE);
                    } else {
                        Fax1.trim();
                        mBinding.includeLayoutDetails.txtWork.setText(Fax1);
                    }

                    if (userImg.equalsIgnoreCase("")) {
                        mBinding.includeLayoutViepager.imgProfileCard.setImageResource(R.drawable.usr_white1);
                        displayProfile = "";
                    } else {
                        Picasso.with(CardDetail.this).load(Utility.BASE_IMAGE_URL+"UserProfile/" + userImg)
                                .resize(300, 300)
                                .onlyScaleDown()
                                .skipMemoryCache().into(mBinding.includeLayoutViepager.imgProfileCard);
                        displayProfile = userImg;
                    }

                    if (frontCardImg.equalsIgnoreCase("") && backCardImg.equalsIgnoreCase("")) {
                        mBinding.appbar.setVisibility(View.GONE);
                    } else {
                        mBinding.appbar.setVisibility(View.VISIBLE);
                    }

                    if (frontCardImg.equalsIgnoreCase("")) {
                        recycle_image1 = Utility.BASE_IMAGE_URL+"Cards/Back_for_all.jpg";
                    } else {
                        recycle_image1 = Utility.BASE_IMAGE_URL+"Cards/" + frontCardImg;
                    }

                    if (backCardImg.equalsIgnoreCase("")) {
                        recycle_image2 = Utility.BASE_IMAGE_URL+"Cards/Back_for_all.jpg";
                    } else {
                        recycle_image2 = Utility.BASE_IMAGE_URL+"Cards/" + backCardImg;
                    }

                    image.add(recycle_image1);
                    image.add(recycle_image2);
                    myPager = new CardSwipe(getApplicationContext(), image);

                    mBinding.viewPager.setClipChildren(false);
                    mBinding.viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mBinding.viewPager.setOffscreenPageLimit(1);
                    //  mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                    mBinding.viewPager.setAdapter(myPager);

                    mBinding.viewPager1.setClipChildren(false);
                    mBinding.viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                    mBinding.viewPager1.setOffscreenPageLimit(1);
                    //   viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                    mBinding.viewPager1.setAdapter(myPager);

                    String fullAddress =
                            jsonObject.getString("Address1")+" "+jsonObject.getString("Address2")+" "+
                                    jsonObject.getString("Address3")+" "+jsonObject.getString("Address4")+" "+
                                    jsonObject.getString("City")+" "+jsonObject.getString("State")+" "+
                                    jsonObject.getString("Country")+" "+jsonObject.getString("Postalcode");

                    String add1 = jsonObject.getString("Address1");
                    String add2 = jsonObject.getString("Address2");
                    String add3 = jsonObject.getString("Address3");
                    String add4 = jsonObject.getString("Address4");
                    String city = jsonObject.getString("City");
                    String state = jsonObject.getString("State");
                    String country = jsonObject.getString("Country");
                    String postalCode = jsonObject.getString("Postalcode");

                    if (!add1.equals("")) { add1 = add1+", "; }
                    if (!add2.equals("")) { add2 = add2+", "; }
                    if (!add3.equals("")) { add3 = add3+", "; }
                    if (!add4.equals("")) { add4 = add4+", "; }
                    if (!city.equals("")) { city = city+", "; }
                    if (!state.equals("")) { state = state+", "; }
                    if (!country.equals("")) { country = country+" "; }
                    if (!postalCode.equals("")) { postalCode = postalCode ; }

                    personAddress = add1+add2+add3+add4+city+state+country+postalCode ;

                    if (personAddress.isEmpty() || personAddress.toString().length() == 0)
                    {
                        mBinding.includeLayoutDetails.llAddressBox.setVisibility(View.GONE);
                        mBinding.includeLayoutDetails.llMapView.setVisibility(View.GONE);
                    }
                    else
                    {
                        mBinding.includeLayoutDetails.txtAddress.setText(personAddress);
                        GeocodingLocation locationAddress = new GeocodingLocation();
                        locationAddress.getAddressFromLocation(fullAddress, getApplicationContext(), new GeocoderHandler());

//                       createMarker1(personAddress,(add1+add2));

                    }

                        /*FriendConnection nfcModelTag = new FriendConnection();
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

                        nfcModelTag.setNfc_tag("en000000001");
                        allTags.add(nfcModelTag);*/
//                        GetData(getContext());

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GeocoderHandler extends Handler
    {
        @Override
        public void handleMessage(Message message)
        {
            String locationAddress;
            String latLang ;
            String msg ;
            switch (message.what)
            {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    latLang = bundle.getString("latlang");

                    msg = bundle.getString("message");
                    if (msg.equalsIgnoreCase("Get Latitude and Longitude for this address location."))
                    {
                        Double Latitude = bundle.getDouble("latitude");
                        Double Longitude = bundle.getDouble("longitude");

                        createMarker(Latitude,Longitude);
                        mBinding.includeLayoutDetails.llMapView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
//                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        mBinding.includeLayoutDetails.llMapView.setVisibility(View.GONE);
                    }

                    break;
                default:
                    locationAddress = null;
                    latLang = null ;
            }
//            tvLatLang.setText(latLang+" of "+locationAddress);
        }
    }

   /* public String POST(String url) {
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
            jsonObject.accumulate("profileid", profile_id);

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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        Utility.deleteCache(getApplicationContext());
        Utility.freeMemory();
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);


        Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null)
        {
//            Toast.makeText(getApplicationContext(), "tag == null", Toast.LENGTH_LONG).show();
            //textViewInfo.setText("tag == null");
        }
        else
        {
            String tagInfo = tag.toString() + "\n";
            String id = "";
            tagInfo += "\nTag Id: \n";
            byte[] tagId = tag.getId();
            tagInfo += "length = " + tagId.length + "\n";
            for (int i = 0; i < tagId.length; i++) {
                tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                // id += Integer.toHexString(tagId[i] & 0xFF) + " ";
            }
            id = bytesToHex(tagId);
               /* try {

                    if (allTags != null){
                        Bitmap bmp = BitmapFactory.decodeByteArray(allTags.getCard_front(), 0, allTags.getCard_front().length);
                        imgCard.setImageBitmap(bmp);

                        Bitmap bmp1 = BitmapFactory.decodeByteArray(allTags.getUser_image(), 0, allTags.getUser_image().length);
                        imgProfileCard.setImageBitmap(bmp1);
                    }

                }catch (Exception e){

                }*/


            //  Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            // callData(id);
            for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values())
            {
//                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                List<NFCModel> modelList = db.getNFCbyTag(data);

                try {

                    if (modelList != null) {

                        for (NFCModel tag1 : modelList) {
                            // Toast.makeText(getApplicationContext(), tag1.getName(), Toast.LENGTH_LONG).show();

                            // Bitmap bmp = BitmapFactory.decodeByteArray(tag1.getCard_front(), 0, tag1.getCard_front().length);
                            mBinding.includeLayoutViepager.imgCard.setImageResource(tag1.getCard_front());

                            // Bitmap bmp1 = BitmapFactory.decodeByteArray(tag1.getUser_image(), 0, tag1.getUser_image().length);
                            mBinding.includeLayoutViepager.imgProfileCard.setImageResource(tag1.getUser_image());
                            mBinding.includeLayoutViepager.txtName.setText(tag1.getName());
                            mBinding.includeLayoutViepager.txtCompany.setText(tag1.getCompany());
                            mBinding.includeLayoutDetails.txtWebsite.setText(tag1.getWebsite());
                            mBinding.includeLayoutDetails.txtEmail.setText(tag1.getEmail());
                            mBinding.includeLayoutDetails.txtPH.setText(tag1.getPh_no());
                            mBinding.includeLayoutDetails.txtWork.setText(tag1.getWork_no());
                            mBinding.includeLayoutDetails.txtMob.setText(tag1.getMob_no());
                            mBinding.includeLayoutDetails.txtAddress.setText(tag1.getAddress());
                            //  txtRemark.setText(tag1.getRemark());
                            mBinding.includeLayoutViepager.txtDesi.setText(tag1.getDesignation());
                            image.add(String.valueOf(tag1.getCard_front()));   // its change from integer to string
                            image.add(String.valueOf(tag1.getCard_back()));    // its change from integer to string
                            myPager = new CardSwipe(getApplicationContext(), image);

                            mBinding.viewPager.setClipChildren(false);
                            mBinding.viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                            mBinding.viewPager.setOffscreenPageLimit(1);
                            //   mViewPager.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer

                            mBinding.viewPager1.setAdapter(myPager);

                            mBinding.viewPager1.setClipChildren(false);
                            mBinding.viewPager1.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
                            mBinding.viewPager1.setOffscreenPageLimit(1);
                            //    viewPager1.setPageTransformer(false, new CarouselEffectTransformer(getApplicationContext())); // Set transformer
                            mBinding.viewPager1.setAdapter(myPager);

                        }
                    }

                } catch (Exception e) {

                }
            }
        }
    }

    private class HttpAsyncTaskGroupsFetch extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  dialog = new ProgressDialog(CardDetail.this);
            dialog.setMessage("Fetching My Account...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected String doInBackground(String... urls)
        {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ProfileId", profile_id);
                jsonObject.accumulate("UserId", user_id );
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1" );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
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
                    String success = jsonObject.getString("success").toString();
                    String message = jsonObject.getString("message").toString();
                    String counts = jsonObject.getString("Count").toString();

//                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                    JSONArray groupsArray = jsonObject.getJSONArray("Groups");

                    ArrayList<String> img = null;
                    ArrayList<String> name = null;
                    ArrayList<String> desc = null;

                    if (groupsArray.length() == 0)
                    {
                        mBinding.includeLayoutDetails.tvAddedGroupInfo.setVisibility(View.VISIBLE);
                        mBinding.includeLayoutDetails.recyclerView.setVisibility(View.GONE);
                    }
                    else
                    {
                        for (int i = 0; i < groupsArray.length(); i++)
                        {
                            JSONObject groupsObj = groupsArray.getJSONObject(i);

                            String groupid = groupsObj.getString("group_ID");
                            String groupname = groupsObj.getString("group_Name");
                            String groupdesc = groupsObj.getString("group_desc");
                            String groupphoto = groupsObj.getString("group_photo");

                            img = new ArrayList<>();
                            name = new ArrayList<>();
                            desc = new ArrayList<>();

                            img.add(groupphoto);
                            name.add(groupname);
                            desc.add(groupdesc);
                        }

                        if (name.size() == 0)
                        {
                            mBinding.includeLayoutDetails.tvAddedGroupInfo.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            mBinding.includeLayoutDetails.tvAddedGroupInfo.setVisibility(View.GONE);
                        }
                            /*GroupsInCardDetailAdapter groupsInCardDetailAdapter = new GroupsInCardDetailAdapter(CardDetail.this, img,name,desc);
                            groupListView.setAdapter(groupsInCardDetailAdapter);
                            groupsInCardDetailAdapter.notifyDataSetChanged();*/

                        GroupsRecyclerAdapter groupsRecyclerAdapter = new GroupsRecyclerAdapter(CardDetail.this, img, name, desc);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mBinding.includeLayoutDetails.recyclerView.setLayoutManager(new LinearLayoutManager(CardDetail.this, LinearLayoutManager.HORIZONTAL, true));
//                             recycler_view.setLayoutManager(mLayoutManager);
                        mBinding.includeLayoutDetails.recyclerView.setItemAnimator(new DefaultItemAnimator());
                        mBinding.includeLayoutDetails.recyclerView.setAdapter(groupsRecyclerAdapter);
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Not able to fetch circles", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

 /*   public  String FetchGroupDataPost(String url)
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
            jsonObject.accumulate("ProfileId", profile_id);
            jsonObject.accumulate("UserId", user_id );
            jsonObject.accumulate("numofrecords", "10");
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
    }*/
}
