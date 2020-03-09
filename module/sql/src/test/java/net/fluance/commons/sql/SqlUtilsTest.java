package net.fluance.commons.sql;

import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.TestCase;

public class SqlUtilsTest extends TestCase{

	private static final String TEST = "test";
	
	private static Timestamp timestamp;

	
	private ResultSet rs;
	private String columnName = "Any_column";
	
	@Before
	public void setUp() throws SQLException{
		rs = Mockito.mock(ResultSet.class);		
		when(rs.getInt(columnName)).thenReturn(0);
		when(rs.getLong(columnName)).thenReturn(new Long(0));
		when(rs.getString(columnName)).thenReturn(TEST);
		timestamp = new Timestamp(2018, 9, 12, 21, 19, 9, 1);
		when(rs.getTimestamp(columnName)).thenReturn(timestamp);
		when(rs.getBoolean(columnName)).thenReturn(true);
		when(rs.wasNull()).thenReturn(true);
		
	}
	
	@Test
	public void testGetIntNotNull() throws SQLException{
		boolean acceptNull = false;
		Integer actual = SqlUtils.getInt(acceptNull, rs, columnName);
		Integer expected = 0;

		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetIntNull() throws SQLException{
		boolean acceptNull = true;
		Integer actual = SqlUtils.getInt(acceptNull, rs, columnName);
		assertNull(actual);
	}
	
	@Test
	public void testGetLongNotNull() throws SQLException{
		boolean acceptNull = false;
		Long actual = SqlUtils.getLong(acceptNull, rs, columnName);
		Long expected = new Long(0);

		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetLonGNull() throws SQLException{
		boolean acceptNull = true;
		Long actual = SqlUtils.getLong(acceptNull, rs, columnName);
		assertNull(actual);
	}
	
	@Test
	public void testGetStringNotNull() throws SQLException{
		boolean acceptNull = false;
		String actual = SqlUtils.getString(acceptNull, rs, columnName);
		assertNotNull(actual);
		assertEquals(TEST, actual);
	}
	
	@Test
	public void testGetStringNull() throws SQLException{
		boolean acceptNull = true;
		String actual = SqlUtils.getString(acceptNull, rs, columnName);
		assertNull(actual);
	}
	
	@Test
	public void testGetBooleanNotNull() throws SQLException{
		boolean acceptNull = false;
		boolean actual = SqlUtils.getBoolean(acceptNull, rs, columnName);
		assertNotNull(actual);
		assertEquals(true, actual);
	}
	
	@Test
	public void testGetBooleanNull() throws SQLException{
		boolean acceptNull = true;
		Boolean actual = SqlUtils.getBoolean(acceptNull, rs, columnName);
		assertNull(actual);
	}
	
	@Test
	public void testGetDateNotNull() throws SQLException{
		boolean acceptNull = false;
		Date actual = SqlUtils.getDate(acceptNull, rs, columnName);
		assertNotNull(actual);
		assertEquals(new Date(timestamp.getTime()), actual);
	}
	
	@Test
	public void testGetDateNull() throws SQLException{
		boolean acceptNull = true;
		java.util.Date actual = SqlUtils.getDate(acceptNull, rs, columnName);
		assertNull(actual);
	}
}
