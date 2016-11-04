package in.zollet.abhilash.retailstore.UI;

import android.content.Intent;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.zollet.abhilash.retailstore.Adapters.CartAdapter;
import in.zollet.abhilash.retailstore.Common.Utility;
import in.zollet.abhilash.retailstore.R;
import in.zollet.abhilash.retailstore.data.ProductColumns;
import in.zollet.abhilash.retailstore.data.ProductProvider;

public class CartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CURSOR_LOADER_ID = 1;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    Cursor cursor;
    CardView totalPriceDetailCard,totalPriceCard;
    TextView productTotalPrice,productActualTotalPrice,productTotalDiscount,productPaybaleAmount,emptyText;
    ImageView emptyImage;
    Button checkOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productTotalPrice = (TextView) findViewById(R.id.productTotalPrice);
        productActualTotalPrice = (TextView) findViewById(R.id.productActualTotalPrice);
        productTotalDiscount = (TextView) findViewById(R.id.productTotalDiscount);
        productPaybaleAmount = (TextView) findViewById(R.id.productPaybaleAmount);
        totalPriceDetailCard = (CardView) findViewById(R.id.totalPriceDetailCard);
        totalPriceCard = (CardView) findViewById(R.id.totalPriceCard);
        emptyText = (TextView) findViewById(R.id.emptyText);
        emptyImage = (ImageView) findViewById(R.id.emptyImage);
        checkOut = (Button) findViewById(R.id.checkOut);

        recyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

        cartAdapter = new CartAdapter(this, null);
        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {

            @Override
            public void onClickRemove(View itemView, int position) {
                cursor.moveToPosition(position);
                String id = cursor.getString(cursor.getColumnIndex(ProductColumns._ID));
                Uri uri = ProductProvider.Product.ID(id);
                Utility.updateCount(getApplicationContext(),uri,0);
                resetLoader();
            }

            @Override
            public void onClickCard(View itemView, int position) {
                cursor.moveToPosition(position);
                String id = cursor.getString(cursor.getColumnIndex(ProductColumns._ID));
                Uri uri = ProductProvider.Product.ID(id);
                Intent detailIntent = new Intent(getApplicationContext(),ProductDetailActivity.class)
                        .setData(uri);
                startActivity(detailIntent);
            }

            @Override
            public void onClickAdd(View itemView, int position) {
                cursor.moveToPosition(position);
                String id = cursor.getString(cursor.getColumnIndex(ProductColumns._ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(ProductColumns.QUANTITY));
                if(quantity>5){
                    Toast.makeText(CartActivity.this, "Reached maximum count", Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = ProductProvider.Product.ID(id);
                    Utility.updateCount(getApplicationContext(), uri, quantity+1);
                    resetLoader();
                }

            }

            @Override
            public void onClickMinus(View itemView, int position) {
                cursor.moveToPosition(position);
                String id = cursor.getString(cursor.getColumnIndex(ProductColumns._ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(ProductColumns.QUANTITY));
                Uri uri = ProductProvider.Product.ID(id);
                Utility.updateCount(getApplicationContext(), uri, quantity-1);
                resetLoader();
            }
        });
        recyclerView.setAdapter(cartAdapter);

    }

    private void resetLoader() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ProductProvider.Product.CONTENT_URI, null,
                ProductColumns.QUANTITY + " !=? "
                , new String[]{"0"}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int count = data.getCount();
        cursor = data;
        getSupportActionBar().setTitle("My Cart(" +count +")");
        cartAdapter.swapCursor(data);
        if(count != 0) {
            Double totalPrice = 0.0;
            Double payblePrice = 0.0;
            Double totalDiscount = 0.0;
            while (data.moveToNext()) {
                int quantity = data.getInt(data.getColumnIndex(ProductColumns.QUANTITY));
                Float sellingPrice = Float.valueOf(data.getString(data.getColumnIndex(ProductColumns.SELLING_PRICE)));
                Float actualPrice = Float.valueOf(data.getString(data.getColumnIndex(ProductColumns.ACTUAL_PRICE)));
                totalDiscount = totalDiscount + (actualPrice - sellingPrice) * quantity;
                totalPrice = totalPrice + (actualPrice * quantity);
                payblePrice = payblePrice + (sellingPrice * quantity);
            }

            productTotalPrice.setText("₹ " + String.valueOf(payblePrice));
            productPaybaleAmount.setText("₹ " + String.valueOf(payblePrice));
            productTotalDiscount.setText("\u002D ₹ " + String.valueOf(totalDiscount));
            productActualTotalPrice.setText("₹ " + String.valueOf(totalPrice));
        } else {
            emptyImage.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
            totalPriceDetailCard.setVisibility(View.GONE);
            totalPriceCard.setVisibility(View.GONE);
            checkOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
