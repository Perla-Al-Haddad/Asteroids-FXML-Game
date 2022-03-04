package application;

public class KineticLaserFactory extends KineticBodyFactory {

	@Override
	protected KineticBody createKineticBody() {
		return new KineticLaser("\\images\\laser.png");
	}

	@Override
	protected KineticBody createKineticBody(KineticBody parent) {
		return null;
	}

}
