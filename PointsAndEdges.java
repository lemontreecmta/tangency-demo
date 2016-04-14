
import objectdraw.*;
import java.awt.*;

public class PointsAndEdges {

	Line[] edges;
	FilledOval[] points;

	public PointsAndEdges(FilledOval[] p, Line[] e){
		points = p;
		edges = e;
		}

	public Line[] getEdges(){
		return edges;
	}

	public FilledOval[] getPoints(){
		return points;
	}

}
