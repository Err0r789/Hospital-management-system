package com.proj.hms.doctor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.Driver;

public class Doctor {
	 private Connection connection;

	    public Doctor(Connection connection){
	        this.connection = connection;
	    }

	    public void viewDoctors(){
	        String query = "select * from doctors";
	        try{
	        	Driver driver = new Driver();
	        	DriverManager.registerDriver(driver);
	        	FileInputStream fileInputStream = new FileInputStream("dbconfig.properties");
				Properties properties = new Properties();
				properties.load(fileInputStream);
				connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital", properties);
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            System.out.println("Doctors: ");
	            System.out.println("+------------+--------------------+------------------+");
	            System.out.println("| Doctor Id  | Name               | Specialization   |");
	            System.out.println("+------------+--------------------+------------------+");
	            while(resultSet.next()){
	                int id = resultSet.getInt("id");
	                String name = resultSet.getString("name");
	                String specialization = resultSet.getString("specialization");
	                System.out.printf("| %-10s | %-18s | %-16s |\n", id, name, specialization);
	                System.out.println("+------------+--------------------+------------------+");
	            }

	        }catch (SQLException e){
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    public boolean getDoctorById(int id){
	        String query = "SELECT * FROM doctors WHERE id = ?";
	        try{
	        	Driver driver = new Driver();
	        	DriverManager.registerDriver(driver);
	        	FileInputStream fileInputStream = new FileInputStream("dbconfig.properties");
				Properties properties = new Properties();
				properties.load(fileInputStream);
				connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital", properties);
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setInt(1, id);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if(resultSet.next()){
	                return true;
	            }else{
	                return false;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return false;
	    }
}
