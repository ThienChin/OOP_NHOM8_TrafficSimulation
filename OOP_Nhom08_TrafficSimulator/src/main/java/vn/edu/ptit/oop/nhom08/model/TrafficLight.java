package vn.edu.ptit.oop.nhom08.model;

public class TrafficLight {

    private Direction direction;
    private LightState state;
    private int duration;

    public TrafficLight(Direction direction, int duration) {
        this.direction = direction;
        this.duration = duration;
        this.state = LightState.RED;
    }

    public Direction getDirection() {
        return direction;
    }

    public LightState getState() {
        return state;
    }

    public int getDuration() {
        return duration;
    }

    public void setState(LightState state) {
        this.state = state;
    }

    //Thoi gian den hoat dong, tinh bang giay
    public void setDuration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        }
    }

    public void changeState(LightState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "direction=" + direction +
                ", state=" + state +
                ", duration=" + duration +
                '}';
    }
}