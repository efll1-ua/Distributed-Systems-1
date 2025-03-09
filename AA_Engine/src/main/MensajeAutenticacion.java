package main;

import java.io.Serializable;

public class MensajeAutenticacion implements Serializable{
	private boolean autenticado;
	private int id;
	private int ef, ec;
	private int nivel;
	private String errorDescrip;

	public MensajeAutenticacion(boolean autenticado) {
		super();
		this.autenticado = autenticado;
	}

	public String getErrorDescrip() {
		return errorDescrip;
	}


	public void setErrorDescrip(String errorDescrip) {
		this.errorDescrip = errorDescrip;
	}


	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}



	public int getEf() {
		return ef;
	}

	public void setEf(int ef) {
		this.ef = ef;
	}


	public int getEc() {
		return ec;
	}



	public void setEc(int ec) {
		this.ec = ec;
	}



	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
