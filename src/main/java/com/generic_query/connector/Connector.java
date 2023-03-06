package com.generic_query.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

	public static Connection createConnection(String dbStore, String username, String password, String host,
			String port, String database) {

		String constr = null;
		Connection conn = null;
		try {
			if (dbStore.equalsIgnoreCase("mysql") || (dbStore.equalsIgnoreCase("postgresql"))) {
				constr = "jdbc:" + dbStore + "//" + host + ":" + port + "/" + database;
			}
			if (dbStore.equalsIgnoreCase("sqlserver")) {
				constr = "jdbc:" + dbStore + "//" + host + ":" + port + ";databaseName=" + database;
			}

			conn = DriverManager.getConnection(constr, username, password);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
