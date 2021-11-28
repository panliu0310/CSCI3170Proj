import java.io.*;
import java.io.PrintStream;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
    public static void CreateTable(Connection con) {
        String TABLE_User_Category = "CREATE TABLE USER_CATEGORY(" +
            "ucid INTEGER PRIMARY KEY, " +
            "max INTEGER(2) NOT NULL, " +
            "period INTEGER(2) NOT NULL)";
        String TABLE_LibUser = "CREATE TABLE LIBUSER(" +
            "libuid VARCHAR(10) PRIMARY KEY, " +
            "name VARCHAR(25) NOT NULL, " +
            "age INTEGER(3) NOT NULL, " +
            "address VARCHAR(100) NOT NULL, " +
            "ucid INTEGER(1) NOT NULL)";
        String TABLE_Book_Category = "CREATE TABLE BOOK_CATEGORY(" +
            "bcid INTEGER(1) PRIMARY KEY, " +
            "bcname VARCHAR(30) NOT NULL)";
        String TABLE_Books = "CREATE TABLE BOOKS(" +
            "callnum VARCHAR(8) PRIMARY KEY, " +
            "title VARCHAR(30) NOT NULL, " +
            "publish VARCHAR(10) NOT NULL, " +
            "rating FLOAT, " +
            "tborrowed INTEGER(2) NOT NULL, " +
            "bcid INTEGER(1) NOT NULL)";
        String TABLE_Copy = "CREATE TABLE COPY(" +
            "callnum VARCHAR(8) NOT NULL," +
            "copynum INTEGER NOT NULL," +
            "PRIMARY KEY (callnum, copynum)," +
            "FOREIGN KEY (callnum) REFERENCES BOOKS(callnum))";
        String TABLE_Borrow = "CREATE TABLE BORROW(" +
            "libuid VARCHAR(10) NOT NULL, " +
            "callnum VARCHAR(8) NOT NULL, " +
            "copynum INTEGER NOT NULL, " +
            "checkout VARCHAR(10) NOT NULL, " +
            "return_date VARCHAR(10), " +
            "PRIMARY KEY (libuid, callnum, copynum, checkout)," +
            "FOREIGN KEY (libuid) REFERENCES LIBUSER(libuid), " +
            "FOREIGN KEY (callnum) REFERENCES BOOKS(callnum))";
            //"FOREIGN KEY (copynum) REFERENCES COPY(copynum))";
        String TABLE_Authorship = "CREATE TABLE AUTHORSHIP(" +
            "aname VARCHAR(767), " +
            "callnum VARCHAR(8), " +
            "PRIMARY KEY (aname, callnum)," +
            "FOREIGN KEY (callnum) REFERENCES BOOKS(callnum))" ;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(TABLE_User_Category);
            stmt.executeUpdate(TABLE_LibUser);
            stmt.executeUpdate(TABLE_Book_Category);
            stmt.executeUpdate(TABLE_Books);
            stmt.executeUpdate(TABLE_Copy);
            stmt.executeUpdate(TABLE_Borrow);
            stmt.executeUpdate(TABLE_Authorship);
            System.out.println("Done. Database is initialized.");
        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }finally{
            Administrator(con);
        }
    }

    // Administrator operation 2: Delete Table
    public static void DeleteTable(Connection con) {
        String Delete_User_Category = "DROP TABLE IF EXISTS USER_CATEGORY";
        String Delete_LibUser = "DROP TABLE IF EXISTS LIBUSER";
        String Delete_Book_Category = "DROP TABLE IF EXISTS BOOK_CATEGORY";
        String Delete_Books = "DROP TABLE IF EXISTS BOOKS";
        String Delete_Copy = "DROP TABLE IF EXISTS COPY";
        String Delete_Borrow = "DROP TABLE IF EXISTS BORROW";
        String Delete_Authorship = "DROP TABLE IF EXISTS AUTHORSHIP";
        
        try {
            Statement stmt = con.createStatement();
            // delete the table with foreign key first
            // borrow point to copy so need to delete borrow first
            stmt.executeUpdate(Delete_Borrow);
            stmt.executeUpdate(Delete_Copy);
            stmt.executeUpdate(Delete_Authorship);

            stmt.executeUpdate(Delete_User_Category);
            stmt.executeUpdate(Delete_LibUser);
            stmt.executeUpdate(Delete_Book_Category);
            stmt.executeUpdate(Delete_Books);
            System.out.println("Done. Database is removed.");
        }catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }finally {
            Administrator(con);
        }
    }

    // Administrator operation 3: Load data from a dataset
    public static void LoadUserCategory(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/user_category.txt"));
            String InsertUserCategorySQL = "INSERT INTO USER_CATEGORY VALUES";
            String data = "";  
            while (infile.hasNext()){
                for (int i = 0; i < 3; i++){ // there are 3 columns in user_category.txt
                    if (data == ""){
                        data = data + infile.next(); 
                    } else {
                        data = data + ", " + infile.next(); 
                    }
                }
                Statement stmt = con.createStatement();
                try{
                    stmt.executeUpdate(InsertUserCategorySQL + "(" + data + ")");
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                data = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadLibUser(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/user.txt"));
            String dataTXT = "";
            String InsertLibUserPSQL = "INSERT INTO LIBUSER VALUES(?,?,?,?,?)";
            while (infile.hasNextLine()){
                dataTXT = dataTXT + infile.nextLine();
                String[] rowDetail = new String[5];
                rowDetail = dataTXT.split("\\t");
                PreparedStatement pstmt = con.prepareStatement(InsertLibUserPSQL);
                pstmt.setString(1, rowDetail[0]);
                pstmt.setString(2, rowDetail[1]);
                pstmt.setInt(3, Integer.parseInt(rowDetail[2]));
                pstmt.setString(4, rowDetail[3]);
                pstmt.setInt(5, Integer.parseInt(rowDetail[4]));
                try{
                    pstmt.executeUpdate();
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                dataTXT = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadBookCategory(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/book_category.txt"));
            String dataTXT = "";
            String dataSQL = "";
            String InsertBookCategorySQL = "INSERT INTO BOOK_CATEGORY VALUES";
            while (infile.hasNextLine()){
                dataTXT = dataTXT + infile.nextLine();
                String[] rowDetail = new String[2]; // there are 2 columns in book_category.txt
                rowDetail = dataTXT.split("\\t");
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        dataSQL += rowDetail[i];
                    } else {
                        dataSQL += ", '" + rowDetail[i] + "'";
                    }
                }
                Statement stmt = con.createStatement();
                try{
                    stmt.executeUpdate(InsertBookCategorySQL + "(" + dataSQL + ")");
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                dataTXT = "";
                dataSQL = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadBook(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/book.txt"));
            String dataTXT = "";
            String dataSQL = "";
            String InsertBooksSQL = "INSERT INTO BOOKS VALUES";
            while (infile.hasNextLine()){
                dataTXT = dataTXT + infile.nextLine();
                String[] rowDetail = new String[8]; // there are 8 columns in book.txt
                rowDetail = dataTXT.split("\\t");
                /*
                book.txt
                callnum, copynum, title, aname, publish, rating, tborrowed, bcid
                we only need
                callnum, title, publish, rating, tborrowed, bcid
                i == 0, 2, 4, 5, 6, 7
                */
                for (int i = 0; i < 8; i++) {
                    if (i == 0) {
                        dataSQL += "'" + rowDetail[i] + "'";
                    }else if (i == 2){
                        dataSQL += ", '" + rowDetail[i] + "'";
                    }else if (i == 4) {
                        dataSQL += ", '" + rowDetail[i] + "'";
                    }else if (i == 5 || i == 6 || i == 7){
                        dataSQL += ", " + rowDetail[i];
                    }
                }
                Statement stmt = con.createStatement();
                try{
                    stmt.executeUpdate(InsertBooksSQL + "(" + dataSQL + ")");
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                dataTXT = "";
                dataSQL = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadCopy(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/check_out.txt"));
            String dataTXT = "";
            String dataSQL = "";
            String InsertCopySQL = "INSERT INTO COPY VALUES";
            while (infile.hasNextLine()){
                dataTXT = dataTXT + infile.nextLine();
                String[] rowDetail = new String[5]; // there are 5 columns in check_out.txt
                rowDetail = dataTXT.split("\\t");
                for (int i = 0; i < 5; i++) {
                    if (i == 0) {
                        dataSQL += "'" + rowDetail[i] + "'";
                    }else if (i == 1){
                        dataSQL += ", " + rowDetail[i];
                    }
                }
                Statement stmt = con.createStatement();
                try{
                    stmt.executeUpdate(InsertCopySQL + "(" + dataSQL + ")");
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                dataTXT = "";
                dataSQL = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadBorrow(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/check_out.txt"));
            String dataTXT = "";
            String dataSQL = "";
            String InsertBorrowSQL = "INSERT INTO BORROW VALUES";
            while (infile.hasNextLine()){
                dataTXT = dataTXT + infile.nextLine();
                String[] rowDetail = new String[5]; // there are 5 columns in check_out.txt
                rowDetail = dataTXT.split("\\t"); // split string by tab
                /*
                check_out.txt
                callnum, copynum, libuid, checkout, return
                rearrange to
                libuid, callnum, copynum, checkout, return
                0,1,2,3,4 ==> 2,0,1,3,4
                */
                dataSQL += "'" + rowDetail[2] + "', '" + rowDetail[0] + "', " + rowDetail[1] + ", '" + rowDetail[3] + "', '" + rowDetail[4] + "'";
                Statement stmt = con.createStatement();
                try{
                    stmt.executeUpdate(InsertBorrowSQL + "(" + dataSQL + ")");
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                dataTXT = "";
                dataSQL = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadAuthorship(Connection con, String path) {
        try{
            Scanner infile = new Scanner(new File("./" + path + "/book.txt"));
            String dataTXT = "";
            String dataSQL = "";
            String InsertAuthorshipSQL = "INSERT INTO AUTHORSHIP VALUES";
            while (infile.hasNextLine()){
                dataTXT = dataTXT + infile.nextLine();
                String[] rowDetail = new String[8]; // there are 8 columns in book.txt
                rowDetail = dataTXT.split("\\t"); // split string by tab
                dataSQL += "'" + rowDetail[3] + "', '" + rowDetail[0] + "' ";
                Statement stmt = con.createStatement();
                try{
                    stmt.executeUpdate(InsertAuthorshipSQL + "(" + dataSQL + ")");
                }catch (SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                dataTXT = "";
                dataSQL = "";
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    public static void LoadDatafile(Connection con) { // TO-DO
        Scanner sc = new Scanner(System.in);
        System.out.print("\nType in the Source Data Folder Path: ");
        String path = sc.next();
        try{
            LoadUserCategory(con, path);
            LoadLibUser(con, path); // BUG user000003 didnt work
            LoadBookCategory(con, path);
            LoadBook(con, path);
            LoadCopy(con, path);
            LoadBorrow(con, path);
            LoadAuthorship(con, path);
            
            System.out.println("Data is inputted to the database.");
        }catch (Exception ex){
            System.out.println(ex);
        }finally{
            Administrator(con);
        }
    }

    // Administrator operation 4: Show the number of records in each table
    public static void ShowRecord(Connection con) {
        
        String showRecordSQL = "SELECT table_name, table_rows " +
            "FROM INFORMATION_SCHEMA.TABLES " +
            "WHERE TABLE_SCHEMA = 'db14'; ";
        
        try {
            Statement stmt = con.createStatement();
            System.out.println("Number of records in each table");
            ResultSet resultSet = stmt.executeQuery(showRecordSQL);
            if(!resultSet.isBeforeFirst())
	            System.out.println("No records found.");
            else
	            while(resultSet.next()){
		            System.out.print(resultSet.getString("table_name"));
                    System.out.print(": ");
                    System.out.print(resultSet.getInt("table_rows"));
                    System.out.print("\n");
	            }
        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }finally{
            Administrator(con);
        }
    }
    /*
    * Administrator operations end
    * Library User operations start
    */

    /*
    * Library User operations end
    * Librarian operations start
    */
    public static void BorrowBook(Connection con){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        String user = sc.nextLine();
        System.out.print("Enter The Call Number: ");
        String callNumber = sc.nextLine();
        System.out.print("Enter The Copy Number: ");
        String CopyNumber = sc.nextLine();

        boolean canBorrow = false; // if the book is not borrowed, set canBorrow be true

        try {
            String checkReturnSQL = "SELECT return_date FROM BORROW WHERE " +
                "callnum = ? AND copynum = ?";
            PreparedStatement pstmt = con.prepareStatement(checkReturnSQL);
            pstmt.setString(1, callNumber);
            pstmt.setInt(2, Integer.parseInt(CopyNumber));
            ResultSet resultSet = pstmt.executeQuery();
            if(!resultSet.isBeforeFirst())
	            canBorrow = true;
            else{
                resultSet.last();
                if (resultSet.getString("return_date") == "null"){
                    canBorrow = true;
                } else{
                    canBorrow = false;
                }
            }
        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }finally{
            if (canBorrow == true){
                String borrowBookSQL = "INSERT INTO BORROW VALUES(?,?,?,?,?)";
                try{
                    PreparedStatement pstmt = con.prepareStatement(borrowBookSQL);
                    pstmt.setString(1, user);
                    pstmt.setString(2, callNumber);
                    pstmt.setString(3, CopyNumber);
                    String datePattern = "dd/MM/yyyy";
                    String dateInString = new SimpleDateFormat(datePattern).format(new Date());
                    pstmt.setString(4, dateInString);
                    pstmt.setString(5, "null");
                    pstmt.executeUpdate();
                    System.out.println("Book borrowing performed successfully.");
                }catch (SQLException ex){
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }
            try{
                Librarian(con);
            }catch (Exception ex){

            }
        }

            //check if the user id, call no. and copy no. are exist, if yes:
            //else
            System.out.println("Book borrowing failed.");
    }
    public static void ReturnBook(){

    }
    public static void ListAllUnReturnedBook(){

    }
    /*
    * Librarian operations end
    */

    public static void Administrator(Connection con){ // TO-DO
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWhat kind of operation would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputAdmin = sc.nextInt();
        if(inputAdmin == 1){
            //Create all tables
            CreateTable(con);
        }else if(inputAdmin == 2){
            //Delete all tables
            DeleteTable(con);
        }else if(inputAdmin == 3){ // TO-DO
            LoadDatafile(con);
        }else if(inputAdmin == 4){ // TO-DO
            ShowRecord(con);
        }else{
            try{
                bookSystem(con);
            } catch(Exception e) {
                System.out.println("Fail to escape from Administrator to main menu");
                System.exit(0);
            }
        }

        sc.close();
    }

    public static void LibraryUser(Connection con){ // TO-DO
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWhat kind of operation would you like to perform?");
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
                bookSystem(con);
            } catch(Exception e) {
                System.out.println("Fail to escape from Library User to main menu");
                System.exit(0);
            }
        }
    }

    public static void Librarian(Connection con) throws ParseException{ // TO-DO
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWhat kind of operation would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. List all un-returbned book copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputLib = sc.nextInt();
        if(inputLib == 1){
            BorrowBook(con);
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
                bookSystem(con);
            } catch(Exception e) {
                System.out.println("Fail to escape from Librarian to main menu");
                System.exit(0);
            }
        }
    }

    public static void bookSystem(Connection con) throws ParseException{
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWhat kinds of operations would you like to perform?");
        System.out.println("1. Operations for Administrator");
        System.out.println("2. Operations for Library User");
        System.out.println("3. Operations for Librarian");
        System.out.println("4. Exit this program");
        System.out.print("Enter your choice: ");
        int input = sc.nextInt();
        if(input == 1){
            Administrator(con);
        }else if(input == 2){
            LibraryUser(con);
        }else if(input ==3){
            Librarian(con);
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
            System.out.println("Connected to database successfully");
            try{
                bookSystem(con);
            } catch(Exception e) {
                System.out.println("Fail to get in book system initially");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } 
    }
}