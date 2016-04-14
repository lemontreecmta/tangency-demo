
import objectdraw.*;
import java.awt.*;

public class DoubleTangencyDemo2 extends WindowController{

public static void main (String[] args ) { 
	new DoubleTangencyDemo2().startController (900, 900);
	}

private Polygon X;
private boolean OnPointX = false;
private Location lastPoint;
private int index_x;
private Line[] ray;
private Text c, same, opp, inf;

public void begin() {
	
	Location[] input = new Location[8];
	input[0] = new Location(100, 100);
	input[1] = new Location(150, 50);
	input[2] = new Location(200, 100);
	input[3] = new Location(200, 200);
	input[4] = new Location(150, 250);
	input[5] = new Location(100, 200);
	input[6] = new Location(150, 300);
	input[7] = new Location(90, 90);
	
	X = new Polygon(input);
	X.display(canvas);
	Tuple result = connect_self_tangencies(X, canvas);
	//Tuple result = connect_tangencies(X, Y, canvas);
	c = new Text("C(x) = " + pol_self_intersection(X), 10, 10, canvas);
	opp = new Text("I(x) = " + (int) result.getY(), 10, 25, canvas);
	same = new Text("II(x) = " + (int) result.getX(), 10, 40, canvas);
	inf = new Text("F(x)/2 = " + (int) inflection_edge(X)/2, 10, 55, canvas);
	}



public void onMousePress(Location point) {
	lastPoint = point;
	for (int i = 0; i < X.loc.length; i++) {
		if (X.pol_pe.getPoints()[i].contains(point)) {
			OnPointX = true;
			index_x = i; // keep index			
			i = X.loc.length + 1; //get out of loop
		}
	}

}

public void onMouseDrag(Location point) {
		
	if (OnPointX) {
		X.remove();
		remove_all_connections();
		X.loc[index_x] = point;
		X.display(canvas);
		
		Tuple result = connect_self_tangencies(X, canvas);
		c.setText("C(x) = " + pol_self_intersection(X));
		opp.setText("I(x) = " + (int) result.getY());
		same.setText("II(x) = " + (int) result.getX());
		inf.setText("F(x)/2 = " + (int) inflection_edge(X)/2);
					
		lastPoint = point;

	}

//	Tuple result = connect_tangencies(X, Y, canvas);	
//	c.setText("C(x, y) = " + (int) pol_intersection(X, Y));
//	opp.setText("I(x, y) = " + (int) result.getY());
//	same.setText("II(x, y) = " + (int) result.getX());
	
			
}
/*
21
31 32
41 42 43
51 52 53 54
61 62 63 64 65
*/

//FIND I(X) and II(X)

public Tuple connect_self_tangencies(Polygon X, DrawingCanvas canvas) {

	int count = 0;
	int same_side = 0;
	int opp_side = 0;

	ray = new Line[(int) Math.floor(X.loc.length * X.loc.length / 2)];

	for (int i = 1; i < X.loc.length; i++) {
		for (int j = 0; j < (i - 1); j++) {

				ray[count] = new Line(X.loc[i], X.loc[j], canvas);
				double za1 = z_length(X.loc[next(i, X.loc.length - 1)], ray[count]);
				double za2 = z_length(X.loc[prev(i, X.loc.length - 1)], ray[count]);
				double zb1 = z_length(X.loc[next(j, X.loc.length - 1)], ray[count]);
				double zb2 = z_length(X.loc[prev(j, X.loc.length - 1)], ray[count]);

				if ((za1 * za2 > 0) && (zb1 * zb2 > 0)) { //double tangencies
					if (za1 * zb1 > 0) { 
						ray[count].setColor(Color.GREEN);//same side tangency 
						same_side++;
					}
					else if (za1 * zb1 < 0) {
						ray[count].setColor(Color.RED);// opposite side tangency
						opp_side++;
					}
				}
				else {
					ray[count].hide(); 
				}
				count++;
		}
	}

	Tuple result = new Tuple(same_side, opp_side);
	return result;

}


//FIND C(X)

public boolean intersect(Line line1, Line line2) {
	Location X1 = line1.getStart();
	Location Y1 = line1.getEnd();
	Location X2 = line2.getStart();
	Location Y2 = line2.getEnd();
	return ((z_length(X1, line2) * z_length(Y1, line2) < 0) && (z_length(X2, line1) * z_length(Y2, line1) < 0));
}

public int pol_self_intersection(Polygon X) {
	int count = 0;
	for (int i = 1; i < X.loc.length; i++) {
		for (int j = 0; j < i; j++) {
			if (intersect(X.pol_pe.getEdges()[i], X.pol_pe.getEdges()[j])) count++; 
		}
	}
	return count;
}


//FIND INFLECTION EDGES F(X)

public int inflection_edge(Polygon X) {
	int count = 0;
	int limit = X.loc.length - 1;
	for (int i = 0; i < X.loc.length; i++) {
		Line l = X.pol_pe.getEdges()[i];
		if (z_length(X.loc[prev(i, limit)], l) * z_length(X.loc[next(next(i, limit), limit)], l) < 0) count++;//inflection point because two adjacent points of edges are on opposite side	
	}
	return count;
}

//FILLER METHODS
//cross product of two vector - negative on 1 side, positive on the other

public double z_length(Location A, Line L) {
	double x_a = A.getX();
	double y_a = A.getY();
	double x_1 = L.getStart().getX();
	double y_1 = L.getStart().getY();
	double x_2 = L.getEnd().getX();
	double y_2 = L.getEnd().getY();
	double z = (x_a - x_2) * (y_a - y_1) - (x_a - x_1) * (y_a - y_2);
	return z;
}


public void remove_all_connections() {

		for (int i = 0; i<ray.length; i++) {
				if(ray[i] != null) {
					ray[i].removeFromCanvas();
				}		
		}	
	}

public int next(int i, int limit) {
	if (i < limit) return (i + 1);
	else return 0;
}

public int prev(int i, int limit) {
	if (i > 0) return (i - 1);
	else return limit;
}

}



