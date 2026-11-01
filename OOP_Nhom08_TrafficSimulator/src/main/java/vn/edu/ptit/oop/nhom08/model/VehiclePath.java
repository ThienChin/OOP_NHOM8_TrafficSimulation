package vn.edu.ptit.oop.nhom08.model;
import java.awt.geom.Point2D;
/** Bản đồ 900x700, đi bên phải; 4 làn mỗi chiều, 44px/làn. */
public final class VehiclePath {
    public static final int W=1200,H=760,CX=600,CY=380,LANE_WIDTH=44,ROAD_HALF=88;
    private VehiclePath() {}
    public static double entry(Direction d) { return (d==Direction.EAST || d==Direction.WEST) ? 340 : 165; }
    public static int assignedLane(Vehicle v) {
        Direction from=v.getDirection(), to=v.getDestination();
        if(to==from) return v.getLane()==2?2:1;
        boolean left=(from==Direction.EAST&&to==Direction.NORTH)||(from==Direction.WEST&&to==Direction.SOUTH)
            ||(from==Direction.NORTH&&to==Direction.WEST)||(from==Direction.SOUTH&&to==Direction.EAST);
        return left?0:3;
    }
    public static Point2D.Double at(Direction d,int lane,double along) {
        double offset=(lane+.5)*LANE_WIDTH;
        switch(d) {
            case EAST: return new Point2D.Double(CX-2*ROAD_HALF+along,CY+offset);
            case WEST: return new Point2D.Double(CX+2*ROAD_HALF-along,CY-offset);
            case NORTH: return new Point2D.Double(CX+offset,CY+2*ROAD_HALF-along);
            default: return new Point2D.Double(CX-offset,CY-2*ROAD_HALF+along);
        }
    }
    private static Point2D.Double start(Vehicle v) {return at(v.getDirection(),v.getLane(),0);}
    private static Point2D.Double end(Vehicle v) {return at(v.getDestination(),v.isTurning()?1:v.getLane(),ROAD_HALF*4);}
    private static Point2D.Double control(Point2D.Double a,Point2D.Double b,Direction from) {
        return (from==Direction.EAST||from==Direction.WEST)?new Point2D.Double(b.x,a.y):new Point2D.Double(a.x,b.y);
    }
    private static Point2D.Double bezier(Point2D.Double a,Point2D.Double c,Point2D.Double b,double t) {
        double u=1-t;return new Point2D.Double(u*u*a.x+2*u*t*c.x+t*t*b.x,u*u*a.y+2*u*t*c.y+t*t*b.y);
    }
    public static double curveLength(Vehicle v) {
        if(!v.isTurning())return ROAD_HALF*4;
        Point2D.Double a=start(v),b=end(v),c=control(a,b,v.getDirection()),prev=a;
        double length=0;for(int i=1;i<=80;i++){Point2D.Double q=bezier(a,c,b,i/80.0);length+=prev.distance(q);prev=q;}return length;
    }
    public static Point2D.Double point(Vehicle v,double progress) {
        double e=entry(v.getDirection());
        if(progress<=e)return at(v.getDirection(),v.getLane(),progress-e);
        double len=curveLength(v);
        if(progress>=e+len)return at(v.getDestination(),v.isTurning()?1:v.getLane(),ROAD_HALF*4+progress-e-len);
        if(!v.isTurning())return at(v.getDirection(),v.getLane(),progress-e);
        Point2D.Double a=start(v),b=end(v),c=control(a,b,v.getDirection()),prev=a;
        double covered=0,wanted=progress-e;
        for(int i=1;i<=100;i++) {Point2D.Double q=bezier(a,c,b,i/100.0);double seg=prev.distance(q);
            if(covered+seg>=wanted){double r=(wanted-covered)/Math.max(seg,0.00001);return new Point2D.Double(prev.x+(q.x-prev.x)*r,prev.y+(q.y-prev.y)*r);}covered+=seg;prev=q;}
        return b;
    }
    public static double angle(Vehicle v,double progress){Point2D.Double a=point(v,progress-2),b=point(v,progress+2);return Math.atan2(b.y-a.y,b.x-a.x);}
    public static double exit(Vehicle v){return entry(v.getDirection())+curveLength(v);}
}
