package in.zollet.abhilash.retailstore.UI;

import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.zollet.abhilash.retailstore.Common.Utility;
import in.zollet.abhilash.retailstore.R;
import in.zollet.abhilash.retailstore.data.ProductColumns;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductDetailActivityFragment extends Fragment {

    public static final String DETAIL_URI = "detailURL";

    private Uri mUri;
    private int  quantity;

    private boolean mTransitionAnimation;
    int isFAV;
    ImageView productImage;
    Button buy_now,addToCart,productFAV;
    TextView productName, productSellingPrice, productActualPrice, productDiscount;

    public ProductDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(ProductDetailActivityFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        productFAV = (Button) rootView.findViewById(R.id.productFAV);
        productImage = (ImageView) rootView.findViewById(R.id.productImage);
        productName = (TextView) rootView.findViewById(R.id.productName);
        productSellingPrice = (TextView) rootView.findViewById(R.id.productSellingPrice);
        productActualPrice = (TextView) rootView.findViewById(R.id.productActualPrice);
        productDiscount = (TextView) rootView.findViewById(R.id.productDiscount);
        addToCart = (Button) rootView.findViewById(R.id.addToCart);
        buy_now = (Button) rootView.findViewById(R.id.buy_now);

        final Cursor cursor = getContext().getContentResolver().query(mUri,null,null,null,null);
        if(cursor != null && cursor.getCount()!= 0){

            cursor.moveToFirst();
            isFAV = cursor.getInt(cursor.getColumnIndex(ProductColumns.IS_FAV));
            if(isFAV ==1){
                Drawable img = getContext().getResources().getDrawable( R.drawable.ic_favorite_black_24dp );
                productFAV.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                productFAV.setText("Added to Wishlist");
            } else {
                Drawable img = getContext().getResources().getDrawable( R.drawable.ic_favorite_border_black_24dp );
                productFAV.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                productFAV.setText(getResources().getString(R.string.added_to_wishlist));
            }
            quantity = cursor.getInt(cursor.getColumnIndex(ProductColumns.QUANTITY));
            String name=cursor.getString(cursor.getColumnIndex(ProductColumns.NAME));
            productName.setText(name);
            getActivity().setTitle(name);
            productImage.setImageResource(cursor.getInt(cursor.getColumnIndex(ProductColumns.IMAGE)));
            String actualPrice = cursor.getString(cursor.getColumnIndex(ProductColumns.ACTUAL_PRICE));
            String sellingPrice = cursor.getString(cursor.getColumnIndex(ProductColumns.SELLING_PRICE));
            String discount = String.valueOf(Math.round((Float.parseFloat(actualPrice) - Float.parseFloat(sellingPrice))/Float.parseFloat(actualPrice)*100));
            productActualPrice.setText(actualPrice);

            productSellingPrice.setText("\u20B9 " + sellingPrice);
            productActualPrice.setPaintFlags(productActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productDiscount.setText(discount + " \u0025 off");
        }

        productFAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.updateFAVProduct(getContext(),mUri,isFAV);

                if(isFAV ==1){
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_favorite_border_black_24dp );
                    productFAV.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    productFAV.setText(getResources().getString(R.string.add_to_wishlist));
                } else {
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_favorite_black_24dp );
                    productFAV.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    productFAV.setText(getResources().getString(R.string.added_to_wishlist));
                }
                isFAV= isFAV*-1;

            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = cursor.getInt(cursor.getColumnIndex(ProductColumns.QUANTITY));
                if(quantity>5){
                    Toast.makeText(getContext(), "Reached maximum count", Toast.LENGTH_SHORT).show();
                } else {
                    Utility.updateCount(getContext(), mUri, ++quantity);
                    Toast.makeText(getContext(), productName.getText() + " Added to cart", Toast.LENGTH_SHORT).show();
                }



            }
        });
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

}
