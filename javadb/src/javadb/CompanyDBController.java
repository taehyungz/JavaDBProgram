package javadb;

import java.sql.*;
import java.util.ArrayList;
import java.io.*;

public class CompanyDBController { // LJH
    private Connection conn = null;
    private String sqlID = null;
    private String sqlPw = null;

    public CompanyDBController(File f) throws SQLException {
        try {
            FileReader filereader = new FileReader(f);
            BufferedReader bufReader = new BufferedReader(filereader);
            sqlID = bufReader.readLine();
            sqlPw = bufReader.readLine();
            String dbname = bufReader.readLine();
                
            Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결

            this.connectDB(dbname);

            bufReader.close();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	public CompanyDBController(final String sqlID, final String sqlPw) throws ClassNotFoundException, SQLException{
		CompanyDBController(sqlID, sqlPw, "COMPANY");
	}

	private void CompanyDBController(final String sqlID, final String sqlPw, final String dbName) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver"); //JDBC 드라이버 연결

        this.sqlID = sqlID;
        this.sqlPw = sqlPw;
        this.connectDB(dbName);
	}

	public boolean connectDB(final String dbName) throws SQLException{
        
        final String url = "jdbc:mysql://localhost:3306/"+dbName+"?serverTimezone=UTC";
        conn = DriverManager.getConnection(url, sqlID, sqlPw);
        return true;
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

    // 콤보관련 쿼리 수정 (PHJ)
    public ResultSet selectEmp(int[] checked, int ParIndex, String ChildItem) {
        String[] attrName = {"CONCAT(E.Fname,' ',E.Minit,' ',E.Lname) AS NAME", "E.SSN", "E.BDATE", "E.ADDRESS", "E.SEX", "E.SALARY",
                             "CONCAT(S.Fname,' ',S.Minit,' ',S.Lname) AS SUPERVISOR", "Dname AS DEPARTMENT"};
        String stmt = "SELECT ";
        for(int i = 0; i < checked.length; i++) {
            if(checked[i] == 1) {
                stmt += attrName[i] + ", ";
            }
        }
        //{"전체","BDATE","SEX","SALARY","SUPERVISOR","DEPARTMENT"};
        String par = " WHERE ";
        switch(ParIndex) {
            case 0:
                par = "";
                break;
            case 1:
                par += attrName[ParIndex+1] + " = \"" + ChildItem + "\"";
                break;
            case 4:
            case 5:
                par += attrName[ParIndex+2].split(" AS ")[0] + " = \"" + ChildItem + "\"";
                break;
            default:
                par += attrName[ParIndex+2]+ " = \"" + ChildItem + "\"";
                break;
        }
        if(stmt.substring(stmt.length()-2,stmt.length()).equals(", ")) {
            stmt = stmt.substring(0,stmt.length()-2);
            stmt += " FROM (EMPLOYEE AS E LEFT JOIN EMPLOYEE AS S ON E.Super_ssn = S.Ssn)" +
                    " JOIN DEPARTMENT ON E.Dno = Dnumber";
            stmt += par;
        } else {
            stmt += "null";
        }

        try {
            PreparedStatement p =
                conn.prepareStatement(stmt,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

            return p.executeQuery();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public ResultSet selectSsn(int ParIndex, String ChildItem) {
        String[] attrName = {"CONCAT(E.Fname,' ',E.Minit,' ',E.Lname) AS NAME", "E.SSN", "E.BDATE", "E.ADDRESS", "E.SEX", "E.SALARY",
                             "CONCAT(S.Fname,' ',S.Minit,' ',S.Lname) AS SUPERVISOR", "Dname AS DEPARTMENT"};
        String stmt = "SELECT CONCAT(E.Fname,' ',E.Minit,' ',E.Lname) AS NAME, E.SSN";
        stmt += " FROM (EMPLOYEE AS E LEFT JOIN EMPLOYEE AS S ON E.Super_ssn = S.Ssn)" +
                    " JOIN DEPARTMENT ON E.Dno = Dnumber";
        
        String par = " WHERE ";
        switch(ParIndex) {
            case 0:
                par = "";
                break;
            case 1:
                par += attrName[ParIndex+1] + " = \"" + ChildItem + "\"";
                break;
            case 4:
            case 5:
                par += attrName[ParIndex+2].split(" AS ")[0] + " = \"" + ChildItem + "\"";
                break;
            default:
                par += attrName[ParIndex+2]+ " = \"" + ChildItem + "\"";
                break;
        }

        stmt += par;
        try {
            PreparedStatement p = conn.prepareStatement(stmt);
            return p.executeQuery();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public String[] getAttrs() {
        try {
            String stmt = "SELECT CONCAT(E.Fname,' ',E.Minit,' ',E.Lname) AS NAME, E.SSN, E.BDATE, E.ADDRESS, E.SEX," +
                            " E.SALARY, CONCAT(S.Fname,' ',S.Minit,' ',S.Lname) AS SUPERVISIOR, Dname AS DEPARTMENT" + 
                            " FROM (EMPLOYEE AS E LEFT JOIN EMPLOYEE AS S ON E.Super_ssn = S.Ssn)" +
                            " JOIN DEPARTMENT ON E.Dno = Dnumber";
            PreparedStatement p = conn.prepareStatement(stmt);

            ResultSet r = p.executeQuery();
            ResultSetMetaData rsmd = r.getMetaData();

            ArrayList<String> array = new ArrayList<String>();
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                array.add(rsmd.getColumnLabel(i));
            }

            String[] result = array.toArray(new String[0]);

            return result;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public ResultSet getTables() {
        try {
            String stmt = "Show tables;";
            PreparedStatement p = conn.prepareStatement(stmt);
            return p.executeQuery();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    // 조건 선택
    public boolean updateEmp(String ssn, double newSalary) {
        String stmt = "UPDATE EMPLOYEE ";
        stmt += "SET Salary = ? ";
        stmt += "WHERE Ssn = ?;";
        try {
            PreparedStatement p = conn.prepareStatement(stmt);
            p.clearParameters();
            p.setDouble(1, newSalary);
            p.setString(2, ssn);
        
            p.executeUpdate();
            return true;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmp(String ssn) {
        String stmt = "DELETE FROM EMPLOYEE ";
        stmt += "WHERE Ssn = ?;";
        try {
            PreparedStatement p = conn.prepareStatement(stmt);
            p.clearParameters();
            p.setString(1, ssn);
        
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
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "Error";
        }  
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
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return result;
    }

    public String[] getAttrsName(ResultSet r) {
        try {
            ResultSetMetaData rsmd = r.getMetaData();
            int colNum = rsmd.getColumnCount() + 1;
            String[] result = new String[colNum];
            result[0] = "CheckBox";
            for(int i=1; i < result.length; i++) {
                result[i] = rsmd.getColumnLabel(i);
            }
            return result;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
        
    }

    public Object[][] getTuples(ResultSet r) {
        try {
            ResultSetMetaData rsmd = r.getMetaData();
            int rowNum = 0;
            if(r.last()) {
                rowNum = r.getRow();
                r.beforeFirst();
            }
            int colNum = rsmd.getColumnCount() + 1;
            Object[][] result = new Object[rowNum][colNum];
            int row = 0;
            while(r.next()) {
                result[row][0] = Boolean.FALSE;
                for(int col = 1; col < colNum; col++) {
                    result[row][col] = r.getString(col);
                }
                row++;
            }
            
            return result;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    ////콤보박스 자식검색 쿼리 검색(PHJ)
    public int ChildSearch(String Par,ArrayList<String> Child) { // PHJ
    	String stmt="";
    	ArrayList<String> ArrList = new ArrayList<String>();
    	int size=0;
    	
    	if(Par =="전체") {
    		return 0;
    	}
    	else if(Par == "SUPERVISOR") {
    		stmt = "SELECT " + 
    				"DISTINCT CONCAT(S.Fname,' ',S.Minit,' ',S.Lname) AS SUPERVISOR "+
    				"FROM (EMPLOYEE AS E LEFT JOIN EMPLOYEE AS S ON E.Super_ssn = S.Ssn) " + 
    				"JOIN DEPARTMENT ON E.Dno = Dnumber";
    	}
    	else if(Par == "DEPARTMENT") {
    		stmt="SELECT " + 
    				"DISTINCT Dname " + 
    				"FROM (EMPLOYEE AS E LEFT JOIN EMPLOYEE AS S ON E.Super_ssn = S.Ssn) " + 
    				"JOIN DEPARTMENT ON E.Dno = Dnumber";
    		
    	}
    	else {
    		stmt = "SELECT DISTINCT "+Par+" FROM EMPLOYEE";
    	}
    	
    	try{
            PreparedStatement p = conn.prepareStatement(stmt);  
            
            ResultSet r = p.executeQuery();
            while(r.next()) {
                ArrList.add(r.getString(1));
            }

            String[] result = new String[ArrList.size()];
            ArrList.toArray(result);
            for(int i = 0;i<ArrList.size();i++) {
                Child.add(i, ArrList.get(i));
            }
            size = result.length;
            return size;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }
}
