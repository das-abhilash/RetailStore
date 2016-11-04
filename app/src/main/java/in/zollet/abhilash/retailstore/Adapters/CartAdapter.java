package in.zollet.abhilash.retailstore.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import in.zollet.abhilash.retailstore.R;
import in.zollet.abhilash.retailstore.data.ProductColumns;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    private DataSetObserver mDataSetObserver;
    private boolean dataIsValid;
    private int rowIdColumn;

    public CartAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    public CartAdapter() {

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

        void onClickRemove(View itemView, int position);

        void onClickCard(View itemView, int position);

        void onClickAdd(View itemView, int position);

        void onClickMinus(View itemView, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        CartAdapter.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
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
        holder.productSellingPrice.setText("\u20B9 " + sellingPrice);
        holder.productActualPrice.setPaintFlags(holder.productActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productDiscount.setText(discount + "\u0025 off");
        int quantity = mCursor.getInt(mCursor.getColumnIndex(ProductColumns.QUANTITY));
        holder.productQuantity.setText(String.valueOf(quantity));
        Float totalPrice = Float.parseFloat(sellingPrice) * quantity;
        holder.productTotoalPrice.setText("Price: \u20B9 " + totalPrice);

    }

    @Override
    public int getItemCount() {

        return mCursor == null ? 0 : mCursor.getCount();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage,qunatiyMinus,qunatiyAdd;
        TextView productName, productSellingPrice, productActualPrice, productDiscount,productTotoalPrice,productQuantity;
        Button productRemove;

        ViewHolder(View view) {
            super(view);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            qunatiyMinus = (ImageView) view.findViewById(R.id.qunatiyMinus);
            qunatiyAdd = (ImageView) view.findViewById(R.id.qunatiyAdd);
            productName = (TextView) view.findViewById(R.id.productName);
            productSellingPrice = (TextView) view.findViewById(R.id.productSellingPrice);
            productActualPrice = (TextView) view.findViewById(R.id.productActualPrice);
            productDiscount = (TextView) view.findViewById(R.id.productDiscount);
            productTotoalPrice = (TextView) view.findViewById(R.id.productPrice);
            productQuantity = (TextView) view.findViewById(R.id.productQuantity);
            productRemove = (Button) view.findViewById(R.id.productRemove);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickCard(itemView, getLayoutPosition());

                }
            });

            productRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickRemove(itemView, getLayoutPosition());

                }
            });
            qunatiyAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickAdd(itemView, getLayoutPosition());

                }
            });

            qunatiyMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickMinus(itemView, getLayoutPosition());

                }
            });
        }
    }

}

