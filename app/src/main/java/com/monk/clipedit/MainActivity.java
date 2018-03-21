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
import android.widget.EditText;

/**
 * 編集画面
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EditActivity";

    // 表示アイテム
    private EditText mEditText;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 常駐Notification設定
        NotificationWrapper notification = new NotificationWrapper(this);
        notification.setNotification();

        // 入力エリアの設定
        mEditText = findViewById(R.id.editText);

        // OSのクリップボード 及び DB のデータを取得
        ClipboardManagerWrapper clipboardManager = new ClipboardManagerWrapper(this);
        clipboardManager.addPrimaryClipChangedListener();
        String[] clipboardTexts = clipboardManager.getClipboardTextList();
        if (null != clipboardTexts) {
            // クリップボードの内容をテキストエディタに表示
            mEditText.setText(clipboardTexts[0]);
            // クリップボードの内容を左メニューに表示
//            ArrayAdapter<String> drawerList = new ArrayAdapter<>(this,  R.layout.drawer_list_item, clipboardTexts);
//            mDrawerList.setAdapter(drawerList);
        }

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
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        // ナビゲーションバーの設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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
            // 設定
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ////////// Private Method //////////
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

        // 左メニューに表示を更新
//        String[] clipboardTexts = clipboardManager.getClipboardTextList();
//        if (null != clipboardTexts) {
//            ArrayAdapter<String> drawerList = new ArrayAdapter<>(this, R.layout.drawer_list_item, clipboardTexts);
//            mDrawerList.setAdapter(drawerList);
//        }
    }

}