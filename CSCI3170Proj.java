import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class CSCI3170Proj {

    // Administrator operation 1: Create Table
    public static void CreateTable(Connection con) {
        String sql = "Create Table User_Category (" +
            "ucid Integer PRIMARY KEY, " +
            "max Integer, " +
            "period Integer)";
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Table Created!");
        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    // Administrator operation 2: Delete Table
    public static void DeleteTable() {

    }
    // Administrator operation 3: Load data from a dataset
    public static void LoadDatafile() {

    }
    // Administrator operation 4: Show the number of records in each table
    public static void ShowRecord() {

    }
    // Administrator operation 5: Return to main menu
    public static void ReturnToMainMenu() {

    }
    // show main menu
    public static void MainMenu() {

    }

    public static void main(String[] args) {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db14";
        String dbUsername = "Group14";
        String dbPassword = "3170A";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (Exception e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } finally {
            System.out.println("Connected database successfully");
            CreateTable(con);
        }
    }
}