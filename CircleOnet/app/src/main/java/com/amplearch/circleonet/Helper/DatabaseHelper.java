package com.amplearch.circleonet.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.amplearch.circleonet.Model.NFCModel;
import com.amplearch.circleonet.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper
{

    Context context;
	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "CircleOneDatabase";
	// Table Names
	private static final String TABLE_NFC = "nfc_data";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_COMPANY = "company";
	private static final String KEY_DESIGNATION = "designation";
	private static final String KEY_MOB = "mob_no";
	private static final String KEY_WORK = "work_no";
	private static final String KEY_PH = "ph_no";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_WEBSITE = "website";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LNG = "lng";
	private static final String KEY_REMARK = "remark";
    private static final String KEY_FACEBOOK_ID = "fb_id";
    private static final String KEY_LINKEDIN_ID = "linkedin_id";
    private static final String KEY_GOOGLE_ID = "google_id";
    private static final String KEY_TWITTER_ID = "twitter_id";
    private static final String KEY_YOUTUBE_ID = "youtube_id";
    private static final String KEY_CARD_FRONT = "card_front";
	private static final String KEY_CARD_BACK = "card_back";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_NFC_TAG = "nfc_tag";
    private static final String KEY_USER_IMG = "user_image";
    private static final String KEY_DATE = "date";

	private static final String CREATE_TABLE_NFC = "CREATE TABLE "
			+ TABLE_NFC
			+ "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
			+ KEY_COMPANY + " TEXT,"
			+ KEY_DESIGNATION + " TEXT,"
			+ KEY_MOB + " TEXT,"
            + KEY_WORK + " TEXT,"
			+ KEY_PH + " TEXT,"
			+ KEY_EMAIL + " TEXT,"
			+ KEY_WEBSITE + " TEXT,"
			+ KEY_ADDRESS + " TEXT,"
            + KEY_LAT + " TEXT,"
            + KEY_LNG + " TEXT,"
            + KEY_REMARK + " TEXT,"
            + KEY_FACEBOOK_ID + " TEXT,"
            + KEY_LINKEDIN_ID + " TEXT,"
            + KEY_GOOGLE_ID + " TEXT,"
            + KEY_TWITTER_ID + " TEXT,"
            + KEY_YOUTUBE_ID + " TEXT,"
            + KEY_CARD_FRONT + " BLOB,"
            + KEY_CARD_BACK + " BLOB,"
            + KEY_ACTIVE + " TEXT,"
            + KEY_NFC_TAG + " TEXT,"
            + KEY_USER_IMG + " BLOB,"
            + KEY_DATE + " TEXT"
			+ ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
	}

    public boolean deleteNFCbyID(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NFC, KEY_ID + "=" + id, null) > 0;
    }

	public boolean verification(String id) throws SQLException
	{
		int count = -1;
		Cursor c = null;
		try {
			String query = "SELECT COUNT(*) FROM " + TABLE_NFC + " WHERE " + KEY_NFC_TAG + " = ? AND " + KEY_ACTIVE +" = ?";
			SQLiteDatabase db = this.getWritableDatabase();
			c = db.rawQuery(query, new String[] {id, "true"});
			if (c.moveToFirst())
			{
				count = c.getInt(0);
			}
			return count > 0;
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
	}

	public void deleteNFC(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NFC, null, null);
    }

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		// creating required tables
		db.execSQL(CREATE_TABLE_NFC);

        ByteArrayOutputStream baosf1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosb1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosf2 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosb2 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosf3 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosb3 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosf4 = new ByteArrayOutputStream();
        ByteArrayOutputStream baosb4 = new ByteArrayOutputStream();

        Bitmap bitmapf1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card1f);
        Bitmap bitmapb1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card1b);

        Bitmap bitmapf2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card2f);
        Bitmap bitmapb2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card2f);

        Bitmap bitmapf3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card3_1);
        Bitmap bitmapb3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card3_1);

        Bitmap bitmapf4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card4);
        Bitmap bitmapb4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.card4);

        bitmapf1.compress(Bitmap.CompressFormat.JPEG, 100, baosf1);
        bitmapb1.compress(Bitmap.CompressFormat.JPEG, 100, baosb1);

        bitmapf2.compress(Bitmap.CompressFormat.JPEG, 100, baosf2);
        bitmapb2.compress(Bitmap.CompressFormat.JPEG, 100, baosb2);

        bitmapf3.compress(Bitmap.CompressFormat.JPEG, 100, baosf3);
        bitmapb3.compress(Bitmap.CompressFormat.JPEG, 100, baosb3);

        bitmapf4.compress(Bitmap.CompressFormat.JPEG, 100, baosf4);
        bitmapb4.compress(Bitmap.CompressFormat.JPEG, 100, baosb4);

        byte[] imageBytesf1 = baosf1.toByteArray();
        byte[] imageBytesb1 = baosb1.toByteArray();

        byte[] imageBytesf2 = baosf2.toByteArray();
        byte[] imageBytesb2 = baosb2.toByteArray();

        byte[] imageBytesf3 = baosf3.toByteArray();
        byte[] imageBytesb3 = baosb3.toByteArray();

        byte[] imageBytesf4 = baosf4.toByteArray();
        byte[] imageBytesb4 = baosb4.toByteArray();
       // String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        ByteArrayOutputStream user1 = new ByteArrayOutputStream();
        ByteArrayOutputStream user2 = new ByteArrayOutputStream();
        ByteArrayOutputStream user3 = new ByteArrayOutputStream();
        ByteArrayOutputStream user4 = new ByteArrayOutputStream();

        Bitmap bitmapu1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile2);
        Bitmap bitmapu2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile1);
        Bitmap bitmapu3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile3);
        Bitmap bitmapu4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile4);

        bitmapu1.compress(Bitmap.CompressFormat.JPEG, 100, user1);
        bitmapu2.compress(Bitmap.CompressFormat.JPEG, 100, user2);
        bitmapu3.compress(Bitmap.CompressFormat.JPEG, 100, user3);
        bitmapu4.compress(Bitmap.CompressFormat.JPEG, 100, user4);

        byte[] imageBytesu1 = user1.toByteArray();
        byte[] imageBytesu2 = user2.toByteArray();
        byte[] imageBytesu3 = user3.toByteArray();
        byte[] imageBytesu4 = user4.toByteArray();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, "Jonathan Por");
        values.put(KEY_COMPANY, "PIF Capital Pte Ltd");
        values.put(KEY_DESIGNATION, "Chairman");
        values.put(KEY_MOB, "+65 9005 0101");
        values.put(KEY_WORK, "+65 9005 0102");
        values.put(KEY_PH, "+65 6443 1113");
        values.put(KEY_EMAIL, "jonathan@pifcapital.com");
        values.put(KEY_WEBSITE, "www.pif.com.sg");
        values.put(KEY_ADDRESS, "53 Mohamed Sultan Road Level 3 Singapore 238993");
        values.put(KEY_LAT, "23.012102");
        values.put(KEY_LNG, "72.522634");
        values.put(KEY_REMARK, "Nothing");
        values.put(KEY_FACEBOOK_ID, "www.facebook.com");
        values.put(KEY_LINKEDIN_ID, "www.linkedin.com");
        values.put(KEY_GOOGLE_ID, "www.google.com");
        values.put(KEY_TWITTER_ID, "www.twitter.com");
        values.put(KEY_YOUTUBE_ID, "www.twitter.com");
        values.put(KEY_CARD_FRONT, imageBytesf2);
        values.put(KEY_CARD_BACK, imageBytesb2);
        values.put(KEY_ACTIVE, "true");
        values.put(KEY_NFC_TAG, "en000000001");
        values.put(KEY_USER_IMG, imageBytesu2);
        values.put(KEY_DATE, "2017-07-01 10:42:38");
        db.insert(TABLE_NFC, null, values);

        values.put(KEY_NAME, "Kiki Tai");
        values.put(KEY_COMPANY, "AtMedia");
        values.put(KEY_DESIGNATION, "Creative Director");
        values.put(KEY_MOB, "+65 9738 5801");
        values.put(KEY_WORK, "+65 9738 5802");
        values.put(KEY_PH, "+65 9738 5803");
        values.put(KEY_EMAIL, "kiki@atmedia.com.sg");
        values.put(KEY_WEBSITE, "www.atmedia.com.sg");
        values.put(KEY_ADDRESS, "10 Anson Road #26-04 International Plaza, Singapore 079903");
        values.put(KEY_LAT, "37.4224082");
        values.put(KEY_LNG, "-122.0856086");
        values.put(KEY_REMARK, "Nothing");
        values.put(KEY_FACEBOOK_ID, "www.facebook.com");
        values.put(KEY_LINKEDIN_ID, "www.linkedin.com");
        values.put(KEY_GOOGLE_ID, "www.google.com");
        values.put(KEY_TWITTER_ID, "www.twitter.com");
        values.put(KEY_YOUTUBE_ID, "www.twitter.com");
        values.put(KEY_CARD_FRONT, imageBytesf4);
        values.put(KEY_CARD_BACK, imageBytesb4);
        values.put(KEY_ACTIVE, "true");
        values.put(KEY_NFC_TAG, "en000000004");
        values.put(KEY_USER_IMG, imageBytesu1);
        values.put(KEY_DATE, "2017-06-30 12:42:38");
		db.insert(TABLE_NFC, null, values);

        values.put(KEY_NAME, "Justin Yuan Fei");
        values.put(KEY_COMPANY, "ESIN");
        values.put(KEY_DESIGNATION, "General Manager");
        values.put(KEY_MOB, "+65 9102 0862");
        values.put(KEY_WORK, "+65 6822 3908");
        values.put(KEY_PH, "+65 6822 3908");
        values.put(KEY_EMAIL, "yuanfei@esin.com.sg");
        values.put(KEY_WEBSITE, "www.esin.com.sg");
        values.put(KEY_ADDRESS, "A:60 Paya Lebar Road, #09-06 Paya Lebar Square, Singapore 409051");
        values.put(KEY_LAT, "37.4224082");
        values.put(KEY_LNG, "-122.0856086");
        values.put(KEY_REMARK, "Nothing");
        values.put(KEY_FACEBOOK_ID, "www.facebook.com");
        values.put(KEY_LINKEDIN_ID, "www.linkedin.com");
        values.put(KEY_GOOGLE_ID, "www.google.com");
        values.put(KEY_TWITTER_ID, "www.twitter.com");
        values.put(KEY_YOUTUBE_ID, "www.twitter.com");
        values.put(KEY_CARD_FRONT, imageBytesf3);
        values.put(KEY_CARD_BACK, imageBytesb3);
        values.put(KEY_ACTIVE, "false");
        values.put(KEY_NFC_TAG, "en000000002");
        values.put(KEY_USER_IMG, imageBytesu3);
        values.put(KEY_DATE, "2017-06-01 12:42:38");
        db.insert(TABLE_NFC, null, values);

        values.put(KEY_NAME, "Physician Ong");
        values.put(KEY_COMPANY, "TCMOng Medicare pte ltd");
        values.put(KEY_DESIGNATION, "");
        values.put(KEY_MOB, "9874561230");
        values.put(KEY_WORK, "0791234567");
        values.put(KEY_PH, "0792456789");
        values.put(KEY_EMAIL, "TCMOng@tcmong.com.sg");
        values.put(KEY_WEBSITE, "www.tcmong.com.sg");
        values.put(KEY_ADDRESS, "1600 Amphitheatre Parkway, Mountain View, CA");
        values.put(KEY_LAT, "37.4224082");
        values.put(KEY_LNG, "-122.0856086");
        values.put(KEY_REMARK, "Nothing");
        values.put(KEY_FACEBOOK_ID, "www.facebook.com");
        values.put(KEY_LINKEDIN_ID, "www.linkedin.com");
        values.put(KEY_GOOGLE_ID, "www.google.com");
        values.put(KEY_TWITTER_ID, "www.twitter.com");
        values.put(KEY_YOUTUBE_ID, "www.twitter.com");
        values.put(KEY_CARD_FRONT, imageBytesf1);
        values.put(KEY_CARD_BACK, imageBytesb1);
        values.put(KEY_ACTIVE, "true");
        values.put(KEY_NFC_TAG, "en000000003");
        values.put(KEY_USER_IMG, imageBytesu4);
        values.put(KEY_DATE, "2017-07-01 12:42:38");
        db.insert(TABLE_NFC, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NFC);

		// create new tables
		onCreate(db);
	}

	public long createNFC(NFCModel favourites, long[] tag_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, favourites.getId());
		values.put(KEY_NAME, favourites.getName());
		values.put(KEY_COMPANY, favourites.getCompany());
        values.put(KEY_DESIGNATION, favourites.getDesignation());
        values.put(KEY_MOB, favourites.getMob_no());
        values.put(KEY_WORK, favourites.getWork_no());
        values.put(KEY_PH, favourites.getPh_no());
        values.put(KEY_EMAIL, favourites.getEmail());
        values.put(KEY_WEBSITE, favourites.getWebsite());
        values.put(KEY_ADDRESS, favourites.getAddress());
        values.put(KEY_LAT, favourites.getLat());
        values.put(KEY_LNG, favourites.getLng());
        values.put(KEY_REMARK, favourites.getRemark());
        values.put(KEY_FACEBOOK_ID, favourites.getFb_id());
        values.put(KEY_LINKEDIN_ID, favourites.getLinkedin_id());
        values.put(KEY_GOOGLE_ID, favourites.getGoogle_id());
        values.put(KEY_TWITTER_ID, favourites.getTwitter_id());
        values.put(KEY_YOUTUBE_ID, favourites.getYoutube_id());
        values.put(KEY_CARD_FRONT, favourites.getCard_front());
        values.put(KEY_CARD_BACK, favourites.getCard_back());
        values.put(KEY_ACTIVE, favourites.getActive());
        values.put(KEY_NFC_TAG, favourites.getNfc_tag());
        values.put(KEY_USER_IMG, favourites.getUser_image());
        values.put(KEY_DATE, favourites.getDate());

        // insert row
		long todo_id = db.insert(TABLE_NFC, null, values);


		return todo_id;
	}

	/*public List<NFCModel> getNFCData()
	{
		List<NFCModel> todos = new ArrayList<NFCModel>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = null ;

		int value = 3 ;

		try{
			String selectQuery = TABLE_NFC + " * " + KEY_ID + " IN " + value ;

			Log.d(LOG, selectQuery);

			String Table = TABLE_FAVOURITES ;
			String[] table_Columns = new String[] { " * " };
			String[] args_Data = new String[] { "1,3,5,7" };
			String selctioonArgs = Arrays.toString(args_Data);

			selctioonArgs = selctioonArgs.replace("[","(");
			selctioonArgs = selctioonArgs.replace("]",")");

			String whereClause = KEY_ID + " IN " + selctioonArgs ;

			c = db.query(Table,table_Columns,whereClause,null,null,null,null);
		}catch (Exception e){
			e.printStackTrace();
		}
		if(c.moveToFirst())
		{
		do
			{
				Favourites fvt = new Favourites();
				fvt.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				fvt.setProduct_id((c.getString(c.getColumnIndex(KEY_PRODUCTID))));
				fvt.setUser_id(c.getString(c.getColumnIndex(KEY_USERID)));
				todos.add(fvt);
			}
			while (c.moveToNext());
		}
		return todos;
	}*/
	/*
	 * get single todo
	 */
    public List<NFCModel> getNFCbyTag(String tag) {
        List<NFCModel> tags = new ArrayList<NFCModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NFC + " WHERE " + KEY_NFC_TAG + "='" + tag + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                NFCModel td = new NFCModel();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                td.setCompany(c.getString(c.getColumnIndex(KEY_COMPANY)));
                td.setDesignation(c.getString(c.getColumnIndex(KEY_DESIGNATION)));
                td.setMob_no(c.getString(c.getColumnIndex(KEY_MOB)));
                td.setWork_no(c.getString(c.getColumnIndex(KEY_WORK)));
                td.setPh_no(c.getString(c.getColumnIndex(KEY_PH)));
                td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                td.setWebsite(c.getString(c.getColumnIndex(KEY_WEBSITE)));
                td.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
                td.setRemark(c.getString(c.getColumnIndex(KEY_REMARK)));
                td.setFb_id(c.getString(c.getColumnIndex(KEY_FACEBOOK_ID)));
                td.setLinkedin_id(c.getString(c.getColumnIndex(KEY_LINKEDIN_ID)));
                td.setGoogle_id(c.getString(c.getColumnIndex(KEY_GOOGLE_ID)));
                td.setTwitter_id(c.getString(c.getColumnIndex(KEY_TWITTER_ID)));
                td.setYoutube_id(c.getString(c.getColumnIndex(KEY_YOUTUBE_ID)));
                td.setCard_front(c.getBlob(c.getColumnIndex(KEY_CARD_FRONT)));
                td.setCard_back(c.getBlob(c.getColumnIndex(KEY_CARD_BACK)));
                td.setActive(c.getString(c.getColumnIndex(KEY_ACTIVE)));
                td.setNfc_tag(c.getString(c.getColumnIndex(KEY_NFC_TAG)));
                td.setUser_image(c.getBlob(c.getColumnIndex(KEY_USER_IMG)));
                td.setDate(c.getString(c.getColumnIndex(KEY_DATE)));

                // adding to todo list
                tags.add(td);
            } while (c.moveToNext());
        }

        return tags;
    }

    public List<NFCModel> getActiveNFC() {
        List<NFCModel> tags = new ArrayList<NFCModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NFC + " WHERE " + KEY_ACTIVE + "='true' ORDER BY datetime(\"date\") DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                NFCModel td = new NFCModel();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                td.setCompany(c.getString(c.getColumnIndex(KEY_COMPANY)));
                td.setDesignation(c.getString(c.getColumnIndex(KEY_DESIGNATION)));
                td.setMob_no(c.getString(c.getColumnIndex(KEY_MOB)));
                td.setWork_no(c.getString(c.getColumnIndex(KEY_WORK)));
                td.setPh_no(c.getString(c.getColumnIndex(KEY_PH)));
                td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                td.setWebsite(c.getString(c.getColumnIndex(KEY_WEBSITE)));
                td.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
                td.setRemark(c.getString(c.getColumnIndex(KEY_REMARK)));
                td.setFb_id(c.getString(c.getColumnIndex(KEY_FACEBOOK_ID)));
                td.setLinkedin_id(c.getString(c.getColumnIndex(KEY_LINKEDIN_ID)));
                td.setGoogle_id(c.getString(c.getColumnIndex(KEY_GOOGLE_ID)));
                td.setTwitter_id(c.getString(c.getColumnIndex(KEY_TWITTER_ID)));
                td.setYoutube_id(c.getString(c.getColumnIndex(KEY_YOUTUBE_ID)));
                td.setCard_front(c.getBlob(c.getColumnIndex(KEY_CARD_FRONT)));
                td.setCard_back(c.getBlob(c.getColumnIndex(KEY_CARD_BACK)));
                td.setActive(c.getString(c.getColumnIndex(KEY_ACTIVE)));
                td.setNfc_tag(c.getString(c.getColumnIndex(KEY_NFC_TAG)));
                td.setUser_image(c.getBlob(c.getColumnIndex(KEY_USER_IMG)));
                td.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                // adding to todo list
                tags.add(td);
            } while (c.moveToNext());
        }

        return tags;
    }


    public List<NFCModel> getAllNFC() {
        List<NFCModel> tags = new ArrayList<NFCModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NFC;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                NFCModel td = new NFCModel();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                td.setCompany(c.getString(c.getColumnIndex(KEY_COMPANY)));
                td.setDesignation(c.getString(c.getColumnIndex(KEY_DESIGNATION)));
                td.setMob_no(c.getString(c.getColumnIndex(KEY_MOB)));
                td.setWork_no(c.getString(c.getColumnIndex(KEY_WORK)));
                td.setPh_no(c.getString(c.getColumnIndex(KEY_PH)));
                td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                td.setWebsite(c.getString(c.getColumnIndex(KEY_WEBSITE)));
                td.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
                td.setRemark(c.getString(c.getColumnIndex(KEY_REMARK)));
                td.setFb_id(c.getString(c.getColumnIndex(KEY_FACEBOOK_ID)));
                td.setLinkedin_id(c.getString(c.getColumnIndex(KEY_LINKEDIN_ID)));
                td.setGoogle_id(c.getString(c.getColumnIndex(KEY_GOOGLE_ID)));
                td.setTwitter_id(c.getString(c.getColumnIndex(KEY_TWITTER_ID)));
                td.setYoutube_id(c.getString(c.getColumnIndex(KEY_YOUTUBE_ID)));
                td.setCard_front(c.getBlob(c.getColumnIndex(KEY_CARD_FRONT)));
                td.setCard_back(c.getBlob(c.getColumnIndex(KEY_CARD_BACK)));
                td.setActive(c.getString(c.getColumnIndex(KEY_ACTIVE)));
                td.setNfc_tag(c.getString(c.getColumnIndex(KEY_NFC_TAG)));
                td.setUser_image(c.getBlob(c.getColumnIndex(KEY_USER_IMG)));
                td.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                // adding to tags list
                tags.add(td);
            } while (c.moveToNext());
        }
        return tags;
	}

	public int getNFCCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NFC;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a todo
	 */

    public boolean tagVerification(String id) throws SQLException
    {
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM " + TABLE_NFC + " WHERE " + KEY_NFC_TAG + " = ?";
            SQLiteDatabase db = this.getWritableDatabase();
            c = db.rawQuery(query, new String[] {id});
            if (c.moveToFirst())
            {
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public int makeCardActive(String id, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean aBoolean = tagVerification(id);
        if (aBoolean == true) {
            ContentValues values = new ContentValues();
            values.put(KEY_NFC_TAG, id);
            values.put(KEY_ACTIVE, "true");
            values.put(KEY_DATE, date);
            return db.update(TABLE_NFC, values, KEY_NFC_TAG + " = ?",
                    new String[] { String.valueOf(id) });
        }else {
            return 11;
        }
        // updating row
    }


	public int DeactiveCards(int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_ACTIVE, "false");
		// updating row
		return db.update(TABLE_NFC, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}


	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
}
