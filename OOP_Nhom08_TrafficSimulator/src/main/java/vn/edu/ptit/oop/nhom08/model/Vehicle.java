package vn.edu.ptit.oop.nhom08.model;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Vehicle {
    protected String id;
    protected double speed;
    protected Direction direction;
    protected int position;
    protected Color color;
    private Direction destination;
    private int lane = 1;
    public int getLane() { return lane; }
    public void setLane(int lane) { if (lane < 0 || lane > 3) throw new IllegalArgumentException("Làn phải từ 1 đến 4"); this.lane = lane; }
    private boolean intersectionEntered;
    public Direction getDestination() { return destination == null ? direction : destination; }
    public void setDestination(Direction destination) { this.destination = destination; }
    public boolean hasEnteredIntersection() { return intersectionEntered; }
    public void setIntersectionEntered(boolean value) { intersectionEntered = value; }
    public boolean isTurning() { return getDestination() != direction; }
    public Color getColor() { return color; }

    public Vehicle(String id, double speed, Direction direction, int position, Color color) {
        this.id = id;
        this.speed = speed;
        this.direction = direction;
        this.position = position;
        this.color = color;
    }

    public Direction getDirection() { return direction; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public double getSpeed() { return speed; }
    public String getId() { return id; }

    // Phương thức trừu tượng OOP bắt buộc
    public abstract void draw(Graphics2D g);
    
    // Di chuyển cơ bản
    public void move() {
        position += speed;
    }
}