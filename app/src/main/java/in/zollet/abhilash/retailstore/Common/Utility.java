package in.zollet.abhilash.retailstore.Common;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.ArrayList;

import in.zollet.abhilash.retailstore.data.ProductColumns;
import in.zollet.abhilash.retailstore.data.ProductProvider;


public class Utility {

    public static void updateCount(Context context, Uri mUri, int quantity){
        ArrayList<ContentProviderOperation> addToCart = new ArrayList<ContentProviderOperation>();
        addToCart.add(ContentProviderOperation.newUpdate(mUri).withValue(ProductColumns.QUANTITY,quantity).build());
        try {
            context.getContentResolver().applyBatch(ProductProvider.AUTHORITY, addToCart);
        } catch (RemoteException | OperationApplicationException e) {
            Toast.makeText(context, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateFAVProduct (Context context, Uri mUri,int isFav){
        ArrayList<ContentProviderOperation> updateFAV = new ArrayList<ContentProviderOperation>();
        updateFAV.add(ContentProviderOperation.newUpdate(mUri).withValue(ProductColumns.IS_FAV,isFav*(-1)).build());
        try {
            context.getContentResolver().applyBatch(ProductProvider.AUTHORITY, updateFAV);
        } catch (RemoteException | OperationApplicationException e) {
            Toast.makeText(context, "OOPS! Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
