

import objectdraw.*;
import java.awt.*;

public class Polygon{

	int OFFSET_SIZE = 10; //SIZE of individual points in polygon

	public Location[] loc; //location of vertex
	public PointsAndEdges pol_pe;

	public Polygon(Location[] input) {
		loc = input;	//keep track of vertices
	
	}

	public void display(DrawingCanvas canvas) { //draw a new polygon based upon the input locations

		int noe = this.loc.length;
		Line[] edges = new Line[noe];
		FilledOval[] points = new FilledOval[noe];
	
		for (int i = 0; i< (noe - 1); i++) {
		
			points[i] = new FilledOval(this.loc[i].getX() - OFFSET_SIZE/2, this.loc[i].getY() - OFFSET_SIZE/2, OFFSET_SIZE, OFFSET_SIZE, canvas); 
			//keep track of a filled oval representing the vertex		 
			edges[i] = new Line(this.loc[i].getX(), this.loc[i].getY(), this.loc[i+1].getX(), this.loc[i+1].getY(), canvas);// create an edge
		}

		points[noe - 1] = new FilledOval(this.loc[noe - 1].getX() - OFFSET_SIZE/2, this.loc[noe - 1].getY() - OFFSET_SIZE/2, OFFSET_SIZE, OFFSET_SIZE, canvas);
		edges[noe - 1] = new Line(this.loc[noe - 1].getX(), this.loc[noe - 1].getY(), this.loc[0].getX(), this.loc[0].getY(), canvas);

		PointsAndEdges pe = new PointsAndEdges(points, edges);
		this.pol_pe = pe;		
		//this.loc = loc;
	}

	public void remove() { //remove the drawing from canvas
		for (int i = 0; i < this.loc.length; i++) {		
			this.pol_pe.getEdges()[i].removeFromCanvas();
			this.pol_pe.getPoints()[i].removeFromCanvas();
		}
	}

	public Location getCenter() {
	//get center 
		double x = 0;
		double y = 0;
		for (int i = 0; i < this.loc.length; i++) {
			x += this.loc[i].getX();	
			y += this.loc[i].getY();
		}
			x = x/this.loc.length;	
			y = y/this.loc.length;
		Location center = new Location(x, y);
		return center;
	}

	public void rotate(double theta) {
		for (int i = 0; i<this.loc.length; i++) {
			double x = this.loc[i].getX();
			double y = this.loc[i].getY();
			double newx = x * Math.cos(theta) - y * Math.sin(theta);
			double newy = x * Math.sin(theta) + y * Math.cos(theta);
			this.loc[i] = new Location(newx, newy);
		}
	}

	public void resize(double scale) {
		
		double x_origin = this.getCenter().getX();
		double y_origin = this.getCenter().getY();

		for (int i = 0; i<this.loc.length; i++) {
			double newx = scale * (this.loc[i].getX() - x_origin) + x_origin;
			double newy = scale * (this.loc[i].getY() - y_origin) + y_origin;
			this.loc[i] = new Location(newx, newy);
		}
	}
}

	//RESIZE
	//input: a polygon, a scale number
	//output: none (scaled polygon)
	//need work
	/*
	public void resize(double scale) {

		double x_origin = this.getCenter().getX();
		double y_origin = this.getCenter().getY();

		for (int i = 0; i<this.noe; i++) {
				
			double newx1 = scale * (this.x[i] - x_origin) + x_origin;
			double newy1 = scale * (this.y[i] - y_origin) + y_origin;
			double newx2 = scale * (this.x[i+1] - x_origin) + x_origin;
			double newy2 = scale * (this.x[i+1] - x_origin) + x_origin;
			edges[i].setStart(newx1, newy1);
			edges[i].setEnd(newx2, newy2);
		}

	}


	}
	*/
	/*

	//CHANGE ANGLE (USING MOUSE)

	//TANGENCY

	//input: two polygons 
	//output: create line of all tangent across two polygons

	public void lines_through_edges(Polygon A, Polygon B, canvas aCanvas) {

	for (int i = 0; i<A.noe; i++) {
	for (int j = 0; j<B.noe; j++) {
	double a_x = A.x[i];
	double a_y = A.y[i];
	double b_x = B.x[i];
	double b_y = B.y[i];	
	line = new Line(0, (y2 - y1) / (x2 - x1) * x1 - y1, 1000, (y2 - y1) / (x2 - x1) * (1000 + x1) - y1, aCanvas); //calculate x = 0 and x = 10000 point
	//condition for line intersection: if (intersect(line, ))	
	}
	}
	}
	//create all Line
	//eliminate the one which goes through one or more

	//write a ray object, which takes two points and return a ray that goes through canvas
	//write a method that checks if 


} */
