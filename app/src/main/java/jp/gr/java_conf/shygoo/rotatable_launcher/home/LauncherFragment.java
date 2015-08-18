package jp.gr.java_conf.shygoo.rotatable_launcher.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.gr.java_conf.shygoo.rotatable_launcher.R;
import jp.gr.java_conf.shygoo.rotatable_launcher.app_list.AppListActivity;
import jp.gr.java_conf.shygoo.rotatable_launcher.util.PreferencesUtil;

/**
 * ランチャー画面用Fragment
 */
public class LauncherFragment extends Fragment {

    @Bind(R.id.button_app_list)
    Button buttonAppList;

    @Bind(R.id.launcher_root)
    FrameLayout launcherRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        ButterKnife.bind(this, view);

        // 画面角度に応じて余白サイズを調整
        setPadding();

        // クリックイベントを設定
        buttonAppList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppList();
            }
        });
        return view;
    }

    /**
     * 余白サイズ調整
     */
    private void setPadding() {

        //TODO: 回転対応
        launcherRoot.setPadding(
                PreferencesUtil.getPaddingPortLeft(getActivity()),
                PreferencesUtil.getPaddingPortTop(getActivity()),
                PreferencesUtil.getPaddingPortRight(getActivity()),
                PreferencesUtil.getPaddingPortBottom(getActivity())
        );
    }

    /**
     * アプリ一覧画面を表示
     */
    private void showAppList() {
        Intent intent = new Intent(getActivity(), AppListActivity.class);
        startActivity(intent);
    }
}
