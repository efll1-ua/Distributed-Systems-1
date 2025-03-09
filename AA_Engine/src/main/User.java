package main;

import java.io.Serializable;

public class User implements Serializable{
	private String userName;
	private String contraseña;
	private int ef;
	private int ec;
	private int nivel;
	private boolean npc = false;
	public User() {}
	
	public User(String userName, String contraseña, boolean npc) {
		super();
		this.userName = userName;
		this.contraseña = contraseña;
		this.npc = npc;
	}
	
	public boolean isNpc() {
		return npc;
	}

	public void setNpc(boolean npc) {
		this.npc = npc;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	
}
