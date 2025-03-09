package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author eva
 */

//Lo que tengo que mostrar continuamente será el mapa, se actualizará
public class AA_Engine {

	private static Mapa mapa = null;
	private ArrayList<Ciudad> ciudades = null;

	public AA_Engine() {

		mapa = new Mapa();
		ciudades = new ArrayList<Ciudad>();
		FileReader fr;
		try {
			fr = new FileReader("ciudades");
			BufferedReader bf = new BufferedReader(fr);
			String linea = null;
			while ((linea = bf.readLine()) != null) {
				ObjectOutputStream output;
				ObjectInputStream input;
				Socket socket = new Socket(Parametros.getInstance().getDireccionWeather(), Parametros.getInstance().getPuertoWeather());
				input = new ObjectInputStream(socket.getInputStream());
				output = new ObjectOutputStream(socket.getOutputStream());
				SolicitudTemperatura solicitud = new SolicitudTemperatura();
				solicitud.setCiudad(linea);
				output.writeObject(solicitud);
				Ciudad ciudad = (Ciudad) input.readObject();
				if (ciudad != null) {
					ciudades.add(ciudad);
					System.out.println(ciudad);
				} else {
					System.out.println("La ciudad no existe");
				}
				input.close();
				output.close();
				socket.close();
			}

			bf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("No ha sido posible conectar con el servidor de clima");
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapa.setCiudades(ciudades);
	}

	public void enviarMapa() {
		Properties props = new Properties();
		props.put("bootstrap.servers",
				Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
		// We configure the serializer to describe the format in which we want to
		// produce data into
		// our Kafka cluster
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// Since we need to close our producer, we can use the try-with-resources
		// statement to
		// create
		// a new producer
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		String jsonMapa = prettyGson.toJson(mapa);
		try (Producer<String, String> producer = new KafkaProducer<>(props)) {
			// here, we run an infinite loop to sent a message to the cluster every second
			String key = Integer.toString(1);
			String message = "this is message " + Integer.toString(1);
			// actualizacion_mapa es el identificador de la cola
			producer.send(new ProducerRecord<String, String>("actualizacion_mapa", key, jsonMapa));
			producer.flush();
			// log a confirmation once the message is written
			System.out.println("sent msg " + key);

		} catch (Exception e) {
			System.out.println("Could not start producer: " + e);
		}
	}

	// devolver el mapa para que lo vea el player actualizado
	public static void main(String[] args) {
		Parametros.getInstance().setPuertoServidorAutenticaciones(Integer.parseInt(args[0]));
		Parametros.getInstance().setMaxJugadores(Integer.parseInt(args[1]));
		Parametros.getInstance().setDireccionWeather((args[2]));
		Parametros.getInstance().setPuertoWeather(Integer.parseInt(args[3]));
		Parametros.getInstance().setDireccionKafka((args[4]));
		Parametros.getInstance().setPuertoKafka(Integer.parseInt(args[5]));
		/*System.out.println(Parametros.getInstance().getPuertoServidorAutenticaciones() +" "+ Parametros.getInstance().getMaxJugadores() +
			" "+	Parametros.getInstance().getDireccionWeather() + " "+ Parametros.getInstance().getPuertoWeather() + " " +
				Parametros.getInstance().getDireccionKafka() + " "+ Parametros.getInstance().getPuertoKafka());*/
		AA_Engine engine = new AA_Engine();	
		ThreadServidorAutenticacion autenticacionServer = new ThreadServidorAutenticacion(mapa);
		autenticacionServer.start();
		ThreadBotonInicioPartida incioPartida = new ThreadBotonInicioPartida(autenticacionServer.getControl());
		incioPartida.start();
		engine.enviarMapa();
		ThreadLeerJugadas threadd = new ThreadLeerJugadas(mapa);
		threadd.start();
		ThreadKafkaPing kafkaPing = new ThreadKafkaPing(autenticacionServer, incioPartida, threadd);
    	kafkaPing.start();
		try {
			threadd.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
