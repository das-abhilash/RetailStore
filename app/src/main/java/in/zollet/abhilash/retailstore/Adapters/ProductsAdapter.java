package in.zollet.abhilash.retailstore.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.zollet.abhilash.retailstore.R;
import in.zollet.abhilash.retailstore.data.ProductColumns;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    private DataSetObserver mDataSetObserver;
    private boolean dataIsValid;
    private int rowIdColumn;

    public ProductsAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    public ProductsAdapter() {

    }

    private static OnItemClickListener listener;

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            dataIsValid = true;
            notifyDataSetChanged();
        } else {
            rowIdColumn = -1;
            dataIsValid = false;
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    public interface OnItemClickListener {

        void onClickFAV(View itemView, int position);

        void onClickCard(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ProductsAdapter.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.productName.setText(mCursor.getString(mCursor.getColumnIndex(ProductColumns.NAME)));
        holder.productImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(ProductColumns.IMAGE)));
        String actualPrice = mCursor.getString(mCursor.getColumnIndex(ProductColumns.ACTUAL_PRICE));
        String sellingPrice = mCursor.getString(mCursor.getColumnIndex(ProductColumns.SELLING_PRICE));
        String discount = String.valueOf(Math.round((Float.parseFloat(actualPrice) - Float.parseFloat(sellingPrice))/Float.parseFloat(actualPrice)*100));
        holder.productActualPrice.setText(actualPrice);
        if(mCursor.getInt(mCursor.getColumnIndex(ProductColumns.IS_FAV)) ==1) {
            holder.productFAV.setImageResource(R.drawable.ic_favorite_black_24dp);
            holder.productFAV.setTag("FAV");
        } else {
            holder.productFAV.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            holder.productFAV.setTag("notFAV");
        }
        holder.productSellingPrice.setText("\u20B9 " + sellingPrice);
        holder.productActualPrice.setPaintFlags(holder.productActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productDiscount.setText(discount + "\u0025 off");
    }

    @Override
    public int getItemCount() {

        return mCursor == null ? 0 : mCursor.getCount();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, productFAV;
        TextView productName, productSellingPrice, productActualPrice, productDiscount;

        ViewHolder(View view) {
            super(view);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productFAV = (ImageView) view.findViewById(R.id.productFAV);
            productName = (TextView) view.findViewById(R.id.productName);
            productSellingPrice = (TextView) view.findViewById(R.id.productSellingPrice);
            productActualPrice = (TextView) view.findViewById(R.id.productActualPrice);
            productDiscount = (TextView) view.findViewById(R.id.productDiscount);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickCard(itemView, getLayoutPosition());

                }
            });

            productFAV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickFAV(itemView, getLayoutPosition());

                }
            });
        }
    }

}

