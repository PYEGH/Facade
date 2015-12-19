package com.epam.facade;

import java.sql.Connection;
import java.sql.SQLException;

public class FacadeDemo {

	public static void main(String[] args) {
		try {
			ConnectionPool cp = new ConnectionPool(5);
			try {
				Connection connection = cp.getConnection();
				System.out.println("Connection was got.");
				cp.closeConnection(connection);
				System.out.println("Connection was closed.");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
