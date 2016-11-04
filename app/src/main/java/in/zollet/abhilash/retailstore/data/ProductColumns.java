package in.zollet.abhilash.retailstore.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;


public class ProductColumns {
    @DataType(DataType.Type.INTEGER)
    @AutoIncrement  @PrimaryKey
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)  @NotNull
    public static final String NAME = "name";

    @DataType(DataType.Type.INTEGER)
    public static final String IMAGE = "image";

    @DataType(DataType.Type.TEXT)
    public static final String ACTUAL_PRICE = "actualPrice";

    @DataType(DataType.Type.TEXT)
    public static final String SELLING_PRICE = "sellingPrice";

    @DataType(DataType.Type.TEXT)
    public static final String CATEGORY = "category";

    @DataType(DataType.Type.INTEGER)
    public static final String IS_FAV = "is_fav";

    @DataType(DataType.Type.INTEGER)
    public static final String QUANTITY = "quantity";

}
