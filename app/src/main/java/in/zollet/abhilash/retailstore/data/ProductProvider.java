package in.zollet.abhilash.retailstore.data;


import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;



@ContentProvider(authority = ProductProvider.AUTHORITY, database = ProductDatabase.class)
public class ProductProvider {
    public static final String AUTHORITY = "in.zollet.abhilash.retailstore.data";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String Product= "product";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = ProductDatabase.PRODUCT)
    public static class Product {
        @ContentUri(
                path = Path.Product,
                type = "vnd.android.cursor.dir/product"
        )
        public static final Uri CONTENT_URI = buildUri(Path.Product);

        @InexactContentUri(
                name = "Product_ID",
                path = Path.Product + "/*",
                type = "vnd.android.cursor.item/product",
                whereColumn = ProductColumns._ID,
                pathSegment = 1
        )
        public static Uri ID(String ID) {
            return buildUri(Path.Product, ID);
        }
    }
}

