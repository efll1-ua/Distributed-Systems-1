package main;

public class Parametros {
	private int puerto;

	private static Parametros instance;
	
	
	public Parametros() {
		
	}
	
	public static Parametros getInstance() {
		if(instance == null) {
			instance = new Parametros();
		}
		return instance;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
}
