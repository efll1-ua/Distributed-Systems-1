package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

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

//rebirá el mapa y actualizara sus posisciones
public class AA_NPC {
	public volatile Jugador jugador = new Jugador();
	/*
	private int filaInicial, columnaInicial; //para saber su posicion (fila, col)
	private String[] alias; //hasta 20 caracteres
	private int level_player = 1;
	private int eFrio; //random entre -10 y 10
	private int eCalor; //random entre -10 y 10
	*/	
	
	
	private static void enviarJugada(Jugada jugada) {
		/*System.out.println("ID" + jugada.getJugador().getId());
		System.out.println("SIMBOLO " + jugada.getJugador().anterior);
		System.out.println("POSICIONES" + jugada.getJugador().getAnteriorx() + "," + jugada.getJugador().getAnteriory());*/
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String jsonJugada = prettyGson.toJson(jugada);
        
		Properties props = new Properties();
        props.put("bootstrap.servers", Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
        // We configure the serializer to describe the format in which we want to produce data into
        // our Kafka cluster
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Since we need to close our producer, we can use the try-with-resources statement to
        // create
        // a new producer
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            // here, we run an infinite loop to sent a message to the cluster every second
                String key = Integer.toString(1);
                String message = "this is message " + Integer.toString(1);

                producer.send(new ProducerRecord<String, String>("jugadas", key, jsonJugada));
                producer.flush();
                // log a confirmation once the message is written
                System.out.println("sent msg " + key);    
        } catch (Exception e) {
            System.out.println("Could not start producer: " + e);
        }
	}
	
    public static void main(String[] args) throws ClassNotFoundException, IOException {
    	//autenticacion
    	Parametros.getInstance().setDireccionEngine(args[0]);
    	Parametros.getInstance().setPuertoEngine(Integer.parseInt(args[1]));
    	Parametros.getInstance().setDireccionKafka(args[2]);
    	Parametros.getInstance().setPuertoKafka(Integer.parseInt(args[3]));
    	boolean autenticacion = false;
    	MensajeAutenticacion mensaje = null;
    	ObjectInputStream input = null;
    	ObjectOutputStream output = null;
    	Socket cliente = null; 
		try {
			cliente = new Socket(Parametros.getInstance().getDireccionEngine(), Parametros.getInstance().getPuertoEngine());
			output = new ObjectOutputStream(cliente.getOutputStream());
	    	input = new ObjectInputStream(cliente.getInputStream());
        	User user = new User("NPC", "contraseñaNPC", true);
        	output.writeObject(user);
        	mensaje = (MensajeAutenticacion) input.readObject();
        	
		} catch(IOException ex) {
			//ex.printStackTrace();
			System.out.println("No ha sido posible realizar la conexión con el Engine");
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	AA_NPC npc = new AA_NPC();
    	System.out.println("Esperando a que comience la partida");
    	InicioPartida inicio = (InicioPartida) input.readObject();
    	
    	npc.jugador.setId(mensaje.getId());
    	Random random = new Random();
    	npc.jugador.setNivel(random.nextInt(1, 25)); //por argumento
    	npc.jugador.setNpc(true);
    	ThreadLeerMapa threadd = new ThreadLeerMapa(npc.jugador);
    	npc.jugador.setAnterior(" ");
    	//lanza el hilo
    	threadd.start();
    	ThreadPing ping = new ThreadPing(output, input, threadd);
		ping.start();
		ThreadKafkaPing kafkaPing = new ThreadKafkaPing(threadd, ping);
    	kafkaPing.start();
    	int xInicial = random.nextInt(20);
    	int yInicial = random.nextInt(20);
    	npc.jugador.setX(xInicial);
    	npc.jugador.setY(yInicial);
    	Jugada jugadaInicial = new Jugada(xInicial, yInicial, npc.jugador);
    	jugadaInicial.setJugadaInicial(true);
    	enviarJugada(jugadaInicial);
    	
    	while(npc.jugador.isMuerto() == false && npc.jugador.isGanador() == false) {
    		int movimiento = random.nextInt(8);
    		if (movimiento == 0) {
    			if (npc.jugador.getX()-1 < 0) {
    				npc.jugador.setX(19);
    			} else {
    				npc.jugador.setX((npc.jugador.getX()-1));
    			}
    		} else if (movimiento == 1) {
    			npc.jugador.setX((npc.jugador.getX()+1)%20);
    		} else if (movimiento == 2) {
    			if (npc.jugador.getY()-1 < 0) {
    				npc.jugador.setY(19);
    			} else {
    				npc.jugador.setY((npc.jugador.getY()-1));
    			}
    		} else if (movimiento == 3) {
    			npc.jugador.setY((npc.jugador.getY()+1)%20);
    		} else if (movimiento == 4) {
    			npc.jugador.setX(npc.jugador.getX()-1);
    			npc.jugador.setY((npc.jugador.getY()+1)%20);
    			if(npc.jugador.getX() < 0) {
    				npc.jugador.setX(19);
    			}    			
    		} else if (movimiento == 5) {
    			npc.jugador.setX(npc.jugador.getX()-1);
    			npc.jugador.setY(npc.jugador.getY()-1);
    			if(npc.jugador.getX() < 0) {
    				npc.jugador.setX(19);
    			} 
    			if(npc.jugador.getY() < 0) {
    				npc.jugador.setY(19);
    			}
    		} else if (movimiento == 6) {
    			npc.jugador.setX((npc.jugador.getX()+1)%20);
    			npc.jugador.setY((npc.jugador.getY()+1)%20);
    		} else if (movimiento == 7) {
    			npc.jugador.setX((npc.jugador.getX()+1)%20);
    			npc.jugador.setY(npc.jugador.getY()-1);
    			if(npc.jugador.getY() < 0) {
    				npc.jugador.setY(19);
    			}
    		}
    		
        	Jugada jugada = new Jugada(npc.jugador.getX(), npc.jugador.getY(), npc.jugador);
        	enviarJugada(jugada);
        	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	/*System.out.print("\033[H\033[2J"); //limpia la consola
        	System.out.flush();*/
    	}
    }
}
