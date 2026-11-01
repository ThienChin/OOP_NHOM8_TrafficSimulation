package vn.edu.ptit.oop.nhom08.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/** Giao diện Swing thống nhất, không phụ thuộc thư viện ngoài. */
public final class UiStyle {
    public static final Color PRIMARY = new Color(36, 99, 179);
    public static final Color BACKGROUND = new Color(246, 248, 251);
    public static final Color TEXT = new Color(36, 49, 67);
    public static final Color MUTED = new Color(104, 119, 139);
    private UiStyle() {}

    public static void button(AbstractButton button, boolean primary) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusable(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(primary ? PRIMARY : Color.WHITE);
        button.setForeground(primary ? Color.WHITE : TEXT);
        button.setBorder(new CompoundBorder(new LineBorder(primary ? PRIMARY : new Color(212, 221, 232), 1, true),
                new EmptyBorder(9, 15, 9, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static JPanel form(int rows) {
        JPanel panel = new JPanel(new GridLayout(rows, 2, 14, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 24, 14, 24));
        return panel;
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    public static void field(JComponent field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(210, 34));
        if (field instanceof JTextField) {
            field.setBorder(new CompoundBorder(new LineBorder(new Color(203, 213, 226), 1, true), new EmptyBorder(5, 9, 5, 9)));
        }
    }

    public static JPanel footer(JButton primary, JButton cancel) {
        button(primary, true);
        button(cancel, false);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(0, 14, 4, 14));
        panel.add(cancel);
        panel.add(primary);
        return panel;
    }

    public static void dialog(JDialog dialog, String heading, JPanel content, JPanel footer, Frame owner) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        JLabel title = new JLabel(heading);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);
        title.setBorder(new EmptyBorder(19, 24, 6, 24));
        root.add(title, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        dialog.setContentPane(root);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(Math.max(dialog.getWidth(), 475), dialog.getHeight()));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);
    }
}
