package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadJugador extends Thread {
	private boolean autenticado = false;
	private boolean parar;
	private Socket socketJugador;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private int id;
	private ThreadControlPartida control;
	private ThreadServidorAutenticacion autenticacion;
	private boolean npc = false;

	public ThreadJugador(Socket socketJugador, int id, ThreadControlPartida control,
			ThreadServidorAutenticacion autenticacion) {
		this.socketJugador = socketJugador;
		this.id = id;
		this.control = control;
		this.autenticacion = autenticacion;
	}

	public boolean isNpc() {
		return npc;
	}

	public void setNpc(boolean npc) {
		this.npc = npc;
	}

	public int getIdentificador() {
		return id;
	}

	public void setIdentificador(int id) {
		this.id = id;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}

	public void enviarMensajeInicioPartida() throws IOException {
		this.output.writeObject(new InicioPartida());
		this.output.flush(); // para que lo envie directamente, instantaneo
	}

	public ArrayList<User> leerUsuarios() {
		ArrayList<User> users = new ArrayList<User>();
		try {
			FileReader fr = new FileReader("users.txt");
			BufferedReader br = new BufferedReader(fr);
			String linea = " ";
			while ((linea = br.readLine()) != null) {
				User us = new User(linea.split(";")[0], linea.split(";")[1], false);
				us.setNivel(Integer.parseInt(linea.split(";")[2]));
				us.setEc(Integer.parseInt(linea.split(";")[3]));
				us.setEf(Integer.parseInt(linea.split(";")[4]));
				users.add(us);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

	public void run() {
		try {
			input = new ObjectInputStream(socketJugador.getInputStream());
			output = new ObjectOutputStream(socketJugador.getOutputStream());
			while (!parar) {
				User user = (User) input.readObject();

				if (user.isNpc() == false) {
					if (autenticacion.contarJugadoresEnPartida() >= Parametros.getInstance().getMaxJugadores()) {
						MensajeAutenticacion autenticacion = new MensajeAutenticacion(false);
						autenticacion.setErrorDescrip("Superado el limite maximo de jugadores");
						output.writeObject(autenticacion);
						parar = true;
						autenticado = true;
					} else if(control.isPartidaEnCurso()) {
						MensajeAutenticacion autenticacion = new MensajeAutenticacion(false);
						autenticacion.setErrorDescrip("La partida ya está en curso");
						output.writeObject(autenticacion);
						parar = true;
						autenticado = true;
					} else {
						System.out.println("USERNAME: " + user.getUserName());
						ArrayList<User> users = leerUsuarios();
						System.out.println(users.size());
						for (int i = 0; i < users.size(); i++) {
							if (users.get(i).getUserName().equals(user.getUserName())) {
								if (users.get(i).getContraseña().equals(user.getContraseña())) {
									System.out.println("Correcto");
									autenticacion.getJugadoresPartida().add(this); // todos los jugadores en un
																					// arraylist
									MensajeAutenticacion autenticacion = new MensajeAutenticacion(true);
									autenticacion.setNivel(users.get(i).getNivel());
									autenticacion.setEc(users.get(i).getEc());
									autenticacion.setEf(users.get(i).getEf());
									autenticacion.setId(id);
									output.writeObject(autenticacion);
									parar = true;
									autenticado = true;

								}
							}
						}
						if (autenticado == false) {
							System.out.println("Contraseña incorrecta vuelva a intentarlo");
							MensajeAutenticacion autenticacion = new MensajeAutenticacion(false);
							autenticacion.setErrorDescrip("AUTENTICACION INCORRECTA");
							output.writeObject(autenticacion);
						}
					}
				} else {
					/*
					 * System.out.println(this.autenticacion.getJugadoresPartida().size());
					 * System.out.println(Parametros.getInstance().getMaxJugadores());
					 */
					this.npc = true;
					// limite superado
					MensajeAutenticacion autenticacion = new MensajeAutenticacion(true);
					this.autenticacion.getJugadoresPartida().add(this); // todos los jugadores en un arraylist
					autenticacion.setId(id);
					output.writeObject(autenticacion);
					parar = true;
					autenticado = true;
					if (this.control.isPartidaEnCurso() && user.isNpc()) {
						this.enviarMensajeInicioPartida();
					}
				}
			}
			//System.out.println("NO PING");
			// sleep(5000);
			while (true) {
				if (control.isPartidaEnCurso()) {
					sleep(1000);
					//System.out.println("PING ENVIADO");
					output.writeObject(new Ping());
					output.flush();
					input.readObject();
				}
			}

		} catch (IOException e) {
			System.out.println("Se ha desconectado un jugador");
			autenticacion.eliminarJugador(this); // eliminamos al jugador del mapa y la partida
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
