package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
    
    private static Properties props = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                System.out.println("Unable to find config.properties");
                return;
            }
            
            props.load(input);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        try {
            Class.forName(props.getProperty("db.driver"));
            return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"), 
                props.getProperty("db.password")
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}