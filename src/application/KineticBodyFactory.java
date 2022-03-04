package application;

public abstract class KineticBodyFactory {

	protected abstract KineticBody createKineticBody();
	protected abstract KineticBody createKineticBody(KineticBody parent);
	
	public KineticBody getKineticBody() {
		KineticBody k = this.createKineticBody();
		return k;
	}
	
	public KineticBody getKineticBody(KineticBody parent) {
		KineticBody k = this.createKineticBody(parent);
		return k;
	}
	
}
