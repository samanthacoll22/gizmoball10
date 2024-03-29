package newGizmo.model;

import java.awt.Color;
import java.awt.Graphics;

import newGizmo.GizmoDriver;
import newGizmo.GizmoSettings;
import newGizmo.SavleLoadable;
import newGizmo.Utils;
import newGizmo.model.AbstractGizmoModel.DeactivateTask;
import newGizmo.model.AbstractGizmoModel.onColisionTimeTask;
import physics.Circle;
import physics.Geometry; 
import physics.LineSegment;
import physics.Vect;

public class SquereGizmo extends AbstractGizmoModel implements SavleLoadable {
	private final int L = GizmoSettings.getInstance().getGizmoL();

	LineSegment squareLines[] = new LineSegment[4];
	Circle corner1 = null;
	Circle corner2 = null;
	Circle corner3 = null;
	Circle corner4 = null;
	public SquereGizmo(int x, int y) {
		super(x, y);
		setBoundaryBox();
	}

	/**
	 * setup the boundary lines around the box for find the time to collisions
	 * and reflect the ball as needed
	 */
	public void setBoundaryBox() {
		squareLines[0] = new LineSegment(x, y, x + L, y);
		squareLines[1] = new LineSegment(x + L, y, x + L, y + L);
		squareLines[2] = new LineSegment(x + L, y + L, x, y + L);
		squareLines[3] = new LineSegment(x, y + L, x, y);
		corner1 = new Circle(x, y, 0);
		corner2 = new Circle(x, y + L, 0);
		corner3 = new Circle(x + L, y, 0);
		corner4 = new Circle(x + L, y + L, 0);
	}

	private static final Color gizmoColor = GizmoSettings.getInstance()
			.getSquereGizmoColor();
	private static final Color gizmoActivColor = GizmoSettings.getInstance()
			.getSqureGizmoActivatedColor();
	private Color curent = gizmoColor;

	@Override
	public Graphics paint(Graphics g) {

		g.setColor(curent);
		g.fillRect((int) x, (int) y, L,L);
		return g;

	}

	@Override
	public void update(double dtime) {
		// nohing

	}

	@Override
	public void onActivationEvent() {
		curent = gizmoActivColor;
		activateLinkedGizmos();
	}

	@Override
	public void onDeactivationEvent() {
		curent = gizmoColor;
		deactivateLinkedGizmos();

	}

	@Override
	public double timeToColision(GizmoBall ball) {
		double tempTime = Double.POSITIVE_INFINITY;
		LineSegment templine = squareLines[0];
		double time = 0;

		for (LineSegment l : squareLines) {
			time = Geometry.timeUntilWallCollision(l, ball.getShape(),
					ball.getVolecity());

			// if(time<0.00000001){
			// LineSegment linesegment = (LineSegment) l;
			// Vect velocity = Geometry.reflectWall(linesegment,
			// ball.getVolecity(), 0.75);
			//
			// ball.setVelocity(velocity);

			if (tempTime > time) { 
				templine = l;
				tempTime = time;
			}
		}

		// when time to collisions is less them tiem tick run timeTask on exacly
		// colision time

		if (!isReflecting)
			if (tempTime < GizmoSettings.getInstance()
					.getBallMovementUpdateDtime()) {
				isReflecting = true;
				long msec = Utils.Sec2Msec(tempTime);
				// update ball position on hit moment
				GizmoDriver.getInstance().runTask(ball.newTask(tempTime), msec);
				// run onHit method of gizmo on hit time
				GizmoDriver.getInstance().runTask(
						new onColisionTimeTask(templine), msec);
			}
		return tempTime;
	}

	// return ;

	@Override
	public void onColisionTime(GizmoBall ball, Object o) {
		isReflecting = false;
		if (o instanceof LineSegment) {
			LineSegment linesegment = (LineSegment) o;
			Vect velocity = Geometry.reflectWall(linesegment,
					ball.getVolecity(), 0.75);

			ball.setVelocity(velocity);

		}

	}

	@Override
	public String getDescription() {

		String retstr = "";
		// retstr += "Name: " + name + "\n";
		retstr += "Type: Square\n";
		retstr += "Position: (" + (x / GizmoSettings.getInstance().getGizmoL())
				+ "," + (y / GizmoSettings.getInstance().getGizmoL()) + ")\n";
		retstr += "Connects to:";
		// if (triggers.isEmpty())
		// retstr += " (none)";
		return retstr;
	}

	@Override
	public String getSaveString() {
		return "Square " + name + " "
				+ ((int)x) + " "
				+ ((int)y);
	}

	@Override
	public String getType() {
		return "Square";
	}
	
	@Override
	public String getName(){
		return name;
	}
}
