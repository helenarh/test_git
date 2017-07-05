import java.util.ArrayList;
import java.util.List;

public class Staff {
	//variables
	private String type;
	private String id;
	private List<String > details = new ArrayList<String>();
	
	//functions
	public Staff(String type, String id) {
		this.type = type;
		this.id = id;
	}//ends constructor
	public void addDetails(String line) {
		details.add(line);			
	}//ends add Details
	public List<String> getDetails() {
		return details;
	}//getDetails
	public void printDetails() {
			System.out.println(this.getStaffDetails());
	}//ends printDetails
	public String getStaffDetails() {
		String result = "";
			result += "type: "+type +" id:"+ id + "\n";
			
		for(int h = 0; h < details.size(); h++) {
			//result += details.get(h) + "\t";
			result += "\t"+details.get(h) + "\n";	
		}//ends for
		return result;
	}//ends getStaffDetails
	public String getType() {
		return type;
	}//ends getType
	public String getID() {
		return id;
	}//ends getType
	public void setType(String type) {
		this.type = type;
	}//ends setType
	public void setId(String id) {
		this.id = id;
	}//ends setId

}//ends staff class
