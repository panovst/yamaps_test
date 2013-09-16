package ru.mig.gadm;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RussianNameUpdater {

	public static void main(String[] args) {
		Connection connection = null;
		try {
			RussianNameUpdater app = new RussianNameUpdater();
			app.loadDriver();

			connection = app.getConnection();
			app.parseCsv("/home/spanov/dev/maps/gadm/RUS_adm/RUS_adm0.csv", new Adm0Updater(), connection);
			app.parseCsv("/home/spanov/dev/maps/gadm/RUS_adm/RUS_adm1.csv", new Adm1Updater(), connection);
			app.parseCsv("/home/spanov/dev/maps/gadm/RUS_adm/RUS_adm2.csv", new Adm2Updater(), connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDriver() {
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
	}

	private Connection getConnection() {
		Connection result = null;

		try {

			result = DriverManager.getConnection(
					"jdbc:postgresql://localhost/gadm", "gadm", "123");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}
		return result;
	}

	private void parseCsv(String path, BaseUpdater updater, Connection connection) throws IOException, SQLException {
		CSVParser parser = new CSVParser(new FileReader(path), CSVStrategy.EXCEL_STRATEGY);
		String[] values = parser.getLine();
		while (values != null) {
			if (parser.getLineNumber() != 1) {
				updater.update(parser.getLineNumber(), values, connection);
			}
			values = parser.getLine();
		}
	}

}
