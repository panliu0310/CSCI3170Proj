//Show all un-returned books
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

public static void ListAllUnReturnedBook(Connection con){
    Scanner scLAURB = new Scanner(System.in);
    System.out.print("Type in the starting date [dd/mm/yyyy]: ");
    String startDate = scLAURB.nextLine();
    System.out.print("Type in the ending date [dd/mm/yyyy]: ");
    String endDate = scLAURB.nextLine();
    System.out.println("List of Un-Returned Books: ");
    try{
        PreparedStatement ps = con.prepareStatement("SELECT libuid, callnum, copynum, checkout FROM BORROW WHERE return_date = 'null'");                    
        //ps.setString(1, startDate);
        //ps.setString(2, endDate);
        ArrayList <SortDate> sortDate = new ArrayList();

        ResultSet rs = ps.executeQuery();
        System.out.println("| LibUID | CallNum | CopyNum | Checkout |");
        while(rs.next()){  
            //System.out.println(startDate);
            // String datePattern = "yyyy-dd-MM";
            // String dateInString = new SimpleDateFormat(datePattern).format(new Date(startDate));
            Date sd = ConvertDateType(startDate) ;
            Date ed = ConvertDateType(endDate);
            Date checkoutDate = ConvertDateType(rs.getString("checkout"));
            
            if(checkoutDate.compareTo(sd) >= 0 && checkoutDate.compareTo(ed) <= 0){
                
                
                SortDate sd1 = new SortDate(rs.getString("libuid"),rs.getString("callnum"),rs.getInt("copynum"),checkoutDate);
                sortDate.add(sd1); //add the obj tho the Array List

                                 
            }
            

            
            
        }
        Collections.sort(sortDate);
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