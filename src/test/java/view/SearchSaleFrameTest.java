package view;

import model.dao.SaleDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SearchSaleFrameTest {

    @Mock
    private SaleDAO saleDAO;
    @InjectMocks
    private SearchSaleFrame frame;

    @Mock
    private JButton backButton; // Mock the backButton explicitly


    @BeforeEach
    public void setUp() {
        // Create an instance of the frame before each test case
        try {
            MockitoAnnotations.openMocks(this);
            frame = new SearchSaleFrame();
            frame.setSaleDAO(saleDAO);
            frame.backButton = backButton;
        } catch (Exception e) {
            fail("Failed to initialize the frame: " + e.getMessage());
        }
    }

    //UI COMPONENTS LOAD TESTS
    @Test
    public void testFrameTitle() {
        // Test if the frame title is correct
        assertEquals("Search Sale", frame.getTitle(), "Frame title should be 'Search Sale'");
    }

    @Test
    public void testFrameSize() {
        // Test if the frame size is correct
        assertEquals(1000, frame.getWidth(), "Frame width should be 1000");
        assertEquals(720, frame.getHeight(), "Frame height should be 720");
    }

    @Test
    public void testComponentsAreLoaded() {
        // Test if key components are loaded and visible
        assertNotNull(frame.startDateLabel, "Start Date label should be initialized");
        assertNotNull(frame.endDateLabel, "End Date label should be initialized");
        assertNotNull(frame.startDateTextField, "Start Date text field should be initialized");
        assertNotNull(frame.endDateTextField, "End Date text field should be initialized");
        assertNotNull(frame.searchButton, "Search button should be initialized");
        assertNotNull(frame.generateCSVButton, "Generate CSV button should be initialized");
        assertNotNull(frame.table, "Table should be initialized");
        assertNotNull(frame.backButton, "Back button should be initialized");
    }

    @Test
    public void testTableLoaded() {
        // Test if the table is loaded with the correct column headers
        assertNotNull(frame.table.getModel(), "Table model should not be null");
        assertEquals(4, frame.table.getColumnCount(), "Table should have 4 columns");
        assertEquals("ID", frame.table.getColumnName(0), "First column name should be 'ID'");
        assertEquals("Total cost", frame.table.getColumnName(1), "Second column name should be 'Total cost'");
        assertEquals("Attendant", frame.table.getColumnName(2), "Third column name should be 'Attendant'");
        assertEquals("Date", frame.table.getColumnName(3), "Fourth column name should be 'Date'");
    }

    @Test
    public void testButtonsAreClickable() {
        // Test if buttons are functional (they should not throw exceptions when clicked)
        try {
            frame.searchButton.doClick();
            frame.generateCSVButton.doClick();
            frame.backButton.doClick();
        } catch (Exception e) {
            fail("Button click failed: " + e.getMessage());
        }
    }

    @Test
    public void testStartDateTextFieldIsEditable() {
        // Test if the Start Date text field is editable
        assertTrue(frame.startDateTextField.isEditable(), "Start Date text field should be editable");
    }

    @Test
    public void testEndDateTextFieldIsEditable() {
        // Test if the End Date text field is editable
        assertTrue(frame.endDateTextField.isEditable(), "End Date text field should be editable");
    }



    //Search Test case functionality
    /*@Test
    public void testSearchSaleButtonFunctionality() throws SQLException, SQLException {
        // Prepare mock data for the SaleDAO.readSalesTableData method
        String[][] mockData = {
                {"1", "100.00", "John Doe", "2024-10-20"},
                {"2", "200.00", "Jane Doe", "2024-10-21"}
        };


        // Set dates in the text fields
        frame.startDateTextField.setText("2024/12/20");
        frame.endDateTextField.setText("2024/12/21");

        // Click the search button
        frame.searchButton.doClick();

        // Simulate SaleDAO returning mock data
        when(saleDAO.readSalesTableData("2024/12/20", "2024/12/21")).thenReturn(mockData);


        // Verify the table data is populated
        JTable table = frame.table;
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        assertEquals(2, tableModel.getRowCount(), "The table should contain 2 rows of data.");
        assertEquals("100.00", tableModel.getValueAt(0, 1), "The first row's Total cost should be 100.00");
        assertEquals("John Doe", tableModel.getValueAt(0, 2), "The first row's Attendant should be John Doe");
    }*/
    @Test
    public void testSearchSaleButtonFunctionality() throws SQLException {
        // Prepare mock data for the SaleDAO.readSalesTableData method
        String[][] mockData = {
                {"1", "100.00", "John Doe", "2024-10-20"},
                {"2", "200.00", "Jane Doe", "2024-10-21"}
        };

        // Simulate the SaleDAO returning mock data
        when(saleDAO.readSalesTableData(anyString(), anyString())).thenReturn(mockData);

        // Set dates in the text fields
        frame.startDateTextField.setText("2024/10/20"); // Use yyyy-MM-dd format
        frame.endDateTextField.setText("2024/10/21");

        // Click the search button
        frame.searchButton.doClick();

        // Verify the table data is populated
        JTable table = frame.table;
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        assertEquals(2, tableModel.getRowCount(), "The table should contain 2 rows of data.");
        assertEquals("100.00", tableModel.getValueAt(0, 1), "The first row's Total cost should be 100.00");
        assertEquals("John Doe", tableModel.getValueAt(0, 2), "The first row's Attendant should be John Doe");
    }

    @Test
    public void testSearchTableDataValidDates() {
        // Set valid dates for testing
        frame.startDateTextField.setText("01/01/2023");
        frame.endDateTextField.setText("31/12/2023");

        // Simulate the search button click
        mock(ActionEvent.class);
        frame.searchButton.doClick();

        // Verify that the DAO's readSalesTableData method is called with the correct dates
        try {
            verify(saleDAO, times(1)).readSalesTableData("2023-01-01", "2023-12-31");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchTableDataInvalidDateFormat() {

        // Set invalid dates for testing
        frame.startDateTextField.setText("2023-01-01");
        frame.endDateTextField.setText("2023-12-31");

        // Use Mockito to mock the static method of JOptionPane
        try (MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {

            // Simulate the search button click
            ActionEvent actionEvent = mock(ActionEvent.class); // Mock ActionEvent
            when(actionEvent.getSource()).thenReturn(frame.searchButton); // Set source if needed

            // Trigger the click action
            frame.searchButton.doClick();

            // Verify that the showMessageDialog was called with the expected arguments
            mockedJOptionPane.verify(() ->
                            JOptionPane.showMessageDialog(any(Component.class),
                                    eq("Try this date format: dd/mm/yyyy!"),
                                    eq("Date format error"),
                                    eq(JOptionPane.WARNING_MESSAGE)),
                    times(1)
            );
        }
    }

    @Test
    public void testBackButtonAction() {
        // Create a spy of the frame to verify the dispose method was called
        SearchSaleFrame spyFrame = Mockito.spy(frame);

        // Simulate a click on the back button
        spyFrame.backButton.doClick();

        // Verify that dispose() was called, indicating the frame was closed
        Mockito.verify(spyFrame).dispose();
    }






}


