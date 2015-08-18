package jp.gr.java_conf.shygoo.rotatable_launcher.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import jp.gr.java_conf.shygoo.rotatable_launcher.R;
import jp.gr.java_conf.shygoo.rotatable_launcher.util.PreferencesUtil;

/**
 * ホーム画面
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 画面サイズ計測済みか否かでFragment切り替え
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (PreferencesUtil.isMeasured(this)) {

            // ランチャー画面へ
            fragmentTransaction.add(R.id.container_home, new LauncherFragment());
        } else {

            // 画面サイズ計測画面へ
            fragmentTransaction.add(R.id.container_home, new MeasureFragment());
        }
        fragmentTransaction.commit();
    }
}
