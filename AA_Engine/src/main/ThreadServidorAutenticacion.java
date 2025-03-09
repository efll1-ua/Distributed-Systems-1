package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadServidorAutenticacion extends Thread {
	private ArrayList<ThreadJugador> jugadoresPartida; // jugadores en partida
	private boolean parar;
	private Mapa mapa;
	private ThreadControlPartida control;

	public ThreadServidorAutenticacion(Mapa mapa) {
		jugadoresPartida = new ArrayList<>();
		this.mapa = mapa;
		control = new ThreadControlPartida(jugadoresPartida, mapa);
		control.start();
	}
	
	public boolean isParar() {
		return parar;
	}

	public void setParar(boolean parar) {
		this.parar = parar;
	}

	public int contarJugadoresEnPartida() {
		int numJugadores = 0;
		for(ThreadJugador jugador : jugadoresPartida) {
			if(jugador.isNpc() == false) {
				numJugadores++;
			}
		}
		return numJugadores;
	}
	
	public ArrayList<ThreadJugador> getJugadoresPartida() {
		return jugadoresPartida;
	}



	public void setJugadoresPartida(ArrayList<ThreadJugador> jugadoresPartida) {
		this.jugadoresPartida = jugadoresPartida;
	}



	public ThreadControlPartida getControl() {
		return control;
	}

	public void setControl(ThreadControlPartida control) {
		this.control = control;
	}

	public void eliminarJugador(ThreadJugador jugador) {
		if (mapa.jugadores.get(jugador.getIdentificador()) != null) {
			jugadoresPartida.remove(jugador);
			mapa.tablero[mapa.jugadores.get(jugador.getIdentificador()).x][mapa.jugadores
					.get(jugador.getIdentificador()).y] = " ";
			mapa.jugadores.remove(jugador.getIdentificador());
		}
		// System.out.println("IDENTIFICADOR " + jugador.getIdentificador());
	}

	public void run() {
		try {
			ServerSocket server = new ServerSocket(Parametros.getInstance().getPuertoServidorAutenticaciones());
			System.out.println("Servidor de autenticaciones escuchando");

			while (!parar) {
				Socket cliente = server.accept();
				ThreadJugador jugador = new ThreadJugador(cliente, jugadoresPartida.size() + 1, control, this); 
				jugador.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
