package view;

import model.dao.SaleDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SearchSaleFrameTest {

    @Mock
    private SaleDAO saleDAO;

    @InjectMocks
    private SearchSaleFrame frame;



    @BeforeEach
    public void setUp() {
        // Create an instance of the frame before each test case
        try {
            MockitoAnnotations.openMocks(this);
            frame = new SearchSaleFrame();
            frame.setSaleDAO(saleDAO);

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
    public void testStartDateTextFieldIsEditable() {
        // Test if the Start Date text field is editable
        assertTrue(frame.startDateTextField.isEditable(), "Start Date text field should be editable");
    }

    @Test
    public void testEndDateTextFieldIsEditable() {
        // Test if the End Date text field is editable
        assertTrue(frame.endDateTextField.isEditable(), "End Date text field should be editable");
    }


    /*@Test
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
    }*/

    @Test
    public void testSearchSaleButtonFunctionality() throws SQLException {
        // Set dates in the text fields
        frame.startDateTextField.setText("2024/10/20");  // Use yyyy-MM-dd format
        frame.endDateTextField.setText("2024/10/21");

        // Click the search button (this triggers the search functionality)
        frame.searchButton.doClick();

        // Verify the table data is populated (simulating what the button press does)
        JTable table = frame.table;
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // Check the number of rows in the table
        assertEquals(2, tableModel.getRowCount(), "The table should contain 2 rows of data.");

        // Check specific values for the first row
        assertEquals("100.00", tableModel.getValueAt(0, 1), "The first row's Total cost should be 100.00");
        assertEquals("John Doe", tableModel.getValueAt(0, 2), "The first row's Attendant should be John Doe");

        // Check specific values for the second row
        assertEquals("200.00", tableModel.getValueAt(1, 1), "The second row's Total cost should be 200.00");
        assertEquals("Jane Doe", tableModel.getValueAt(1, 2), "The second row's Attendant should be Jane Doe");
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
    public void testSearchTableDataInvalidDates() {
        // Set invalid dates for testing
        frame.startDateTextField.setText("2023-13-01"); // Invalid month (13)
        frame.endDateTextField.setText("2023-12-32");  // Invalid day (32)

        // Simulate the search button click
        mock(ActionEvent.class);
        frame.searchButton.doClick();

        // Verify that the DAO's readSalesTableData method is NOT called
        try {
            // Assuming the invalid date case does not trigger the DAO call
            verify(saleDAO, times(0)).readSalesTableData(anyString(), anyString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Optionally, you can verify that some error handling or feedback (like a dialog) is shown
        // In case your application shows a dialog for invalid dates, you can verify that here:
        // verify(frame, times(1)).showErrorMessage(anyString());  // Example, if such a method exists
    }

    /*@Test
    public void testSearchTableDataNoData() {
        // Set valid dates for testing
        frame.startDateTextField.setText("01/01/2023");
        frame.endDateTextField.setText("31/12/2023");

        // Mock the DAO to return an empty list (no data found)
        try {
            when(saleDAO.readSalesTableData("2023-01-01", "2023-12-31")).thenReturn(new ArrayList<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Simulate the search button click
        mock(ActionEvent.class);
        frame.searchButton.doClick();

        // Verify that the DAO's readSalesTableData method is called with the correct dates
        try {
            verify(saleDAO, times(1)).readSalesTableData("2023-01-01", "2023-12-31");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Optionally, verify that no data is displayed or a message is shown if no data is found
        // Example, assuming there is some label or message shown when no data is found:
        // verify(frame, times(1)).showNoDataMessage();  // Example method, adjust based on actual implementation
    }*/


    @Test
    public void testSearchTableDataStartDateGreaterThanEndDate() {
        // Set the start date greater than the end date
        frame.startDateTextField.setText("01/02/2023");
        frame.endDateTextField.setText("31/01/2023");

        // Simulate the search button click
        ActionEvent mock = mock(ActionEvent.class);
        frame.searchButton.doClick();

        // Verify that the DAO's readSalesTableData method is NOT called
        try {
            verify(saleDAO, times(0)).readSalesTableData(anyString(), anyString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Verify that an error message is shown for invalid date range
        // Assuming the method shows a dialog when the start date is greater than the end date
        verify(frame, times(1)).showErrorMessage("Start date must be before or equal to end date.");
    }


    // TESTS FOR ACTIONLISTENER BUTTONS
    @Test
    public void testSearchButtonAction() {
        // Simulate a click on the Search button
        frame.searchButton.doClick();

        // Ensure that the DAO's readSalesTableData method is called
        try {
            verify(saleDAO, times(1)).readSalesTableData(anyString(), anyString());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException should not occur during the test");
        }

        // Ensure that the action listener has been triggered
        verify(frame.searchButton, times(1)).doClick();
    }


    @Test
    public void testCSVButtonAction() {
        // Mock behavior for generateCSVFromTable (not requiring mocking of non-mocked methods)
        SearchSaleFrame spyFrame = spy(frame);
        doNothing().when(spyFrame).generateCSVFromTable();  // Mock the method behavior

        // Simulate a click on the Generate CSV button
        spyFrame.generateCSVButton.doClick();

        // Verify that the generateCSVFromTable method is invoked
        verify(spyFrame, times(1)).generateCSVFromTable();
    }

    @Test
    public void testBackButtonAction() {
        SearchSaleFrame spyFrame = spy(frame);

        // Simulate a click on the Back button
        spyFrame.backButton.doClick();

        // Verify that the dispose method is invoked (on the spy)
        verify(spyFrame, times(1)).dispose();
    }

    @Test
    public void testUpdateTable() {
        // Create mock table model
        DefaultTableModel mockTableModel = mock(DefaultTableModel.class);

        // Inject the mock table model into the frame
        frame.table.setModel(mockTableModel);

        // Prepare some sample data
        Object[][] sampleData = {
                {"1", "100.00", "John Doe", "2024-10-20"},
                {"2", "200.00", "Jane Doe", "2024-10-21"}
        };
        String[] columns = {"ID", "Total cost", "Attendant", "Date"};

        // Set tableData and tableColumns in the frame
        frame.tableData = sampleData;
        frame.tableColumns = columns;

        // Call the updateTable method
        frame.updateTable();

        // Verify that the setRowCount method was called twice:
        // Once to set it to 0 (clear the table), and then to restore the row count
        verify(mockTableModel, times(1)).setRowCount(0); // Clearing the table
        verify(mockTableModel, times(1)).setRowCount(2); // Setting the row count to 2 (number of rows in sampleData)

        // Verify that setDataVector was called with the correct data and columns
        verify(mockTableModel, times(1)).setDataVector(sampleData, columns);
    }


    //CSV file generation test cases
    @Test
    public void testCSVGenerationCreatesValidFile() throws IOException {
        // Set up the test data
        String[][] mockData = {
                {"1", "100.00", "John Doe", "2024-10-20"},
                {"2", "200.00", "Jane Doe", "2024-10-21"}
        };

        // Simulate filling the table with data
        DefaultTableModel model = (DefaultTableModel) frame.table.getModel();
        model.setRowCount(0);  // Clear any existing rows
        for (String[] row : mockData) {
            model.addRow(row);
        }

        // Create a spy to track the file creation method
        SearchSaleFrame spyFrame = spy(frame);
        File tempFile = File.createTempFile("sales_report", ".csv");
        doNothing().when(spyFrame).saveCSVToFile(tempFile);  // Mock the actual file save

        // Trigger the CSV generation
        spyFrame.generateCSVButton.doClick();

        // Verify that the file saving method was called once
        verify(spyFrame, times(1)).saveCSVToFile(tempFile);

        // Check if the file exists and is of valid CSV format
        assertTrue(tempFile.exists(), "CSV file should be created.");
        assertTrue(tempFile.getName().endsWith(".csv"), "File should have a '.csv' extension.");
    }


    @Test
    public void testCSVGenerationCancelledDialog() {
        // Mock the file chooser behavior to simulate cancellation
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(any(Component.class))).thenReturn(JFileChooser.CANCEL_OPTION);

        // Inject the mock file chooser into the frame
        frame.setFileChooser(fileChooser);

        // Spy on the frame to verify the method call
        SearchSaleFrame spyFrame = spy(frame);

        // Simulate clicking the Generate CSV button
        spyFrame.generateCSVButton.doClick();

        // Verify that the dialog was shown, but the save method was not called due to cancellation
        verify(fileChooser, times(1)).showSaveDialog(any(Component.class));
        verify(spyFrame, times(0)).saveCSVToFile(any(File.class));
    }

    @Test
    public void testCSVFileFormatValidation() {
        // Set up the file chooser to accept only CSV files
        JFileChooser fileChooser = mock(JFileChooser.class);
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        };
        when(fileChooser.getAcceptAllFileFilter()).thenReturn(fileFilter);
        when(fileChooser.showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        // Inject the mock file chooser into the frame
        frame.setFileChooser(fileChooser);

        // Simulate clicking the Generate CSV button
        SearchSaleFrame spyFrame = spy(frame);
        spyFrame.generateCSVButton.doClick();

        // Verify that the file chooser showed up and the CSV file filter was used
        verify(fileChooser, times(1)).showSaveDialog(any(Component.class));
        verify(fileChooser, times(1)).setFileFilter(fileFilter);
    }















}


