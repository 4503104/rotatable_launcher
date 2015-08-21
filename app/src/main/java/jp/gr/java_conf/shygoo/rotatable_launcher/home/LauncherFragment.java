package jp.gr.java_conf.shygoo.rotatable_launcher.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.gr.java_conf.shygoo.rotatable_launcher.R;
import jp.gr.java_conf.shygoo.rotatable_launcher.util.PreferencesUtil;

/**
 * ランチャー画面用Fragment
 */
public class LauncherFragment extends Fragment {

    @Bind(R.id.frame_port)
    FrameLayout framePort;

    @Bind(R.id.frame_land)
    FrameLayout frameLand;

    @Bind(R.id.grid_port_normal)
    GridView gridPortNormal;

    @Bind(R.id.grid_port_reverse)
    GridView gridPortReverse;

    @Bind(R.id.grid_land_normal)
    GridView gridLandNormal;

    @Bind(R.id.grid_land_reverse)
    GridView gridLandReverse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        ButterKnife.bind(this, view);

        // 余白を設定
        setPaddings();

        // コンテンツを設定
        setContents();

        // アイテムクリックのイベント処理
        setItemClickListeners();

        // 画面の向きに応じたコンテンツを表示
        showContents();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 画面の向きに応じたコンテンツを表示
        showContents();
    }

    /**
     * 余白設定
     */
    private void setPaddings() {

        // 縦画面・横画面のどちらも全く同じ位置に表示されるよう余白をセット
        Context context = getActivity();
        framePort.setPadding(
                PreferencesUtil.getPaddingPortLeft(context),
                PreferencesUtil.getPaddingPortTop(context),
                PreferencesUtil.getPaddingPortRight(context),
                PreferencesUtil.getPaddingPortBottom(context)
        );
        frameLand.setPadding(
                PreferencesUtil.getPaddingLandLeft(context),
                PreferencesUtil.getPaddingLandTop(context),
                PreferencesUtil.getPaddingLandRight(context),
                PreferencesUtil.getPaddingLandBottom(context)
        );
    }

    /**
     * コンテンツ設定
     * TODO: ユーザ自身が自由にアイコンを配置できるように
     * TODO: ページングを実現
     * TODO: ウィジェットにも対応
     */
    private void setContents() {

        // GridViewに表示するデータ配列を作成
        Context context = getActivity();
        int gridCountLong = PreferencesUtil.getGridCountLong(context);
        int gridCountShort = PreferencesUtil.getGridCountShort(context);
        ComponentName[][] apps = new ComponentName[gridCountLong][gridCountShort];

        // 起動可能なActivityの一覧を取得
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        // コンポート名として利用
        int size = activities.size();
        List<ComponentName> names = new ArrayList<>(size);
        for (ResolveInfo act : activities) {
            names.add(new ComponentName(act.activityInfo.packageName, act.activityInfo.name));
        }

        // 配列に格納（とりあえず適当）
        int i = 0;
        for (int x = 0; x < gridCountLong; x++) {
            for (int y = 0; y < gridCountShort; y++) {
                if (i == size) {
                    break;
                }
                apps[x][y] = names.get(i++);
            }
        }

        // GridViewの列数を固定
        gridPortNormal.setNumColumns(gridCountShort);
        gridPortReverse.setNumColumns(gridCountShort);
        gridLandNormal.setNumColumns(gridCountLong);
        gridLandReverse.setNumColumns(gridCountLong);

        // 4方向どこから見ても同じ配置になるようアイコンの順序を入れ替えて使用
        gridPortNormal.setAdapter(new LauncherAdapter(context, getAppListPortNormal(apps)));
        gridPortReverse.setAdapter(new LauncherAdapter(context, getAppListPortReverse(apps)));
        gridLandNormal.setAdapter(new LauncherAdapter(context, getAppListLandNormal(apps)));
        gridLandReverse.setAdapter(new LauncherAdapter(context, getAppListLandReverse(apps)));
    }

    /**
     * アプリ情報List化(縦・通常)
     * @param apps
     * @return
     */
    private List<ComponentName> getAppListPortNormal(ComponentName[][] apps) {
        int gridCountLong = apps.length;
        int gridCountShort = apps[0].length;
        List<ComponentName> appList = new ArrayList<>();

        // 格納した順序でList化
        for (int x = 0; x < gridCountLong; x++) {
            for (int y = 0; y < gridCountShort; y++) {
                appList.add(apps[x][y]);
            }
        }
        return appList;
    }

    /**
     * アプリ情報List化(縦・逆さ)
     * @param apps
     * @return
     */
    private List<ComponentName> getAppListPortReverse(ComponentName[][] apps) {
        int gridCountLong = apps.length;
        int gridCountShort = apps[0].length;
        List<ComponentName> appList = new ArrayList<>();

        // 逆順でList化
        for (int x = gridCountLong - 1; x >= 0; x--) {
            for (int y = gridCountShort - 1; y >= 0; y--) {
                appList.add(apps[x][y]);
            }
        }
        return appList;
    }

    /**
     * アプリ情報List化(横・通常)
     * @param apps
     * @return
     */
    private List<ComponentName> getAppListLandNormal(ComponentName[][] apps) {
        int gridCountLong = apps.length;
        int gridCountShort = apps[0].length;
        List<ComponentName> appList = new ArrayList<>();

        // x軸とy軸を入れ替え、y軸のみ逆順でList化
        for (int y = gridCountShort - 1; y >= 0; y--) {
            for (int x = 0; x < gridCountLong; x++) {
                appList.add(apps[x][y]);
            }
        }
        return appList;
    }

    /**
     * アプリ情報List化(横・逆さ)
     * @param apps
     * @return
     */
    private List<ComponentName> getAppListLandReverse(ComponentName[][] apps) {
        int gridCountLong = apps.length;
        int gridCountShort = apps[0].length;
        List<ComponentName> appList = new ArrayList<>();

        // x軸とy軸を入れ替え、x軸のみ逆順でList化
        for (int y = 0; y < gridCountShort; y++) {
            for (int x = gridCountLong - 1; x >= 0; x--) {
                appList.add(apps[x][y]);
            }
        }
        return appList;
    }

    /**
     * アイコンクリック時のイベント処理
     */
    private void setItemClickListeners() {

        // リスナーインスタンスを作成
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 選択されたアプリを起動する
                ComponentName item = (ComponentName) parent.getAdapter().getItem(position);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(item);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
            }
        };

        // どの画面の向きでも有効
        gridPortNormal.setOnItemClickListener(listener);
        gridPortReverse.setOnItemClickListener(listener);
        gridLandNormal.setOnItemClickListener(listener);
        gridLandReverse.setOnItemClickListener(listener);
    }

    /**
     * コンテンツ表示
     * TODO: 画面の向きや角度が想定外の場合に対応
     */
    private void showContents() {

        // 現在の画面の向き（縦/横）
        int orientation = getResources().getConfiguration().orientation;

        // 現在の画面の角度(0/90/180/270)
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

        // 角度に応じたViewを1個だけ表示
        framePort.setVisibility(View.INVISIBLE);
        frameLand.setVisibility(View.INVISIBLE);
        gridPortNormal.setVisibility(View.INVISIBLE);
        gridPortReverse.setVisibility(View.INVISIBLE);
        gridLandNormal.setVisibility(View.INVISIBLE);
        gridLandReverse.setVisibility(View.INVISIBLE);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            framePort.setVisibility(View.VISIBLE);
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                gridPortNormal.setVisibility(View.VISIBLE);
            } else {
                gridPortReverse.setVisibility(View.VISIBLE);
            }
        } else {
            frameLand.setVisibility(View.VISIBLE);
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                gridLandNormal.setVisibility(View.VISIBLE);
            } else {
                gridLandReverse.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * ランチャー画面用Adapter
     */
    public static class LauncherAdapter extends ArrayAdapter<ComponentName> {

        public LauncherAdapter(Context context, List<ComponentName> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Viewは使い回す
            View view;
            if (convertView == null) {
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.item_launcher, null);
            } else {
                view = convertView;
            }

            // アプリ(厳密にはActivity)の情報を取得
            ComponentName item = getItem(position);

            // アプリ情報があればアイコンとラベルを取得
            Drawable icon = null;
            CharSequence label = "";
            if (item != null) {
                PackageManager packageManager = getContext().getPackageManager();
                try {
                    ActivityInfo activityInfo = packageManager.getActivityInfo(item, 0);
                    icon = activityInfo.loadIcon(packageManager);// FIXME: 動くけど警告ログが出る
                    label = activityInfo.loadLabel(packageManager);
                } catch (PackageManager.NameNotFoundException e) {
                    icon = null;
                    label = "";
                }
            }

            // アイコンとラベルをセット
            ((ImageView) view.findViewById(R.id.app_icon)).setImageDrawable(icon);
            ((TextView) view.findViewById(R.id.app_label)).setText(label);

            // Activity情報がセットされたViewを返す
            return view;
        }
    }
}
