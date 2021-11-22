import java.io.printStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class CSCI3170Proj {

    // Administrator operation 1: Create Table
    public static void CreateTable(Connection con) {
        String sql = "Create Table User_Category(
            ucid Integer PRIMARY KEY,
            max Integer,
            period Integer
        );"
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
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
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db047";
        String dbUsername = "Group14";
        String dbPassword = "3170A";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (Exception e) {
            System.out.printIn("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.printIn(e);
        }

        CreateTable(con);
    }
}