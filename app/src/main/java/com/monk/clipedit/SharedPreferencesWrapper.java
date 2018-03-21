package com.monk.clipedit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * SharedPreferences にアクセスするためのクラス
 */

class SharedPreferencesWrapper {
    private static final String TAG = "SharedPref...";

    private SharedPreferences mSharedPref;

    ///// Key /////
    // ClipBoard Text List
    // (最大数は ClipboardManagerWrapper の MAX_DATA_SIZE で管理しているため、変更時はそちらも)
    private static final String CLIPBOARD_TEXT = "clipboardText";
    private static final String CLIPBOARD_TEXT_00 = "clipboardText00";
    private static final String CLIPBOARD_TEXT_01 = "clipboardText01";
    private static final String CLIPBOARD_TEXT_02 = "clipboardText02";
    private static final String CLIPBOARD_TEXT_03 = "clipboardText03";
    private static final String CLIPBOARD_TEXT_04 = "clipboardText04";
    private static final String CLIPBOARD_TEXT_05 = "clipboardText05";

    SharedPreferencesWrapper(Context context) {
        if (null == mSharedPref) {
            mSharedPref = context.getSharedPreferences(CLIPBOARD_TEXT, Context.MODE_PRIVATE);
        }
    }

    void setClipboardTextList(String[] clipboardTextList) {
        Log.d(TAG, "setClipboardTextList()");

        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(CLIPBOARD_TEXT_00, clipboardTextList[0]);
        editor.putString(CLIPBOARD_TEXT_01, clipboardTextList[1]);
        editor.putString(CLIPBOARD_TEXT_02, clipboardTextList[2]);
        editor.putString(CLIPBOARD_TEXT_03, clipboardTextList[3]);
        editor.putString(CLIPBOARD_TEXT_04, clipboardTextList[4]);
        editor.putString(CLIPBOARD_TEXT_05, clipboardTextList[5]);
        editor.apply();
    }

    String[] getClipboardTextList() {
        Log.d(TAG, "getClipboardTextList()");

        String[] retClipboardTextList = {
                mSharedPref.getString(CLIPBOARD_TEXT_00, null),
                mSharedPref.getString(CLIPBOARD_TEXT_01, null),
                mSharedPref.getString(CLIPBOARD_TEXT_02, null),
                mSharedPref.getString(CLIPBOARD_TEXT_03, null),
                mSharedPref.getString(CLIPBOARD_TEXT_04, null),
                mSharedPref.getString(CLIPBOARD_TEXT_05, null)
        };
        return retClipboardTextList;
    }

}
