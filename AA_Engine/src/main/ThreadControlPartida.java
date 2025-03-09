package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ThreadControlPartida extends Thread{
	private ArrayList<ThreadJugador> jugadoresPartida;
	private Mapa mapa;
	private boolean partidaIniciada = false;
	private volatile boolean partidaEnCurso = false;
	private static final int MAX_TIEMPO_ESPERA = 30;
	private static final int MAX_TIEMPO_PARTIDA = 300;


	public ThreadControlPartida(ArrayList<ThreadJugador> jugadoresPartida, Mapa mapa) {
		super();
		this.jugadoresPartida = jugadoresPartida;
		this.mapa = mapa;
	}
	
	
	
	public boolean isPartidaEnCurso() {
		return partidaEnCurso;
	}



	public void setPartidaEnCurso(boolean partidaEnCurso) {
		this.partidaEnCurso = partidaEnCurso;
	}



	public ArrayList<ThreadJugador> getJugadoresPartida() {
		return jugadoresPartida;
	}

	public void setJugadoresPartida(ArrayList<ThreadJugador> jugadoresPartida) {
		this.jugadoresPartida = jugadoresPartida;
	}
	
	private int getUsuariosAutenticados() {
		int numUsersAutenticados = 0;
		for(int i = 0; i < jugadoresPartida.size(); i++) {
				if (jugadoresPartida.get(i).isAutenticado()) {
					numUsersAutenticados++;
				}
		}
		return numUsersAutenticados;
	}
	
	private void obtenerGanador() {
		int idJugadorGanador = 0;
		int nivelJugadorGanador = 0;
		for (Map.Entry<Integer, Jugador> entry : mapa.jugadores.entrySet()) { //encontramos quien ha ganado
		    Integer key = entry.getKey();
		    Jugador jugador = entry.getValue();
		    if(jugador.getNivelEfecto() > nivelJugadorGanador) {
		    	idJugadorGanador = jugador.getId();
		    	nivelJugadorGanador = jugador.getNivelEfecto();
		    }
		}
		
		for (Map.Entry<Integer, Jugador> entry : mapa.jugadores.entrySet()) { //matamos al que no haya ganado
		    Integer key = entry.getKey();
		    Jugador jugador = entry.getValue();
		    if((jugador.getId() != idJugadorGanador) && (jugador.isMuerto() == false)) {
		    	jugador.setMuerto(true);
		    	enviarFinalPartida(jugador.getId());
		    }
		}
		
		mapa.jugadores.get(idJugadorGanador).setGanador(true); //establecemos el ganador
		enviarFinalPartida(idJugadorGanador);
	}
	
	private void enviarFinalPartida(int idJugador) {
		 Properties props = new Properties();
	        props.put("bootstrap.servers", Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
	        // We configure the serializer to describe the format in which we want to produce data into
	        // our Kafka cluster
	        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

	        // Since we need to close our producer, we can use the try-with-resources statement to
	        // create
	        // a new producer
	        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	        String jsonMapa = prettyGson.toJson(mapa);
	        //System.out.println(jsonMapa);
	        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
                //actualizacion_mapa es el identificador de la cola
        		producer.send(new ProducerRecord<String, String>("actualizacion_mapa_" + idJugador, String.valueOf(idJugador), jsonMapa));
                producer.flush();
	        } catch (Exception e) {
	            System.out.println("Could not start producer: " + e);
	        }
	}
	
	public void parar() {
		this.partidaEnCurso = false;
	}
	
	public void iniciarPartida() {
		this.partidaIniciada = true;
		this.partidaEnCurso = true;
		enviarMensjeInicioPartida();
	}
	
	private void enviarMensjeInicioPartida() {
		for(int i = 0; i < jugadoresPartida.size(); i++) {
			try {
				if (jugadoresPartida.get(i).isAutenticado())
					jugadoresPartida.get(i).enviarMensajeInicioPartida();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		int tiempoTranscurrido = 0;
		while(!partidaIniciada) {
			int numUserariosAutenticados = getUsuariosAutenticados();
			if((tiempoTranscurrido >= MAX_TIEMPO_ESPERA || numUserariosAutenticados >= Parametros.getInstance().getMaxJugadores()) && numUserariosAutenticados > 1) {
				//enviar mensajes de inicio de partida
				enviarMensjeInicioPartida();
				partidaIniciada = true;
				partidaEnCurso = true;
			} else {
				try {
					sleep(1000); //se duerme un segundo y se va despertando, cuando se despierta incrementamos en 1s el t. transc
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tiempoTranscurrido++;
		}
		tiempoTranscurrido = 0;
		while(partidaEnCurso) {
			try {			
				sleep(1000);
				//System.out.println("HOLI");
				tiempoTranscurrido++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (tiempoTranscurrido >= MAX_TIEMPO_PARTIDA) {
				//finalizar partida
				obtenerGanador();
				partidaEnCurso = false;
			}
		}
	}
}
