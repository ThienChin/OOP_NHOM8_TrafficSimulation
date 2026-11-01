package vn.edu.ptit.oop.nhom08.controller;

import javax.swing.Timer;
import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;
import vn.edu.ptit.oop.nhom08.model.Direction;
import vn.edu.ptit.oop.nhom08.model.LightState;

/** Một đồng hồ chung, không cho hai trục xanh đồng thời. */
public class TrafficLightController implements LightController {
    private int greenDuration=15, yellowDuration=3, redDuration=18;
    private int elapsed=0;
    private final Timer timer;
    public TrafficLightController() { timer=new Timer(1000,e -> elapsed=(elapsed+1)%cycle()); }
    private int clearance() { return redDuration-greenDuration-yellowDuration; }
    private int cycle() { return 2*(greenDuration+yellowDuration+clearance()); }
    @Override public void start() { if(!timer.isRunning())timer.start(); }
    @Override public void pause() { timer.stop(); }
    @Override public void reset() { pause(); elapsed=0; }
    private int axisStart(Direction d) { return d==Direction.EAST||d==Direction.WEST?0:greenDuration+yellowDuration+clearance(); }
    private int phaseTime(Direction d) { return (elapsed-axisStart(d)+cycle())%cycle(); }
    @Override public LightState getState(Direction d) {
        int t=phaseTime(d);
        if(t<greenDuration)return LightState.GREEN;
        if(t<greenDuration+yellowDuration)return LightState.YELLOW;
        return LightState.RED;
    }
    @Override public int getRemainingSeconds(Direction d) {
        int t=phaseTime(d);
        if(t<greenDuration)return greenDuration-t;
        if(t<greenDuration+yellowDuration)return greenDuration+yellowDuration-t;
        return cycle()-t;
    }
    @Override public int getGreenDuration() {return greenDuration;}
    @Override public int getYellowDuration() {return yellowDuration;}
    @Override public int getRedDuration() {return redDuration;}
    @Override public void setDurations(int green,int yellow,int red) throws InvalidSimulationDataException {
        if(green<=0)throw new InvalidSimulationDataException("green","Thời gian xanh phải lớn hơn 0!");
        if(yellow<=0)throw new InvalidSimulationDataException("yellow","Thời gian vàng phải lớn hơn 0!");
        if(red<green+yellow)throw new InvalidSimulationDataException("red","Đèn đỏ phải >= xanh + vàng ("+(green+yellow)+" giây)!");
        greenDuration=green; yellowDuration=yellow; redDuration=red;
        elapsed=0; // Đồng bộ chu kỳ ngay khi thay đổi cấu hình.
    }
}
