package vn.edu.ptit.oop.nhom08.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Motorcycle extends Vehicle {
    public Motorcycle(String id, double speed, Direction direction, int position) {
        // Tốc độ nhanh hơn (nhân 1.5)
        super(id, speed, direction, position, Color.MAGENTA);
    }

@Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        int cx = 325, cy = 300, w = 650, h = 600;
        int x = 0, y = 0;
        int width = 16, height = 10; 

        switch (direction) {
            case EAST:  x = position; y = cy + 20; break;
            case WEST:  x = w - position - width; y = cy - 20 - height; break;
            case NORTH: 
                x = cx + 20; y = h - position - width; 
                width = 10; height = 16; 
                break;
            case SOUTH: 
                x = cx - 20 - width; y = position; 
                width = 10; height = 16; 
                break;
        }
        g.fillRoundRect(x, y, width, height, 4, 4);
    }
}