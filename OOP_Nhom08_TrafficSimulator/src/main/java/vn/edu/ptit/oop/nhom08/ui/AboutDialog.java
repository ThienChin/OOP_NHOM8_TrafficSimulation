package vn.edu.ptit.oop.nhom08.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Hộp thoại giới thiệu (JDialog modal). */
public class AboutDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(Frame owner) {
        super(owner, "Giới thiệu", true);
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(java.awt.Color.WHITE);
        JLabel info = new JLabel("<html><div style='width:350px;line-height:1.6'>"
                + "<b>Nhóm 08 · TrafficSimulator</b><p>"
                + "Mô phỏng nút giao 4 làn mỗi chiều, tín hiệu đèn đếm ngược, "
                + "ô tô, xe máy, xe buýt và xe cứu thương.</p>"
                + "<p>Ứng dụng các nguyên lý lập trình hướng đối tượng (OOP).</p></div></html>");
        info.setBorder(BorderFactory.createEmptyBorder(10, 24, 20, 24));
        content.add(info, BorderLayout.CENTER);
        JButton close = new JButton("Đóng");
        close.addActionListener(e -> dispose());
        UiStyle.button(close, true);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 12));
        footer.setBackground(UiStyle.BACKGROUND);
        footer.add(close);
        UiStyle.dialog(this, "Giới thiệu dự án", content, footer, owner);
        getRootPane().setDefaultButton(close);
    }
}
