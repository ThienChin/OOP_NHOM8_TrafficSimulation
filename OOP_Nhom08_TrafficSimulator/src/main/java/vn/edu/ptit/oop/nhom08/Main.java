package vn.edu.ptit.oop.nhom08;

import vn.edu.ptit.oop.nhom08.ui.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Sử dụng giao diện mặc định của hệ thống (Windows/Mac) thay vì FlatLaf
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Không thể cài đặt giao diện hệ thống.");
        }

        // Chạy ứng dụng
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindow window = new MainWindow();
                window.setVisible(true);
            } catch (Exception e) {
                System.err.println("LỖI NGHIÊM TRỌNG KHI KHỞI TẠO GIAO DIỆN:");
                e.printStackTrace(); 
            }
        });
    }
}