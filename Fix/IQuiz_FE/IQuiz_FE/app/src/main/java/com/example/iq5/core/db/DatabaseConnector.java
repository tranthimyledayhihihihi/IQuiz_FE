package com.example.iq5.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // CHỈ DÙNG TRÊN BACKEND SERVER.
    // KHÔNG DÙNG TRONG ỨNG DỤNG ANDROID.
    private static final String DB_URL = "jdbc:sqlserver://QUANGQUANG:1433;databaseName=QuanLyDoAn;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getConnection() throws SQLException {
        System.out.println("Đang cố gắng kết nối đến database...");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Kết nối đến database QuanLyDoAn thành công!");
            } else {
                System.out.println("❌ Kết nối thất bại (Connection is null).");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi SQL trong quá trình kết nối:");
            e.printStackTrace();
        }
    }
}