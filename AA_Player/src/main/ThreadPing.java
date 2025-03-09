package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ThreadPing extends Thread {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private volatile boolean parar = false;
	private ThreadLeerMapa threadLeerMapa;
	
	public ThreadPing(ObjectOutputStream output, ObjectInputStream input, ThreadLeerMapa threadLeerMapa) {
		this.input = input;
		this.output = output;
		this.threadLeerMapa = threadLeerMapa;
	}

	public boolean isParar() {
		return parar;
	}



	public void setParar(boolean parar) {
		this.parar = parar;
	}



	public void run() {
		while(!parar) {
			try {
				input.readObject();
				output.writeObject(new Ping());
				output.flush();
				sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				parar = true;
				threadLeerMapa.parar();
				System.out.println("El Engine no está disponible");
				System.exit(-1);
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("THREAD PARADO PING");
	}
}
