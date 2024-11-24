package view;
import java.util.logging.Logger;
import java.sql.SQLException;



public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            new LoginFrame();
        } catch (SQLException sqlException) {
            logger.severe("SQLException occurred: " + sqlException.getMessage());
            logger.severe("Stack Trace: ");
            for (StackTraceElement element : sqlException.getStackTrace()) {
                logger.severe(element.toString());
            }
        }
    }
}

