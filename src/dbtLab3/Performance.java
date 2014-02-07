package dbtLab3;

public class Performance {
	String date;
	String movieName;
	String theaterName;
	int nbrOfSeats;

	public Performance(String date, String movieName, String theaterName, int nbrOfSeats){
		this.date = date;
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.nbrOfSeats = nbrOfSeats;
	}
	public String getDate () {
		return date;
	}
	public String getMovieName() {
		return movieName;
		
	}
	public String getTheaterName() {
		return theaterName;
	}
	public int nbrofSeats(){
	return nbrOfSeats;
	}
}