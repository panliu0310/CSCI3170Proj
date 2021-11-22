import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//can see any updates?
public class cmdLine{
    
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

    public static void Administrator(){
        Scanner sc = new Scanner(System.in);

        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputAdmin = sc.nextInt();
        if(inputAdmin == 1){
            //Create all tables
            System.out.println("Done. Database is initialized.");
        }else if(inputAdmin == 2){
            //Delete all tables
            System.out.println("Done. Database is removed.");
        }else if(inputAdmin == 3){
            //Load from datafile
            System.out.println("Done. Data is inputted to the database.");
        }else if(inputAdmin == 4){
            //Show the number of records in each table
            System.out.println("Number of records in each table: ");
            System.out.println("Table1: "); //plus the table
            System.out.println("Table2: "); //plus the table
            System.out.println("Table3: "); //plus the table
            System.out.println("Table4: "); //plus the table
        }else{
            bookSystem();
        }

        sc.close();
    }

    public static void LibraryUser(){
        Scanner sc = new Scanner(System.in);

        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Search for Books");
        System.out.println("2. Show loan record of a user");
        System.out.println("3. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputLU = sc.nextInt();
        if(inputLU == 1){
           

            System.out.println("Choose the Search criterion: ");
            System.out.println("1. call number");
            System.out.println("2. title");
            System.out.println("3. author");
            System.out.println("Choose the search criterion: ");
            int crit = sc.nextInt();
            if(crit == 1){
                System.out.print("Type in the Search Keyword: ");
                String sk = sc.next();
                //Show the result of the search
            }else if(crit == 2){
                System.out.print("Type in the title: ");
                String title = sc.next();
                //Show the result of the search
            }else{
                System.out.print("Type in the author: ");
                String author = sc.next();
                //Show the result of the search
            }
        }else if(inputLU == 2){
            System.out.println("Enter The User ID: ");
            String user = sc.next();
            System.out.println("Loan Record");
            //show all loan record of the user
        }else{
            bookSystem();
        }
    }

    public static void Librarian() throws ParseException{
        Scanner sc = new Scanner(System.in);

        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Book Borrowing");
        System.out.println("2. Book Returning");
        System.out.println("3. List all un-returbned book copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        System.out.print("Enter your choice: ");
        int inputLib = sc.nextInt();
        if(inputLib == 1){
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
        }else if(inputLib == 2){
            System.out.print("Enter The User ID: ");
            String user = sc.nextLine();
            System.out.print("Enter The Call Number: ");
            String callNumber = sc.nextLine();
            System.out.print("Enter The Copy Number: ");
            String CopyNumber = sc.nextLine();

            //if the info input above is equal to borrowing record, then perform returning books; else, cannot return.
            System.out.println("Book returning performed successfully.");
        }else if(inputLib == 3){
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
            bookSystem();
        }
    }
   
}