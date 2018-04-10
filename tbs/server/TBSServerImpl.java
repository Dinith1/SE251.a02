package tbs.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBSServerImpl implements tbs.server.TBSServer {
	
	private final Map<Integer, String> errorCodes = new HashMap<Integer, String>() {{
		put( 0, "");
		put( 1, "ERROR: File not found \n");
		put( 2, "ERROR: Incorrect file format \n");
		put( 3, "ERROR: Entered artist name is empty \n");
		put( 4, "ERROR: Artist with same name already exists \n");
		put( 5, "ERROR: Entered act title is empty \n");
		put( 6, "ERROR: Entered artist ID is empty \n");
		put( 7, "ERROR: Entered artist ID does not exist \n");
		put( 8, "ERROR: Entered act duration is invalid (<= 0) \n");
		put( 9, "ERROR: Entered act ID is empty \n");
		put(10, "ERROR: Entered act ID does not exist \n");
		put(11, "ERROR: Entered theatre ID is empty \n");
		put(12, "ERROR: Entered theatre ID does not exist \n");
		put(13, "ERROR: Entered start time is empty \n");
		put(14, "ERROR: Entered start time is not in the correct format (yyyy-mm-ddThh:mm) \n");
		put(15, "ERROR: Entered premium price is empty \n");
		put(16, "ERROR: Entered premium price is not in correct format ($d) \n");
		put(17, "ERROR: Entered premium price is not valid (< 0) \n");
		put(18, "ERROR: Entered cheap price is empty \n");
		put(19, "ERROR: Entered cheap price is not in correct format ($d) \n");
		put(20, "ERROR: Entered cheap price is not valid (< 0) \n");
		put(21, "ERROR: Entered performance ID is empty \n");
		put(22, "ERROR: Entered performance ID does not exist \n");
		put(23, "ERROR: Entered row number is invalid (<= 0) \n");
		put(24, "ERROR: Entered row number is too high \n");
		put(25, "ERROR: Entered seat number is invalid (<= 0) \n");
		put(26, "ERROR: Entered seat number is too high \n");
		put(27, "ERROR: Seat already taken \n");
	}};
	
	private List<TBSTheatre> theatreList = new ArrayList<TBSTheatre>();
	private List<TBSArtist> artistList = new ArrayList<TBSArtist>();
	private List<TBSAct> actList = new ArrayList<TBSAct>();
	private List<TBSPerformance> performanceList = new ArrayList<TBSPerformance>();
	private List<TBSTicket> ticketList = new ArrayList<TBSTicket>();
	 
	
	
	public String initialise(String path) {
		BufferedReader buffRead;
		int errorCode = 0;
		String line;
		List<TBSTheatre> tempTheatreList = new ArrayList<TBSTheatre>();
		
		try {
			buffRead = new BufferedReader(new FileReader(path));
			while ((line = buffRead.readLine()) != null) {
				// Check that each line matches the specified format 
				if (line.matches("THEATRE\t\\S+\t\\d+\t\\d+")) {
					String[] theatreValues = line.split("\t");
					TBSTheatre theatre = new TBSTheatre(theatreValues[1], theatreValues[2], theatreValues[3]);
					tempTheatreList.add(theatre);
					errorCode = 0;
				}
				else {
					errorCode = 2;
					break;
				}
			}
		}
		catch (FileNotFoundException e) {
				e.printStackTrace();
				errorCode = 1;
		} 
		catch (IOException e) {
			e.printStackTrace();
			errorCode = 2;
		}
		theatreList.addAll(tempTheatreList);
		return errorCodes.get(errorCode);
	}

	
	
	public String addArtist(String name) {
		if (name.equals("") || name.equals(null)) {
			return errorCodes.get(3);
		}
		else {
			for (int i = 0; i < artistList.size(); i++) {
				TBSArtist tempArtist = artistList.get(i);
				if (name.toLowerCase().equals(tempArtist.getName())) {
					return errorCodes.get(4);
				}
			}
		}
		TBSArtist artist = new TBSArtist(name);
		artistList.add(artist);
		return artist.getID();
	}

	
	
	public String addAct(String title, String artistID, int minutesDuration) {
		int[] errorCode = {0,0,0};
		
		if (title.equals("") || title.equals(null)) {
			errorCode[0] = 5;
		}
		
		if (minutesDuration <= 0) {
			errorCode[2] = 8;
		}
		
		if (artistID.equals("") || artistID.equals(null)) {
			errorCode[1] = 6;
		}
		else {
			for (int i = 0; i < artistList.size(); i++) {
				TBSArtist tempArtist = artistList.get(i);
				if (artistID.equals(tempArtist.getID())) {
					TBSAct act = new TBSAct(title, artistID, minutesDuration);
					actList.add(act);
					return act.getID();
				}
			}
		}
		
		return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]);
	}

	
	
	public String schedulePerformance(String actID, String theatreID, String startTimeStr, String premiumPriceStr, String cheapSeatsStr) {
		boolean inputErrorExists = false;
		boolean actIDEmpty = false;
		boolean actIDExists = false;
		boolean theatreIDEmpty = false;
		boolean theatreIDExists = false;
		int[] errorCode = {0, 0, 0, 0, 0};
		
		// Check for all possible input errors
		if (actID.equals("") || actID.equals(null)) {
			errorCode[0] = 9;
			inputErrorExists = true;
			actIDEmpty = true;
		}
		
		if (theatreID.equals("") || theatreID.equals(null)) {
			errorCode[1] = 11;
			inputErrorExists = true;
			theatreIDEmpty = true;
		}
		
		if (startTimeStr.equals("") || startTimeStr.equals(null)) {
			errorCode[2] = 13;
			inputErrorExists = true;
		}
		else if (!startTimeStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
			errorCode[2] = 14;
			inputErrorExists = true;
		}
		
		if (premiumPriceStr.equals("") || premiumPriceStr.equals(null)) {
			errorCode[3] = 15;
			inputErrorExists = true;
		}
		else if (!premiumPriceStr.matches("\\$\\d+")) {
			errorCode[3] = 16;
			inputErrorExists = true;
		}
		else if (Integer.parseInt(premiumPriceStr.substring(1, premiumPriceStr.length())) < 0) {
			errorCode[3] = 17;
			inputErrorExists = true;
		}
		
		if (cheapSeatsStr.equals("") || cheapSeatsStr.equals(null)) {
			errorCode[4] = 18;
			inputErrorExists = true;
		}
		else if (!cheapSeatsStr.matches("\\$\\d+")) {
			errorCode[4] = 19;
			inputErrorExists = true;
		}
		else if (Integer.parseInt(cheapSeatsStr.substring(1, cheapSeatsStr.length())) < 0) {
			errorCode[4] = 20;
			inputErrorExists = true;
		}
		
		if (!actIDEmpty) {
			for (int i = 0; i < actList.size(); i++) {
				TBSAct tempAct = actList.get(i);
				if (actID.equals(tempAct.getID())) {
					actIDExists = true;
				}
			}
			if (!actIDExists) {
				errorCode[0] = 10;
			}
		}
		
		if (!theatreIDEmpty) {
			for (int i = 0; i < theatreList.size(); i++) {
				TBSTheatre tempTheatre = theatreList.get(i);
				if (theatreID.equals(tempTheatre.getID())) {
					theatreIDExists = true;
				}
			}
			if (!theatreIDExists) {
				errorCode[1] = 12;
			}
		}
		
		
		if (inputErrorExists || actIDEmpty || !actIDExists || theatreIDEmpty || !theatreIDExists) {
			return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]) + errorCodes.get(errorCode[3]) + errorCodes.get(errorCode[4]);
		}
		
		// If there are no input errors
		int premiumPrice = Integer.parseInt(premiumPriceStr.substring(1, cheapSeatsStr.length()));
		int cheapPrice = Integer.parseInt(premiumPriceStr.substring(1, cheapSeatsStr.length()));
		TBSPerformance performance = new TBSPerformance(actID, theatreID, startTimeStr, premiumPrice, cheapPrice);
		performanceList.add(performance);
		return performance.getID();
	}
	
	
	
	public String issueTicket(String performanceID, int rowNumber, int seatNumber) {
		int[] errorCode = {0, 0, 0};
		
		if (performanceID.equals("") || performanceID.equals(null)) {
			errorCode[0] = 21;
		}
		
		if (rowNumber <= 0) {
			errorCode[1] = 23;
		}
		
		if (seatNumber <= 0) {
			errorCode[2] = 25;
		}
		
		if (errorCode[0] != 0) {
			return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]);
		}
		
		// Assume for now the entered performance ID does not exist
		errorCode[0] = 22;
		
		// Check that a performance with the given ID exists and that the row/seat is valid
		for (int i = 0; i < performanceList.size(); i++) {
			TBSPerformance tempPerformance = performanceList.get(i);
			// MAYBE SHOULD BE .equals(input) TO AVOID NULL POINTER EXCEPTION ~ But this is not possible in this case due to above code...
			if (performanceID.equals(tempPerformance.getID()) && !(rowNumber <= 0) && !(seatNumber <= 0)) {
				
				// Check that input row/seat is valid
				for (int j = 0; j < theatreList.size(); j++) {
					TBSTheatre tempTheatre = theatreList.get(j);
					int seatingDimension = tempTheatre.getSeatingDimensions();
					
					// Check which theatre the performance is in 
					if (tempPerformance.getTheatreID().equals(tempTheatre.getID())) {
						if (!(rowNumber > seatingDimension) && !(seatNumber > seatingDimension)) {
							
							// Check if seat is already taken
							for (int k = 0; k < ticketList.size(); k++) {
								TBSTicket tempTicket = ticketList.get(k);
								// This code is reached only if the only possible error is "seat taken"
								if (performanceID.equals(tempTicket.getPerformanceID()) && (rowNumber == tempTicket.getRowNumber()) && (seatNumber == tempTicket.getSeatNumber())) {
									return errorCodes.get(27);
								}
							}
							
							// If seat is not taken then there are no other possible errors at this point
							TBSTicket ticket = new TBSTicket(performanceID, rowNumber, seatNumber);
							ticketList.add(ticket);
							return ticket.getID();
						}
						else {
							if (rowNumber > seatingDimension) {
								errorCode[1] = 24;
							}
							if (seatNumber > seatingDimension) {
								errorCode[2] = 26;
							}
							// The performance can only be in one theatre, so the break prevents further pointless iteration
							break;
						}
					}
				}
				// The performance can only be in the performanceList once, so the break prevents further pointless iteration
				break;
			}
			else if (performanceID.equals(tempPerformance.getID())) {
				errorCode[0] = 0;
				break;
			}
		}
		return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]);
	}
	
	
	
	public List<String> getTheatreIDs() {
		List<String> theatreIDs = new ArrayList<String>();
		
		for (int i = 0; i < theatreList.size(); i++) {
			TBSTheatre tempTheatre = theatreList.get(i);
			theatreIDs.add(tempTheatre.getID());
		}
		
		Collections.sort(theatreIDs);
		return theatreIDs;
	}

	
	
	public List<String> getArtistIDs() {
		List<String> artistIDs = new ArrayList<String>();
		
		for (int i = 0; i < artistList.size(); i++) {
			TBSArtist tempArtist = artistList.get(i);
			artistIDs.add(tempArtist.getID());
		}
		
		Collections.sort(artistIDs);
		return artistIDs;
	}

	
	
	public List<String> getArtistNames() {
		List<String> artistNames = new ArrayList<String>();
		
		for (int i = 0; i < artistList.size(); i++) {
			TBSArtist tempArtist = artistList.get(i);
			artistNames.add(tempArtist.getName());
		}
		
		Collections.sort(artistNames);
		return artistNames;
	}

	
	
	public List<String> getActIDsForArtist(String artistID) {
		List<String> actIDs = new ArrayList<String>();
		int errorCode = 0;
		
		if (artistID.equals("") || artistID.equals(null)) {
			errorCode = 6;
		}
		else {
			for (int i = 0; i < artistList.size(); i++) {
				TBSArtist tempArtist = artistList.get(i);
				// Check whether input artistID exists
				if (artistID.equals(tempArtist.getID())) {
					for (int j = 0; j < actList.size(); j++) {
						TBSAct tempAct = actList.get(j);
						// Check which acts belong to the artistID
						if (artistID.equals(tempAct.getArtistID())) {
							actIDs.add(tempAct.getID());
						}
					}
					Collections.sort(actIDs);
					return actIDs;
				}
			}
			// This line is reached only if the input artistID does not exist
			errorCode = 7;
		}
		
		actIDs.add(errorCodes.get(errorCode));
		return actIDs;
	}

	
	
	public List<String> getPeformanceIDsForAct(String actID) {
		List<String> performanceIDs = new ArrayList<String>();
		int errorCode = 0;
		
		if (actID.equals("") || actID.equals(null)) {
			errorCode = 9;
		}
		else {
			for (int i = 0; i < actList.size(); i++) {
				TBSAct tempAct = actList.get(i);
				// Check whether input actID exists
				if (actID.equals(tempAct.getID())) {
					for (int j = 0; j < performanceList.size(); j++) {
						TBSPerformance tempPerformance = performanceList.get(j);
						// Check which performances belong to the actID
						if (actID.equals(tempPerformance.getActID())) {
							performanceIDs.add(tempPerformance.getActID());
						}
					}
					Collections.sort(performanceIDs);
					return performanceIDs;
				}
			}
			// This line is reached only if the input actID does not exist
			errorCode = 10;
		}
		
		performanceIDs.add(errorCodes.get(errorCode));
		return performanceIDs;
	}

	
	
	public List<String> getTicketIDsForPerformance(String performanceID) {
		List<String> ticketIDs = new ArrayList<String>();
		int errorCode = 0;
		
		if (performanceID.equals("") || performanceID.equals(null)) {
			errorCode = 21;
		}
		else {
			for (int i = 0; i < ticketList.size(); i++) {
				TBSPerformance tempPerformance = performanceList.get(i);
				// Check whether input performanceID exists
				if (performanceID.equals(tempPerformance.getID())) {
					for (int j = 0; j < ticketList.size(); j++) {
						TBSTicket tempTicket = ticketList.get(j);
						// Check which tickets belong to the performanceID
						if (performanceID.equals(tempTicket.getPerformanceID())) {
							ticketIDs.add(tempTicket.getID());
						}
					}
					Collections.sort(ticketIDs);
					return ticketIDs;
				}
			}
			// This line is reached only if the input performanceID does not exist
			errorCode = 22;
		}
		
		ticketIDs.add(errorCodes.get(errorCode));
		return ticketIDs;
	}

	
	
	public List<String> seatsAvailable(String performanceID) {
		List<String> seatsAvailable = new ArrayList<String>();
		int errorCode = 0;
		
		if (performanceID.equals("") || performanceID.equals(null)) {
			errorCode = 21;
		}
		else {
			for (int i = 0; i < performanceList.size(); i++) {
				TBSPerformance tempPerformance = performanceList.get(i);
				
				// Check if the input performanceID exists
				if (performanceID.equals(tempPerformance.getID())) {
					for (int j = 0; j < theatreList.size(); j++) {
						TBSTheatre tempTheatre = theatreList.get(j);
						
						// Check which theatre the performance is in
						if (tempPerformance.getTheatreID().equals(tempTheatre.getID())) {
							int seatingDimension = tempTheatre.getSeatingDimensions();
							
							// Iterate through all seats in theatre to check which ones are available
							for (int row = 1; row <= seatingDimension; row++) {
								for (int seat = 1; seat <= seatingDimension; seat++) {
									boolean seatAvaliable = true;
									
									for (int k = 0; k < ticketList.size(); k++) {
										TBSTicket tempTicket = ticketList.get(k);
										if ((row == tempTicket.getRowNumber()) && (seat == tempTicket.getSeatNumber())) {
											seatAvaliable = false;
										}
									}
									if (seatAvaliable) {
										seatsAvailable.add(Integer.toString(row) + "\t" + Integer.toString(seat));
									}
								}
							}
							return seatsAvailable;
						}
					
					}
				}
			}
			errorCode = 22;
		}
		
		seatsAvailable.add(errorCodes.get(errorCode));
		return seatsAvailable;
	}

	
	
	public List<String> salesReport(String actID) {

		
	}

	
	
	public List<String> dump() {

		
	}
	

}
