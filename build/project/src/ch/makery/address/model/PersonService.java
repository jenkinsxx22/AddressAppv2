package ch.makery.address.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import ch.makery.address.util.DateUtil;
public class PersonService {

	private String dbURL="jdbc:sqlite:persons.db";

	private String createTable="CREATE TABLE PERSONS("
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
			+ "FIRSTNAME TEXT,\n"
			+ "LASTNAME TEXT,\n"
			+ "STREET TEXT,\n"
			+ "POSTAL NUMBER,\n"
			+ "CITY TEXT,\n"
			+ "BIRTHDAY DATE"
			+ ");";
	private String insertSQL="INSERT INTO PERSONS(FIRSTNAME,LASTNAME,STREET,POSTAL,CITY,BIRTHDAY)"
			+ " VALUES(?,?,?,?,?,?);";
	private String updateSQL="UPDATE PERSONS SET FIRSTNAME=?, LASTNAME=?, STREET=?, POSTAL=?, CITY=?,BIRTHDAY=?"
			+ " WHERE ID=?";
	private String deleteSQL="DELETE FROM PERSONS WHERE ID=?";
	private String selectSQL="SELECT FIRSTNAME,LASTNAME,STREET,POSTAL,CITY,BIRTHDAY FROM PERSONS WHERE ID=?";
	private String selectAllSQL="SELECT ID,FIRSTNAME,LASTNAME,STREET,POSTAL,CITY,BIRTHDAY FROM PERSONS";
	 
	public void SavePersons(Person person) {
		try(Connection conn = DriverManager.getConnection(dbURL)){			
			PreparedStatement stmt = conn.prepareStatement(insertSQL);
			stmt.setString(1, person.getFirstName());
			stmt.setString(2, person.getLastName());
			stmt.setString(3, person.getStreet());
			stmt.setInt(4, person.getPostalCode());
			stmt.setString(5, person.getCity());
			stmt.setString(6, DateUtil.format(person.getBirthday()));
			stmt.execute();
			System.out.println("User saved!");
		}catch(SQLException sqle) {	CreateTable(); sqle.printStackTrace();}
		
	}
	
	public void UpdatePerson(Person person) {

		try(Connection conn = DriverManager.getConnection(dbURL)){			
			PreparedStatement stmt = conn.prepareStatement(updateSQL);
			stmt.setString(1, person.getFirstName());
			stmt.setString(2, person.getLastName());
			stmt.setString(3, person.getStreet());
			stmt.setInt(4, person.getPostalCode());
			stmt.setString(5, person.getCity());
			stmt.setString(6, DateUtil.format(person.getBirthday()));
			stmt.setInt(7, person.getId());	
			stmt.execute();
			System.out.println("User updated!");
		}catch(SQLException sqle) { sqle.printStackTrace();}
		
	}
	
	public void DeletePerson(int id) {
		try(Connection conn = DriverManager.getConnection(dbURL)){			
			PreparedStatement stmt = conn.prepareStatement(deleteSQL);
			stmt.setInt(7, id);	
			stmt.execute();
			System.out.println("User deleted!");
		}catch(SQLException sqle) { sqle.printStackTrace();}
		
	}
	public Person GetPersonById(int id) {
		Person person = new Person();
		try(Connection conn = DriverManager.getConnection(dbURL)){			
			PreparedStatement stmt = conn.prepareStatement(selectSQL);
			stmt.setInt(1, id);	
			ResultSet res = stmt.executeQuery();
			while(res.next()) {
				person.setId(id);
				person.setFirstName(res.getString("FIRSTNAME"));
				person.setLastName(res.getString("LASTNAME"));
				person.setPostalCode(res.getInt("POSTAL"));
				person.setStreet(res.getString("STREET"));
				person.setCity(res.getString("CITY"));
				person.setBirthday(DateUtil.parse(res.getString("BIRTHDAY")));				
			}

			System.out.println("User gotted! with id "+id);
		}catch(SQLException sqle) { sqle.printStackTrace();}
		
		
		return person;
	}
	
	public List<Person>  GetAllPersons() {
		List<Person> listPersons=new ArrayList<Person>();

		try(Connection conn = DriverManager.getConnection(dbURL)){			
			PreparedStatement stmt = conn.prepareStatement(selectAllSQL);
			ResultSet res = stmt.executeQuery();
			while(res.next()) {
				Person person = new Person();
				person.setId(res.getInt("ID"));
				person.setFirstName(res.getString("FIRSTNAME"));
				person.setLastName(res.getString("LASTNAME"));
				person.setPostalCode(res.getInt("POSTAL"));
				person.setStreet(res.getString("STREET"));
				person.setCity(res.getString("CITY"));
				person.setBirthday(DateUtil.parse(res.getString("BIRTHDAY")));	
				listPersons.add(person);
			}

			System.out.println("Query rows "+listPersons.size());
		}catch(SQLException sqle) { sqle.printStackTrace();}
		
		
		return listPersons;
	}
	
	public void CreateTable() {
		try(Connection conn = DriverManager.getConnection(dbURL)){			
			Statement stmt = conn.createStatement();
			stmt.execute(createTable);			
		}catch(SQLException sqle) {	sqle.printStackTrace();}
		
	}
}
