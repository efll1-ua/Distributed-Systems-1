package main;

public class Parametros {
	private String direccionEngine;
	private int puertoEngine;
	private String direccionKafka;
	private int puertoKafka;
	private String direccionRegistry;
	private int puertoRegistry;
	
	private static Parametros instance;
	
	
	public Parametros() {
		
	}
	
	public String getDireccionRegistry() {
		return direccionRegistry;
	}



	public void setDireccionRegistry(String direccionRegistry) {
		this.direccionRegistry = direccionRegistry;
	}



	public int getPuertoRegistry() {
		return puertoRegistry;
	}



	public void setPuertoRegistry(int puertoRegistry) {
		this.puertoRegistry = puertoRegistry;
	}



	public static Parametros getInstance() {
		if(instance == null) {
			instance = new Parametros();
		}
		return instance;
	}

	public String getDireccionEngine() {
		return direccionEngine;
	}

	public void setDireccionEngine(String direccionEngine) {
		this.direccionEngine = direccionEngine;
	}

	public int getPuertoEngine() {
		return puertoEngine;
	}

	public void setPuertoEngine(int puertoEngine) {
		this.puertoEngine = puertoEngine;
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
