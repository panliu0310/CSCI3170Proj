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
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    public static void LoadDatafile(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nType in the Source Data Folder Path: ");
        String path = sc.next();
        try{
            LoadUserCategory(con, path);
            LoadLibUser(con, path);
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
    public static void SearchForBooks_callnum(Connection con){
        Scanner sc1 = new Scanner(System.in);
        System.out.print("Type in the Search Keyword: ");
        String sk = sc1.next();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKS JOIN AUTHORSHIP JOIN BOOK_CATEGORY JOIN COPY WHERE " + 
            "BOOKS.callnum = AUTHORSHIP.callnum AND BOOKS.bcid = BOOK_CATEGORY.bcid AND BOOKS.callnum = COPY.callnum " +
            "AND BOOKS.callnum = ?");                    
            ps.setString(1, sk);
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.print("|" + rs.getString("callnum"));
                System.out.print("|" + rs.getString("title"));
                System.out.print("|" + rs.getString("bcname"));
                System.out.print("|" + rs.getString("aname"));
                System.out.print("|" + rs.getFloat("rating"));
                System.out.print("|" + rs.getInt("copynum") + "|\n");
            }
            System.out.println("End of Query");      
        }catch (Exception ex){
            System.out.println("SQL Exception: " + ex.getMessage());                  
        }finally{
            LibraryUser(con);
        }
    }

    public static void SearchForBooks_title(Connection con){
        Scanner sc2 = new Scanner(System.in);
    
        System.out.print("Type in the title: ");
        String title = sc2.nextLine();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKS JOIN AUTHORSHIP JOIN BOOK_CATEGORY JOIN COPY WHERE " + 
                "BOOKS.callnum = AUTHORSHIP.callnum AND BOOKS.bcid = BOOK_CATEGORY.bcid AND BOOKS.callnum = COPY.callnum " +
                "AND title LIKE CONCAT('%',?,'%')");               
            ps.setString(1, title);
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.print("|" + rs.getString("callnum"));
                System.out.print("|" + rs.getString("title"));
                System.out.print("|" + rs.getString("bcname"));
                System.out.print("|" + rs.getString("aname"));
                System.out.print("|" + rs.getFloat("rating"));
                System.out.print("|" + rs.getInt("copynum") + "|\n");
            }
            System.out.println("End of Query");      
        }catch (Exception ex){
            System.out.println("SQL Exception: " + ex.getMessage());
        }finally{
            LibraryUser(con);
        }
    }

    public static void SearchForBooks_author(Connection con){
        Scanner sc3 = new Scanner(System.in);
        System.out.print("Type in the author: ");
        String author = sc3.nextLine();
        //Show the result of the search
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKS JOIN AUTHORSHIP JOIN BOOK_CATEGORY JOIN COPY WHERE " + 
                "BOOKS.callnum = AUTHORSHIP.callnum AND BOOKS.bcid = BOOK_CATEGORY.bcid AND BOOKS.callnum = COPY.callnum " +
                "AND aname LIKE CONCAT('%',?,'%')"); 
            ps.setString(1, author);
            System.out.println("|Call Num|Title|Book Category|Author|Rating|Available No. of Copy");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.print("|" + rs.getString("callnum"));
                System.out.print("|" + rs.getString("title"));
                System.out.print("|" + rs.getString("bcname"));
                System.out.print("|" + rs.getString("aname"));
                System.out.print("|" + rs.getFloat("rating"));
                System.out.print("|" + rs.getInt("copynum") + "|\n");
            }
            System.out.println("End of Query");   
        }catch (Exception ex){
            System.out.println("SQL Exception: " + ex.getMessage());
        }finally{
            LibraryUser(con);
        }
    }

    public static void ShowLoanRecord(Connection con){
        Scanner sc4 = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        String userid = sc4.nextLine();
        try{
            System.out.println("Loan Record: ");
            //show all loan record of the user
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKS JOIN BORROW JOIN AUTHORSHIP WHERE " + 
            "BOOKS.callnum = AUTHORSHIP.callnum AND BOOKS.callnum = BORROW.callnum " +
            "AND BORROW.libuid = ?");
            ps.setString(1, userid);
            System.out.println("|CallNum|CopyNum|Title|Author|Check-out|Returned?");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.print("|" + rs.getString("callnum"));
                System.out.print("|" + rs.getString("copynum"));
                System.out.print("|" + rs.getString("title"));
                System.out.print("|" + rs.getString("aname"));
                System.out.print("|" + rs.getString("checkout"));
                if (rs.getString("return_date").equals("null")){
                    System.out.print("|No|\n");
                }else{
                    System.out.print("|Yes|\n");
                }
            }
            System.out.println("End of Query");
        }catch (Exception ex){
            System.out.println("SQL Exception: " + ex.getMessage());
        }finally{
            LibraryUser(con);
        }
        
    }
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
            String checkReturnSQL = "SELECT * FROM BORROW WHERE " +
                "callnum = ? AND copynum = ? " +
                "ORDER BY return_date";
            PreparedStatement pstmt = con.prepareStatement(checkReturnSQL);
            pstmt.setString(1, callNumber);
            pstmt.setInt(2, Integer.parseInt(CopyNumber));
            ResultSet resultSet = pstmt.executeQuery();
            if(!resultSet.isBeforeFirst())
	            canBorrow = true;
            else{
                resultSet.last();
                if (resultSet.getString("return_date").equals("null")){
                    canBorrow = false;
                } else{
                    canBorrow = true;
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
            }else{
                System.out.println("[Error]: The book has been borrowed.");
            }
            try{
                Librarian(con);
            }catch (Exception ex){

            }
        }
    }
    public static void ReturnBook(Connection con){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        String user = sc.nextLine();
        System.out.print("Enter The Call Number: ");
        String callNumber = sc.nextLine();
        System.out.print("Enter The Copy Number: ");
        String copyNumber = sc.nextLine();
        System.out.print("Enter Your Rating of the Book: ");
        String userRating = sc.nextLine();

        try { // check the book has been borrowed
            String checkReturnSQL = "SELECT * FROM BORROW WHERE " +
                "libuid = ? AND callnum = ? AND copynum = ? AND return_date = ?";
            PreparedStatement pstmt = con.prepareStatement(checkReturnSQL);
            pstmt.setString(1, user);
            pstmt.setString(2, callNumber);
            pstmt.setInt(3, Integer.parseInt(copyNumber));
            pstmt.setString(4, "null");
            ResultSet resultSet = pstmt.executeQuery();
            if(!resultSet.isBeforeFirst())
                System.out.println("[Error]: An matching borrow record is not found. The book has not been borrowed yet.");
            else{
                //UPDATE BORROW
                try{
                    String returnBookSQL = "UPDATE BORROW SET return_date = ? WHERE " +
                        "libuid = ? AND callnum = ? AND copynum = ? AND return_date = ?";
                    PreparedStatement pstmt2 = con.prepareStatement(returnBookSQL);
                    String datePattern = "dd/MM/yyyy";
                    String dateInString = new SimpleDateFormat(datePattern).format(new Date());
                    pstmt2.setString(1, dateInString);
                    pstmt2.setString(2, user);
                    pstmt2.setString(3, callNumber);
                    pstmt2.setInt(4, Integer.parseInt(copyNumber));
                    pstmt2.setString(5, "null");
                    pstmt2.executeUpdate();
                }catch (SQLException ex){
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                // UPDATE BOOKS
                try{
                    // get original book rating
                    String getBookRatingSQL = "SELECT rating, tborrowed FROM BOOKS WHERE callnum = '" + callNumber + "'";
                    Statement stmt = con.createStatement();
                    ResultSet resultSet2 = stmt.executeQuery(getBookRatingSQL);
                    float bookRating = 0;
                    int timeBorrowed = 0;
                    if(!resultSet2.isBeforeFirst())
                        System.out.println("No records found.");
                    else{
                        while(resultSet2.next()){
                            if(resultSet2.getString("rating") == "null"){
                                bookRating = 0;
                            }else{
                                bookRating = resultSet2.getFloat("rating");
                                timeBorrowed = resultSet2.getInt("tborrowed");
                            }
                        }   
                    }
                    // update book rating and time borrowed
                    String updateRatingSQL = "UPDATE BOOKS SET rating = ?, tborrowed = ? WHERE " +
                        "callnum = ?";
                    PreparedStatement pstmt2 = con.prepareStatement(updateRatingSQL);
                    float newBookRating = (bookRating * timeBorrowed + Float.parseFloat(userRating))/(timeBorrowed + 1); 
                    pstmt2.setFloat(1, newBookRating);
                    pstmt2.setInt(2, timeBorrowed + 1);
                    pstmt2.setString(3, callNumber);
                    pstmt2.executeUpdate();
                }catch (SQLException ex){
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }finally{
                    System.out.println("Book returning performed successfully.");
                }
            }
        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }finally{
            try{
                Librarian(con);
            }catch (Exception ex){

            }
        }
    }

    public static Date ConvertDateType(String d){
        Date date1 = null;
        try{           
            String sDate1 = d;
            date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            //System.out.println(sDate1+"\t"+date1);
            
        }catch(Exception e){
            System.out.println(e);
        }
        return date1;      
    }

    public static String dateConvertToString(Date d){
        String datePattern = "dd/MM/yyyy";
        String dateInString = new SimpleDateFormat(datePattern).format(d);
        return dateInString;
    }


    public static class SortDate implements Comparable<SortDate>{
        String _libuid;
        String _callnum;
        Integer _copynum;
        Date _checkout;
        public Date getCheckout(){
            return _checkout;
        }
        
        public void setCheckout(Date co){
            this._checkout = co;
        }
        
        public SortDate(String Libuid, String Callnum, Integer Copynum, Date Checkout){
            _libuid = Libuid;
            _callnum = Callnum;
            _copynum = Copynum;
            _checkout = Checkout;
            
        }

        
        public int compareTo(SortDate obj){
            return getCheckout().compareTo(obj.getCheckout());
        }
    }

    public static ArrayList<SortDate> reverseArrayList(ArrayList<SortDate> alist)
    {
        // Arraylist for storing reversed elements

        ArrayList<SortDate> revArrayList = new ArrayList<SortDate>();

        for (int i = alist.size() - 1; i >= 0; i--) {
 

            // Append the elements in reverse order

            revArrayList.add(alist.get(i));

        }
        // Return the reversed arraylist
        return revArrayList;
    }

    public static void ListAllUnReturnedBook(Connection con){
        Scanner scLAURB = new Scanner(System.in);
        System.out.print("Type in the starting date [dd/mm/yyyy]: ");
        String startDate = scLAURB.nextLine();
        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
        String endDate = scLAURB.nextLine();
        System.out.println("List of Un-Returned Books: ");
        try{
            PreparedStatement ps = con.prepareStatement("SELECT libuid, callnum, copynum, checkout FROM BORROW WHERE return_date = 'null'");                    
            ArrayList <SortDate> sortDate = new ArrayList();

            ResultSet rs = ps.executeQuery();
            System.out.println("| LibUID | CallNum | CopyNum | Checkout |");
            while(rs.next()){
                Date sd = ConvertDateType(startDate) ;
                Date ed = ConvertDateType(endDate);
                Date checkoutDate = ConvertDateType(rs.getString("checkout"));
                
                if(checkoutDate.compareTo(sd) >= 0 && checkoutDate.compareTo(ed) <= 0){
                    SortDate sd1 = new SortDate(rs.getString("libuid"),rs.getString("callnum"),rs.getInt("copynum"),checkoutDate);
                    sortDate.add(sd1); //add the obj tho the Array List                    
                }  
            }
            
            Collections.sort(sortDate);
            sortDate = reverseArrayList(sortDate);

            for(SortDate obj: sortDate){
                //sortDate[i]
                System.out.print("| " + obj._libuid + " ");
                System.out.print("| " + obj._callnum + " ");
                System.out.print("| " + obj._copynum + " ");
                System.out.println("| " + dateConvertToString(obj._checkout) + " ");
            }
            System.out.println("End of Query");
            Librarian(con);
        }
        catch(Exception e){
            System.out.println(e);
        }finally{
            
        } 
         
    }

    /*
    * Librarian operations end
    */

    public static void Administrator(Connection con){
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
        }else if(inputAdmin == 3){
            LoadDatafile(con);
        }else if(inputAdmin == 4){
            ShowRecord(con);
        }else{
            try{
                bookSystem(con);
            } catch(Exception e) {
                System.out.println("Fail to escape from Administrator to main menu");
                System.exit(0);
            }
        }
    }

    public static void LibraryUser(Connection con){
        try{
            Scanner sc_lb = new Scanner(System.in);

            System.out.println("\nWhat kind of operation would you like to perform?");
            System.out.println("1. Search for Books");
            System.out.println("2. Show loan record of a user");
            System.out.println("3. Return to the main menu");
            System.out.print("Enter your choice: ");
            int inputLU = 1;
            try{
                inputLU = Integer.parseInt(sc_lb.nextLine());
            }catch (Exception e){
                System.out.println("catch input " + e);
            }
            
            if(inputLU == 1){
                Scanner sc_lb1 = new Scanner(System.in);
                System.out.println("Choose the Search criterion: ");
                System.out.println("1. call number");
                System.out.println("2. title");
                System.out.println("3. author");
                System.out.print("Choose the search criterion: ");
                int crit = sc_lb1.nextInt();
                if(crit == 1){
                    SearchForBooks_callnum(con);
                    //Show the result of the search
                }else if(crit == 2){
                    SearchForBooks_title(con);
                    //Show the result of the search
                }else{
                    SearchForBooks_author(con);
                }
            }else if(inputLU == 2){ //show all loan record of the user
                    ShowLoanRecord(con);
            }else{
                try{
                    bookSystem(con);
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Fail to escape from Library User to main menu");
                    System.exit(0);
                }
            }
                   
        }catch(Exception e){
            System.out.println("hi");
            System.out.println (e);
        }
    }

    public static void Librarian(Connection con) throws ParseException{
        Scanner sc = new Scanner(System.in);

        System.out.println("\nWhat kind of operation would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. List all un-returned book copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputLib = sc.nextInt();
        if(inputLib == 1){
            BorrowBook(con);
        }else if(inputLib == 2){
            ReturnBook(con);
        }else if(inputLib == 3){
            ListAllUnReturnedBook(con);
        }else{
            try{
                bookSystem(con);
            }catch (Exception ex){

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
                System.out.println(e);
                System.out.println("Fail to get in book system initially");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } 
    }
}