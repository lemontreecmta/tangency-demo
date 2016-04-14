import objectdraw.*;
import java.awt.*;

public class PolygonDemo extends WindowController{

public static void main (String[] args ) { 
	new PolygonDemo().startController (900, 900);
	}

private Polygon X;
private boolean OnPointX = false;
private boolean doubleClick = false;
private int index_x; //index of vertex of interaction
private Line[][] ray;
private Location[] input;
private Text no_edges;



public void begin() {
	
	input = new Location[3];
	input[0] = new Location(100, 100);
	input[1] = new Location(150, 50);
	input[2] = new Location(200, 100);
	
	X = new Polygon(input);
	X.display(canvas);
	no_edges = new Text("Vertices: " + X.loc.length, 10, 10, canvas);	
	}


public void onMousePress(Location point) {

	boolean clickOnPoint = false;
		for (int i = 0; i < X.loc.length; i++) {
			if (X.pol_pe.getPoints()[i].contains(point)) { //if this clicks on one vertex

				if (doubleClick && index_x == i) { //if this is the second click
					Location[] adjusted_input = addOneVertex(input, index_x);
					input = adjusted_input;
					X.remove();
					X = new Polygon(input);
					no_edges.setText("Vertices: " + X.loc.length);
					X.display(canvas);

				}
				else if (!doubleClick) { //if this is only the first click
					doubleClick = true;	
					index_x = i; // keep index
				
				}

				clickOnPoint = true;			
				i = X.loc.length + 1; //get out of loop
			}
		}

	OnPointX = clickOnPoint;

}

public void onMouseDrag(Location point) {
		
	if (doubleClick) { //if it is a double click, add a new vertex and do nothing else
		X.remove();
		X.loc[index_x] = point;
		X.display(canvas);
		no_edges.setText("Vertices: " + X.loc.length);
		doubleClick = false;
		}

	else if (OnPointX) { //if this is on point but not a double click, drag the click vertex and check for overlapping

		X.remove();
		X.loc[index_x] = point;
		X.display(canvas);
		no_edges.setText("Vertices: " + X.loc.length);
		doubleClick = false;

		for (int i = 0; i < X.loc.length; i++) { 
			if (X.pol_pe.getPoints()[i].contains(point) && (index_x != i)) { //if they are overlapping vertices
				Location[] adjusted_input = subOneVertex(input, index_x);
				input = adjusted_input;			
				X.remove();
				X = new Polygon(input);
				X.display(canvas);
				no_edges.setText("Vertices: " + X.loc.length);

				//reset index
				int copy_i = i;
				if (i < index_x) index_x = copy_i;
				else index_x = copy_i - 1;

				i = X.loc.length + 1;			
			}
		}
	
		}
	}

public Location[] addOneVertex(Location[] input, int index_x) {
	Location[] adjusted_input = new Location[input.length + 1];
	System.arraycopy(input, 0, adjusted_input, 0, index_x + 1); //copy from 0 to index_x (total index_x + 1 item)
	adjusted_input[index_x + 1] = input[index_x]; //location index_x + 1 is a new point
		
	if (input.length > index_x + 1) {
		System.arraycopy(input, index_x + 1, adjusted_input, index_x + 2, input.length - index_x - 1);
		}
	return adjusted_input;
}


public Location[] subOneVertex(Location[] input, int index_x) {

	Location[] adjusted_input = new Location[input.length - 1];
	System.arraycopy(input, 0, adjusted_input, 0, index_x); 
	
	if (input.length > (index_x + 1)) {
		System.arraycopy(input, index_x + 1, adjusted_input, index_x, input.length - index_x - 1);
		}
	return adjusted_input;
}

}