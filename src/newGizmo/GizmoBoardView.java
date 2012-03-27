package newGizmo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import newGizmo.controller.EventListener;
import newGizmo.model.AbstractGizmoModel;
import newGizmo.model.GizmoBall;
import newGizmo.model.GizmoBoard;

public class GizmoBoardView extends Canvas {

	public GizmoBoardView() {
		
		long ltime = GizmoSettings.getInstance().getScreenRefreshRate();
		GizmoDriver.getInstance().runShudledTask(new GizmoUpdateViewTask(),
				1000, ltime);
	}

	private class GizmoUpdateViewTask extends GizmoDriver.GizmoTask {
		public GizmoUpdateViewTask() {
			GizmoDriver.getInstance().super();
		}

		@Override
		public void onRun(GizmoBoard board) {
			switch (getCurentDriverState())
			{
			case RUN_STATE: 
				break;
			}
			paint(getGraphics());

		}

	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 600, 600);
		g.setColor(Color.BLACK);
		GizmoBall ball = GizmoBoard.getInstance().getBall();



		
	
		
		if (ball != null)
			ball.paint(g);

		for (AbstractGizmoModel giz : GizmoBoard.getInstance().getGizmos()) {
			giz.paint(g);
		}

	}

}
