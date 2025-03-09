package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServidor extends Thread{
	private boolean parar = false;
	public ThreadServidor() {
		
	}
	
	public void run() {
		try {
			ServerSocket server = new ServerSocket(Parametros.getInstance().getPuerto());
			System.out.println("Escuchando conexiones registro");
			while(!parar) {
				Socket cliente = server.accept();
				ThreadCliente threadCliente = new ThreadCliente(cliente);
				threadCliente.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
