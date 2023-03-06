package com.generic_query.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {

	public static List<Map<String, Object>> selectFromTable(Connection conn, String tableName, List<String> columnNames,
			List<String> whereClauses) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> results = new ArrayList<>();

		try {
			StringBuilder query = new StringBuilder("SELECT ");
			for (int i = 0; i < columnNames.size(); i++) {
				if (i > 0) {
					query.append(", ");
				}
				query.append(columnNames.get(i));
			}
			query.append(" FROM ").append(tableName);
			if (whereClauses != null && !whereClauses.isEmpty()) {
				query.append(" WHERE ");
				for (int i = 0; i < whereClauses.size(); i++) {
					if (i > 0) {
						query.append(" AND ");
					}
					query.append(whereClauses.get(i));
				}
			}
			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					row.put(metaData.getColumnName(i), rs.getObject(i));
				}
				results.add(row);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return results;
	}

	public static void insertIntoTable(Connection conn, String tableName, String[] columnNames, Object[] values)
			throws SQLException {
		String query = "INSERT INTO " + tableName + " (";
		for (int i = 0; i < columnNames.length; i++) {
			query += columnNames[i];
			if (i < columnNames.length - 1) {
				query += ", ";
			}
		}
		query += ") VALUES (";
		for (int i = 0; i < values.length; i++) {
			query += "?";
			if (i < values.length - 1) {
				query += ", ";
			}
		}
		query += ")";
		PreparedStatement stmt = conn.prepareStatement(query);
		for (int i = 0; i < values.length; i++) {
			stmt.setObject(i + 1, values[i]);
		}
		stmt.executeUpdate();
	}

	public static void updateTable(Connection conn, String tableName, String[] columnNames, Object[] values,
			Map<String, Object> whereClause) throws SQLException {
		StringBuilder queryBuilder = new StringBuilder("UPDATE " + tableName + " SET ");

		int[] index = {1};


		for (int i = 0; i < columnNames.length; i++) {
			queryBuilder.append(columnNames[i]).append(" = ?");
			if (i < columnNames.length - 1) {
				queryBuilder.append(", ");
			}
		}
		queryBuilder.append(" WHERE ");
		whereClause.forEach((k, v) -> queryBuilder.append(k).append(" = ? AND "));
		queryBuilder.setLength(queryBuilder.length() - 5);
		PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString());

		for (Object value : values) {
			stmt.setObject(index[0]++, value);
		}
		whereClause.values().forEach(value -> {
			try {
				stmt.setObject(index[0]++, value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		stmt.executeUpdate();
	}
}
