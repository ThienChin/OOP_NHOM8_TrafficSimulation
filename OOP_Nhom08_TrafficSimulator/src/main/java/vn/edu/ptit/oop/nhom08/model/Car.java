package vn.edu.ptit.oop.nhom08.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Car extends Vehicle {
    public Car(String id, double speed, Direction direction, int position) {
        super(id, speed, direction, position, new Color(30, 100, 200)); 
    }

    @Override
    public void draw(Graphics2D g) {
        int cx = 325, cy = 300, w = 650, h = 600; 
        int x = 0, y = 0;
        int width = 32, height = 18;

        switch (direction) {
            case EAST:  x = position; y = cy + 12; break;
            case WEST:  x = w - position - width; y = cy - 12 - height; break;
            case NORTH: x = cx + 12; y = h - position - width; width = 18; height = 32; break;
            case SOUTH: x = cx - 12 - width; y = position; width = 18; height = 32; break;
        }
        
        // Vẽ thân xe có viền
        g.setColor(color);
        g.fillRoundRect(x, y, width, height, 6, 6);
        g.setColor(color.darker());
        g.drawRoundRect(x, y, width, height, 6, 6);

        // Vẽ kính chắn gió và đèn tùy theo hướng
        g.setColor(Color.DARK_GRAY);
        if (direction == Direction.EAST) {
            g.fillRect(x + 20, y + 2, 6, height - 4); // Kính trước
            g.fillRect(x + 4, y + 2, 4, height - 4);  // Kính sau
            
            g.setColor(Color.YELLOW); g.fillRect(x + width - 2, y + 2, 2, 4);
            g.fillRect(x + width - 2, y + height - 6, 2, 4); //den  Pha
            
            g.setColor(Color.RED);    g.fillRect(x, y + 2, 2, 4);
            g.fillRect(x, y + height - 6, 2, 4); //den  Hậu
            
            
        } else if (direction == Direction.WEST) {
            g.fillRect(x + 6, y + 2, 6, height - 4); 
            g.fillRect(x + 24, y + 2, 4, height - 4);
            
            g.setColor(Color.YELLOW); g.fillRect(x, y + 2, 2, 4);
            g.fillRect(x, y + height - 6, 2, 4);
            
            g.setColor(Color.RED);    g.fillRect(x + width - 2, y + 2, 2, 4);
            g.fillRect(x + width - 2, y + height - 6, 2, 4);
            
        } else if (direction == Direction.NORTH) {
            g.fillRect(x + 2, y + 6, width - 4, 6);
            g.fillRect(x + 2, y + 24, width - 4, 4);
            
            g.setColor(Color.YELLOW); g.fillRect(x + 2, y, 4, 2); g.fillRect(x + width - 6, y, 4, 2);
            g.setColor(Color.RED);    g.fillRect(x + 2, y + height - 2, 4, 2); g.fillRect(x + width - 6, y + height - 2, 4, 2);
            
        } else if (direction == Direction.SOUTH) {
            g.fillRect(x + 2, y + 20, width - 4, 6);
            g.fillRect(x + 2, y + 4, width - 4, 4);
            
            g.setColor(Color.YELLOW); g.fillRect(x + 2, y + height - 2, 4, 2); g.fillRect(x + width - 6, y + height - 2, 4, 2);
            g.setColor(Color.RED);    g.fillRect(x + 2, y, 4, 2); g.fillRect(x + width - 6, y, 4, 2);
        }
    }
}