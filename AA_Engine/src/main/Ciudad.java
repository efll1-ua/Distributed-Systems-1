package main;

import java.io.Serializable;

public class Ciudad implements Serializable {
	public String ciudad;
	public int temperatura;
	
	public Ciudad() {}
	
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public int getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(int temperatura) {
		this.temperatura = temperatura;
	}

	@Override
	public String toString() {
		return "Ciudad [ciudad=" + ciudad + ", temperatura=" + temperatura + "]";
	}
}
