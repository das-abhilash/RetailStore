package in.zollet.abhilash.retailstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

import in.zollet.abhilash.retailstore.ApplicationLoader;

@Database(version = ProductDatabase.VERSION)
public class ProductDatabase {
    private ProductDatabase() {
    }
    static final int VERSION = 1;

    @Table(ProductColumns.class)
    public static final String PRODUCT = "Products";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {

    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        PreferenceManager.getDefaultSharedPreferences(ApplicationLoader.applicationContext).edit().putBoolean("loadDB", false)
                .putBoolean("updateDB", false).apply();

    }
}
