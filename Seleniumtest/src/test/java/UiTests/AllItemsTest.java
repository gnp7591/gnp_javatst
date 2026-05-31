package UiTests;
import Base.DriverFactory;
import Base.InventoryPage;
import Base.LoginPage;
import Base.UiBaseClass;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;  
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import Base.AllureReportListener;
import Base.ConfigReader;
import java.util.List;

@Listeners(AllureReportListener.class)
@Feature("All Items Page")
@Story("Verify All Items Page Loads Correctly")
public class AllItemsTest extends UiBaseClass {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void loginAndNavigateToAllItems() {
        DriverFactory.getDriver().get(ConfigReader.getApplicationUrl());
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        inventoryPage = new InventoryPage(DriverFactory.getDriver());
        inventoryPage.waitForProductsToLoad();
    }

    // ===== PAGE LOAD AND DISPLAY TESTS =====
    @Test(description = "Verify All Items page loads correctly after login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login and verify that the user is directed to the All Items page")
    public void testAllItemsPageLoads() {
        assert DriverFactory.getDriver().getCurrentUrl().contains(ConfigReader.getInventoryUrl()) : "Not on inventory page";
    }

    @Test(description = "Verify All Items page displays products")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are displayed on the All Items page")
    public void testAllItemsDisplay() {
        assert inventoryPage.areProductsDisplayed() : "Products are not displayed on the page";
    }

    // ===== PRODUCT COUNT TESTS =====
    @Test(description = "Verify product count on All Items page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the correct number of products are displayed")
    public void testProductCount() {
        int productCount = inventoryPage.getProductCount();
        assert productCount == 6 : "Expected 6 products, but found " + productCount;
    }

    // ===== PRODUCT NAME TESTS =====
    @Test(description = "Verify all product names are displayed")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all product names are visible on the page")
    public void testProductNamesDisplay() {
        List<String> productNames = inventoryPage.getAllProductNames();
        assert !productNames.isEmpty() : "No product names found";
        assert productNames.stream().allMatch(name -> !name.isEmpty()) : "Some product names are empty";
    }

    // ===== PRODUCT PRICE TESTS =====
    @Test(description = "Verify all product prices are displayed")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all products have prices displayed")
    public void testProductPricesDisplay() {
        List<String> prices = inventoryPage.getAllProductPrices();
        assert !prices.isEmpty() : "No product prices found";
        assert prices.stream().allMatch(price -> price.contains("$")) : "Some prices do not contain currency symbol";
    }

    @Test(description = "Verify product prices can be parsed as numbers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that prices are in valid numeric format")
    public void testProductPricesFormatValid() {
        List<Double> prices = inventoryPage.getAllProductPricesAsDouble();
        assert !prices.isEmpty() : "No product prices found";
        assert prices.stream().allMatch(price -> price > 0) : "Some prices are not positive numbers";
    }

    // ===== PRODUCT DESCRIPTION TESTS =====
    @Test(description = "Verify all product descriptions are displayed")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all products have descriptions displayed")
    public void testProductDescriptionsDisplay() {
        List<String> descriptions = inventoryPage.getAllProductDescriptions();
        assert !descriptions.isEmpty() : "No product descriptions found";
        assert descriptions.stream().allMatch(desc -> !desc.isEmpty()) : "Some descriptions are empty";
    }

    // ===== SORT BY PRICE TESTS =====
    @Test(description = "Verify sorting products by price low to high")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are correctly sorted by price from lowest to highest")
    public void testSortByPriceLowToHigh() {
        inventoryPage.sortByPriceLowToHigh();
        assert inventoryPage.isPricesSortedLowToHigh() : "Products are not sorted by price low to high";
    }

    @Test(description = "Verify sorting products by price high to low")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are correctly sorted by price from highest to lowest")
    public void testSortByPriceHighToLow() {
        inventoryPage.sortByPriceHighToLow();
        assert inventoryPage.isPricesSortedHighToLow() : "Products are not sorted by price high to low";
    }

    // ===== SORT BY NAME TESTS =====
    @Test(description = "Verify sorting products by name A to Z")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are correctly sorted by name alphabetically")
    public void testSortByNameAZ() {
        inventoryPage.sortByNameAZ();
        assert inventoryPage.areNamesSortedAZ() : "Products are not sorted by name A to Z";
    }

    @Test(description = "Verify sorting products by name Z to A")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are correctly sorted by name in reverse alphabetically")
    public void testSortByNameZA() {
        inventoryPage.sortByNameZA();
        assert inventoryPage.areNamesSortedZA() : "Products are not sorted by name Z to A";
    }

