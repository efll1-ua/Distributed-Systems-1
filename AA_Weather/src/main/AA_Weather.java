package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AA_Weather {
	
	public static Map<String, Ciudad> leerTemperaturas() {
		Map<String, Ciudad> mapa = new HashMap<>();
		try {
			FileReader fr = new FileReader("temperaturas");
			BufferedReader bf = new BufferedReader(fr);
			String linea = null;
			while((linea = bf.readLine()) != null) {
				Ciudad ciudad = new Ciudad();
				ciudad.setCiudad(linea.split(";")[0]);
				ciudad.setTemperatura(Integer.parseInt(linea.split(";")[1]));
				mapa.put(ciudad.getCiudad(), ciudad); //clave nombre, valor la ciudad
			}
			bf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapa;
	}
	public static void main(String args[]) {
		Parametros.getInstance().setPuerto(Integer.parseInt(args[0]));
		Map<String, Ciudad> mapa = leerTemperaturas();
		ThreadServidor thread = new ThreadServidor(mapa);
		thread.start();
	}
}
