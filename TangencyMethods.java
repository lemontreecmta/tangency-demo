
import objectdraw.*;
import java.awt.*;

public class TangencyMethods extends WindowController{

private Line[] ray;
private Location[] input;
private Text c, same, opp, inf;

//////////////////////////////////////////////
///////////FIND DOUBLE TANGENCIES/////////////
//////////////////////////////////////////////

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

//////////////////////////////////////////////
///////////FIND SELF-INTERSECTION/////////////
//////////////////////////////////////////////

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

//////////////////////////////////////////////
///////////FIND INFLECTION EDGES//////////////
//////////////////////////////////////////////

public int inflection_edge(Polygon X) {
	int count = 0;
	int limit = X.loc.length - 1;
	for (int i = 0; i < X.loc.length; i++) {
		Line l = X.pol_pe.getEdges()[i];
		if (z_length(X.loc[prev(i, limit)], l) * z_length(X.loc[next(next(i, limit), limit)], l) < 0) count++;//inflection point because two adjacent points of edges are on opposite side	
	}
	return count;
}

//////////////////////////////////////////////
///////////FILLER METHODS/////////////////////
//////////////////////////////////////////////

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



