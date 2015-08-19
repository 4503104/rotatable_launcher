package jp.gr.java_conf.shygoo.rotatable_launcher.home;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Method;

import jp.gr.java_conf.shygoo.rotatable_launcher.R;
import jp.gr.java_conf.shygoo.rotatable_launcher.util.PreferencesUtil;

/**
 * 画面サイズ計測画面用Fragment
 */
public class MeasureFragment extends Fragment {

    // Fragment呼び出し時の画面の向き
    private int orgOrientation;

    // 各種サイズ
    private Point realSizePort;
    private int statusBarHeightPort;
    private int navigationBarHeightPort;
    private Point realSizeLand;
    private int statusBarHeightLand;
    private int navigationBarWidthLand;
    private int navigationBarHeightLand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 画面の向きを覚えておく
        orgOrientation = getActivity().getRequestedOrientation();

        // サイズ計測の為、向きを固定（縦）
        // TODO: 回転非対応の端末を考慮
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 計測中に表示しておく画面
        View view = inflater.inflate(R.layout.fragment_measure, container, false);

        // 画面サイズ計測準備
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            // 画面レイアウト完了時の処理
            @Override
            public void onGlobalLayout() {
                switch (getActivity().getRequestedOrientation()) {
                    case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:

                        // 縦画面のサイズ測定
                        measurePortraitSize();
                        break;
                    case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:

                        // このListenerを解除（念の為）
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        // 横画面のサイズ測定
                        measureLandscapeSize();
                        break;
                    default:
                        // あり得ない
                        break;
                }
            }
        });
        return view;
    }

    /**
     * 縦サイズ計測
     */
    private void measurePortraitSize() {

        // 各種画面サイズを取得
        realSizePort = getRealSize();
        Point logicalSizePort = getLogicalSize();
        Point availableSizePort = getAvailableSize();

        // ナビゲーションバー（画面下 or なし）の高さを算出
        navigationBarHeightPort = realSizePort.y - logicalSizePort.y;

        // ステータスバー（画面上）の高さを算出
        statusBarHeightPort = logicalSizePort.y - availableSizePort.y;

        // 画面を横向きに変更
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 横サイズ計測
     */
    private void measureLandscapeSize() {

        // 各種画面サイズを取得
        realSizeLand = getRealSize();
        Point logicalSizeLand = getLogicalSize();
        Point availableSizeLand = getAvailableSize();

        // ナビゲーションバー（画面右 or 下 or なし）のサイズを算出
        navigationBarWidthLand = realSizeLand.x - logicalSizeLand.x;
        navigationBarHeightLand = realSizeLand.y - logicalSizeLand.y;

        // ステータスバー（画面上）の高さを算出
        statusBarHeightLand = logicalSizeLand.y - availableSizeLand.y;

        // 縦サイズと比較して画面サイズを確定
        calculateLauncherSize();
    }

    /**
     * 実サイズ取得
     *
     * @return ハードウェア仕様上の縦横サイズ
     */
    private Point getRealSize() {
        Point size = new Point();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 専用メソッド
            display.getRealSize(size);
        } else {
            try {
                // 隠しメソッド
                Method getRawWidth = Display.class.getMethod("getRawWidth");
                Method getRawHeight = Display.class.getMethod("getRawHeight");
                int width = (Integer) getRawWidth.invoke(display);
                int height = (Integer) getRawHeight.invoke(display);
                size.set(width, height);
            } catch (Exception e) {
            }
        }
        return size;
    }

    /**
     * 論理サイズ取得
     *
     * @return アプリが実際に利用できる縦横サイズ（ナビゲーションバーを含まない）
     */
    private Point getLogicalSize() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        return size;
    }

    /**
     * 有効サイズ取得
     *
     * @return この画面が占有中の縦横サイズ（ステータスバー・アクションバー・ナビゲーションバーを含まない）
     */
    private Point getAvailableSize() {
        Point size = new Point();
        View currentView = getView();
        size.set(currentView.getMeasuredWidth(), currentView.getMeasuredHeight());
        return size;
    }

    /**
     * ランチャー画面のサイズ計算
     *
     * 端末をどの角度に回転しても全く同じ位置に表示できるようなViewサイズを算出する。
     *
     * TODO: このメソッドでカバーできない端末がないか確認
     */
    private void calculateLauncherSize() {

        // 画面実サイズから、長辺と短辺を決定
        int displayLengthLong = realSizeLand.x;
        if (realSizePort.y < displayLengthLong) {
            displayLengthLong = realSizePort.y;
        }
        int displayLengthShort = realSizeLand.y;
        if (realSizePort.x < displayLengthShort) {
            displayLengthShort = realSizePort.x;
        }

        // ステータスバー(縦画面上)・ナビゲーションバー(縦画面下/横画面右)の内、最も大きいものが長辺の余白
        int paddingLongSide = statusBarHeightPort;
        if (navigationBarHeightPort > paddingLongSide) {
            paddingLongSide = navigationBarHeightPort;
        }
        if (navigationBarWidthLand > paddingLongSide) {
            paddingLongSide = navigationBarWidthLand;
        }

        // ステータスバー(横画面上)・ナビゲーションバー(横画面下)の内、大きい方が短辺の余白
        int paddingShortSide = statusBarHeightLand;
        if (navigationBarHeightLand > paddingShortSide) {
            paddingShortSide = navigationBarHeightLand;
        }

        // 画面実サイズから余白を引いたものが、利用可能サイズ
        int viewLengthLong = displayLengthLong - paddingLongSide * 2;
        int viewLengthShort = displayLengthShort - paddingShortSide * 2;

        // 置けるアイコンの数を算出
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int gridSize = getResources().getDimensionPixelSize(R.dimen.launcher_grid_size);
        int gridCountLong = viewLengthLong / gridSize;
        int gridCountShort = viewLengthShort / gridSize;
        PreferencesUtil.setGridCounts(getActivity(), gridCountLong, gridCountShort);

        // アイコンの数から、最終的なViewサイズを確定
        viewLengthLong = gridSize * gridCountLong;
        viewLengthShort = gridSize * gridCountShort;

        // Viewサイズと各種バーのサイズから、最終的な余白サイズを確定
        paddingLongSide = (displayLengthLong - viewLengthLong) / 2;
        paddingShortSide = (displayLengthShort - viewLengthShort) / 2;

        int paddingPortLeft = paddingShortSide;
        int paddingPortTop = paddingLongSide - statusBarHeightPort;
        int paddingPortRight = paddingShortSide;
        int paddingPortBottom = paddingLongSide - navigationBarHeightPort;
        PreferencesUtil.setPaddingsPort(getActivity(), paddingPortLeft, paddingPortTop, paddingPortRight, paddingPortBottom);

        int paddingLandLeft = paddingLongSide;
        int paddingLandTop = paddingShortSide - statusBarHeightLand;
        int paddingLandRight = paddingLongSide - navigationBarWidthLand;
        int paddingLandBottom = paddingShortSide - navigationBarHeightLand;
        PreferencesUtil.setPaddingsLand(getActivity(), paddingLandLeft, paddingLandTop, paddingLandRight, paddingLandBottom);

        // ランチャー画面を表示
        showLauncher();
    }

    /**
     * ランチャー画面表示
     */
    private void showLauncher() {

        // 画面の向きを元に戻しておく
        getActivity().setRequestedOrientation(orgOrientation);

        // 計測完了
        PreferencesUtil.setMeasured(getActivity());

        // この画面を終了し、ランチャー画面を起動
        getFragmentManager().beginTransaction()
                .hide(this)
                .remove(this)
                .add(android.R.id.content, new LauncherFragment())
                .commit();
    }
}
