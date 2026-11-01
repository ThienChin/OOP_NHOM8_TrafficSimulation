package vn.edu.ptit.oop.nhom08.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Ambulance extends Vehicle {
    public Ambulance(String id, double speed, Direction direction, int position) {
        super(id, speed, direction, position, Color.WHITE);
    }

    @Override
    public void draw(Graphics2D g) {
        int cx = 325, cy = 300, w = 650, h = 600;
        int x = 0, y = 0;
        int width = 36, height = 20;

        switch (direction) {
            case EAST:  x = position; y = cy + 10; break;
            case WEST:  x = w - position - width; y = cy - 10 - height; break;
            case NORTH: x = cx + 10; y = h - position - width; width = 20; height = 36; break;
            case SOUTH: x = cx - 10 - width; y = position; width = 20; height = 36; break;
        }
        
        g.setColor(color);
        g.fillRoundRect(x, y, width, height, 6, 6);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 6, 6);
        
        // Vẽ chữ thập
        g.setColor(Color.RED);
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        if (direction == Direction.EAST || direction == Direction.WEST) {
            g.fillRect(centerX - 2, centerY - 5, 4, 10);
            g.fillRect(centerX - 5, centerY - 2, 10, 4);
        } else {
            g.fillRect(centerX - 5, centerY - 2, 10, 4);
            g.fillRect(centerX - 2, centerY - 5, 4, 10);
        }

        // Đèn chớp cứu thương nhấp nháy Xanh/Đỏ liên tục 
        boolean isRedFlash = (System.currentTimeMillis() / 150) % 2 == 0;
        g.setColor(isRedFlash ? Color.RED : Color.BLUE);
        
        if (direction == Direction.EAST) {
            g.fillRect(x + 28, y + 2, 4, height - 4);
        } else if (direction == Direction.WEST) {
            g.fillRect(x + 4, y + 2, 4, height - 4);
        } else if (direction == Direction.NORTH) {
            g.fillRect(x + 2, y + 4, width - 4, 4);
        } else if (direction == Direction.SOUTH) {
            g.fillRect(x + 2, y + 28, width - 4, 4);
        }
    }
}