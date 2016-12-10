package com.visellico.Physics;

import com.visellico.util.MathUtils;

public class Physics {

	private double acceleration;
	private double velocity;
	private double position;
	
	private double maxVelocity;
	
//	private double initialTime;
//	private double deltaTime;
	
	public Physics(double pos) {
		position = pos;
	}
	
	//TODO determine if position etc. should be done with delta position, which is then added onto the current position.
	//My thinking is this: If we do it with delta position, then start position can be always 0
	public void calcPosition() {
		
		position = position + velocity;	//Crap this isn't going to work as I wanted it to, because position is more nuanced.
			//This also completely ignores collision which is no bueno
	}
	
	public void calcVelocity() {
		//This should for all purposes work as we want it to, but my fear is that since we arent updating physics constantly and we'll need to limit it to a time step anyway, then
		//	we need to keep track of time, and might as well use Newtonian calculations.
		velocity = velocity + acceleration;
		
		velocity = MathUtils.clamp(velocity, -maxVelocity, maxVelocity);
	}

	//When no more acceleration is applied.
	public void halt() {
		// "Deceleration" is half that of acceleration- Makes it less abrupt, the parabola gets wider as time goes on.
		if (velocity/acceleration > 0) acceleration = -(acceleration / 2);
		if (Math.abs(velocity) - Math.abs(acceleration) < 0) {
			velocity = 0;
			acceleration = 0;
		}
	}
	
	//more dramatic, immediately stops movement.
	public void stop() {
		velocity = 0;
	}
	
	public void update() {
		calcVelocity();
		calcPosition();
		
	}
	
	public double getPosition() {
		return position;
	}
	
	public void setAccelleration(Double acc) {
		acceleration = acc;
	}
	
	public void setVelocity(Double vel) {
		velocity = vel;
	}
	
	public void setPosition(Double pos) {
		position = pos;
	}
	
	public void setMaxSpeed(double maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	
}
