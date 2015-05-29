package listeners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import config.ConnectionManager;

/**
 * Application Lifecycle Listener implementation class DataBaseMigrationListener
 *
 */
@WebListener
public class DataBaseMigrationListener implements ServletRequestListener {

    /**
     * Default constructor. 
     */
    public DataBaseMigrationListener() {}

	/**
     * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
     */
	public void requestDestroyed(ServletRequestEvent arg0)  {}

	/**
	 * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent arg0)  { 
		ClassLoader classLoader = getClass().getClassLoader();
		String filePath = classLoader.getResource("resources/config_project.ini").getPath();
		
		Ini ini = null;
        try {
	        ini = new Ini(new FileReader(filePath));
        } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        };
		Ini.Section config = ini.get("database");

		String migration = config.get("migration");
		int migrationNum = migration == null ? 0 : Integer.valueOf(migration);
		if (updateDBStructure(migrationNum)) {
			config.replace("migration", Integer.valueOf(migrationNum + 1).toString());
			try {
		        ini.store(new File(filePath));
	        } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
	        }
		}
	}

	private boolean updateDBStructure(int migrationId) {
		ArrayList<String> querysList = new ArrayList<>();
		if (migrationId < 1) {
			String query = "ALTER TABLE `ourproject`.`students` "
					+ "CHANGE COLUMN `teacherId` `groupName` VARCHAR(32) NOT NULL DEFAULT '' ;";
			querysList.add(query);
		}
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		Statement stmt = null;
        try {
	        stmt = con.createStatement();
        } catch (SQLException e) {
	        e.printStackTrace();
        }
		
		for (String query : querysList) {
			try {
	            stmt.execute(query);
            } catch (SQLException e) {
	            e.printStackTrace();
            }
		}
		return !querysList.isEmpty();
	}

}
