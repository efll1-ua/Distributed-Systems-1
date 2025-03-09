package main;

import java.util.Scanner;

public class ThreadBotonInicioPartida extends Thread {
	private ThreadControlPartida partida = null;
	public ThreadBotonInicioPartida(ThreadControlPartida partida) {
		this.partida = partida;
	}
	
	public void run() {
		System.out.println("Para inciar partida debe escribir: iniciar");
		boolean mensajeCorrecto = false;
		Scanner sc = new Scanner(System.in);
		while(mensajeCorrecto == false) {
			String mensaje = sc.nextLine();
			if(mensaje.equals("iniciar")) {
				System.out.println("Se ha iniciado la partida");
				partida.iniciarPartida();
				mensajeCorrecto = true;
			} else {
				System.out.println("Para inciar partida debe escribir: iniciar");
				System.out.println("Vuelva a introducir el comando");
			}
		}
	}
}
