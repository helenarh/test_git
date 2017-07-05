import java.util.ArrayList;
import java.util.List;

public class Meeting {
	
	private String type;
	private List<String > details = new ArrayList<String>();
		
	public Meeting(){
		type = null;
	}//ends default constructor
	public Meeting(String type) {
		this.type = type;
	}//ends constructor
	public void setType(String type) {
		this.type = type;
	}//ends setType
	public void addDetails(String line) {
		details.add(line);
	}//end addDetails
	public String getMeetingDetails() {
		String result = "";
		result += "type: "+type +"\n";
			
		for(int h = 0; h < details.size(); h++) {
			result += "\t"+details.get(h) + "\n";	
		}//ends for
		return result;
	}//ends getMeetingDetails
	public String getType() {
		return type;
	}//ends getType()
	
}//ends Meeting
