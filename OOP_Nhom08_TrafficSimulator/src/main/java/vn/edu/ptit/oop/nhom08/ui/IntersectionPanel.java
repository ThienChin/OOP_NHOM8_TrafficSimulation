package vn.edu.ptit.oop.nhom08.ui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import vn.edu.ptit.oop.nhom08.model.*;
import vn.edu.ptit.oop.nhom08.simulation.Simulation;

/** Giao diện ngã tư 4 làn mỗi chiều, tự co giãn theo cửa sổ. */
public class IntersectionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Simulation sim;
    public IntersectionPanel(Simulation simulation) {
        sim = simulation;
        setPreferredSize(new Dimension(VehiclePath.W, VehiclePath.H));
        setBackground(new Color(68,149,74));
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D a = (Graphics2D) g.create();
        a.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double scale = Math.min(getWidth()/(double)VehiclePath.W, getHeight()/(double)VehiclePath.H);
        a.translate((getWidth()-VehiclePath.W*scale)/2, (getHeight()-VehiclePath.H*scale)/2);
        a.scale(scale, scale);
        int x=VehiclePath.CX, y=VehiclePath.CY, h=VehiclePath.ROAD_HALF, w=VehiclePath.W, ht=VehiclePath.H;
        a.setColor(new Color(68,149,74)); a.fillRect(0,0,w,ht);
        a.setColor(new Color(163,168,163));
        a.fillRect(0,y-2*h-12,w,4*h+24);
        a.fillRect(x-2*h-12,0,4*h+24,ht);
        a.setColor(new Color(55,60,68));
        a.fillRect(0,y-2*h,w,4*h);
        a.fillRect(x-2*h,0,4*h,ht);
        // Vạch vàng phân cách hai chiều, KHÔNG vẽ xuyên vùng giao nhau.
        a.setColor(new Color(250,197,63)); a.setStroke(new BasicStroke(2.8f));
        a.drawLine(0,y,x-2*h,y); a.drawLine(x+2*h,y,w,y);
        a.drawLine(x,0,x,y-2*h); a.drawLine(x,y+2*h,x,ht);
        // 4 làn cho mỗi chiều: vạch trắng song song, cách nhau 22px.
        a.setColor(new Color(236,240,242,190));
        a.setStroke(new BasicStroke(1.5f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10,new float[]{13,9},0));
        for (int i=1;i<4;i++) {
            int d=i*VehiclePath.LANE_WIDTH;
            for (int sign : new int[]{-1,1}) {
                int yy=y+sign*d, xx=x+sign*d;
                a.drawLine(0,yy,x-2*h,yy); a.drawLine(x+2*h,yy,w,yy);
                a.drawLine(xx,0,xx,y-2*h); a.drawLine(xx,y+2*h,xx,ht);
            }
        }
        // Vạch dừng nằm NGAY TRƯỚC mép ngã tư, chỉ chắn 4 làn đi vào.
        // Không vẽ các đoạn trắng xuyên qua trung tâm nút giao.
        a.setStroke(new BasicStroke(4)); a.setColor(Color.WHITE);
        int edge=2*h, margin=7;
        a.drawLine(x-edge-margin,y+4,x-edge-margin,y+edge-5); // Từ Tây đi Đông
        a.drawLine(x+edge+margin,y-edge+5,x+edge+margin,y-4); // Từ Đông đi Tây
        a.drawLine(x+4,y+edge+margin,x+edge-5,y+edge+margin); // Từ Nam đi Bắc
        a.drawLine(x-edge+5,y-edge-margin,x-4,y-edge-margin); // Từ Bắc đi Nam
        // Vạch qua đường chỉ nằm ngoài vùng ngã tư.
        for(int k=-2*h;k<2*h;k+=17) {
            a.fillRect(x-2*h-33,y+k,13,10);
            a.fillRect(x+2*h+20,y+k,13,10);
            a.fillRect(x+k,y-2*h-33,10,13);
            a.fillRect(x+k,y+2*h+20,10,13);
        }
        a.setFont(new Font("SansSerif",Font.BOLD,16)); a.setColor(Color.WHITE);
        a.drawString("TÂY",24,y-2*h-25); a.drawString("ĐÔNG",w-92,y-2*h-25);
        a.drawString("BẮC",x+2*h+28,28); a.drawString("NAM",x+2*h+28,ht-26);
        // Mũi tên chỉ làn: ở khoảng cách trước vạch dừng.
        for (Direction d:Direction.values()) for(int lane=0;lane<4;lane++) {
            Point2D.Double p=VehiclePath.at(d,lane,-80);
            double angle=d==Direction.EAST?0:d==Direction.WEST?Math.PI:d==Direction.SOUTH?Math.PI/2:-Math.PI/2;
            Graphics2D b=(Graphics2D)a.create(); b.translate(p.x,p.y); b.rotate(angle);
            b.setColor(new Color(245,245,245,195)); b.setStroke(new BasicStroke(2));
            b.drawLine(-11,0,11,0); b.drawLine(11,0,3,-6); b.drawLine(11,0,3,6); b.dispose();
        }
        // Đèn trên vỉa hè, bên phải hướng đi, có bộ đếm riêng cho mỗi hướng.
        drawLightHorizontal(a,x-2*h-124,y+2*h+25,Direction.EAST);
        drawLightHorizontal(a,x+2*h+25,y-2*h-60,Direction.WEST);
        drawLight(a,x+2*h+30,y+2*h+30,Direction.NORTH);
        drawLight(a,x-2*h-59,y-2*h-101,Direction.SOUTH);
        for(Vehicle v:sim.getVehicles()) drawVehicle(a,v);
        a.dispose();
    }
    /** Đèn trục Đông–Tây nằm ngang, ba bóng từ trái sang phải, giây ở bên phải. */
    private void drawLightHorizontal(Graphics2D a,int x,int y,Direction d) {
        LightState state=sim.getTrafficLightController().getState(d);
        int seconds=sim.getTrafficLightController().getRemainingSeconds(d);
        a.setColor(new Color(24,27,33)); a.fillRoundRect(x,y,119,34,10,10);
        LightState[] states={LightState.RED,LightState.YELLOW,LightState.GREEN};
        Color[] colors={new Color(255,57,57),new Color(255,207,48),new Color(42,231,91)};
        for(int i=0;i<3;i++) {
            a.setColor(state==states[i]?colors[i]:new Color(69,73,77));
            a.fillOval(x+7+i*24,y+8,18,18);
        }
        a.setFont(new Font("SansSerif",Font.BOLD,17));
        a.setColor(state==LightState.RED?new Color(255,100,100):state==LightState.YELLOW?new Color(255,213,71):new Color(92,255,135));
        String count=Integer.toString(Math.max(0,seconds));
        FontMetrics fm=a.getFontMetrics(); a.drawString(count,x+100-fm.stringWidth(count)/2,y+23);
    }
    private void drawLight(Graphics2D a,int x,int y,Direction d) {
        LightState state=sim.getTrafficLightController().getState(d);
        int seconds=sim.getTrafficLightController().getRemainingSeconds(d);
        a.setColor(new Color(24,27,33)); a.fillRoundRect(x,y,30,99,10,10);
        LightState[] states={LightState.RED,LightState.YELLOW,LightState.GREEN};
        Color[] colors={new Color(255,57,57),new Color(255,207,48),new Color(42,231,91)};
        for(int i=0;i<3;i++) {
            a.setColor(state==states[i]?colors[i]:new Color(69,73,77));
            a.fillOval(x+7,y+5+i*21,16,16);
        }
        a.setFont(new Font("SansSerif",Font.BOLD,17));
        a.setColor(state==LightState.RED?new Color(255,100,100):state==LightState.YELLOW?new Color(255,213,71):new Color(92,255,135));
        String count=Integer.toString(Math.max(0,seconds));
        FontMetrics fm=a.getFontMetrics(); a.drawString(count,x+(30-fm.stringWidth(count))/2,y+91);
    }
    private void drawVehicle(Graphics2D a,Vehicle v) {
        Point2D.Double p=VehiclePath.point(v,sim.getRenderPosition(v));
        Graphics2D b=(Graphics2D)a.create(); b.translate(p.x,p.y);
        b.rotate(VehiclePath.angle(v,sim.getRenderPosition(v)));
        int length=v instanceof Bus?43:v instanceof Motorcycle?17:29;
        int width=v instanceof Motorcycle?10:15;
        b.setColor(Color.BLACK); b.fillRoundRect(-length/2-1,-width/2-1,length+2,width+2,5,5);
        b.setColor(v.getColor()); b.fillRoundRect(-length/2,-width/2,length,width,5,5);
        b.setColor(new Color(22,33,44,170)); b.fillRect(0,-width/2+3,Math.max(3,length/5),width-6);
        b.setColor(Color.YELLOW); b.fillRect(length/2-2,-width/2+2,2,3);
        b.fillRect(length/2-2,width/2-5,2,3); b.dispose();
    }
}
