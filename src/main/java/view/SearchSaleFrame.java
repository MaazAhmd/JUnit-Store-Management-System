package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import lombok.Setter;
import model.dao.SaleDAO;

public class SearchSaleFrame extends JFrame implements ActionListener {
    public final JPanel bottomPanel, topPanel, tablePanel;
    public final JLabel startDateLabel, endDateLabel;
    public final JTextField startDateTextField, endDateTextField;
    public JButton backButton, searchButton, generateCSVButton;
    public final Dimension inputBoxDimension = new Dimension(100, 20),
            tableDimension = new Dimension(800, 500), buttonsDimension = new Dimension(100, 25);
    public final Color mainColor = Color.white, inputColor = Color.black;
    public final JTable table;
    public final DefaultTableModel tableModel;
    public final JScrollPane scrollPane;
    @Setter
    public JFileChooser fileChooser;
    public Object[][] tableData;
    public String[] tableColumns = {"ID", "Total cost", "Attendant", "Date"};

    @Setter
    public SaleDAO saleDAO;

    SearchSaleFrame() throws SQLException {

        saleDAO = new SaleDAO();

        /****************************** Frame ******************************/

        this.setTitle("Search Sale");
        this.setSize(1000, 720);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        tablePanel = new JPanel(new GridBagLayout());
        tablePanel.setBackground(mainColor);
        this.add(tablePanel, BorderLayout.CENTER);

        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setPreferredSize(new Dimension(0, 50));
        topPanel.setBackground(mainColor);
        this.add(topPanel, BorderLayout.NORTH);

        /****************************** Frame ******************************/
        /****************************** Input ******************************/

        startDateLabel = new JLabel("Start date");
        startDateLabel.setPreferredSize(new Dimension(60, 20));
        startDateLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        topPanel.add(startDateLabel);

        startDateTextField = new JTextField();
        startDateTextField.setPreferredSize(inputBoxDimension);
        startDateTextField.setForeground(inputColor);
        startDateTextField.setBorder(BorderFactory.createLineBorder(inputColor));
        topPanel.add(startDateTextField);

        endDateLabel = new JLabel("End date");
        endDateLabel.setPreferredSize(new Dimension(55, 20));
        endDateLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        topPanel.add(endDateLabel);

        endDateTextField = new JTextField();
        endDateTextField.setPreferredSize(inputBoxDimension);
        endDateTextField.setForeground(inputColor);
        endDateTextField.setBorder(BorderFactory.createLineBorder(inputColor));
        topPanel.add(endDateTextField);

        /****************************** Input ******************************/
        /***************************** Buttons *****************************/

        searchButton = new JButton("Search sale");
        setButtonDesign(searchButton);
        topPanel.add(searchButton);

        generateCSVButton = new JButton("Generate CSV");
        setButtonDesign(generateCSVButton);
        topPanel.add(generateCSVButton);

        /***************************** Buttons *****************************/
        /**************************** Sale Table ****************************/

        tableModel = new DefaultTableModel(null, tableColumns);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(tableDimension);
        tablePanel.add(scrollPane, new GridBagConstraints());

        /**************************** Sale Table ****************************/
        /****************************** Frame ******************************/

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        bottomPanel.setPreferredSize(new Dimension(0, 50));
        bottomPanel.setBackground(mainColor);
        this.add(bottomPanel, BorderLayout.SOUTH);

        backButton = new JButton("Back");
        setButtonDesign(backButton);
        bottomPanel.add(backButton);

        this.setVisible(true);

        /****************************** Frame ******************************/
    }


