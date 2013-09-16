package ru.mig.gadm;

import java.sql.Connection;
import java.sql.SQLException;

public class Adm2Updater extends BaseUpdater {

	@Override
	public void update(int lineNumber, String[] as, Connection connection) throws SQLException {
		executeUpdate(unescapeUnicode(as[8]), Integer.valueOf(as[6]), connection);
	}

	@Override
	String getTableName() {
		return "rus_adm2";
	}

	@Override
	String getUpdateColumnName() {
		return "nl_name_2";
	}

	@Override
	String getIdColumnName() {
		return "id_2";
	}

}
