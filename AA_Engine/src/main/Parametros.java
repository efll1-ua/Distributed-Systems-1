package main;

public class Parametros {
	private int puertoServidorAutenticaciones;
	private int maxJugadores;
	private int puertoWeather;
	private String direccionKafka;
	private int puertoKafka;
	private String direccionWeather;
	
	private static Parametros instance;
	
	
	public Parametros() {
		
	}

	
	public String getDireccionWeather() {
		return direccionWeather;
	}



	public void setDireccionWeather(String direccionWeather) {
		this.direccionWeather = direccionWeather;
	}



	public static Parametros getInstance() {
		if(instance == null) {
			instance = new Parametros();
		}
		return instance;
	}

	public int getPuertoServidorAutenticaciones() {
		return puertoServidorAutenticaciones;
	}

	public void setPuertoServidorAutenticaciones(int puertoServidorAutenticaciones) {
		this.puertoServidorAutenticaciones = puertoServidorAutenticaciones;
	}

	public int getMaxJugadores() {
		return maxJugadores;
	}

	public void setMaxJugadores(int maxJugadores) {
		this.maxJugadores = maxJugadores;
	}

	public int getPuertoWeather() {
		return puertoWeather;
	}

	public void setPuertoWeather(int puertoWeather) {
		this.puertoWeather = puertoWeather;
	}

	public String getDireccionKafka() {
		return direccionKafka;
	}

	public void setDireccionKafka(String direccionKafka) {
		this.direccionKafka = direccionKafka;
	}

	public int getPuertoKafka() {
		return puertoKafka;
	}

	public void setPuertoKafka(int puertoKafka) {
		this.puertoKafka = puertoKafka;
	}
}
