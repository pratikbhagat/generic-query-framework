package com.generic_query;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.generic_query.connector.Connector;
import com.generic_query.query.Query;

public class App {
	public static void main(String[] args) {
		try {

			// Load the properties file from resources
			ClassLoader classLoader = Connector.class.getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("application.properties");

			// Create a Properties object and load the file
			Properties props = new Properties();

			try {
				props.load(inputStream);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Now you can retrieve properties using the getProperty() method
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");
			String host = props.getProperty("db.host");
			String database = props.getProperty("db.database");
			String port = props.getProperty("db.port");

			String tableName = "info";
			Connection conn = Connector.createConnection("mysql", username, password, host, port, database);

			List<String> columnNames = Arrays.asList("id", "name", "email");
			// List<String> whereClauses = Arrays.asList("id=11");

			List<Map<String, Object>> selectResult = Query.selectFromTable(conn, tableName, columnNames, null);
			System.out.println("Select Query Result: " + selectResult);

			String[] insertColumnNames = { "id", "name", "age", "email" };
			Object[] insertColumnValues = { 12, "priyanka", 29, "priyanka@example.com" };
			conn = Connector.createConnection("mysql", username, password, host, port, database);

			Query.insertIntoTable(conn, tableName, insertColumnNames, insertColumnValues);
			System.out.println("Insert Success");
			String[] updateColumnNames = { "age" };
			Object[] updateColumnvalues = { 30 };
			Map<String, Object> updatewhereClause = new HashMap<>();
			updatewhereClause.put("id", 11);
			conn = Connector.createConnection("mysql", username, password, host, port, database);

			Query.updateTable(conn, tableName, updateColumnNames, updateColumnvalues, updatewhereClause);
			System.out.println("UpdateSuccess");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
