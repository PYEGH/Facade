package com.epam.facade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Connection Pool - is an example of facade pattern
 * @author Pavel
 *
 */
public class ConnectionPool {

    private BlockingQueue<Connection> connectionQueue;
    private static final String WARN1 = "Database access error";
    private static final String WARN2 = "Returning connection not added. Possible `leakage` of connections";
    private static final String WARN3 = "Trying to return closed connection. Possible `leakage` of connections";
    private static final String WARN4 = "SQL exception.";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "abc123";



    public ConnectionPool(int poolSize) throws SQLException, ClassNotFoundException {
        
        Class.forName("oracle.jdbc.OracleDriver");
        connectionQueue = new ArrayBlockingQueue<Connection> (poolSize);
        try {
            for (int i = 0; i < poolSize; i++) {
                Connection conn = null;
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                connectionQueue.offer(conn);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() throws InterruptedException {
        Connection connection = null;
        connection = connectionQueue.take();
        return connection;
    }


    public void closeConnection(Connection connection) {
        boolean closed = true;
        try {
            closed = connection.isClosed();
        } catch(SQLException e) {
            System.out.println(WARN1 + " " + e);
        }
        if(!closed) {
            if(!connectionQueue.offer(connection)) {
                System.out.println(WARN2);
            }
        } else {
            System.out.println(WARN3);
        }
    }
    public void dispose() {
        Connection connection;
        while ((connection = connectionQueue.poll()) != null) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                connection.close();
            } catch(SQLException e) {
                System.out.println(WARN4 + " " + e);
            }
        }
    }    
}