    public void setButtonDesign(JButton button) {
        button.setPreferredSize(buttonsDimension);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(inputColor));
        button.setBackground(mainColor);
        button.setForeground(inputColor);
        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public void updateTable() {
        int currentRowCount = tableModel.getRowCount();
        tableModel.setRowCount(0);
        tableModel.setRowCount(currentRowCount);
        tableModel.setDataVector(tableData, tableColumns);
    }


    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public void searchTableData() {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDateDate = null;
        Date endDateDate = null;

        // Attempt to parse the start and end dates
        try {
            startDateDate = originalFormat.parse(startDateTextField.getText());
            endDateDate = originalFormat.parse(endDateTextField.getText());
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            // Display error message for invalid date format
            showErrorMessage("Try this date format: dd/mm/yyyy!");
            return; // Exit method to prevent further execution with invalid dates
        }

        // Check if dates are valid (e.g., not null and within a valid range)
        if (startDateDate == null || endDateDate == null || startDateDate.after(endDateDate)) {
            // Show error message if dates are invalid
            showErrorMessage("Start date must be before or equal to end date.");
            return; // Exit method to prevent further execution with invalid dates
        }

        // Format the dates into the required SQL format
        String startDateString = sqlFormat.format(startDateDate);
        String endDateString = sqlFormat.format(endDateDate);

        try {
            // Call the DAO to get the table data for the specified date range
            tableData = saleDAO.readSalesTableData(startDateString, endDateString);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            // Optionally, you can display an error message for the SQLException
            showErrorMessage("Error fetching data from the database.");
        }

        // Update the table with the fetched data
        updateTable();
    }





    /*public void generateCSVFromTable() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "CSV Files";
            }

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String filename = file.getName().toLowerCase();
                    return filename.endsWith(".csv");
                }
            }
        });
        int fileChooserResponse = fileChooser.showSaveDialog(null);

        if (fileChooserResponse == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("ID,Total cost,Attendant,Date");
                for(int i = 0; i < table.getRowCount(); i++) {
                    fileWriter.append("\n");
                    for(int j = 0; j < 4; j++) {
                        if (j > 0) fileWriter.append(",");
                        fileWriter.append((String) tableData[i][j]);
                    }
                }
                fileWriter.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }*/

    public void generateCSVFromTable() {
        fileChooser = new JFileChooser();  // Initialize JFileChooser

        // Set file filter to only accept CSV files
        fileChooser.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "CSV Files";  // Description shown in the file chooser dialog
            }

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;  // Allow directories
                } else {
                    String filename = file.getName().toLowerCase();
                    return filename.endsWith(".csv");  // Only allow CSV files
                }
            }
        });

        // Show the save file dialog and get the user's response
        int fileChooserResponse = fileChooser.showSaveDialog(null);

        // If the user selected a file, proceed to save it
        if (fileChooserResponse == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            saveCSVToFile(file);  // Call the method to save the file
        }
    }

    void saveCSVToFile(File file) {
        try {
            // Create FileWriter to write CSV content
            FileWriter fileWriter = new FileWriter(file);

            // Write CSV header
            fileWriter.write("ID,Total cost,Attendant,Date");

            // Get the table's model to access the data
            TableModel model = table.getModel();

            // Write table data row by row
            for (int i = 0; i < model.getRowCount(); i++) {
                fileWriter.append("\n");  // Add a new line for each row
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (j > 0) fileWriter.append(",");  // Separate columns with a comma
                    // Assuming the data is stored as String; if it's another type, adjust accordingly
                    fileWriter.append(model.getValueAt(i, j).toString());
                }
            }

            // Close the writer to save the file
            fileWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();  // Handle IO exceptions if needed
        }
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source.equals(searchButton)) {
            // Trigger the search table data functionality
            searchTableData();
        } else if (source.equals(generateCSVButton)) {
            // Generate CSV from the table data
            generateCSVFromTable();
        } else if (source.equals(backButton)) {
            // Navigate to the menu frame and dispose of the current frame
            try {
                new MenuFrame(); // Open the menu frame
            } catch (IOException | SQLException exception) {
                exception.printStackTrace();
            } finally {
                // Ensure the current frame is disposed of

                this.dispose();
            }
        }
    }
}


