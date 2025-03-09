package main;

import java.io.Serializable;

public class EditarPerfil implements Serializable{
	private String alias = null;
	private String passw = null;

	private String nuevoAlias = null;
	private String nuevoPassw = null;
	private int nuevoNivel = 0;
	private int nuevoEf = 0; 
	private int nuevoEc = 0;
	
	public EditarPerfil(String alias, String passw, String nuevoAlias, String nuevoPassw, int nuevoNivel, int nuevoEf,
			int nuevoEc) {
		super();
		this.alias = alias;
		this.passw = passw;
		this.nuevoAlias = nuevoAlias;
		this.nuevoPassw = nuevoPassw;
		this.nuevoNivel = nuevoNivel;
		this.nuevoEf = nuevoEf;
		this.nuevoEc = nuevoEc;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassw() {
		return passw;
	}
	public void setPassw(String passw) {
		this.passw = passw;
	}
	public String getNuevoAlias() {
		return nuevoAlias;
	}
	public void setNuevoAlias(String nuevoAlias) {
		this.nuevoAlias = nuevoAlias;
	}
	public String getNuevoPassw() {
		return nuevoPassw;
	}
	public void setNuevoPassw(String nuevoPassw) {
		this.nuevoPassw = nuevoPassw;
	}
	public int getNuevoNivel() {
		return nuevoNivel;
	}
	public void setNuevoNivel(int nuevoNivel) {
		this.nuevoNivel = nuevoNivel;
	}
	public int getNuevoEf() {
		return nuevoEf;
	}
	public void setNuevoEf(int nuevoEf) {
		this.nuevoEf = nuevoEf;
	}
	public int getNuevoEc() {
		return nuevoEc;
	}
	public void setNuevoEc(int nuevoEc) {
		this.nuevoEc = nuevoEc;
	}
	
	
}