    // ===== ADD TO CART TESTS =====
    @Test(description = "Verify adding single product to cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a product can be added to the cart")
    public void testAddProductToCart() {
        int initialCount = inventoryPage.getCartItemCount();
        inventoryPage.addProductToCart(0);
        assert inventoryPage.getCartItemCount() == initialCount + 1 : "Product was not added to cart";
    }

    @Test(description = "Verify adding multiple products to cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that multiple products can be added to the cart")
    public void testAddMultipleProductsToCart() {
        inventoryPage.addMultipleProductsToCart(0, 1, 2);
        assert inventoryPage.getCartItemCount() == 3 : "Expected 3 items in cart, found " + inventoryPage.getCartItemCount();
    }

    @Test(description = "Verify adding product to cart by name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a product can be added to cart using product name")
    public void testAddProductToCartByName() {
        String productName = inventoryPage.getAllProductNames().get(0);
        int initialCount = inventoryPage.getCartItemCount();
        inventoryPage.addProductToCartByName(productName);
        assert inventoryPage.getCartItemCount() == initialCount + 1 : "Product " + productName + " was not added to cart";
    }

    @Test(description = "Verify button text changes after adding to cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that add to cart button changes to REMOVE after product is added")
    public void testButtonTextChangesAfterAddToCart() {
        inventoryPage.addProductToCart(0);
        String buttonText = inventoryPage.getButtonTextForProduct(0);
        assert buttonText.contains("REMOVE") : "Button text is not 'REMOVE' after adding to cart";
    }

    // ===== REMOVE FROM CART TESTS =====
    @Test(description = "Verify removing product from cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a product can be removed from the cart")
    public void testRemoveProductFromCart() {
        inventoryPage.addProductToCart(0);
        int countAfterAdd = inventoryPage.getCartItemCount();
        inventoryPage.removeProductFromCart(0);
        assert inventoryPage.getCartItemCount() == countAfterAdd - 1 : "Product was not removed from cart";
    }

    @Test(description = "Verify removing product from cart by name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a product can be removed from cart using product name")
    public void testRemoveProductFromCartByName() {
        String productName = inventoryPage.getAllProductNames().get(0);
        inventoryPage.addProductToCartByName(productName);
        int countAfterAdd = inventoryPage.getCartItemCount();
        inventoryPage.removeProductFromCartByName(productName);
        assert inventoryPage.getCartItemCount() == countAfterAdd - 1 : "Product " + productName + " was not removed from cart";
    }

    @Test(description = "Verify button text changes after removing from cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that REMOVE button changes to ADD TO CART after product is removed")
    public void testButtonTextChangesAfterRemoveFromCart() {
        inventoryPage.addProductToCart(0);
        inventoryPage.removeProductFromCart(0);
        String buttonText = inventoryPage.getButtonTextForProduct(0);
        assert buttonText.contains("ADD TO CART") : "Button text is not 'ADD TO CART' after removing from cart";
    }

    // ===== CART BADGE TESTS =====
    @Test(description = "Verify cart badge displays correct count")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that cart badge shows the correct number of items")
    public void testCartBadgeDisplaysCorrectCount() {
        inventoryPage.addProductToCart(0);
        inventoryPage.addProductToCart(1);
        assert inventoryPage.getCartItemCount() == 2 : "Cart badge does not show correct count";
    }

    @Test(description = "Verify cart badge is hidden when cart is empty")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that cart badge is not displayed when no items are in cart")
    public void testCartBadgeHiddenWhenEmpty() {
        assert !inventoryPage.isCartBadgeDisplayed() : "Cart badge should not be displayed when cart is empty";
    }

    @Test(description = "Verify cart badge is displayed when items added")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that cart badge appears when items are added to cart")
    public void testCartBadgeDisplayedWhenItemsAdded() {
        inventoryPage.addProductToCart(0);
        assert inventoryPage.isCartBadgeDisplayed() : "Cart badge should be displayed when items are in cart";
    }

    // ===== PRODUCT CLICK TESTS =====
    @Test(description = "Verify clicking on product navigates to details page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that clicking a product name navigates to product details page")
    public void testClickProductNavigates() {
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        inventoryPage.clickProduct(0);
        String newUrl = DriverFactory.getDriver().getCurrentUrl();
        assert !currentUrl.equals(newUrl) : "URL did not change after clicking product";
        assert newUrl.contains("inventory-item") : "Not navigated to product details page";
    }

}

