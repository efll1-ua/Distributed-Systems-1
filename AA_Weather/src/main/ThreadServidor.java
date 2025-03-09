package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ThreadServidor extends Thread{
	private boolean parar = false;
	private Map<String, Ciudad> mapa;
	public ThreadServidor(Map<String, Ciudad> map) {
		this.mapa = map;
	}
	
	public void run() {
		try {
			ServerSocket server = new ServerSocket(Parametros.getInstance().getPuerto());
			System.out.println("Escuchando conexiones clima");
			while(!parar) {
				Socket cliente = server.accept();
				ThreadCliente threadCliente = new ThreadCliente(cliente, mapa);
				threadCliente.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
