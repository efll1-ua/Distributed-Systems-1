package main;

import java.io.Serializable;

public class DatosJugador implements Serializable{
	private String alias;
	private String password;
	private int nivel;
	private int ec;
	private int ef;
	public DatosJugador(String alias, String password, int nivel, int ec, int ef) {
		super();
		this.alias = alias;
		this.password = password;
		this.nivel = nivel;
		this.ec = ec;
		this.ef = ef;
	}
	
	@Override
	public String toString() {
		return "DatosJugador [alias=" + alias + ", password=" + password + ", nivel=" + nivel + ", ec=" + ec + ", ef="
				+ ef + "]";
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getEc() {
		return ec;
	}
	public void setEc(int ec) {
		this.ec = ec;
	}
	public int getEf() {
		return ef;
	}
	public void setEf(int ef) {
		this.ef = ef;
	}
}
