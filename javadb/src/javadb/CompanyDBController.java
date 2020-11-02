package javadb;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

public class CompanyDBController { // LJH
    private Connection conn = null;
    private String sqlID = null;
    private String sqlPw = null;

    public CompanyDBController(final String sqlID, final String sqlPw, final String dbName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.sqlID = sqlID;
            this.sqlPw = sqlPw;
            this.connectDB(dbName);
        } catch(Exception e) {}
    }

    public boolean connectDB(final String dbName){
        try {
            final String url = "jdbc:mysql://localhost:3306/"+dbName+"?serverTimezone=UTC";
            conn = DriverManager.getConnection(url, sqlID, sqlPw);
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    public boolean deconnectDB() {
        try {
            if(conn != null) {
                conn.close();
                return true;
            }
            return false;
        } catch(SQLException e){ return false;}
    }
    // 부서 선택, 애트리뷰트 선택, 조건 선택
    public ResultSet selectEmp(int[] checked) {
        String[] attrName = {"EFname", "EMinit", "ELname", "ESsn",
                             "EBdate", "EAddress", "ESex", "ESalary",
                             "Fname, Minit, Lname", "Dname"};
        String stmt = "SELECT ";
        for(int i = 0; i < checked.length; i++) {
            if(checked[i] == 1) {
                stmt += attrName[i] + ", ";
            }
        }
        
        
        stmt = stmt.substring(0,stmt.length()-2);
        stmt += " FROM (SELECT E.Fname AS EFname, E.Minit as EMinit, E.Lname as ELname, E.Ssn AS ESsn, " + 
                "E.Bdate As EBdate, E.Address As EAddress, E.Sex AS Esex, E.Salary AS ESalary, " +
                "S.Fname , S.Minit, S.Lname, E.dno FROM EMPLOYEE AS E " +
                "LEFT JOIN EMPLOYEE AS S ON E.Super_ssn = S.Ssn) AS A, DEPARTMENT";
        stmt += " WHERE A.Dno = Dnumber;";

        System.out.println(stmt);
        try {
            PreparedStatement p = conn.prepareStatement(stmt);

            return p.executeQuery();
        } catch(Exception e) {
            return null;
        }
    }
    
    public boolean insertEmp(String name, String ssn, String birDate,
        String address, char sex, double salary, String mgr_ssn, int dnum)
        throws SQLException {

        String stmt = "INSERT INTO EMPLOYEE ";
        stmt += "VALUES (?,?,?,?,?,?,?,?,?,?)";

        String[] names = name.split(" ");

        PreparedStatement p = conn.prepareStatement(stmt);
        p.clearParameters();
        p.setString(1, names[0]);
        p.setString(2, names[1]);
        p.setString(3, names[2]);
        p.setString(4, ssn);
        p.setDate(5, Date.valueOf(birDate));
        p.setString(6, address);
        p.setString(7, String.valueOf(sex));
        p.setDouble(8, salary);
        p.setString(9, mgr_ssn);
        p.setInt(10, dnum);
        
        try {
            p.executeUpdate();
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    public String[] getAttrs(String tableName) throws SQLException {
        String stmt = "SELECT * FROM "+ tableName +";";
        PreparedStatement p = conn.prepareStatement(stmt);

        ResultSet r = p.executeQuery();
        ResultSetMetaData rsmd = r.getMetaData();

        ArrayList<String> array = new ArrayList<String>();
        for(int i = 1; i <= rsmd.getColumnCount(); i++) {
            array.add(rsmd.getColumnLabel(i));
        }

        String[] result = array.toArray(new String[0]);

        return result;
    }

    public ResultSet getTables() throws SQLException {
        String stmt = "Show tables;";
        PreparedStatement p = conn.prepareStatement(stmt);
        return p.executeQuery();
    }

    // 조건 선택
    public boolean updateEmp(String ssn, double newSalary) throws SQLException {
        String stmt = "UPDATE EMPLOYEE ";
        stmt += "SET Salary = ? ";
        stmt += "WHERE Ssn = ?;";

        PreparedStatement p = conn.prepareStatement(stmt);
        p.clearParameters();
        p.setDouble(1, newSalary);
        p.setString(2, ssn);
        
        try {
            p.executeUpdate();
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    public boolean deleteEmp(String ssn) throws SQLException {
        String stmt = "DELETE FROM EMPLOYEE ";
        stmt += "WHERE Ssn = ?;";

        PreparedStatement p = conn.prepareStatement(stmt);
        p.clearParameters();
        p.setString(1, ssn);
        
        try {
            p.executeUpdate();
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    public String getResult(ResultSet resultSet) {
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            String result = "";
            while(resultSet.next()) {
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    result += resultSet.getString(i) + " ";
                }
                result += "\n";
            }

            return result;
        } catch(Exception e) {return "Error";}
        
        
    }

    public String[] getStringSet(ResultSet resultSet) {
        String[] result = null;
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            ArrayList<String> array = new ArrayList<String>();
            
            while(resultSet.next()) {
                String row = "";
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row = resultSet.getString(i) + " ";
                }
                array.add(row);
            }

            result = array.toArray(new String[0]);

            
        } catch(Exception e) {
            
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            CompanyDBController cont = new CompanyDBController("root", "2357ljhmsql@@", "company");

            int[] checked = {1,0,0,1,1,1,1,1,1,1};

            System.out.println(cont.getResult(cont.selectEmp(checked)));

            cont.deconnectDB();
        } catch(Exception e) {

        }
    }
}
