package edu.unipr.valdrin.javafinalassignment2021.controllers;

import edu.unipr.valdrin.javafinalassignment2021.models.CovidCase;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController     //Spring RestController annotation is a convenience annotation that is itself annotated with @Controller and @ResponseBody . This annotation is applied to a class to mark it as a request handler. Spring RestController annotation is used to create RESTful web services using Spring MVC
public class DatabaseController {

        private static String url = "jdbc:sqlite:src/main/resources/Database/Covid.db";

        @RequestMapping("/allpatients")
        public String selectPatients(){
            String sql = "SELECT Patient_name FROM CovidCase";
            StringBuilder builder = new StringBuilder();
            try {
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);      //resultSet to store the result
                while (resultSet.next()){
                    builder.append(resultSet.getString("Patient_Name")).append("<br/>");
                }
                statement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return builder.toString();
        }

        @RequestMapping("/getCases")
        public List<CovidCase> geAllCases (){
            String sql = "SELECT * FROM CovidCase";
            List<CovidCase> covidCases = new ArrayList<>();
            try {
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);      //resultSet to store the result
                while (resultSet.next()){
                    CovidCase covidCase = new CovidCase(); //object covidCase of class CovidCase
                    covidCase.setPatientId(resultSet.getInt("Patient_ID"));
                    covidCase.setPatientName(resultSet.getString("Patient_Name"));
                    covidCase.setDateOfBirth(resultSet.getString("Date_of_Birth"));
                    covidCase.setGender(resultSet.getString("Gender"));
                    covidCase.setDescription(resultSet.getString("Description"));
                    covidCase.setCovid19Timestamp(resultSet.getString("COVID_Timestamp"));
                    covidCase.setHospital(resultSet.getString("Hospital"));
                    covidCase.setCity(resultSet.getString("City"));
                    covidCases.add(covidCase);  //we fill the created list
                }
                statement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return covidCases;
        }

        @GetMapping("Case/{id}")
        public CovidCase getCase(@PathVariable Integer id){
            String sql = "SELECT * FROM CovidCase WHERE Patient_ID=?"; // ? for security reasons we use prepared statements
            CovidCase covidCase = null;
            try {
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,id); //param 1 is replaced by id in the method
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    covidCase = new CovidCase();
                    covidCase.setPatientId(resultSet.getInt("Patient_ID"));
                    covidCase.setPatientName(resultSet.getString("Patient_Name"));
                    covidCase.setDateOfBirth(resultSet.getString("Date_of_Birth"));
                    covidCase.setGender(resultSet.getString("Gender"));
                    covidCase.setDescription(resultSet.getString("Description"));
                    covidCase.setCovid19Timestamp(resultSet.getString("COVID_Timestamp"));
                    covidCase.setHospital(resultSet.getString("Hospital"));
                    covidCase.setCity(resultSet.getString("City"));
                }
                preparedStatement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return covidCase;
        }

    @DeleteMapping("/Case/{id}")
    public Map<String,String> deleteCase(@PathVariable int id){
        String sql = "DELETE FROM CovidCase WHERE Patient_ID=?";
        String message = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            int deletedCases = preparedStatement.executeUpdate();
            if (deletedCases>0){
                message = "Deleted "+String.valueOf(deletedCases)+" case";
            }else {
                message = "The id you provided does not exist...";
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Map<String,String> jsonMessage = new HashMap<>();
        jsonMessage.put("message",message);
        return jsonMessage;
    }

    @PostMapping("/newCase")
    public Map<String,String> insert(@RequestBody Map<String,String> body){
        int patientId = Integer.parseInt(body.get("patientId"));
        String patientName = body.get("patientName");
        String dateOfBirth = body.get("dateOfBirth");
        String gender = body.get("gender");
        String description = body.get("description");
        String covid19Timestamp = body.get("covid19Timestamp");
        String hospital = body.get("hospital");
        String city = body.get("city");

        String sql = "INSERT INTO CovidCase VALUES(?,?,?,?,?,?,?,?)";
        String message = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,patientId);
            preparedStatement.setString(2,patientName);
            preparedStatement.setString(3,dateOfBirth);
            preparedStatement.setString(4,gender);
            preparedStatement.setString(5,description);
            preparedStatement.setString(6,covid19Timestamp);
            preparedStatement.setString(7,hospital);
            preparedStatement.setString(8,city);
            preparedStatement.execute();
            message = "Case inserted successfully";
            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            message = throwables.getLocalizedMessage();
        }
        Map<String,String> jsonMessage = new HashMap<>();
        jsonMessage.put("message",message);
        return jsonMessage;
    }

    @PutMapping("/updateCase/{id}")
    public Map<String, String> updateCase(@PathVariable int id, @RequestBody Map<String,String> body){
        String patientName = body.get("patientName");
        String dateOfBirth = body.get("dateOfBirth");
        String gender = body.get("gender");
        String description = body.get("description");
        String covid19Timestamp = body.get("covid19Timestamp");
        String hospital = body.get("hospital");
        String city = body.get("city");
        String sql = "UPDATE CovidCase " +
                "SET Patient_Name=?," +
                "Date_of_Birth=?," +
                "Gender=?," +
                "Description=?," +
                "COVID_Timestamp=?," +
                "Hospital=?," +
                "City=? " +
                "WHERE Patient_ID=?";
        String message = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,patientName);
            preparedStatement.setString(2,dateOfBirth);
            preparedStatement.setString(3,gender);
            preparedStatement.setString(4,description);
            preparedStatement.setString(5,covid19Timestamp);
            preparedStatement.setString(6,hospital);
            preparedStatement.setString(7,city);
            preparedStatement.setInt(8,id);
            preparedStatement.execute();
            message = "Case updated successfully";
            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            message = throwables.getLocalizedMessage();
        }
        Map<String,String> jsonMessage = new HashMap<>();
        jsonMessage.put("message",message);
        return jsonMessage;
    }
}
