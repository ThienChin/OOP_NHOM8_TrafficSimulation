package vn.edu.ptit.oop.nhom08.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bus extends Vehicle {
    public Bus(String id, double speed, Direction direction, int position) {
        // Tốc độ chậm hơn xe thường (nhân 0.7)
        super(id, speed, direction, position, new Color(255, 200, 0)); // Màu vàng
    }

@Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        int cx = 325, cy = 300, w = 650, h = 600;
        int x = 0, y = 0;
        int width = 50, height = 24; 

        switch (direction) {
            case EAST:  x = position; y = cy + 8; break;
            case WEST:  x = w - position - width; y = cy - 8 - height; break;
            case NORTH: 
                x = cx + 8; y = h - position - width; 
                width = 24; height = 50; 
                break;
            case SOUTH: 
                x = cx - 8 - width; y = position; 
                width = 24; height = 50; 
                break;
        }
        g.fillRoundRect(x, y, width, height, 5, 5);
    }
}