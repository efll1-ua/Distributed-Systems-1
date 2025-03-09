package main;

public class Jugador {
	public int x, y;
	public String alias;
	public int nivel = 1;
	public int ef, ec;
	public boolean muerto = false;
	public int id;
	public boolean npc = false;
	public String anterior;
	public int anteriorx, anteriory;
	public boolean ganador = false;
	public Ciudad ciudad;
	public int nivelEfecto;
	
	public Jugador() {
		
	}
	

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}



	public boolean isGanador() {
		return ganador;
	}

	public void setGanador(boolean ganador) {
		this.ganador = ganador;
	}



	public String getAnterior() {
		return anterior;
	}

	public void setAnterior(String anterior) {
		this.anterior = anterior;
	}

	public int getAnteriorx() {
		return anteriorx;
	}

	public void setAnteriorx(int anteriorx) {
		this.anteriorx = anteriorx;
	}

	public int getAnteriory() {
		return anteriory;
	}

	public void setAnteriory(int anteriory) {
		this.anteriory = anteriory;
	}

	public boolean isNpc() {
		return npc;
	}

	public void setNpc(boolean npc) {
		this.npc = npc;
	}
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}

	public boolean isMuerto() {
		return muerto;
	}

	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getNivelEfecto() {
		if (this.ciudad != null) {
			if(ciudad.getTemperatura() > 20) { //calor
				nivelEfecto = nivel+ec;
			} else {
				nivelEfecto = nivel+ef;
				if(nivelEfecto < 0) nivelEfecto = 0; 
			}
		} else {
			this.nivelEfecto = this.nivel;
		}		
		return nivelEfecto;
	}
	public void setNivelEfecto(int nivelEfecto) {
		this.nivelEfecto = nivelEfecto;
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
	
}
