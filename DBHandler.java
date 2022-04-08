import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHandler {
	
	
	public Connection establishConnection() throws IOException,SQLException, ClassNotFoundException{
		
		Properties props = new Properties();
		
		FileInputStream st = new FileInputStream("db.properties");
		props.load(st);
		
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String pass = props.getProperty("db.pass");
		
		Connection conn = DriverManager.getConnection(url,user,pass);
		return conn;
	}
}
