package model.dao;


import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import model.Product;
import model.dao.ProductDAO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductDAOTest {
    private static ProductDAO productDAO;

    @BeforeAll
    static void setup() throws SQLException {
        productDAO = new ProductDAO();
    }

    @AfterAll
    static void cleanup() throws SQLException {
        productDAO.close();
    }

    @Test
    @Order(1)
    void testCreateProduct() throws SQLException {
        Product product = new Product(0, "TestProduct", "TestCategory", "100");
        boolean isCreated = productDAO.createProduct(product);
        assertTrue(isCreated, "Product should be created successfully");
    }

    @Test
    @Order(2)
    void testReadProductsTableData() throws SQLException {
        String[][] products = productDAO.readProductsTableData();
        assertNotNull(products, "Products table data should not be null");
        assertTrue(products.length > 0, "Products table should contain at least one product");
    }

    @Test
    @Order(3)
    void testUpdateProduct() throws SQLException {
        Product updatedProduct = new Product(1, "UpdatedProduct", "UpdatedCategory", "200");
        boolean isUpdated = productDAO.updateProduct(updatedProduct);
        assertTrue(isUpdated, "Product should be updated successfully");
    }

    @Test
    @Order(4)
    void testDeleteProduct() throws SQLException {
        boolean isDeleted = productDAO.deleteProduct(1); // Assuming ID 1 exists for testing
        assertTrue(isDeleted, "Product should be deleted successfully");
    }
}
