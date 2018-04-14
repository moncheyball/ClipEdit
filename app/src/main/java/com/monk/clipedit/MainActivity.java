package com.monk.clipedit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 編集画面
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EditActivity";

    // 表示アイテム
    private EditText mEditText;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 常駐Notification設定
        NotificationWrapper notification = new NotificationWrapper(this);
        notification.setNotification();

        // 入力エリアの設定
        mEditText = findViewById(R.id.editText);

        // OSのクリップボード 及び DB のデータを取得して EditText に反映
        updateEditText(0);

        // Toolbar の設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // コピーボタンの設定
        FloatingActionButton fabCopy = findViewById(R.id.fab_copy);
        fabCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCopy(view);
            }
        });

        // 左メニューの設定
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerOpened(View drawerView) {
                // 左メニュー表示時にキーボードを閉じる
                InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if (null == inputMethodMgr) {
                    Log.d(TAG, "null == inputMethodMgr");
                }
                else {
                    inputMethodMgr.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ナビゲーションバーの生成
        onCreateNavigationView();
    }

    /**
     * NavigationView の生成
     */
    protected void onCreateNavigationView() {
        // ナビゲーションバーの設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // クリップボードの内容を左メニューに表示
        String[] clipboardTexts = getClipboardTexts();
        if (null == clipboardTexts) {
            Log.d(TAG, "null == clipboardTexts");
        }
        else {
            int id = 0;
            Menu menuDrawer = navigationView.getMenu();
            for (String clipboardText : clipboardTexts) {
                if (null == clipboardText) {
                    Log.d(TAG, "null == clipboardText");
                }
                else if (clipboardText.isEmpty()) {
                    Log.d(TAG, "clipboardText.isEmpty()");
                }
                else {
                    menuDrawer.add(R.id.activity_main_drawer, id, Menu.CATEGORY_CONTAINER, clipboardText);
                }
                id++;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        boolean retSelected = true;

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO 設定
        } else if (id == R.id.action_clear) {
            // クリア：エディットテキストの初期化
            mEditText.getText().clear();
        } else {
            retSelected = super.onOptionsItemSelected(item);
        }

        return retSelected;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "clickCopy");

        int id = item.getItemId();
        Log.d(TAG, "item.getItemId() : " + id);

        if (0 <= id && id <= ClipboardManagerWrapper.MAX_DATA_SIZE) {
            updateEditText(id);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //region Private Method
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * コピーボタン押下イベント
     */
    private void clickCopy(View view) {
        Log.d(TAG, "clickCopy");

        // スナップバー表示
        Snackbar.make(view, getString(R.string.copy_to_clipboard), Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

        // テキストの内容をクリップボードに設定
        ClipboardManagerWrapper clipboardManager = new ClipboardManagerWrapper(this);

        String viewText = mEditText.getText().toString();
        clipboardManager.setClipboard(viewText);
    }

    /**
     * OSのクリップボード 及び DB のデータを取得して EditText に反映
     * @param index 履歴のインデックス
     */
    private void updateEditText(int index) {
        Log.d(TAG, "updateEditText");

        String[] clipboardTexts = getClipboardTexts();
        if (null == clipboardTexts) {
            Log.d(TAG, "null != clipboardTexts");
        }
        else {
            // クリップボードの内容をテキストエディタに表示
            mEditText.setText(clipboardTexts[index]);
        }
    }

    /**
     // OSのクリップボード 及び DB のデータを取得
     * @return クリップボードの履歴リスト
     */
    private String[] getClipboardTexts() {
        ClipboardManagerWrapper clipboardManager = new ClipboardManagerWrapper(this);
        clipboardManager.addPrimaryClipChangedListener();
        return clipboardManager.getClipboardTextList();
    }
}
