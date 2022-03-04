package application;

public class KineticPolygonFactory extends KineticBodyFactory {

	@Override
	protected KineticBody createKineticBody() {
		return new KineticPolygon();
	}

	@Override
	protected KineticBody createKineticBody(KineticBody parent) {
		return new KineticPolygon((KineticPolygon) parent);
	}

}
