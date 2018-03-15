package se.unlogic.standardutils.dao;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface ChainedResultSetPopulator<T> {

	public void populate(T bean, ResultSet resultSet) throws SQLException;
}
