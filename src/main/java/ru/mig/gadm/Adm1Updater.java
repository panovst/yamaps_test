package ru.mig.gadm;

import java.sql.Connection;
import java.sql.SQLException;

public class Adm1Updater extends BaseUpdater {

	@Override
	public void update(int lineNumber, String[] as, Connection connection) throws SQLException {
		executeUpdate(unescapeUnicode(as[6]), Integer.valueOf(as[4]), connection);
	}

	@Override
	String getTableName() {
		return "rus_adm1";
	}

	@Override
	String getUpdateColumnName() {
		return "nl_name_1";
	}

	@Override
	String getIdColumnName() {
		return "id_1";
	}

}
