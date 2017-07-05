import java.util.ArrayList;
import java.util.List;

public class Course {
	private String num;
	private String term;
	private String year;
	private List<Staff> staffers = new ArrayList<Staff>();
	private List<Meeting> meeting = new ArrayList<Meeting>();
	
	public Course() {
		num = null;
		term = null;
		year = null;
	}//ends default constructor
	public Course(String number, String term, String year){
		num = number;
		this.term = term;
		this.year = year;
	}//ends constructor
	public void setStaffList(List<Staff> staff) {
		staffers = staff; 
	}//ends setMeetingList
	public void setMeetingList(List<Meeting> meet) {
		meeting = meet; 
	}//ends setMeetingList
	public void setYear(String year) {
		this.year = year;
	}//ends setYear
	public void setTerm(String term) {
		this.term = term;
	}//ends setTerm
	public void setNum(String numb) {
		num = numb;
	}//ends setNum
	public String getCourseDetails(){
		return num + " " + term + " " + year;
	}//ends getCourseDetails
	public String getStaffDetails() {
		String result = "";
		for(int h = 0; h < staffers.size(); h++) {
			result += staffers.get(h).getStaffDetails() + "\n";
		}//ends for
		return result;
	}//ends getStaffDetails
	public void addStaff(Staff staff) {
		staffers.add(staff);
	}//ends addStaff
	public void addMeeting(Meeting meet) {
		meeting.add(meet);
	}//ends addMeeting
	public String getMeetingDetails() {
		String result = "";
		for(int h = 0; h < meeting.size(); h++) {
			result += meeting.get(h).getMeetingDetails() + "\n";
		}//ends for
		return result;
	}//ends getMeetingDetails
	public List<Staff> getStaffList(){
		return staffers;
	}//ends stafflist
	public List<Meeting> getMeetingList(){
		return meeting;
	}//ends meetList
}//ends course class
