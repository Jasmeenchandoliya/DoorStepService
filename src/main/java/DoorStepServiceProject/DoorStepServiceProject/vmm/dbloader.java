package DoorStepServiceProject.DoorStepServiceProject.vmm;
import java.sql.*;

public class dbloader {

    public static ResultSet executeQuery(String stm) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");

            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/doorstepservice", "root", "J@smeen45");
            System.out.println("Connection Build");

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Statement created");
            ResultSet rs = stmt.executeQuery(stm);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
