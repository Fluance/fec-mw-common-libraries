package net.fluance.commons.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SqlUtils {
	private SqlUtils() {}
	
	/**
	 * 
	 * @param acceptNull
	 * @param resultSet
	 * @param columnName
	 * @return Integer value : null if acceptNull was set as true and the real value is NULL on DB 
	 * @throws SQLException
	 */
	public static Integer getInt(boolean acceptNull, ResultSet resultSet, String columnName) throws SQLException{
		Integer result = resultSet.getInt(columnName);
		if(acceptNull & resultSet.wasNull()){
			return null;
		}
		return result;
	}
	
	/**
	 * 
	 * @param acceptNull
	 * @param resultSet
	 * @param columnName
	 * @return Integer value : null if acceptNull was set as true and the real value is NULL on DB
	 * @throws SQLException
	 */
	public static Long getLong(boolean acceptNull, ResultSet resultSet, String columnName) throws SQLException{
		Long result = resultSet.getLong(columnName);
		if(acceptNull & resultSet.wasNull()){
			return null;
		}
		return result;
	}
	/**
	 * 
	 * @param acceptNull
	 * @param resultSet
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static String getString(boolean acceptNull, ResultSet resultSet, String columnName) throws SQLException{
		String result = resultSet.getString(columnName);
		if(acceptNull & resultSet.wasNull()){
			return null;
		}
		return result;
	}
	
	/**
	 * 
	 * @param acceptNull
	 * @param resultSet
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static Boolean getBoolean(boolean acceptNull, ResultSet resultSet, String columnName) throws SQLException{
		Boolean result = resultSet.getBoolean(columnName);
		if(acceptNull & resultSet.wasNull()){
			return null;
		}
		return result;
	}
	
	/**
	 * 
	 * @param acceptNull
	 * @param resultSet
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static java.util.Date getDate(boolean acceptNull, ResultSet resultSet, String columnName) throws SQLException{
		Timestamp timestamp = resultSet.getTimestamp(columnName);
		Date result = null;
		if(timestamp != null) {
			long time = timestamp.getTime();
			result = new java.util.Date(time);
		}
		if(acceptNull & resultSet.wasNull()){
			return null;
		}
		return result;
	}

	public static String toIn(List<String> elements){
		return "('" + StringUtils.join(elements, "','") + "')";
	}
}
