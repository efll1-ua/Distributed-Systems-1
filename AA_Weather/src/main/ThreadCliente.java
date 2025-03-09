package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class ThreadCliente extends Thread {
	private Socket cliente;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Map<String, Ciudad> mapa;
	public ThreadCliente(Socket cliente, Map<String, Ciudad> mapa) {
		super();
		this.cliente = cliente;
		this.mapa = mapa;
	}
	
	
	public void run() {	
		try {
			output = new ObjectOutputStream(cliente.getOutputStream());
			input = new ObjectInputStream(cliente.getInputStream());
			SolicitudTemperatura solicitudTemperatura = (SolicitudTemperatura) input.readObject();
			if (mapa.get(solicitudTemperatura.getCiudad()) != null) { //la ciudad exite, se le envia la ciudad
				output.writeObject(mapa.get(solicitudTemperatura.getCiudad()));
			} else {
				output.writeObject(null);
			}
			//escribirUsuarios(datosJugador); //escribir
			output.close();
			input.close();
			cliente.close();
			//System.out.println(datosJugador);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
