package com.monk.clipedit;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * ClipboardManager にアクセスするためのクラス
 * 参考 http://develop.logical-studio.com/development/android_app_beginner/20161215-android_clipboard_copy/
 */
class ClipboardManagerWrapper {
    private static final String TAG = "ClipboardManagerWrapper";

    private Context mContext;
    // ClipData クラス使用のための Key
    private static final String TEXT_DATA = "text_data";

    static private String[] sClipboardTextList;
    // ClipBoard Text List の最大数
    // (※変更時は SharedPreferencesWrapper の Key も変更する必要あり)
    static final int MAX_DATA_SIZE = 6;

    /**
     * コンストラクタ
     * @param context コンテキスト
     */
    ClipboardManagerWrapper(Context context) {
        Log.d(TAG, "ClipboardManagerWrapper()");
        this.mContext = context;
        initializeClipboardTextList();
    }

    /**
     * addPrimaryClipChangedListener を設定（クリップボード監視用のリスナー）
     */
    void addPrimaryClipChangedListener() {
        Log.d(TAG, "onPrimaryClipChanged()");

        ClipboardManager clipboardManager = (ClipboardManager) this.mContext.getSystemService(CLIPBOARD_SERVICE);
        if (null != clipboardManager) {
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Log.d(TAG, "onPrimaryClipChanged()");

                    updateClipboardTextList();
                }
            });
        }
    }

    /**
     * アプリ内で保持しているクリップボードテキストデータの配列を取得
     * @return アプリ内で保持しているクリップボードテキストデータの配列
     */
    String[] getClipboardTextList() {
        Log.d(TAG, "getClipboardTextList()");

        String[] retList = null;
        int dataSize = 0;
        // 中身のある（nullでない）要素の数を算出（dataSize）
        for (int i = 0 ; i < MAX_DATA_SIZE ; i++) {
            if (null != sClipboardTextList[i]) {
                dataSize++;
            }
        }
        // 中身のある（nullでない）要素だけの配列を生成して返却
        // （※nullがある配列を Navigation Drawer に設定できないため）
        if (0 != dataSize) {
            retList = new String[dataSize];
            for (int i = 0 ; i < dataSize ; i++) {
                retList[i] = sClipboardTextList[i];
            }
        }
        return retList;
    }

    /**
     * 文字列をクリップボードに設定
     * @param text クリップボードに設定する文字列
     */
    void setClipboard(String text) {
        Log.d(TAG, "setClipboard(" + text +")");

        //クリップボードに格納するClipDataオブジェクトの作成
        ClipData clipData = ClipData.newPlainText(TEXT_DATA, text);

        //クリップボードにデータを格納
        ClipboardManager clipboardManager = (ClipboardManager) this.mContext.getSystemService(CLIPBOARD_SERVICE);
        if (null != clipboardManager) {
            clipboardManager.setPrimaryClip(clipData);
        }

        // ローカルのclipboardTextListを更新
        updateClipboardTextList();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //region Private Method
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * clipboardTextList の初期化
     * コンストラクタ内から呼び出す
     */
    private void initializeClipboardTextList() {
        Log.d(TAG, "initializeClipboardTextList()");

        if (null == sClipboardTextList) {
            sClipboardTextList = new String[MAX_DATA_SIZE];

            SharedPreferencesWrapper sharedPref = new SharedPreferencesWrapper(mContext);
            sClipboardTextList = sharedPref.getClipboardTextList();

            updateClipboardTextList();
        }
    }

    /**
     * clipboardTextList 更新
     */
    private void updateClipboardTextList() {
        Log.d(TAG, "updateClipboardTextList()");

        String primaryClipText = getPrimaryClipText();
        if (null == primaryClipText) {
            Log.d(TAG, "null == primaryClipText");
        } else if (!primaryClipText.equals(sClipboardTextList[0])) {
            // 先頭要素とOSのクリップボードの値が異なる場合、
            // sClipboardTextList の先頭にOSのクリップボードの値を挿入
            for (int i = MAX_DATA_SIZE-1 ; i > 0 ; i--) {
                if (null != sClipboardTextList[i-1]) {
                    sClipboardTextList[i] = sClipboardTextList[i-1];
                }
            }
            sClipboardTextList[0] = primaryClipText;
        }

        // SharedPreferences に保存
        SharedPreferencesWrapper sharedPref = new SharedPreferencesWrapper(mContext);
        sharedPref.setClipboardTextList(sClipboardTextList);
    }

    /**
     * OSのクリップボードテキストデータを取得
     * @return OSのクリップボードテキストデータ
     */
    private String getPrimaryClipText() {
        Log.d(TAG, "getClipboard()");

        String retClipboardText = null;
        ClipData clipData = getClipData();
        if (null != clipData) {
            // クリップデータからテキストを取得
            ClipData.Item item = clipData.getItemAt(0);
            retClipboardText = item.getText().toString();
        }
        return retClipboardText;
    }

    /**
     * ClipData 取得
     * @return ClipData
     */
    private ClipData getClipData() {
        Log.d(TAG, "getClipData()");
        // システムのクリップボードを取得
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        if (null == clipboardManager) {
            Log.d(TAG, "null == clipboardManager");
            return null;
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (!clipboardManager.hasPrimaryClip()) {
            Log.d(TAG, "hasPrimaryClip() == false");
            return null;
        }
        else if (!(clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                && !(clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
            Log.d(TAG, "not MIMETYPE_TEXT_PLAIN or MIMETYPE_TEXT_HTML");
            return null;
        }
        else {
            Log.d(TAG, "success!");
            return clipData;
        }
    }

    //endregion

}
