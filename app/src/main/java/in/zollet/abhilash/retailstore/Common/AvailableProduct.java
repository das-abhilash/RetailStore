package in.zollet.abhilash.retailstore.Common;


public class AvailableProduct {

    int productImage;
    String productName;
    String productActualPrice;
    String productSellingPrice;
    String productCategory;
    String productBrand;

    public AvailableProduct(String productName,String productActualPrice, String  productSellingPrice, int productImage,String productCategory){
        this.productImage = productImage;
        this.productName = productName;
        this.productActualPrice = productActualPrice;
        this.productSellingPrice = productSellingPrice;
        this.productCategory = productCategory;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductActualPrice() {
        return productActualPrice;
    }

    public String getProductSellingPrice() {
        return productSellingPrice;
    }

    public String getProductBrand() {
        return productBrand;
    }
}
