package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
//solo atiende una peticion
public class ThreadCliente extends Thread{
	private Socket cliente;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	public ThreadCliente(Socket cliente) {
		super();
		this.cliente = cliente;
	}
	
	private void escribirUsuarios(DatosJugador datos) {
		FileWriter fw;
		try {
			fw = new FileWriter("users.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
		    bw.write(datos.getAlias() + ";" + datos.getPassword() + ";" + datos.getNivel() + ";" + datos.getEc() + ";" + datos.getEf());
		    bw.newLine();
		    bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void run() {	
		try {
			output = new ObjectOutputStream(cliente.getOutputStream());
			input = new ObjectInputStream(cliente.getInputStream());
			Object object = input.readObject();
			if(object instanceof DatosJugador) {
				escribirUsuarios((DatosJugador)object); //escribir
				System.out.println((DatosJugador)object);
			} else if (object instanceof EditarPerfil) {
				ArrayList<String> usuarios = new ArrayList<String>();
				BufferedReader bf = new BufferedReader(new FileReader("users.txt"));
				String linea;
				while((linea = bf.readLine()) != null) {
					usuarios.add(linea); //leemos el fichero
				}
				bf.close();
				EditarPerfil editarPerfil = (EditarPerfil)object;
				PrintWriter pw = new PrintWriter("users.txt"); //pw para escribir en el fichero
				for(int i = 0; i < usuarios.size(); i++) {
					String[] campos = usuarios.get(i).split(";");
					if(campos[0].equals(editarPerfil.getAlias())) {
						if(campos[1].equals(editarPerfil.getPassw())) {
							if (!editarPerfil.getNuevoAlias().equals("")) {
								campos[0] = editarPerfil.getNuevoAlias();
							} 
							if (!editarPerfil.getNuevoPassw().equals("")) {
								campos[1] = editarPerfil.getNuevoPassw();
							}
							if (editarPerfil.getNuevoNivel() != 0) {
								campos[2] = String.valueOf(editarPerfil.getNuevoNivel());
							}
							if (editarPerfil.getNuevoEc() != 0) {
								campos[3] = String.valueOf(editarPerfil.getNuevoEc());
							}
							if (editarPerfil.getNuevoEf() != 0) {
								campos[4] = String.valueOf(editarPerfil.getNuevoEf());
							}
							pw.println(campos[0] + ";" + campos[1] + ";" + campos[2] + ";" + campos[3] + ";" + campos[4]);
							RespuestaEditarPerfil respuesta = new RespuestaEditarPerfil(true, "Perfil Actualizado Correctamente");
							output.writeObject(respuesta);
						} else {
							RespuestaEditarPerfil respuesta = new RespuestaEditarPerfil(false, "Contraseña incorrecta");
							pw.println(usuarios.get(i));
							output.writeObject(respuesta);
						}
					} else {
						pw.println(usuarios.get(i));
					}
				}
				pw.close();
			}
			
			output.close();
			input.close();
			cliente.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
