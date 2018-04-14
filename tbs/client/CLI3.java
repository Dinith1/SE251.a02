package tbs.client;
 
import java.util.List;
 
import tbs.server.TBSServer;
import tbs.server.TBSServerImpl;
 
public class CLI3 {
    public static void main(String[] args) {
        String path = "theatres1.csv";
       
        if (args.length > 0) {
            path = args[0]; // This allows a different file to be specified as an argument, but the default is theatres1.csv
        }
       
        TBSServer server = new TBSServerImpl();
       
        // -=-=-=-=-=-=-=-=-=- // initialise // -=-=-=-=-=-=-=-=-=- //
        String result = server.initialise(path);
        System.out.println("Result of initialization is: " + result + "\n");
        // Will have to test this myself, change theatres1.csv to contain invalid formats.
       
        // -=-=-=-=-=-=-=-=-=- // addArtist // -=-=-=-=-=-=-=-=-=- //
        String ArtistID1 = server.addArtist("Bon Jovi");
        System.out.print("Expected: an ID \nGot     : " + ArtistID1 + "\n-=-=-=-\n");
        String ArtistID2 = server.addArtist("Ed Sheeran");
        System.out.print("Expected: an ID \nGot     : " + ArtistID2 + "\n-=-=-=-\n");
        String ArtistID3 = server.addArtist("Kanye West");
        System.out.print("Expected: an ID \nGot     : " + ArtistID3 + "\n-=-=-=-\n");
       
        //Adding a duplicate
        String ArtistID4 = server.addArtist("Kanye West");
        System.out.print("Expected: ERROR: Artist already exists\nGot     : " + ArtistID4 + "\n-=-=-=-\n");
       
        //Adding an empty name;
        String ArtistID5 = server.addArtist("");
        System.out.print("Expected: ERROR: No name entered\nGot     : " + ArtistID5 + "\n-=-=-=-\n");
       
        // -=-=-=-=-=-=-=-=-=- // addAct // -=-=-=-=-=-=-=-=-=- //
        String Act1ID = server.addAct("Skinny Love", ArtistID1, 4);
        System.out.print("Expected: an ID \nGot     : " + Act1ID + "\n-=-=-=-\n");
        String Act2ID = server.addAct("Holocene", ArtistID1, 6);
        System.out.print("Expected: an ID \nGot     : " + Act2ID + "\n-=-=-=-\n");
        String Act3ID = server.addAct("Power", ArtistID3, 5);
        System.out.print("Expected: an ID \nGot     : " + Act3ID + "\n-=-=-=-\n");
       
        //Adding an empty title
        String Act4ID = server.addAct("", ArtistID1, 5);
        System.out.print("Expected: ERROR: Empty Title\nGot     : " + Act4ID + "\n-=-=-=-\n");
       
        //Adding to a non-existent artist
        String Act5ID = server.addAct("All Star", "ART4" , 5);
        System.out.print("Expected: ERROR: Artist not found\nGot     : " + Act5ID + "\n-=-=-=-\n");
       
        //Adding to an empty artist
        String Act6ID = server.addAct("All Star", "" , 5);
        System.out.print("Expected: ERROR: Artist not found\nGot     : " + Act6ID + "\n-=-=-=-\n");
       
        //Adding a negative duration
        String Act7ID = server.addAct("Perfect", ArtistID2 , -1);
        System.out.print("Expected: ERROR: Negative time\nGot     : " + Act7ID + "\n-=-=-=-\n");
       
        // -=-=-=-=-=-=-=-=-=- // getActIDsForArtist // -=-=-=-=-=-=-=-=-=- //
        List<String> ActIDs1 = server.getActIDsForArtist(ArtistID1);
        System.out.print("Expected: [an ID, another ID] \nGot     : " + ActIDs1 + "\n-=-=-=-\n");
        List<String> ActIDs2 = server.getActIDsForArtist(ArtistID2);
        System.out.print("Expected: [] \nGot     : " + ActIDs2 + "\n-=-=-=-\n");
       
        //Requesting IDs for an Artist that doesn't exist
        List<String> ActIDs3 = server.getActIDsForArtist("Hurricane Katrina?");
        System.out.print("Expected: ERROR: Artist not found \nGot     : " + ActIDs3 + "\n-=-=-=-\n");
       
        // -=-=-=-=-=-=-=-=-=- // schedulePerformance // -=-=-=-=-=-=-=-=-=- //
        String PerfID1 = server.schedulePerformance(Act1ID, "T1", "1999-05-13T11:27", "$10", "$20");
        System.out.print("Expected: an ID \nGot     : " + PerfID1 + "\n-=-=-=-\n");
        String PerfID2 = server.schedulePerformance(Act1ID, "T1", "2000-05-13T01:27", "$15", "$25");
        System.out.print("Expected: an ID \nGot     : " + PerfID2 + "\n-=-=-=-\n");
        String PerfID3 = server.schedulePerformance(Act3ID, "T2", "2010-07-13T15:27", "$15", "$25");
        System.out.print("Expected: an ID \nGot     : " + PerfID3 + "\n-=-=-=-\n");
       
        // Testing with invalid actID
        String PerfID4 = server.schedulePerformance("invaled", "T2", "2010-07-13T15:27", "$15", "$25");
        System.out.print("Expected: ERROR: Invalid actID \nGot     : " + PerfID4 + "\n-=-=-=-\n");
       
        // Testing with invalid theatre ID
        String PerfID5 = server.schedulePerformance(Act3ID, "Invalid", "2010-07-13T15:27", "$15", "$25");
        System.out.print("Expected: ERROR: Invalid TheatreID \nGot     : " + PerfID5 + "\n-=-=-=-\n");
       
        // Testing with non ISO time
        String PerfID6 = server.schedulePerformance(Act3ID, "T2", "2010-07-13-54:54", "$15", "$25");
        System.out.print("Expected: ERROR: Time format not ISO8601 \nGot     : " + PerfID6 + "\n-=-=-=-\n");
       
        // Testing with invalid prices
        String PerfID7 = server.schedulePerformance(Act3ID, "T2", "2010-07-13T15:27", "-15", "$25");
        System.out.print("Expected: ERROR: Prices are invalid \nGot     : " + PerfID7 + "\n-=-=-=-\n");
        String PerfID8 = server.schedulePerformance(Act3ID, "T2", "2010-07-13T15:27", "xd", "$25");
        System.out.print("Expected: ERROR: Prices are invalid \nGot     : " + PerfID8 + "\n-=-=-=-\n");
       
        // -=-=-=-=-=-=-=-=-=- // issueTicket // -=-=-=-=-=-=-=-=-=- //
        String TicketID1 = server.issueTicket(PerfID1, 4, 5);
        System.out.print("Expected: an ID \nGot     : " + TicketID1 + "\n-=-=-=-\n");
        String TicketID2 = server.issueTicket(PerfID1, 5, 5);
        System.out.print("Expected: an ID \nGot     : " + TicketID2 + "\n-=-=-=-\n");
        String TicketID3 = server.issueTicket(PerfID2, 5, 5);
        System.out.print("Expected: an ID \nGot     : " + TicketID3 + "\n-=-=-=-\n");
       
        // Trying to book a ticket for the same seat
        String TicketID4 = server.issueTicket(PerfID1, 4, 5);
        System.out.print("Expected: ERROR: Seat is booked \nGot     : " + TicketID4 + "\n-=-=-=-\n");
       
        // Trying to book a ticket for a seat out of bounds
        String TicketID5 = server.issueTicket(PerfID1, 4, 500);
        System.out.print("Expected: ERROR: Seat does not exist \nGot     : " + TicketID5 + "\n-=-=-=-\n");
       
        // -=-=-=-=-=-=-=-=-=- // salesReport // -=-=-=-=-=-=-=-=-=- //
        List<String> Sales1 = server.salesReport(Act1ID);
        System.out.println(Sales1);
        List<String> Sales2 = server.salesReport(Act2ID);
        System.out.println(Sales2);
        // Testing with an invalid actID
        List<String> Sales3 = server.salesReport("invalid");
        System.out.println(Sales3);
       
    }
}
