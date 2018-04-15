package tbs.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBSServerImpl implements tbs.server.TBSServer {
	private final Map<Integer, String> errorCodes = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -9158006586374589593L;
	{
		put( 0, ""); // Indicates no error
		put( 1, "ERROR: Entered path is empty \n");
		put( 2, "ERROR: File not found \n");
		put( 3, "ERROR: Incorrect file format \n");
		put( 4, "ERROR: Entered artist name is empty \n");
		put( 5, "ERROR: Entered artist name already exists \n");
		put( 6, "ERROR: Entered artist ID is empty \n");
		put( 7, "ERROR: Entered artist ID does not exist \n");
		put( 8, "ERROR: Entered act title is empty \n");
		put( 9, "ERROR: Entered act duration is invalid (<= 0) \n");
		put(10, "ERROR: Entered act ID is empty \n");
		put(11, "ERROR: Entered act ID does not exist \n");
		put(12, "ERROR: Entered theatre ID is empty \n");
		put(13, "ERROR: Entered theatre ID does not exist \n");
		put(14, "ERROR: Entered start time is empty \n");
		put(15, "ERROR: Entered start time is not in the correct ISO-8601 format (yyyy-mm-ddThh:mm) \n");
		put(16, "ERROR: Entered premium price is empty \n");
		put(17, "ERROR: Entered premium price is not in correct format ($d) \n");
		put(18, "ERROR: Entered premium price is not valid (< 0) \n");
		put(19, "ERROR: Entered cheap price is empty \n");
		put(20, "ERROR: Entered cheap price is not in correct format ($d) \n");
		put(21, "ERROR: Entered cheap price is not valid (< 0) \n");
		put(22, "ERROR: Entered performance ID is empty \n");
		put(23, "ERROR: Entered performance ID does not exist \n");
		put(24, "ERROR: Entered row number is invalid (<= 0) \n");
		put(25, "ERROR: Entered row number is too high \n");
		put(26, "ERROR: Entered seat number is invalid (<= 0) \n");
		put(27, "ERROR: Entered seat number is too high \n");
		put(28, "ERROR: Seat already taken \n");
	}};
	private List<TBSTheatre> theatreList = new ArrayList<TBSTheatre>();
	private List<TBSArtist> artistList = new ArrayList<TBSArtist>();
	private List<TBSAct> actList = new ArrayList<TBSAct>();
	private List<TBSPerformance> performanceList = new ArrayList<TBSPerformance>();
	private List<TBSTicket> ticketList = new ArrayList<TBSTicket>();
	 
	
	public String initialise(String path) {
		if (path.equals("") || path.equals(null)) {
			return errorCodes.get(1);
		}
		
		BufferedReader buffRead;
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
				}
				else {
					buffRead.close();
					return errorCodes.get(3);
				}
			}
			buffRead.close();
		}
		catch (FileNotFoundException e) {
			return errorCodes.get(2);
		} 
		catch (IOException e) {
			return errorCodes.get(3);
		}
		
		theatreList.addAll(tempTheatreList);
		return errorCodes.get(0);
	}

	
	public String addArtist(String name) {
		TBSArtist checkArtist = new TBSArtist(name);
		int errorCode = checkArtist.checkArtistNameError(artistList);
		
		if (errorCode != 0) {
			return errorCodes.get(errorCode);
		}
		
		TBSArtist artist = new TBSArtist(name);
		artistList.add(artist);
		return artist.getID();
	}

	
	public String addAct(String title, String artistID, int minutesDuration) {
		int[] errorCode = {0, 0, 0};
		TBSAct checkAct = new TBSAct(title, artistID, minutesDuration);
		
		errorCode[0] = (title.equals("") || title.equals(null)) ? 8 : 0;
		errorCode[1] = checkAct.checkArtistIDError(artistList);
		errorCode[2] = (minutesDuration <= 0) ? 9 : 0;
		
		if ((errorCode[0] != 0) || (errorCode[1] != 0) || (errorCode[2] != 0)) {
			return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]);
		}
		
		TBSAct act = new TBSAct(title, artistID, minutesDuration);
		actList.add(act);
		return act.getID();
	}

	
	public String schedulePerformance(String actID, String theatreID, String startTimeStr, String premiumPriceStr, String cheapSeatsStr) {
		int[] errorCode = {0, 0, 0, 0, 0};
		TBSPerformance checkPerformance = new TBSPerformance(actID, theatreID, startTimeStr, premiumPriceStr, cheapSeatsStr);
		
		errorCode[0] = checkPerformance.checkActIDError(actList);
		errorCode[1] = checkPerformance.checkTheatreIDError(theatreList);
		errorCode[2] = checkPerformance.checkStartTimeError();
		errorCode[3] = checkPerformance.checkPremiumPriceError();
		errorCode[4] = checkPerformance.checkCheapPriceError();
		
		if ((errorCode[0] != 0) || (errorCode[1] != 0) || (errorCode[2] != 0) || (errorCode[3] != 0) || (errorCode[4] != 0) ) {
			return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]) + errorCodes.get(errorCode[3]) + errorCodes.get(errorCode[4]);
		}
		
		TBSPerformance performance = new TBSPerformance(actID, theatreID, startTimeStr, premiumPriceStr, cheapSeatsStr);
		performanceList.add(performance);
		return performance.getID();
	}
	
	
	public String issueTicket(String performanceID, int rowNumber, int seatNumber) {
		int[] errorCode = {0, 0, 0};
		TBSTicket checkTicket = new TBSTicket(performanceID, rowNumber, seatNumber);
		
		errorCode[0] = checkTicket.checkPerformanceIDError(performanceList);
		errorCode[1] = (rowNumber <= 0) ? 24 : 0;
		errorCode[2] = (seatNumber <= 0) ? 26 : 0;
		
		if (errorCode[0] != 0) {
			return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]);
		}
		
		// This code is reached only if the performanceID exists
		int seatingDimension = checkTicket.findSeatingDimension(performanceList, theatreList);
		
		errorCode[1] = (rowNumber > seatingDimension) ? 25 : errorCode[1];
		errorCode[2] = (seatNumber > seatingDimension) ? 27 : errorCode[2];
		
		if ((errorCode[1] != 0) || (errorCode[2] != 0)) {
			return errorCodes.get(errorCode[0]) + errorCodes.get(errorCode[1]) + errorCodes.get(errorCode[2]);
		}
		
		// Check if seat is already taken. This code is reached only if the only possible error is "seat taken"
		for (TBSTicket tempTicket : ticketList) {
			if (performanceID.equals(tempTicket.getPerformanceID()) && (rowNumber == tempTicket.getRowNumber()) && (seatNumber == tempTicket.getSeatNumber())) {
				return errorCodes.get(28);
			}
		}
		
		TBSTicket ticket = new TBSTicket(performanceID, rowNumber, seatNumber);
		ticketList.add(ticket);
		return ticket.getID();
	}
	
	
	public List<String> getTheatreIDs() {
		List<String> theatreIDs = new ArrayList<String>();
		
		for (TBSTheatre tempTheatre : theatreList) {
			theatreIDs.add(tempTheatre.getID());
		}
		
		Collections.sort(theatreIDs);
		return theatreIDs;
	}

	
	public List<String> getArtistIDs() {
		List<String> artistIDs = new ArrayList<String>();
		
		for (TBSArtist tempArtist : artistList) {
			artistIDs.add(tempArtist.getID());
		}
		
		Collections.sort(artistIDs);
		return artistIDs;
	}

	
	public List<String> getArtistNames() {
		List<String> artistNames = new ArrayList<String>();
		
		for (TBSArtist tempArtist : artistList) {
			artistNames.add(tempArtist.getName());
		}
		
		Collections.sort(artistNames);
		return artistNames;
	}

	
	public List<String> getActIDsForArtist(String artistID) {
		List<String> actIDs = new ArrayList<String>();
		TBSAct checkAct = new TBSAct("foo", artistID, 5);
		int errorCode = checkAct.checkArtistIDError(artistList);
		
		if (errorCode != 0) {
			actIDs.add(errorCodes.get(errorCode));
			return actIDs;
		}
		
		for (TBSAct tempAct : actList) {
			if (artistID.equals(tempAct.getArtistID())) {
				actIDs.add(tempAct.getID());
			}
		}
		Collections.sort(actIDs);
		return actIDs;
	}

	
	public List<String> getPeformanceIDsForAct(String actID) {
		List<String> performanceIDs = new ArrayList<String>();
		TBSPerformance checkPerformance = new TBSPerformance(actID, "foo", "foo", "foo", "foo");
		int errorCode = checkPerformance.checkActIDError(actList);
		
		if (errorCode != 0) {
			performanceIDs.add(errorCodes.get(errorCode));
			return performanceIDs;
		}
		
		for (TBSPerformance tempPerformance : performanceList) {
			if (actID.equals(tempPerformance.getActID())) {
				performanceIDs.add(tempPerformance.getID());
			}
		}
		Collections.sort(performanceIDs);
		return performanceIDs;
	}

	
	public List<String> getTicketIDsForPerformance(String performanceID) {
		List<String> ticketIDs = new ArrayList<String>();
		TBSTicket checkTicket = new TBSTicket(performanceID, 5, 5);
		int errorCode = checkTicket.checkPerformanceIDError(performanceList);
		
		if (errorCode != 0) {
			ticketIDs.add(errorCodes.get(errorCode));
			return ticketIDs;
		}
		
		for (TBSTicket tempTicket : ticketList) {
			if (performanceID.equals(tempTicket.getPerformanceID())) {
				ticketIDs.add(tempTicket.getID());
			}
		}
		Collections.sort(ticketIDs);
		return ticketIDs;
	}

	
	public List<String> seatsAvailable(String performanceID) {
		TBSTicket checkTicket = new TBSTicket(performanceID, 5, 5);
		int errorCode = checkTicket.checkPerformanceIDError(performanceList);
		
		if (errorCode != 0) {
			List<String> seatsAvailable = new ArrayList<String>();
			seatsAvailable.add(errorCodes.get(errorCode));
			return seatsAvailable;
		}
		
		return checkTicket.findSeatsAvailable(ticketList, performanceList, theatreList);
	}

	
	public List<String> salesReport(String actID) {
		TBSPerformance checkPerformance = new TBSPerformance(actID, "foo", "foo", "foo", "foo");
		int errorCode = checkPerformance.checkActIDError(actList);
		
		if (errorCode != 0) {
			List<String> salesReport = new ArrayList<String>();
			salesReport.add(errorCodes.get(errorCode));
			return salesReport;
		}
		
		return checkPerformance.createSalesReport(theatreList, performanceList, ticketList);
	}

	
	public List<String> dump() {
		return null;
	}
}
