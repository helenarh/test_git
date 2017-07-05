import java.util.ArrayList;
import java.util.List;

public class Department {
	//variables
	String deptName;
	String deptPrefix;
	private List<Course > courses = new ArrayList<Course>();
		
	public Department(){
		deptName = null;
		deptPrefix = null;
	}//ends default constructor
	public Department(String name, String prefix){
		deptName = name;
		deptPrefix = prefix;
	}//ends constructor
	public void setCourseList(List<Course> courseList) {
		courses = courseList;
	}//ends setCourseList
	public List<Course> getDetails() {
		return courses;
	}//getDetails
	public int numberOfCourses() {
		return courses.size();
	}//ends numberOfCourses
	public void printCourses() {
		for(int h = 0; h < courses.size(); h++) {
			System.out.print((h+1)+".\t"+ courses.get(h).getCourseDetails());
			System.out.print("\n\t\t" + courses.get(h).getStaffDetails());
			System.out.print("\n\t\t" + courses.get(h).getMeetingDetails());
		}//ends for
	}//ends printDetails	
	public void addCourse(Course course){
		courses.add(course);
	}//ends addCourse
	public void setName(String name) {
		deptName = name;
	}//ends setName
	public void setPrefix(String prefix) {
		deptPrefix = prefix;
	}//ends setPrefix
	public String getLongName(){
		return deptName;
	}//ends getLongName
	public String getPrefix(){
		return deptPrefix;
	}//ends getLongName
	public String getFullName(){
		return deptName+ "("+deptPrefix+")";
	}//ends getFullName
}//ends Department class
