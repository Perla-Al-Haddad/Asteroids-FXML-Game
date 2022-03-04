package application;

public class KineticPlayerFactory extends KineticBodyFactory {

	@Override
	protected KineticBody createKineticBody() {
		return new KineticPlayer("\\images\\player.png");
	}

	@Override
	protected KineticBody createKineticBody(KineticBody parent) {
		return null;
	}

}
