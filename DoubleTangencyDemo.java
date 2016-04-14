
import objectdraw.*;
import java.awt.*;

public class DoubleTangencyDemo extends WindowController{

public static void main (String[] args ) { 
	new DoubleTangencyDemo().startController (900, 900);
	}

private Polygon X, Y;
private boolean OnPointX = false;
private boolean OnPointY = false;
private Location lastPoint;
private int index_x, index_y;
private Line[][] ray;
private Text c, same, opp;

public void begin() {
	
	Location[] input = new Location[5];
	input[0] = new Location(100, 100);
	input[1] = new Location(150, 50);
	input[2] = new Location(200, 100);
	input[3] = new Location(200, 200);
	input[4] = new Location(150, 250);
	//input[5] = new Location(100, 200);
	
	Location[] input2 = new Location[5];
	input2[0] = new Location(600, 300);
	input2[1] = new Location(650, 250);
	input2[2] = new Location(700, 300);
	input2[3] = new Location(700, 400);
	input2[4] = new Location(650, 450);
	//input2[5] = new Location(600, 400);

	X = new Polygon(input);
	Y = new Polygon(input2);
	X.display(canvas);
	Y.display(canvas);
	Tuple result = connect_tangencies(X, Y, canvas);
	c = new Text("C(x, y) = " + pol_intersection(X, Y), 10, 10, canvas);
	opp = new Text("I(x, y) = " + (int) result.getY(), 10, 25, canvas);
	same = new Text("II(x, y) = " + (int) result.getX(), 10, 40, canvas);
	
	}

public void onMousePress(Location point) {
	lastPoint = point;
	for (int i = 0; i < X.loc.length; i++) {
		if (X.pol_pe.getPoints()[i].contains(point)) {
			OnPointX = true;
			OnPointY = false;
			index_x = i; // keep index			
			i = X.loc.length + 1; //get out of loop
		}
	}

	for (int i = 0; i < Y.loc.length; i++) {
		if (Y.pol_pe.getPoints()[i].contains(point)) {
			OnPointY = true;
			OnPointX = false;
			index_y = i; // keep index			
			i = Y.loc.length + 1; //get out of loop
		}
	}
}

public void onMouseDrag(Location point) {
		
	if (OnPointX) {
		X.remove();
		remove_all_connections();
		X.loc[index_x] = point;
		X.display(canvas);
	}
	if (OnPointY) {
		Y.remove();
		remove_all_connections();
		Y.loc[index_y] = point;
		Y.display(canvas);

	}

	Tuple result = connect_tangencies(X, Y, canvas);	
	c.setText("C(x, y) = " + (int) pol_intersection(X, Y));
	opp.setText("I(x, y) = " + (int) result.getY());
	same.setText("II(x, y) = " + (int) result.getX());
	
	lastPoint = point;
			
}

//FIND I(X, Y) and II(X, Y)

public Tuple connect_tangencies(Polygon A, Polygon B, DrawingCanvas canvas) {
	int count = 0;
	int same_side = 0;
	int opp_side = 0;
	//int a = 0;
	ray = new Line[A.loc.length][B.loc.length];

	for (int i = 0; i<A.loc.length; i++) {

			for (int j = 0; j< B.loc.length; j++) {
	
				ray[i][j] = new Line(A.loc[i], B.loc[j], canvas); 

				double za1 = z_length(A.loc[next(i, A.loc.length - 1)], ray[i][j]);
				double za2 = z_length(A.loc[prev(i, A.loc.length - 1)], ray[i][j]);
				double zb1 = z_length(B.loc[next(j, B.loc.length - 1)], ray[i][j]);
				double zb2 = z_length(B.loc[prev(j, B.loc.length - 1)], ray[i][j]);
				
				if ((za1 * za2 > 0) && (zb1 * zb2 > 0)) { //double tangencies
					if (za1 * zb1 > 0) { 
						ray[i][j].setColor(Color.GREEN);//same side tangency 
						same_side++;
					}
					else if (za1 * zb1 < 0) {
						ray[i][j].setColor(Color.RED);// opposite side tangency
						opp_side++;
					}
				}
				else {
					ray[i][j].hide(); 
				}
			}
		}

	Tuple result = new Tuple(same_side, opp_side);
	return result;

	}

//Find C(X, Y)

public boolean intersect(Line line1, Line line2) {
	Location X1 = line1.getStart();
	Location Y1 = line1.getEnd();
	Location X2 = line2.getStart();
	Location Y2 = line2.getEnd();
	return ((z_length(X1, line2) * z_length(Y1, line2) < 0) && (z_length(X2, line1) * z_length(Y2, line1) < 0));
}

public int pol_intersection(Polygon X, Polygon Y) {
	int count = 0;
	for (int i = 0; i < X.loc.length; i++) {
		for (int j = 0; j < Y.loc.length; j++) {
			if (intersect(X.pol_pe.getEdges()[i], Y.pol_pe.getEdges()[j])) count++; 
		}
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



public int next(int i, int limit) {
	if (i < limit) return (i + 1);
	else return 0;
}

public int prev(int i, int limit) {
	if (i > 0) return (i - 1);
	else return limit;
}


public void remove_all_connections() {

		for (int i = 0; i<ray.length; i++) {
			for (int j = 0; j<ray[i].length; j++) {
				if(ray[i][j] != null) {
					ray[i][j].removeFromCanvas();
				}		
			}
		}	

	}

}



