package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.DVrules;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.State;

public class SubspaceStateUOp {
	private State state;
	private String userOp;
	
	public State getState() {
		return this.state;
	}
	public void setState(State s) {
		this.state = s;
	}
	public String getUserOp() {
		return this.userOp;
	}
	public void setUserOp(String uo) {
		this.userOp = uo;
	}
}
