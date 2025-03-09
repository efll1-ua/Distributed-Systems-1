package main;

import java.io.Serializable;

public class RespuestaEditarPerfil implements Serializable {
	boolean correcto;
	String errorDescription;
	
	public RespuestaEditarPerfil(boolean correcto, String errorDescription) {
		super();
		this.correcto = correcto;
		this.errorDescription = errorDescription;
	}
	public boolean isCorrecto() {
		return correcto;
	}
	public void setCorrecto(boolean correcto) {
		this.correcto = correcto;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}	
	
}
