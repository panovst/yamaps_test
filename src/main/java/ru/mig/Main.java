package ru.mig;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws IOException {
		String unicode = "<U+0428><U+043E><U+0432><U+0433><U+0435><U+043D><U+043E><U+0432><U+0441><U+043A><U+0438><U+0439> <U+0440><U+0430><U+0439><U+043E><U+043D>";

		unicode = unicode.replaceAll("<U\\+", "\\\\u").replaceAll(">", "");
		System.out.println(StringEscapeUtils.escapeJava(unicode));

		Main app = new Main();
		app.loadDriver();

		Connection connection = app.getConnection();

		app.parseCsv("/home/spanov/dev/maps/gadm/RUS_adm/RUS_adm2.csv");
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

	private void parseCsv(String path) throws IOException {
		CSVParser parser = new CSVParser(new FileReader(path), CSVStrategy.EXCEL_STRATEGY);
		String[] values = parser.getLine();
		while (values != null) {
			printValues(parser.getLineNumber(), values);
			values = parser.getLine();
		}
	}

	private static void unescapeUnicode(String unicode) {
		unicode = unicode.replaceAll("<U\\+", "\\\\u").replaceAll(">", "");
		System.out.println(StringEscapeUtils.unescapeJava(unicode));
	}
	private static void printValues(int lineNumber, String[] as) {
		System.out.println("Line " + lineNumber + " has " + as.length + " values:");
		unescapeUnicode(as[8]);
	}
}
