package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Mapa {
	//cuadrantes frios
	private static final String ANSI_COLDBLUE_BACKGROUND = "\033[46m";
	//cuadrantes calidos
	private static final String ANSI_LIGHTRED_BACKGROUND = "\033[41m";
	//negro
	private static final String ANSI_RESET = "\u001B[0m";
	public String tablero[][] = null;
	public static final String MINA="M", ALIMENTO="A";
	public static final String ESPACIO=" ";
	public HashMap<Integer, Jugador> jugadores;
	public ArrayList<Ciudad> ciudades = null;
	
	public Mapa() {
		crearTablero();
		jugadores = new HashMap<>();
	}

	public ArrayList<Ciudad> getCiudades() {
		return ciudades;
	}

	public void setCiudades(ArrayList<Ciudad> ciudades) {
		this.ciudades = ciudades;
	}


	private Jugador buscarJugador(int x, int y) {
		Jugador jugadorResultado = null;
		for (Map.Entry<Integer, Jugador> entry : jugadores.entrySet()) {
		    Jugador jugador = entry.getValue();
		    if(x == jugador.getX() && y == jugador.getY()) {
		    	jugadorResultado = jugador;
		    }
		}
		return jugadorResultado;
	}
	
	private void crearTablero() {
		Random random = new Random();
		tablero = new String[20][20];
		int aleatorio = -1;
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				//aleatorio = random.nextInt(3);
				//tablero[i][j] = MINA;
				//tablero[i][j] = ALIMENTO;
				tablero[i][j] = ESPACIO;
			}
		}
		for(int i = 0; i < 50; i++) {
			int fila = random.nextInt(0, 20);
			int columna = random.nextInt(0, 20);
			tablero[fila][columna] = MINA;
		}
		for(int i = 0; i < 100; i++) {
			int fila = random.nextInt(0, 20);
			int columna = random.nextInt(0, 20);
			tablero[fila][columna] = ALIMENTO; 
		}
	}
	
	public void mostrarTablero() {
		for(int i = 0; i < 20; i++) {
			if(i == 10) {
				for(int k = 0; k < 20; k++) 
					System.out.print("---");
				System.out.print("-");
				System.out.println();
			} 
				
			for(int j = 0; j < 20; j++) {
				if(j == 10) System.out.print("|");	
				if (this.ciudades.size() != 0) {
					Ciudad ciudad = this.ciudades.get(this.obtenerCiudad(i, j));
					if (ciudad.temperatura > 20) {
						System.out.print(ANSI_LIGHTRED_BACKGROUND + tablero[i][j] + "  " + ANSI_RESET);
					} else {
						System.out.print(ANSI_COLDBLUE_BACKGROUND + tablero[i][j] + "  " + ANSI_RESET);
					}
				} else {
					System.out.print(tablero[i][j] + "  ");
				} 		
			}
			System.out.println();
		}
	}
	public String[][] getTablero() {
		return tablero;
	}
	public void setTablero(String[][] tablero) {
		this.tablero = tablero;
	}
	
	public int obtenerCiudad(int x, int y) {
		if ((x >= 0 && x < 10) &&  (y >= 0 && y < 10)) {
			return 0; //cuadrante 0
		} else if ((x >= 10 && x < 20) &&  (y >= 0 && y < 10)) {
			return 1; //cuadrante 1
		} else if ((x >= 0 && x < 10) &&  (y >= 10 && y < 20)) {
			return 2;
		} else {
			return 3;
		}
	}
	
	public void ponerFicha(int x, int y, Jugador jugador) {
		Ciudad ciudad = null;
		if (ciudades.size() > 0) {
			ciudad = ciudades.get(obtenerCiudad(x, y));
			
		} 
		jugador.setCiudad(ciudad);
		
		if (this.jugadores.get(jugador.getId()) != null) { //anular la pos anterior
			//el jugador ya esta en el mapa
			this.tablero[this.jugadores.get(jugador.getId()).getX()][this.jugadores.get(jugador.getId()).getY()] = " ";
			if (jugador.isNpc() == false) {
				if (this.tablero[x][y] == ALIMENTO) {
					jugador.setNivel((char)(jugador.getNivel()+1));
					this.tablero[x][y] = String.valueOf(jugador.getId());
				} else if (this.tablero[x][y] == MINA) {
					jugador.setMuerto(true);
				} else if (!this.tablero[x][y].equals(ALIMENTO) && !this.tablero[x][y].equals(MINA) && !this.tablero[x][y].equals(ESPACIO)) {
					//es un jugador enemigo
					//System.out.println("LUCHAR");
					Jugador jugadorOrigen = jugador;
					Jugador jugadorEnemigo = buscarJugador(x, y);
					jugadorEnemigo.setCiudad(ciudad);
					if (jugadorOrigen.getNivelEfecto() < jugadorEnemigo.getNivelEfecto()) {
						jugadorOrigen.setMuerto(true);
					} else if (jugadorOrigen.getNivelEfecto() > jugadorEnemigo.getNivelEfecto()) {
						jugadorEnemigo.setMuerto(true);
						this.tablero[x][y] = String.valueOf(jugadorOrigen.getId());
					} else {
						//empate
						this.tablero[x][y] = this.tablero[x][y] + " " + String.valueOf(jugadorOrigen.getId());
					}
				} else {
					this.tablero[x][y] = String.valueOf(jugador.getId());
				}	
				//System.out.println("HOLAAA " + this.tablero[x][y]);
			} else {
				//System.out.println("ES UN NPC");
				//System.out.println(jugador.getAnterior());
				//System.out.println(this.tablero[x][y]);
				this.tablero[jugador.getAnteriorx()][jugador.getAnteriory()] = jugador.getAnterior();
				jugador.setAnterior(this.tablero[x][y]);
				jugador.setAnteriorx(x);
				jugador.setAnteriory(y);
				if(!this.tablero[x][y].equals(ALIMENTO) && !this.tablero[x][y].equals(MINA) && !this.tablero[x][y].equals(ESPACIO)) {
					Jugador jugadorOrigen = jugador;
					Jugador jugadorEnemigo = buscarJugador(x, y);
					if (jugadorOrigen.getNivel() < jugadorEnemigo.getNivelEfecto()) {
						jugadorOrigen.setMuerto(true);
					} else if (jugadorOrigen.getNivel() > jugadorEnemigo.getNivelEfecto()) {
						jugadorEnemigo.setMuerto(true);
						this.tablero[x][y] = String.valueOf(jugadorOrigen.getNivel());
					} else {
						//empate
						this.tablero[x][y] = this.tablero[x][y] + " " + String.valueOf(jugadorOrigen.getNivel());
					}
				} else {
					this.tablero[x][y] = String.valueOf(jugador.getNivel());
				}		
			}
		} else {
			if (jugador.isNpc() == false) {
				this.tablero[x][y] = String.valueOf(jugador.getId());
				//System.out.println("HOLAAA " + this.tablero[x][y]);
			} else {	
				this.tablero[x][y] = String.valueOf(jugador.getNivel());
			}
		}
		//System.out.println("NIVEL" + (char)jugador.getNivel());
		//System.out.println("NIVEL"+jugador.getNivel());
		this.jugadores.put(jugador.getId(), jugador);
	}
}
