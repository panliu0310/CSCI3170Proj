import java.sql.*;

String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db14";
String dbUsername = "Group14";
String dbPassword = "3170A";

Connection con = null;
try{
    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
}catch(classNotFoundException e){
    System.out.println("[Error]: Java MySQL DB Driver not found!");
    System.exit(0);
}catch(SQLException e){
    System.out.println(e);
    System.exit(0);
}

//sql involved Create, Insert, Update, Drop, Delete: stmt.executeUpdate(sql);
String sql = "SELECT * FROM student 
                WHERE age = %s and gender = %s;";
String aUserInputAge = ***; 
String aUserInputGender = ***;
sql = String.format(sql, aUserInputAge, aUserInputGender);

Connection con = ***;

Statement stmt = con.createStatement(sql); 
ResultSet rs = stmt.executeQuery(); //For select query: ResultSet rs = stmt.executeQuery(sql);