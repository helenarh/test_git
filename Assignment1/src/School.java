import java.util.ArrayList;
import java.util.List;

public class School {
	private String name;
	private List<Department> deptList;
	
	public School() {
		name = "";
		deptList = new ArrayList<Department>();
	}//ends default constructor
	
	public School(String name, List<Department> departments) {
		this.name = name;
		deptList = departments;
	}//ends constructor
	
	public String getSchoolName() {
		return name;
	}//ends getSchoolName
	
	public List<Department> getDept(){
		return deptList;
	}//ends getDept
}//ends School class
