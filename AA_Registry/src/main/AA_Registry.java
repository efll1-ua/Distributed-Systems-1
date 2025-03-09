package main;

public class AA_Registry {
	public static void main(String args[]) {
		Parametros.getInstance().setPuerto(Integer.parseInt(args[0]));
		ThreadServidor servidor = new ThreadServidor();
		servidor.start();
	}
}
