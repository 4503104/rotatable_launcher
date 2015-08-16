package jp.gr.java_conf.shygoo.rotatable_launcher;

import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * アプリ一覧用Fragment
 */
public class AppListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(createLaunchableAppListAdapter());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // 選択されたアプリを起動する
        ComponentName item = (ComponentName) getListAdapter().getItem(position);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(item);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    /**
     * アプリ情報をAdapter化して返す
     *
     * @return 端末にインストール済みの（起動可能な）アプリ一覧のAdapter
     */
    private ListAdapter createLaunchableAppListAdapter() {

        // 起動可能なActivityの一覧を取得
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        // コンポート名として利用
        List<ComponentName> items = new ArrayList<>(activities.size());
        for (ResolveInfo act : activities) {
            items.add(new ComponentName(act.activityInfo.packageName, act.activityInfo.name));
        }

        // Adapterを生成して返す
        return new AppListAdapter(getActivity(), items);
    }

    /**
     * 一覧に表示するアプリ情報を返すAdapter
     */
    public static class AppListAdapter extends ArrayAdapter<ComponentName> {

        public AppListAdapter(Context context, List<ComponentName> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Viewは使い回す
            View view;
            if (convertView == null) {
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.item_app_list, null);
            } else {
                view = convertView;
            }

            // アプリ(厳密にはActivity)の情報を取得
            Drawable icon;
            CharSequence label;
            ComponentName item = getItem(position);
            PackageManager packageManager = getContext().getPackageManager();
            try {
                ActivityInfo activityInfo = packageManager.getActivityInfo(item, 0);
                icon = activityInfo.loadIcon(packageManager);// FIXME: 動くけど警告ログが出る
                label = activityInfo.loadLabel(packageManager);
            } catch (PackageManager.NameNotFoundException e) {

                // 万一取れなかったらデフォルト値を使用
                icon = getContext().getResources().getDrawable(R.drawable.ic_launcher);
                label = getContext().getString(R.string.label_unknown_app);
            }

            // アイコンとラベルをセット
            ((ImageView) view.findViewById(R.id.app_icon)).setImageDrawable(icon);
            ((TextView) view.findViewById(R.id.app_label)).setText(label);

            // Activity情報がセットされたViewを返す
            return view;
        }
    }
}
