package com.golddragon.tools;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import com.golddragon.constant.ConstantsUtil;

public class MyProvider extends ContentProvider {
	private static final String TAG = "provider";
	private Context mContext;
	private DbHelper helper;
	private SQLiteDatabase database;
	private static final UriMatcher uriMatcher;
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ConstantsUtil.AUTHORITY, ConstantsUtil.TABLE, ConstantsUtil.CODE_ALL_DATA);
		uriMatcher.addURI(ConstantsUtil.AUTHORITY, ConstantsUtil.TABLE+"/#", ConstantsUtil.CODE_SPECIFIC_DATA);
	}
	
	@Override
	public boolean onCreate() {
		mContext = this.getContext();
		helper = new DbHelper(mContext);
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
		case ConstantsUtil.CODE_ALL_DATA:
        	return ConstantsUtil.CONTENT_ALL_TYPE;
		case ConstantsUtil.CODE_SPECIFIC_DATA:
        	return ConstantsUtil.CONTENT_SPECIFIC_TYPE;
		default:
            throw new IllegalArgumentException("Unknown URI"+uri);
        }

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		database = helper.getWritableDatabase();
	    long rowId = database.insert(ConstantsUtil.TABLE,"",values);
        Uri noteUri = ContentUris.withAppendedId(ConstantsUtil.CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(noteUri, null);
        return noteUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		database = helper.getWritableDatabase();
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case ConstantsUtil.CODE_ALL_DATA:
			count = database.delete(ConstantsUtil.TABLE, selection, selectionArgs);
			break;
		case ConstantsUtil.CODE_SPECIFIC_DATA:
			
			String id = uri.getPathSegments().get(1);
			count = database.delete(ConstantsUtil.TABLE, ConstantsUtil.ID+"="+id+(!TextUtils.isEmpty(selection)?" AND("+selection+')':""),selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		//getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		database = helper.getWritableDatabase();
		count = database.update(ConstantsUtil.TABLE, values, selection, selectionArgs);
		/*getContext().getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()){
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
			}
        });*/
		return count;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		database = helper.getReadableDatabase();
		Cursor c = null;
		switch (uriMatcher.match(uri)) {
		case ConstantsUtil.CODE_ALL_DATA:
			c = database.query(ConstantsUtil.TABLE, projection, selection, selectionArgs, null, null, null);
			break;
		case ConstantsUtil.CODE_SPECIFIC_DATA:
			
			String id = uri.getPathSegments().get(1);
			c = database.query(ConstantsUtil.TABLE, projection, ConstantsUtil.ID+"="+id+(!TextUtils.isEmpty(selection)?" AND("+selection+')':""),selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		return c;
	}

}
