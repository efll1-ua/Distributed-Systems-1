package main;

import java.io.Serializable;

public class SolicitudTemperatura implements Serializable{
	private String ciudad;
	
	public SolicitudTemperatura() {}
	
	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
}
