package Base;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class InventoryPage {
    
    private WebDriver driver;
    private WebDriverWait wait;

    // Product List Locators
    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "inventory_item_desc")
    private List<WebElement> productDescriptions;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    // Sort and Filter Locators
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    // Cart Locators
    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    // Add to Cart Buttons Locators
    @FindBy(xpath = "//button[contains(@class, 'btn_inventory') and contains(text(), 'ADD TO CART')]")
    private List<WebElement> addToCartButtons;

    // Remove from Cart Buttons Locators
    @FindBy(xpath = "//button[contains(@class, 'btn_inventory') and contains(text(), 'REMOVE')]")
    private List<WebElement> removeFromCartButtons;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // ===== PRODUCT NAME METHODS =====
    public List<WebElement> getProductNames() {
        return productNames;
    }

    public int getProductCount() {
        return productNames.size();
    }

    public List<String> getAllProductNames() {
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    // ===== PRODUCT PRICE METHODS =====
    public List<String> getAllProductPrices() {
        return productPrices.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getProductPrice(int index) {
        return productPrices.get(index).getText();
    }

    public double getPriceAsDouble(String priceText) {
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public List<Double> getAllProductPricesAsDouble() {
        return productPrices.stream()
                .map(WebElement::getText)
                .map(price -> getPriceAsDouble(price))
                .collect(Collectors.toList());
    }

    // ===== PRODUCT DESCRIPTION METHODS =====
    public List<String> getAllProductDescriptions() {
        return productDescriptions.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getProductDescription(int index) {
        return productDescriptions.get(index).getText();
    }

    // ===== PRODUCT CLICK METHODS =====
    public String clickProduct(int index) {
        String productName = productNames.get(index).getText();
        productNames.get(index).click();
        return productName;
    }

    public void clickProductByName(String productName) {
        for (WebElement product : productNames) {
            if (product.getText().equalsIgnoreCase(productName)) {
                product.click();
                return;
            }
        }
        throw new NoSuchElementException("Product with name " + productName + " not found");
    }

    // ===== ADD TO CART METHODS =====
    public void addProductToCart(int index) {
        addToCartButtons.get(index).click();
    }

    public void addProductToCartByName(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (productNames.get(i).getText().equalsIgnoreCase(productName)) {
                addToCartButtons.get(i).click();
                return;
            }
        }
        throw new NoSuchElementException("Product with name " + productName + " not found");
    }

    public void addMultipleProductsToCart(int... indices) {
        for (int index : indices) {
            addProductToCart(index);
        }
    }

    // ===== REMOVE FROM CART METHODS =====
    public void removeProductFromCart(int index) {
        removeFromCartButtons.get(index).click();
    }

    public void removeProductFromCartByName(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (productNames.get(i).getText().equalsIgnoreCase(productName)) {
                removeFromCartButtons.get(i).click();
                return;
            }
        }
        throw new NoSuchElementException("Product with name " + productName + " not found");
    }

    // ===== CART BADGE METHODS =====
    public int getCartItemCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public boolean isCartBadgeDisplayed() {
        try {
            return cartBadge.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickCart() {
        cartLink.click();
    }

    // ===== SORT METHODS =====
    public void sortProductsBy(String sortOption) {
        Select dropdown = new Select(sortDropdown);
        dropdown.selectByValue(sortOption);
        // Wait for products to reload after sorting
        wait.until(ExpectedConditions.stalenessOf(productNames.get(0)));
    }

    public void sortByNameAZ() {
        sortProductsBy("az");
    }

    public void sortByNameZA() {
        sortProductsBy("za");
    }

    public void sortByPriceLowToHigh() {
        sortProductsBy("lohi");
    }

    public void sortByPriceHighToLow() {
        sortProductsBy("hilo");
    }

    // ===== VISIBILITY VERIFICATION METHODS =====
    public boolean areProductsDisplayed() {
        return !inventoryItems.isEmpty() && productNames.get(0).isDisplayed();
    }

    public boolean isProductNameVisible(String productName) {
        return productNames.stream()
                .anyMatch(product -> product.getText().equalsIgnoreCase(productName));
    }

    public boolean isAddToCartButtonDisplayed(int index) {
        try {
            return addToCartButtons.get(index).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getButtonTextForProduct(int index) {
        WebElement item = inventoryItems.get(index);
        WebElement button = item.findElement(By.cssSelector("button.btn_inventory"));
        return button.getText();
    }

    // ===== HELPER METHODS =====
    public void waitForProductsToLoad() {
        wait.until(ExpectedConditions.visibilityOfAllElements(productNames));
    }

    public boolean isPricesSortedLowToHigh() {
        List<Double> prices = getAllProductPricesAsDouble();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPricesSortedHighToLow() {
        List<Double> prices = getAllProductPricesAsDouble();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean areNamesSortedAZ() {
        List<String> names = getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            if (names.get(i).compareToIgnoreCase(names.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean areNamesSortedZA() {
        List<String> names = getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            if (names.get(i).compareToIgnoreCase(names.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }
}
