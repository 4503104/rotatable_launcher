package jp.gr.java_conf.shygoo.rotatable_launcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * ランチャー画面（ホーム画面）
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }
}
