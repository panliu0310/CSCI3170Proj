import java.io.*;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class CSCI3170Proj {

    /*
    * Administrator operations starts
    */
    // Administrator operation 1: Create Table
    public static void CreateTable(Connection con) { // TO-DO
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
    public static void DeleteTable() { // TO-DO

    }
    // Administrator operation 3: Load data from a dataset
    public static void LoadDatafile() { // TO-DO

    }
    // Administrator operation 4: Show the number of records in each table
    public static void ShowRecord() { // TO-DO

    }

    /*
    * Administrator operations end
    * Library User operations start
    */

    /*
    * Library User operations end
    * Librarian operations start
    */

    /*
    * Librarian operations end
    */

    public static void Administrator(){ // TO-DO
        Scanner sc = new Scanner(System.in);

        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputAdmin = sc.nextInt();
        if(inputAdmin == 1){ // TO-DO
            //Create all tables
            System.out.println("Done. Database is initialized.");
        }else if(inputAdmin == 2){ // TO-DO
            //Delete all tables
            System.out.println("Done. Database is removed.");
        }else if(inputAdmin == 3){ // TO-DO
            //Load from datafile
            System.out.println("Done. Data is inputted to the database.");
        }else if(inputAdmin == 4){ // TO-DO
            //Show the number of records in each table
            System.out.println("Number of records in each table: ");
            System.out.println("Table1: "); //plus the table
            System.out.println("Table2: "); //plus the table
            System.out.println("Table3: "); //plus the table
            System.out.println("Table4: "); //plus the table
        }else{
            try{
                bookSystem();
            } catch(Exception e) {
                System.out.println("Fail to escape from Administrator to main menu");
                System.exit(0);
            }
        }

        sc.close();
    }

    public static void LibraryUser(){ // TO-DO
        Scanner sc = new Scanner(System.in);

        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Search for Books");
        System.out.println("2. Show loan record of a user");
        System.out.println("3. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputLU = sc.nextInt();
        if(inputLU == 1){ // TO-DO
            System.out.println("Choose the Search criterion: ");
            System.out.println("1. call number");
            System.out.println("2. title");
            System.out.println("3. author");
            System.out.println("Choose the search criterion: ");
            int crit = sc.nextInt();
            if(crit == 1){ // TO-DO
                System.out.print("Type in the Search Keyword: ");
                String sk = sc.next();
                //Show the result of the search
            }else if(crit == 2){ // TO-DO
                System.out.print("Type in the title: ");
                String title = sc.next();
                //Show the result of the search
            }else{ // TO-DO
                System.out.print("Type in the author: ");
                String author = sc.next();
                //Show the result of the search
            }
        }else if(inputLU == 2){ // TO-DO
            System.out.println("Enter The User ID: ");
            String user = sc.next();
            System.out.println("Loan Record");
            //show all loan record of the user
        }else{
            try{
                bookSystem();
            } catch(Exception e) {
                System.out.println("Fail to escape from Library User to main menu");
                System.exit(0);
            }
        }
    }

    public static void Librarian() throws ParseException{ // TO-DO
        Scanner sc = new Scanner(System.in);

        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. List all un-returbned book copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputLib = sc.nextInt();
        if(inputLib == 1){ // TO-DO
            System.out.print("Enter The User ID: ");
            String user = sc.nextLine();
            System.out.print("Enter The Call Number: ");
            String callNumber = sc.nextLine();
            System.out.print("Enter The Copy Number: ");
            String CopyNumber = sc.nextLine();

            //check if the user id, call no. and copy no. are exist, if yes:
            //if(user ==  && if callNumber == && if CopyNumber ==) {}
            System.out.println("Book borrowing performed successfully.");
            //else
            System.out.println("Book borrowing failed.");
        }else if(inputLib == 2){ // TO-DO
            System.out.print("Enter The User ID: ");
            String user = sc.nextLine();
            System.out.print("Enter The Call Number: ");
            String callNumber = sc.nextLine();
            System.out.print("Enter The Copy Number: ");
            String CopyNumber = sc.nextLine();

            //if the info input above is equal to borrowing record, then perform returning books; else, cannot return.
            System.out.println("Book returning performed successfully.");
        }else if(inputLib == 3){ // TO-DO
            System.out.println("Type in the starting date [dd/mm/yy]: ");
            String d1 = sc.nextLine();            
            
            System.out.println("Type in the ending date [dd/mm/yy]: ");
            String d2 = sc.nextLine();   
            try{
                Date startDate = new SimpleDateFormat("dd/mm/yyyy").parse(d1);        
                Date endDate = new SimpleDateFormat("dd/mm/yyyy").parse(d2);
            } catch(java.text.ParseException e){
                e.printStackTrace();
            }
           
            // System.out.println(startDate);
            // System.out.println(endDate);

            //if dates are valid:
            System.out.println("List of Un-Returned book: ");
        }else{
            try{
                bookSystem();
            } catch(Exception e) {
                System.out.println("Fail to escape from Librarian to main menu");
                System.exit(0);
            }
        }
    }

    public static void bookSystem() throws ParseException{
        Scanner sc = new Scanner(System.in);

        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Operations for Administrator");
        System.out.println("2. Operations for Library User");
        System.out.println("3. Operations for Librarian");
        System.out.println("4. Exit this program");
        System.out.print("Enter your choice: ");
        int input = sc.nextInt();
        if(input == 1){
            Administrator();
        }else if(input == 2){
            LibraryUser();
        }else if(input ==3){
            Librarian();
        }else{
            System.out.println("Program exit");
            System.exit(0);
        }
        sc.close();
        //System.out.println("Your choice is " + input);
        //System.out.println(sc.nextInt());

       
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
            System.out.println("Connected to database successfully");
            try{
                bookSystem();
            } catch(Exception e) {
                System.out.println("Fail to get in book system initially");
                System.exit(0);
            }
        }
    }
}