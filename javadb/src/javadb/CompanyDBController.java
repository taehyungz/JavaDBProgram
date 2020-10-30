package javadb;

import java.sql.*;
import java.util.ArrayList;

import java.io.*;

public class CompanyDBController {
    private Connection conn = null;
    private String sqlID = null;
    private String sqlPw = null;

    public CompanyDBController(final String sqlID, final String sqlPw) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.sqlID = sqlID;
        this.sqlPw = sqlPw;
        this.connectDB();
    }

    public boolean connectDB() throws SQLException {
        try {
            final String url = "jdbc:mysql://localhost:3306/company?serverTimezone=UTC";
            conn = DriverManager.getConnection(url, sqlID, sqlPw);
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    public boolean deconnectDB() throws SQLException {
        try {
            if(conn != null) {
                conn.close();
                return true;
            }
            return false;
        } catch(SQLException e){ return false;}
    }
    // 부서 선택, 애트리뷰트 선택, 조건 선택
    public ResultSet selectEmp() throws SQLException {
        String stmt = "SELECT E.Fname, E.Minit, E.Lname, E.Ssn, E.Bdate, E.Address, E.Sex, E.Salary, S.Fname, S.Minit, S.Lname, D.Dname ";
        stmt += "FROM EMPLOYEE as E, DEPARTMENT as D, EMPLOYEE as S ";
        stmt += "WHERE E.Dno = D.Dnumber and E.Super_ssn = S.Ssn;";

        PreparedStatement p = conn.prepareStatement(stmt);

        return p.executeQuery();
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

    public String getResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        String result = "";
        while(resultSet.next()) {
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                result += resultSet.getString(i) + " ";
            }
            result += "\n";
        }
        return result;
    }

    public String[] getStringSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        ArrayList<String> array = new ArrayList<String>();
        
        while(resultSet.next()) {
            String row = "";
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                row = resultSet.getString(i) + " ";
            }
            array.add(row);
        }

        String[] result = array.toArray(new String[0]);

        return result;
    }


/*    public static void main (String args []) throws SQLException, IOException{
        String dbacct, passwrd;
        dbacct = "root";
        passwrd = "";

        CompanyDBController cont = new CompanyDBController(dbacct, passwrd);
        if(cont.connectDB()) System.out.println("정상적으로 연결되었습니다.");
        if(cont.insertEmp("hahaman S hoho", "123123123", "2020-10-29","hahahohocity", 'M', 12300.00, "123456789", 5)) {
            System.out.println("정상적으로 변경되었습니다.");
        } else {
            System.out.println("투플변경에 실패하였습니다.");
        }
        System.out.println(cont.getResult(cont.selectEmp()));
        if(cont.updateEmp("123123123", 33333.00)) {
            System.out.println("정상적으로 변경되었습니다.");
        } else {
            System.out.println("투플변경에 실패하였습니다.");
        }
        System.out.println(cont.getResult(cont.selectEmp()));
        if(cont.deleteEmp("123123123")) {
            System.out.println("정상적으로 변경되었습니다.");
        } else {
            System.out.println("투플변경에 실패하였습니다.");
        }
        System.out.println(cont.getResult(cont.selectEmp()));
        
        String[] result = cont.getStringSet(cont.getTables());
        System.out.println(Arrays.toString(result));
        result = cont.getAttrs("employee");
        System.out.println(Arrays.toString(result));
        if(cont.deconnectDB()) System.out.println("정상적으로 연결을 해제합니다.");
    }*/
}
