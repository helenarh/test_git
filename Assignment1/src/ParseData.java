import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class ParseData {
	
	public static void main(String[] args) {
		List<School > schools = new ArrayList<School>();
		String schoolName="";
		
		//get input filename
		System.out.print("What is the name of the input file? ");
		Scanner scan = new Scanner(System.in);
		String inputXMLFilename = scan.next();
		boolean flag1 = readFile(inputXMLFilename);
		boolean flag2 = false;
		
		while(!flag2) {
			while(!flag1) { //find file
				inputXMLFilename = scan.next();
				flag1 = readFile(inputXMLFilename);
			}//ends while
			
			try {//try to parse xml file
				File inputFile = new File(inputXMLFilename);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				dbFactory.setValidating(true);
				dbFactory.setNamespaceAware(true);
				dbFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
				      "http://www.w3.org/2001/XMLSchema");
				dbFactory.setIgnoringElementContentWhitespace(true);
				dBuilder.setErrorHandler(new SimpleErrorHandler());
				
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();
				
				flag1 = true;
				flag2 = true;
				
				//NodeList nList = doc.getElementsByTagName("*"); //gets all XML elements from XML file						
				Node root = doc.getDocumentElement(); //gets the root element
				
				if(root.hasAttributes()) {
					NamedNodeMap attr = root.getAttributes();
					for(int i = 0; i < attr.getLength(); i++) {
						Node attrN = attr.item(i);
						schoolName = attrN.getNodeValue();
					}//ends attribute for
				}//ends attribute if
				if(root.hasChildNodes()) {//checks if the root has children(depts)
					NodeList rootChildren = root.getChildNodes();
					
					List<Department > departments = new ArrayList<Department>();
					for(int p = 0; p < rootChildren.getLength(); p++) {
						Node rcNode = rootChildren.item(p);
												
						//skips over #text nodes to the nodeNametext: <nodeNametext></nodeNametext> part
						if((rcNode.getNodeType() == Node.TEXT_NODE) && (rcNode.getNextSibling() != null)) {
							
							rcNode = rcNode.getNextSibling();//traversed to a sibling node
							p++;//reflect the traversal

							//searches for Department tags (children of root but sibling to one another)
							if(rcNode.getNodeName().toUpperCase().equals("DEPARTMENT")) {
								//create department object
								 String deptName = null;
								 String deptPrefix = null;
								 Department currentDept = processDepartment(rcNode, deptName, deptPrefix); 
								 departments.add(currentDept);
								 
							}//ends if
							
							School school = new School(schoolName, departments);
							schools.add(school);
						}//ends if
					}//ends for				
				}//ends if
			} catch(Exception e) {
				e.printStackTrace();
				flag1 = false;
				flag2 = false;
			}//ends catch
		}//ends while
		
		boolean flag3 = false; //List<School > schools pass these two variables
		
		
		//menu options
		while(!flag3) {
			
			printDefaultMenuOption();
			String userInput = scan.next();
			
			while(!validateUserInput(userInput, 2)) {
				printDefaultMenuOption();
				userInput = scan.next();
			}//ends while
			int num = Integer.parseInt(userInput);
			
			boolean schoolMenu = false;
			boolean deptMenu = false;
			boolean courseMenu = false;
			boolean courseStaffMenu = false;
			boolean courseMeetingMenu = false;
			
			switch(num) {
			case 1:
				schoolMenu = true;
				break;
			case 2:
				flag3 = true;
				break;
			}//ends switch
			
			while(schoolMenu) {
				ArrayList<String> schoolNames = new ArrayList<String>();
				for(int s = 0; s < schools.size(); s++) {
					schoolNames.add(schools.get(s).getSchoolName());
				}//ends for
				//add prev menu
				schoolNames.add("Go to Main Menu");
				printMenuOptions(schoolNames, "Schools");
				userInput = scan.next();
				
				while(!validateUserInput(userInput, schoolNames.size()+1)) {
					printMenuOptions(schoolNames, "Schools");
					userInput = scan.next();
				}//ends while
				num = Integer.parseInt(userInput);
				
				if(num == (schoolNames.size()+1)) { //exit selection
					schoolMenu = false;
					flag3 = true;
				}//ends if
				else if(num == (schoolNames.size())){ //main menu selection
					schoolMenu = false;
				}//ends else if
				else { //one of the schools are selected
					String selectedSchool = schoolNames.get(num-1);
					List<Department> selectedSchoolDept = null;
					deptMenu = true;
					
					for(int s = 0; s < schools.size(); s++) {
						if(schools.get(s).getSchoolName().equals(selectedSchool)) {
							selectedSchoolDept = schools.get(s).getDept();
						}//ends if
					}//ends for
					
					while(deptMenu) {
						ArrayList<String> deptNames = new ArrayList<String>();
						for(int s = 0; s < selectedSchoolDept.size(); s++) {
							deptNames.add(selectedSchoolDept.get(s).getFullName());
						}//ends for
						deptNames.add("Go to School Menu");
						
						printMenuOptions(deptNames, "Departments");
						userInput = scan.next();
						
						while(!validateUserInput(userInput, deptNames.size()+1)) {
							printMenuOptions(deptNames, "Departments");
							userInput = scan.next();
						}//ends while
						num = Integer.parseInt(userInput);
						
						if(num == (deptNames.size()+1)) { //exit selection
							schoolMenu = false;
							deptMenu = false;
							flag3 = true;
						}//ends if
						else if(num == (deptNames.size())) { //school menu
							schoolMenu = true;
							deptMenu = false;
						}//ends else if
						else { //one of the Dept are selected
							String selectedDept = deptNames.get(num-1);
							List<Course> selectedCourse = null;
							courseMenu = true;
							
							for(int t = 0; t < selectedSchoolDept.size(); t++) {
								if(selectedSchoolDept.get(t).getFullName().equals(selectedDept)) {
									selectedCourse = selectedSchoolDept.get(t).getDetails();
								}//ends if
							}//ends if
							
							while(courseMenu) {
								ArrayList<String> courseNames = new ArrayList<String>();
								for(int g = 0; g < selectedCourse.size(); g++) {
									courseNames.add(selectedCourse.get(g).getCourseDetails());
								}//ends for
								courseNames.add("Go to Departments Menu");
								
								printMenuOptions(courseNames, "Courses");
								userInput = scan.next();
								
								while(!validateUserInput(userInput, courseNames.size()+1)) {
									printMenuOptions(courseNames, "Courses");
									userInput = scan.next();
								}//ends while
								num = Integer.parseInt(userInput);

								if(num ==(courseNames.size()+1)) {//exit selection
									courseMenu = false;
									deptMenu = false;
									schoolMenu = false;
									flag3 = true;
								}//ends if
								else if(num == (courseNames.size())) { //dept menu
									deptMenu = true;
									courseMenu = false;
								}//ends else if
								else { //one of the courses selected
									String selectedClass = courseNames.get(num-1);
									List<Staff> courseStaff = null;
									List<Meeting> courseMeeting = null;
									ArrayList<String> courseOpt = new ArrayList<String>();
									
									for(int k = 0; k < selectedCourse.size(); k++) {
										if(selectedCourse.get(k).getCourseDetails().equals(selectedClass)) {
											courseStaff = selectedCourse.get(k).getStaffList();
											courseMeeting = selectedCourse.get(k).getMeetingList();
										}//ends if
									}//ends for
									
									courseOpt.add("View course staff");
									courseOpt.add("View meeting information");
									courseOpt.add("Go to Courses Menu");
									
									printMenuOptions(courseOpt,selectedClass);
									userInput = scan.next();
									
									while(!validateUserInput(userInput, courseOpt.size()+1)) {
										printMenuOptions(courseOpt, selectedClass);
										userInput = scan.next();
									}//ends while
									num = Integer.parseInt(userInput);
									
									switch(num) {
									case 1:
										courseStaffMenu = true;
										break;
									case 2:
										courseMeetingMenu = true;
										break;
									case 3:
										courseMenu = true;
										break;
									case 4:
										schoolMenu = false;
										courseMenu = false;
										deptMenu = false;
										flag3 = true;
										break;
									}//ends switch
									
									while(courseStaffMenu) {
										
										ArrayList<String> courseStaffTypes = new ArrayList<String>();
										for(int g = 0; g < courseStaff.size(); g++) {
											courseStaffTypes.add(courseStaff.get(g).getType());
										}//ends for
										courseStaffTypes.add("Go to " + selectedClass);
										
										printMenuOptions(courseStaffTypes, selectedClass);
										userInput = scan.next();
										
										while(!validateUserInput(userInput, courseStaffTypes.size()+1)) {
											printMenuOptions(courseStaffTypes,selectedClass);
											userInput = scan.next();
										}//ends while
										num = Integer.parseInt(userInput);
										
										if(num ==(courseStaffTypes.size()+1)) {//exit selection
											courseStaffMenu = false;
											courseMeetingMenu = false;
											courseMenu = false;
											deptMenu = false;
											schoolMenu = false;
											flag3 = true;
										}//ends if
										else if(num == (courseStaffTypes.size())) { //selectedClass menu
											courseStaffMenu = false;
											courseMeetingMenu = false;
											courseMenu = true;
										}//ends else if
										else {
											String selectedStaff = courseStaffTypes.get(num-1);
											for(int f = 0; f < courseStaff.size(); f++) {
												if(courseStaff.get(f).getType().equals(selectedStaff)) {
													System.out.println(courseStaff.get(f).getStaffDetails());
												}//ends if
											}//ends for
											courseStaffMenu = true;
										}//ends else
									}//ends courseStaffMenu
									
									while(courseMeetingMenu) {
										ArrayList<String> courseMeetTypes = new ArrayList<String>();
										for(int g = 0; g < courseMeeting.size(); g++) {
											courseMeetTypes.add(courseMeeting.get(g).getType());
										}//ends for
										courseMeetTypes.add("Go to " + selectedClass);
										
										printMenuOptions(courseMeetTypes,selectedClass);
										userInput = scan.next();
										
										while(!validateUserInput(userInput, courseMeetTypes.size()+1)) {
											printMenuOptions(courseMeetTypes,selectedClass);
											userInput = scan.next();
										}//ends while
										num = Integer.parseInt(userInput);
										
										if(num ==(courseMeetTypes.size()+1)) {//exit selection
											courseStaffMenu = false;
											courseMeetingMenu = false;
											courseMenu = false;
											deptMenu = false;
											schoolMenu = false;
											flag3 = true;
										}//ends if
										else if(num == (courseMeetTypes.size())) { //selectedClass menu
											courseStaffMenu = false;
											courseMeetingMenu = false;
											courseMenu = true;
										}//ends else if
										else {
											String selectedMeetType = courseMeetTypes.get(num-1);
											for(int f = 0; f < courseMeeting.size(); f++) {
												if(courseMeeting.get(f).getType().equals(selectedMeetType)) {
													System.out.println(courseMeeting.get(f).getMeetingDetails());
												}//ends if
											}//ends for
											courseMeetingMenu = true;
										}//ends else
										
									}//ends courseMeetingMenu while
								}//ends else
							}//ends coursemenu while
							
						}//ends else
					}//ends while
				}//ends else
			}//ends menu
			
		}//ends menu while
		
		System.out.println("Thank you for using the program");
		scan.close();
	}//ends main
	public static void printDefaultMenuOption() {
		System.out.println("1) Display schools");
		System.out.println("2) Exit");
		System.out.print("What you would like to do?  ");
	}//ends printDefault
	public static void printMenuOptions(ArrayList<String> option, String header) {
		System.out.println(header);
		for(int h = 0; h < option.size(); h++) {
			System.out.println("\t"+ (h+1) + ") " + option.get(h));
		}//ends for
		
		System.out.println("\t" + (option.size() + 1) + ") Exit");
		System.out.print("What you would like to do?  ");
	}//ends printMenuOptions
	
	public static boolean validateUserInput(String userIn, int numOfOptions) {
				
		if(!(isInt(userIn))){
			System.out.println("Not a valid option");
			return false;
		}//ends of
			
		if((Integer.parseInt(userIn) < 1) || (Integer.parseInt(userIn) > numOfOptions)) {
			System.out.println("Not a valid option");
			return false;
		}//ends if
		
		return true;
	}//ends validate UserInput
	public static boolean isInt(String number) {
		try {
			Integer.parseInt(number);
		}catch(Exception e) {
			//e.printStackTrace();
			return false;
		}//ends catch
		return true;
	}//ends bool
	
	public static Department processDepartment(Node eElement, String departmentName, String departmentPrefix) {
		 Department result = new Department();
		 
		 if(eElement.hasChildNodes()) {
			  NodeList innerNodes = eElement.getChildNodes();
			  for(int p = 0; p < innerNodes.getLength(); p++) {
   				Node nestedNode = innerNodes.item(p);
   				
   				//skips over #text nodes to the nodeNametext: <nodeNametext></nodeNametext> part
				if((nestedNode.getNodeType() == Node.TEXT_NODE) && (nestedNode.getNextSibling() != null)) {
					
					nestedNode = nestedNode.getNextSibling();//traversed to a sibling node
					p++; //increment reflect the traversal
					
					if(nestedNode.getNodeName().toUpperCase().equals("LONGNAME")) { //grabs the dept's long name
						result.setName(nestedNode.getFirstChild().getNodeValue());
					}//ends if
					else if(nestedNode.getNodeName().toUpperCase().equals("PREFIX")) { //grabs the dept's prefix
						result.setPrefix(nestedNode.getFirstChild().getNodeValue());		
					}//ends else if
					else if(nestedNode.getNodeName().toUpperCase().equals("COURSES")) {
						result.setCourseList(processCourse(nestedNode));
					}//ends else if
				}//ends if
   			}//ends for			  
		  }//ends if
		 return result;
	}//ends processDepartment
	
	public static List<Meeting> processMeet(Node mnode) {
		String type = null;
		List<Meeting > meetObj = new ArrayList<Meeting>();
		
		if(mnode.hasChildNodes()) {
			NodeList mnodeChildren = mnode.getChildNodes();

			for(int m = 0; m < mnodeChildren.getLength(); m++) {
				Node mnodeChild = mnodeChildren.item(m);
				if((mnodeChild.getNodeType() == Node.TEXT_NODE) &&(mnodeChild.getNextSibling()!= null)) {
					mnodeChild = mnodeChild.getNextSibling();
					m++;
					
					Meeting meet = new Meeting();
					if(mnodeChild.hasAttributes()) {//check attributes
						NamedNodeMap attr = mnodeChild.getAttributes();
						for(int i = 0; i < attr.getLength(); i++) {
							Node attrN = attr.item(i);
							if(attrN.getNodeName().toLowerCase().equals("type")) {
								type = attrN.getNodeValue().toUpperCase();
								meet.setType(attrN.getNodeValue().toUpperCase());								
							}//ends if
						}//ends attribute for
					}//ends attribute if
					
					String line = "";
					line = recurseNode(mnodeChild, line); //recursively go thru all the childnodes
					
					//add the staff details to staff object & add to list
					meet.addDetails(line);
					meetObj.add(meet);		
				}//ends #text if
			}//ends for
		}//ends if		
		return meetObj; //add staff to Course Object
	}//ends process Meet
	
	public static List<Staff> processStaff(Node staff) {
		List<Staff > staffObj = new ArrayList<Staff>();
		String staffType = null;
		String staffID = null;	
				
		//go thru sibling nodes
		if(staff.hasChildNodes()) {
			NodeList innerNodes = staff.getChildNodes(); //grabs all child nodes and stores to a nodelist
			
			//goes through each child node
			for(int p = 0; p < innerNodes.getLength(); p++) {
				Node nestedNode = innerNodes.item(p);
				
				//skips over #text nodes to the nodeNametext: <nodeNametext></nodeNametext> part
				if((nestedNode.getNodeType() == Node.TEXT_NODE)
						&& (nestedNode.getNextSibling() != null)) {
					nestedNode = nestedNode.getNextSibling(); //go to sibling
					p++;//reflect the travel to sibling
					
					Staff prof = new Staff(staffType, staffID);	
					//check for attributes for type and id the create staff object, <nodeNametext attr1 = value1 attr2 = value2></nodeNametext>
					if(nestedNode.hasAttributes()){ //Check for attributes
						NamedNodeMap attributes = nestedNode.getAttributes();
						for(int h = 0; h <attributes.getLength(); h++) {
							Node attr = attributes.item(h);
							
							if(attr.getNodeName().toLowerCase().equals("type")) {
								prof.setType(attr.getNodeValue().toUpperCase());				
							}//ends if
							else if(attr.getNodeName().toLowerCase().equals("id")){
								prof.setId(attr.getNodeValue());
							}//ends else
						}//ends for					    					
					}//ends if
						
					String line = "";
					line = recurseNode(nestedNode, line); //recursively go thru all the childnodes
					
					//add the staff details to staff object
					prof.addDetails(line);
					staffObj.add(prof);
				}//ends if
			}//end for
		}//ends if
		//course.addStaff(prof); //add staff to Course Object
		return staffObj;
	}//ends process staff
	
	/*
	 *  Recurses through the node's children and siblings of children and stores the nodeName and node's first child node values
	 * to one long string. Not ideal when storing data to a particular tag
	*/
	public static String recurseNode(Node eNode, String result) {
		NodeList innerNodes = eNode.getChildNodes(); //grabs all the child nodes and stores to a nodelist
		for(int p = 0; p < innerNodes.getLength(); p++) {
			Node nestedNode = innerNodes.item(p);
			//skips over #text nodes to the nodeNametext of: <"nodeNametext">sometext</nodeName> part
			if((nestedNode.getNodeType() == Node.TEXT_NODE)
				&& (nestedNode.getNextSibling() != null)) {
				nestedNode = nestedNode.getNextSibling();
				p++;
				
				result+="\n"+nestedNode.getNodeName(); //stores the nodeNametext				
				//check for attributes
				if(nestedNode.hasAttributes()) {
					NamedNodeMap attributes = nestedNode.getAttributes();
					for(int h = 0; h <attributes.getLength(); h++) {
						Node attr = attributes.item(h);
						result+=": "+attr.getNodeValue();
					}//ends for	
				}//ends attribute if
				//stores only if sometext exist but not with: <"nodeNametext"></"nodeNamtext>
				if(nestedNode.getFirstChild() != null) {
					if(!(nestedNode.getFirstChild().getNodeValue().trim().equals(""))) { 
						result+= ": "+nestedNode.getFirstChild().getNodeValue();
					}//ends if					
				}//ends if
				
				//if the node has additional children, it recursively calls itself
				if(nestedNode.hasChildNodes()) {
					result = recurseNode(nestedNode, result);
				}//ends if
				
			}//ends if
		}//end for				
		return result;
	}//ends recurseNode
	public static List<Course> processCourse(Node courseEl) {
		String num = null;
		String term = null;
		String yr =  null;
		List<Course> courseObj = new ArrayList<Course>();
		
		if(courseEl.hasChildNodes()) {
			NodeList innerNodes = courseEl.getChildNodes();
			for(int p = 0; p < innerNodes.getLength(); p++) {				
 				Node nestedNode = innerNodes.item(p);			
 				//skips over #text nodes to the nodeNametext: <nodeNametext></nodeNametext> part
				if((nestedNode.getNodeType() == Node.TEXT_NODE) && (nestedNode.getNextSibling() != null)) {	
					nestedNode = nestedNode.getNextSibling();//traverse to the sibling node
					p++;//reflect the traversal
					
					Course course = new Course();//create course object
					if(nestedNode.getNodeName().toUpperCase().equals("COURSE")) {
	   					if(nestedNode.hasChildNodes()) {
	   						NodeList inNodes = nestedNode.getChildNodes();	
	   						for(int q= 0; q < inNodes.getLength(); ++q) {							
	   							Node inNode = inNodes.item(q);
	   							
	   							if((inNode.getNodeType() == Node.TEXT_NODE) && (inNode.getNextSibling() != null)){
	   								inNode = inNode.getNextSibling(); //traverse to sibling
	   								q++;//reflect the traversal
	   								
	   								if(inNode.getNodeName().toUpperCase().equals("NUMBER")) {
		   								num = inNode.getFirstChild().getNodeValue();
		   								course.setNum(num);
		   							}//ends if
		   							else if(inNode.getNodeName().toUpperCase().equals("TERM")) {
		   								term = inNode.getFirstChild().getNodeValue();
		   								course.setTerm(term);
		   							}//ends else if
		   							else if(inNode.getNodeName().toUpperCase().equals("YEAR")) {
		   								yr = inNode.getFirstChild().getNodeValue();
		   								course.setYear(yr);
		   							}//ends else if							
		   							else if(inNode.getNodeName().toUpperCase().equals("STAFFMEMBERS")) {
		   								course.setStaffList(processStaff(inNode));
		   							}//ends else if
		   							else if(inNode.getNodeName().toUpperCase().equals("MEETINGS")) {
		   								course.setMeetingList(processMeet(inNode));
		   							}//ends else if
	   							}//#text if	
	   						}//innerfor
	   						
	   						//create Course object & add to dept
	   						courseObj.add(course);
	   					}//inner if
					}//ends course if					
   				}//ends if
			}//ends for
		}//ends if	
		
		return courseObj;
	}//ends processCourse
	private static boolean readFile(String xmlFileName) {
		try {
			FileReader fr = new FileReader(xmlFileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			
			while (line != null) {
				line = br.readLine();
			}//ends while
			br.close();
			fr.close();
			return true;
		} catch (FileNotFoundException fnfe) {
			System.out.println("FileNotFoundException: " + fnfe.getMessage());
			return false;
		} catch (IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
			return false;
		}//ends catch
	}//ends readFile
	
	//error handler
	private static class SimpleErrorHandler implements ErrorHandler {
		public void warning(SAXParseException spe) throws SAXException {
			System.out.println("Warning: " + spe.getMessage());
		}//ends warning
		public void error(SAXParseException spe) throws SAXException{
			System.out.println("Error: " + spe.getMessage());
		}//ends error
		public void fatalError(SAXParseException spe) throws SAXException{
			System.out.println("fatalError: " + spe.getMessage());
		}//ends fatalError
	}//ends private static class
}//ends class





