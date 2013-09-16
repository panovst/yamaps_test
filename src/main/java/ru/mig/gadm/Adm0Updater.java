package ru.mig.gadm;

import java.sql.Connection;
import java.sql.SQLException;

public class Adm0Updater extends BaseUpdater {

	@Override
	public void update(int lineNumber, String[] as, Connection connection) throws SQLException {
		executeUpdate(unescapeUnicode(as[12]), Integer.valueOf(as[1]), connection);
	}

	@Override
	String getTableName() {
		return "rus_adm0";
	}

	@Override
	String getUpdateColumnName() {
		return "name_russi";
	}

	@Override
	String getIdColumnName() {
		return "id_0";
	}


}
