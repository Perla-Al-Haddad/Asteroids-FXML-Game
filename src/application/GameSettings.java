package application;

public class GameSettings {
    public final static Integer MAX_LASER_ON_SCREEN = 2;
    public final static Integer MAX_ASTEROIDS_ON_SCREEN = 12;
    public final static Integer MAX_CHILD_ASTEROIDS_NUMBER = 3;
	public final static Integer MAX_NB_OF_LIVES = 3;
	public final static Integer PLAYER_ROTATIONAL_SPEED = 2;
	public final static Integer PLAYER_BOUNDARY_REDUCTION = 25;
	public final static Double PLAYER_FRICTION_EFFECT = 0.98;
	public final static Integer[] PARENT_ASTEROID_RANGE = {35, 65};
	public final static Integer[] CHILD_ASTEROID_RANGE = {20, 35};
	public final static Integer[] PARENT_ASTEROID_SCORE_RANGE = {10, 50};
	public final static Integer[] CHILD_ASTEROID_SCORE_RANGE = {50, 50};
	
	private Double gameWidth = 800.0;
	private Double gameHeight = 600.0;
	
	private boolean isMuted = false;
	
    private static GameSettings INSTANCE = null;

    private GameSettings() {}
    
    public static GameSettings getInstance() {
        if (INSTANCE == null)
            INSTANCE = new GameSettings();
        return INSTANCE;
    }
    
    public Double getGameWidth() {
		return gameWidth;
	}
	public void setGameWidth(Double gameWidth) {
		this.gameWidth = gameWidth;
	}

	public Double getGameHeight() {
		return gameHeight;
	}
	public void setGameHeight(Double gameHeight) {
		this.gameHeight = gameHeight;
	}

	public boolean isMuted() {
		return isMuted;
	}
	public void setMuted(boolean isMuted) {
		this.isMuted = isMuted;
	}

}
