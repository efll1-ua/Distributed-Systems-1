package main;

public class Jugada {
	public int x, y;
	public Jugador jugador;
	public boolean jugadaInicial;
	
	public Jugada() {}
	public Jugada(int x, int y, Jugador jugador) {
		super();
		this.x = x;
		this.y = y;
		this.jugador = jugador;
	}
	
	public boolean isJugadaInicial() {
		return jugadaInicial;
	}
	public void setJugadaInicial(boolean jugadaInicial) {
		this.jugadaInicial = jugadaInicial;
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
	public Jugador getJugador() {
		return jugador;
	}
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	
	
}
