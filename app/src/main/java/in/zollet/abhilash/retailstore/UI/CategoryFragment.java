package in.zollet.abhilash.retailstore.UI;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.zollet.abhilash.retailstore.Adapters.ProductsAdapter;
import in.zollet.abhilash.retailstore.Common.Utility;
import in.zollet.abhilash.retailstore.R;
import in.zollet.abhilash.retailstore.data.ProductColumns;
import in.zollet.abhilash.retailstore.data.ProductProvider;


public class CategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String CATEGORY = "category";
    private String category;
    RecyclerView recyclerView;
    ProductsAdapter productsAdapter;
    private static final int CURSOR_LOADER_ID = 0;
    private Cursor cursor;
    ImageView wishlistEmpty;

    public CategoryFragment() {

    }

    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        wishlistEmpty = (ImageView) getActivity().findViewById(R.id.wishlistEmpty);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        getActivity().setTitle(category);


        productsAdapter = new ProductsAdapter(getContext(), null);
        productsAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onClickFAV(View itemView, int position) {
                cursor.moveToPosition(position);
                String id = cursor.getString(cursor.getColumnIndex(ProductColumns._ID));
                Uri uri = ProductProvider.Product.ID(id);
                int isFAV = cursor.getInt(cursor.getColumnIndex(ProductColumns.IS_FAV));
                Utility.updateFAVProduct(getContext(),uri,isFAV);
            }

            @Override
            public void onClickCard(View itemView, int position) {
                cursor.moveToPosition(position);
                String id = cursor.getString(cursor.getColumnIndex(ProductColumns._ID));
                Uri uri = ProductProvider.Product.ID(id);
                Intent detailIntent = new Intent(getContext(),ProductDetailActivity.class)
                        .setData(uri);
                startActivity(detailIntent);

            }
        });
        recyclerView.setAdapter(productsAdapter);

        return recyclerView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (!category.equals("Wishlist"))
        return new CursorLoader(getActivity(), ProductProvider.Product.CONTENT_URI, null,
                ProductColumns.CATEGORY + " =? "
                , new String[]{category}, null);
    else
            return new CursorLoader(getActivity(), ProductProvider.Product.CONTENT_URI, null,
                    ProductColumns.IS_FAV + " =? "
                    , new String[]{"1"}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        productsAdapter.swapCursor(data);
        cursor = data;
        if(category.equals("Wishlist") && data.getCount() ==0){
            wishlistEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
