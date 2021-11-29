Group number: 14
Group member 1: 
Name: Liu Hoi Pan 
SID: 1155127464
Group member 2: 
Name: Wong Lok Man
SID: 1155138694

Instructions:

1. Set the Environment

Use WinSCP to transfer the file CSCI3170.Proj and the JAR file to the server
run javac CSCI3170Proj.java
run java -cp .:mysql-connector-java-5.1.47 .jar CSCI3170Proj

2. Book system user interface

In the main interface, we can type 1-4 to access different parts of system.
1. Operations for Administrator
2. Operations for Library user
3. Operations for Librarian
4. Exit this program

3. Administrator interface

If we type 1 in the main interface, we will enter the Administrator interface
1. Create all tables
2. Delete all tables
3. Load from datafile
4. Show number of records in each tables
5. Return to the main menu
If we type 1, we will create all tables. Make sure there is no table with the same name. We suggest to type 2 first.
If we type 2, we will delete all tables if exist.
If we type 3, we can input a path of data. The textfile in the path will be automatically loaded in the table.
If we type 4, the system will show number of records in each tables
If we type 5, we will go back to the main interface

4. Library user interface

If we type 2 in the main interface, we will enter the Library user interface
1. Search for Books
2. Show loan record of a user
3. Return to the main menu
If we type 1, we can perform searching for books.
    we may search the book by call number(exact matching), title(partial matching) or author(exact matching),
    then the data of the book is shown.
If we type 2, we need to input the libuid of a user, then the data of the user is shown.
If we type 3, we will go back to the main interface

5. Librarian interface

If we type 3 in the main interface, we will enter the librarian interface
1. Book Borrowing
2. Book Returning
3. List all un-returned book copies which are checked-out within a period
4. Return to the main menu
If we type 1, we can perform book borrowing.
    we need to input the call number and the copy number of the book to check whether it is borrowed,
    then we need to input the libuid of the user. The borrowing process will be performed.
If we type 2, we can perform book returning.
    we need to input the call number and the copy number of the book, as well as the libuid of the user
    to check whether the book is borrowed to this specific user. If yes, we will ask user to input the
    rating of the book. the returning process is done.
If we type 3, we can list all un-returned book copies which are checked-out within a period
    we need to input the start date and end date, then the query will be shown by the system.
If we type 4, we will go back to the main interface.