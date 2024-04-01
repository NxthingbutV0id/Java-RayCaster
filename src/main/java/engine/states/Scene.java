package engine.states;

public interface Scene {
	void init() throws Exception;
	
	void update(double deltaT);
	
	void render() throws Exception;
}
