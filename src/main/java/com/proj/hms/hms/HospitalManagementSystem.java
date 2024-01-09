package com.proj.hms.hms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import org.postgresql.Driver;

import com.proj.hms.doctor.Doctor;
import com.proj.hms.patient.Patient;

public class HospitalManagementSystem {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Driver driver = new Driver();
    	try {
			DriverManager.registerDriver(driver);
			FileInputStream fileInputStream = new FileInputStream("dbconfig.properties");
			Properties properties = new Properties();
			properties.load(fileInputStream);
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hospital", properties);
			 Scanner scanner = new Scanner(System.in);
			 Patient patient = new Patient(connection, scanner);
	         Doctor doctor = new Doctor(connection);
	         while(true){
	                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
	                System.out.println("1. Add Patient");
	                System.out.println("2. View Patients");
	                System.out.println("3. View Doctors");
	                System.out.println("4. Book Appointment");
	                System.out.println("5. Exit");
	                System.out.println("Enter your choice: ");
	                int choice = scanner.nextInt();

	                switch(choice){
	                    case 1:
	                        // Add Patient
	                        patient.addPatient();
	                        System.out.println();
	                        break;
	                    case 2:
	                        // View Patient
	                        patient.viewPatients();
	                        System.out.println();
	                        break;
	                    case 3:
	                        // View Doctors
	                        doctor.viewDoctors();
	                        System.out.println();
	                        break;
	                    case 4:
	                        // Book Appointment
	                        bookAppointment(patient, doctor, connection, scanner);
	                        System.out.println();
	                        break;
	                    case 5:
	                        System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
	                        return;
	                    default:
	                        System.out.println("Enter valid choice!!!");
	                        break;
	                }

	            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}


public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
    System.out.print("Enter Patient Id: ");
    int patientId = scanner.nextInt();
    System.out.print("Enter Doctor Id: ");
    int doctorId = scanner.nextInt();
    System.out.print("Enter appointment date (YYYY-MM-DD): ");
    String appointmentDate = scanner.next();
    Date sqlDate =Date.valueOf(appointmentDate);
    if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
        if(checkDoctorAvailability(doctorId, sqlDate, connection)){
            String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                preparedStatement.setInt(1, patientId);
                preparedStatement.setInt(2, doctorId);
                preparedStatement.setDate(3, sqlDate);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0){
                    System.out.println("Appointment Booked!");
                }else{
                    System.out.println("Failed to Book Appointment!");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("Doctor not available on this date!!");
        }
    }else{
        System.out.println("Either doctor or patient doesn't exist!!!");
    }
}
public static boolean checkDoctorAvailability(int doctorId, Date sqlDate, Connection connection){
    String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
    try{
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, doctorId);
        preparedStatement.setDate(2, sqlDate);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            int count = resultSet.getInt(1);
            if(count==0){
                return true;
            }else{
                return false;
            }
        }
    } catch (SQLException e){
        e.printStackTrace();
    }
    return false;
}
}
