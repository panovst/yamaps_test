package ru.mig.gadm;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringEscapeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseUpdater {

	abstract public void update(int lineNumber, String[] as, Connection connection) throws SQLException;
	abstract String getTableName();
	abstract String getUpdateColumnName();
	abstract String getIdColumnName();

	protected String unescapeUnicode(final String unicode) {
		String result = String.copyValueOf(unicode.toCharArray());
		return StringEscapeUtils.unescapeJava(result.replaceAll("<U\\+", "\\\\u").replaceAll(">", ""));
	}

	private String getSqlQuery(String value, int id) {
		return String.format(sqlTemplate, getTableName(), getUpdateColumnName(), value, getIdColumnName(), id);
	}

	protected void executeUpdate(String value, int id, Connection connection) throws SQLException {
		String sql = getSqlQuery(value, id);
		System.out.println("SQL: "+sql);
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			Preconditions.checkArgument(ps.executeUpdate() > 0, "Не обновилось ни одной записи! Sql:\n"+sql);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	private final String sqlTemplate = "update %s set %s = '%s' where %s = %d";
}
