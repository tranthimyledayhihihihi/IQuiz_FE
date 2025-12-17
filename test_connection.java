// Test file để kiểm tra kết nối API
// Chạy file này để test trước khi chạy app

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class test_connection {
    public static void main(String[] args) {
        try {
            // Test kết nối đến backend
            URL url = new URL("http://10.0.2.2:5048/swagger");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            if (responseCode == 200) {
                System.out.println("✅ Kết nối thành công!");
                System.out.println("Backend đang chạy trên http://10.0.2.2:5048");
            } else {
                System.out.println("❌ Kết nối thất bại. Response code: " + responseCode);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi kết nối: " + e.getMessage());
            System.out.println("Vui lòng kiểm tra:");
            System.out.println("1. Backend có đang chạy không?");
            System.out.println("2. Port 5048 có đúng không?");
            System.out.println("3. Firewall có chặn không?");
        }
    }
}