package jp.gr.java_conf.shygoo.rotatable_launcher.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preferences用便利クラス
 */
public class PreferencesUtil {

    private static final String PREFERENCES_NAME = "rotatableLauncher";

    private static final String PREFERENCES_KEY_GRID_COUNT_LONG = "gridCountLong";
    private static final String PREFERENCES_KEY_GRID_COUNT_SHORT = "gridCountShort";
    private static final String PREFERENCES_KEY_PADDING_PORT_LEFT = "paddingPortLeft";
    private static final String PREFERENCES_KEY_PADDING_PORT_TOP = "paddingPortTop";
    private static final String PREFERENCES_KEY_PADDING_PORT_RIGHT = "paddingPortRight";
    private static final String PREFERENCES_KEY_PADDING_PORT_BOTTOM = "paddingPortBottom";
    private static final String PREFERENCES_KEY_PADDING_LAND_LEFT = "paddingLandLeft";
    private static final String PREFERENCES_KEY_PADDING_LAND_TOP = "paddingLandTop";
    private static final String PREFERENCES_KEY_PADDING_LAND_RIGHT = "paddingLandRight";
    private static final String PREFERENCES_KEY_PADDING_LAND_BOTTOM = "paddingLandBottom";
    private static final String PREFERENCES_KEY_MEASURED = "measured";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 画面サイズ計測済み判定
     *
     * @param context
     * @return 画面サイズの計測が完了したか否か
     */
    public static boolean isMeasured(Context context) {
        return getPreferences(context).getBoolean(PREFERENCES_KEY_MEASURED, false);
    }

    /**
     * 画面サイズ計測完了
     *
     * @param context
     */
    public static void setMeasured(Context context) {
        getPreferences(context).edit().putBoolean(PREFERENCES_KEY_MEASURED, true).apply();
    }

    /**
     * ランチャーに表示するアイコン数の設定
     *
     * @param context
     * @param countLong 長辺方向のアイコン配置可能数
     * @param countShort 短辺方向のアイコン配置可能数
     */
    public static void setGridCounts(Context context, int countLong, int countShort) {
        getPreferences(context).edit()
                .putInt(PREFERENCES_KEY_GRID_COUNT_LONG, countLong)
                .putInt(PREFERENCES_KEY_GRID_COUNT_SHORT, countShort)
                .apply();
    }

    public static int getPaddingPortLeft(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_PORT_LEFT, 0);
    }

    public static int getPaddingPortTop(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_PORT_TOP, 0);
    }

    public static int getPaddingPortRight(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_PORT_RIGHT, 0);
    }

    public static int getPaddingPortBottom(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_PORT_BOTTOM, 0);
    }

    /**
     * 縦画面のマージン設定
     *
     * @param context
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setPaddingsPort(Context context, int left, int top, int right, int bottom) {
        getPreferences(context).edit()
                .putInt(PREFERENCES_KEY_PADDING_PORT_LEFT, left)
                .putInt(PREFERENCES_KEY_PADDING_PORT_TOP, top)
                .putInt(PREFERENCES_KEY_PADDING_PORT_RIGHT, right)
                .putInt(PREFERENCES_KEY_PADDING_PORT_BOTTOM, bottom)
                .apply();
    }

    public static int getPaddingLandLeft(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_LAND_LEFT, 0);
    }

    public static int getPaddingLandTop(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_LAND_TOP, 0);
    }

    public static int getPaddingLandRight(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_LAND_RIGHT, 0);
    }

    public static int getPaddingLandBottom(Context context) {
        return getPreferences(context).getInt(PREFERENCES_KEY_PADDING_LAND_BOTTOM, 0);
    }

    /**
     * 横画面のマージン設定
     *
     * @param context
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setPaddingsLand(Context context, int left, int top, int right, int bottom) {
        getPreferences(context).edit()
                .putInt(PREFERENCES_KEY_PADDING_LAND_LEFT, left)
                .putInt(PREFERENCES_KEY_PADDING_LAND_TOP, top)
                .putInt(PREFERENCES_KEY_PADDING_LAND_RIGHT, right)
                .putInt(PREFERENCES_KEY_PADDING_LAND_BOTTOM, bottom)
                .apply();
    }
}
