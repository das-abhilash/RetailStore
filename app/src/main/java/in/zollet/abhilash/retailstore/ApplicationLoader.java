package in.zollet.abhilash.retailstore;

import android.app.Application;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.ArrayList;

import in.zollet.abhilash.retailstore.Common.AvailableProduct;
import in.zollet.abhilash.retailstore.data.ProductColumns;
import in.zollet.abhilash.retailstore.data.ProductProvider;

/*
* TO UPDATE AVAILABLE PRODUCT LIST
* ADD THE PRODUCT IN THE "loadArrayList".
* THEN UPDATE THE db VERSION IN THE ProductDatabase.java
* */

public class ApplicationLoader extends Application {

    private ArrayList<AvailableProduct> availableProducts = new ArrayList<AvailableProduct>();
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("loadDB", true) || prefs.getBoolean("updateDB", false)) {
            loadDB();
        }
    }

    private void loadDB() {
        loadArrayList();
        loadArrayListToDB();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("loadDB", false)
                .putBoolean("updateDB", false).apply();
    }

    private void loadArrayListToDB() {

        int size = availableProducts.size();

        ArrayList<ContentProviderOperation> news = new ArrayList<ContentProviderOperation>();
        news.add(ContentProviderOperation.newDelete(ProductProvider.Product.CONTENT_URI).build());
        for (int i = 0; i < size; i++) {
            ContentValues values = new ContentValues();

            values.put(ProductColumns.NAME, String.valueOf(availableProducts.get(i).getProductName()));
            values.put(ProductColumns.IMAGE, String.valueOf(availableProducts.get(i).getProductImage()));
            values.put(ProductColumns.ACTUAL_PRICE, String.valueOf(availableProducts.get(i).getProductActualPrice()));
            values.put(ProductColumns.SELLING_PRICE, String.valueOf(availableProducts.get(i).getProductSellingPrice()));
            values.put(ProductColumns.CATEGORY, String.valueOf(availableProducts.get(i).getProductCategory()));
            values.put(ProductColumns.IS_FAV, -1);
            values.put(ProductColumns.QUANTITY, 0);

            news.add(ContentProviderOperation.newInsert(ProductProvider.Product.CONTENT_URI).withValues(values).build());
        }
        try {
            getApplicationContext().getContentResolver().
                    applyBatch(ProductProvider.AUTHORITY, news);
        } catch (RemoteException | OperationApplicationException e) {
            Toast.makeText(this, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadArrayList() {
        availableProducts.add(new AvailableProduct("LG Microwave", "8000","7000", R.drawable.lgmicrowave, "Electronics"));
        availableProducts.add(new AvailableProduct("Samsung Microwave", "7000","6000", R.drawable.samsungmicrowave, "Electronics"));
        availableProducts.add(new AvailableProduct("Sony Microwave", "10000","9000", R.drawable.panasonicmicrowave, "Electronics"));
        availableProducts.add(new AvailableProduct("LG TV", "18000","17000", R.drawable.lgtv, "Electronics"));
        availableProducts.add(new AvailableProduct("Samsung TV", "19000","18000", R.drawable.samsungtv, "Electronics"));
        availableProducts.add(new AvailableProduct("Sony TV", "16000","15000", R.drawable.sonytv, "Electronics"));
        availableProducts.add(new AvailableProduct("LG Vaccum Cleaner", "2000","1000", R.drawable.vaccumcleaner1, "Electronics"));
        availableProducts.add(new AvailableProduct("Samsung Vaccum Cleaner", "3000","2000", R.drawable.vaccumcleaner2, "Electronics"));
        availableProducts.add(new AvailableProduct("Sony Vaccum Cleaner", "4000","3000", R.drawable.vaccumcleanr3, "Electronics"));
        availableProducts.add(new AvailableProduct("Dining Table", "8000","7000", R.drawable.diningtable, "Furniture"));
        availableProducts.add(new AvailableProduct("Regular Table", "6000","5000", R.drawable.regulartable, "Furniture"));
        availableProducts.add(new AvailableProduct("Dining Chair", "10000","9000", R.drawable.diningchair, "Furniture"));
        availableProducts.add(new AvailableProduct("Regular Chair", "5000","4000", R.drawable.regularchair, "Furniture"));
        availableProducts.add(new AvailableProduct("Double Door Almirah", "12000","8000", R.drawable.doubledooralmirha, "Furniture"));
        availableProducts.add(new AvailableProduct("Multi Door Almirah", "10000","8000", R.drawable.multidooralmirha, "Furniture"));

    }
}
