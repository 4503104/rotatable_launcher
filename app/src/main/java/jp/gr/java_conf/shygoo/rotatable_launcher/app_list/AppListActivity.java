package jp.gr.java_conf.shygoo.rotatable_launcher.app_list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.gr.java_conf.shygoo.rotatable_launcher.R;

/**
 * アプリ一覧画面
 */
public class AppListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
    }
}
