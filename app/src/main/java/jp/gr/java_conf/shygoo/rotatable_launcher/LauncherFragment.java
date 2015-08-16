package jp.gr.java_conf.shygoo.rotatable_launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ランチャー画面用Fragment
 */
public class LauncherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container);
        view.findViewById(R.id.button_app_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppList();
            }
        });
        return view;
    }

    /**
     * アプリ一覧画面を表示
     */
    private void showAppList() {
        Intent intent = new Intent(getActivity(), AppListActivity.class);
        startActivity(intent);
    }
}
