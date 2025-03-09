package main;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ThreadLeerMapa extends Thread {
	volatile public Jugador jugador;
	private boolean parar = false;
	public ThreadLeerMapa(Jugador jugador) {
		this.jugador = jugador;
	}
	
	public void parar() {
		this.parar = true;
	}
	
	public void run() {
		
		//System.out.println("HILO CORRIENDO");
		Properties props = new Properties();
        props.setProperty("bootstrap.servers", Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
        // The group ID is a unique identified for each consumer group
        props.setProperty("group.id", "my-group-id");
        // Since our producer uses a string serializer, we need to use the corresponding
        // deserializer
        props.setProperty("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        // Every time we consume a message from kafka, we need to "commit" - that is, acknowledge
        // receipts of the messages. We can set up an auto-commit at regular intervals, so that
        // this is taken care of in the background
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");

        // Since we need to close our consumer, we can use the try-with-resources statement to
        // create it
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	     
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            // Subscribe this consumer to the same topic that we wrote messages to earlier
            consumer.subscribe(Arrays.asList("actualizacion_mapa_" + jugador.id));
            // run an infinite loop where we consume and print new messages to the topic
            while (jugador.isMuerto() == false && jugador.isGanador() == false && parar == false) {
                // The consumer.poll method checks and waits for any new messages to arrive for the
                // subscribed topic
                // in case there are no messages for the duration specified in the argument (1000 ms
                // in this case), it returns an empty list
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); //tiempo de llamada
                for (ConsumerRecord<String, String> record : records) {
               	 Mapa mapa = prettyGson.fromJson(record.value(), Mapa.class);
               	 System.out.println();
               	 new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
               	if(jugador.isNpc()) {
              		 System.out.println("TU SIMBOLO ES: " + jugador.getNivel());
              	 } else {
              		System.out.println("TU SIMBOLO ES: " + jugador.getId());
              	 }
                	//recibe el movimiento, actualiza el tablero
               	 mapa.mostrarTablero();
               	 if (mapa.jugadores.get(jugador.getId()) != null) {
               		//System.out.println(mapa.jugadores.get(jugador.getId()).getNivel());
               		//actualizamos los campos del jugador
               		jugador.setMuerto(mapa.jugadores.get(jugador.getId()).isMuerto());
               		jugador.setAnteriorx(mapa.jugadores.get(jugador.getId()).getAnteriorx());
               		jugador.setAnteriory(mapa.jugadores.get(jugador.getId()).getAnteriory());
               		jugador.setAnterior(mapa.jugadores.get(jugador.getId()).getAnterior());
               		jugador.setGanador(mapa.jugadores.get(jugador.getId()).isGanador());
                  	//System.out.println(jugador.getAnteriorx() + "," + jugador.anteriory + "," + jugador.getAnterior());
               	 } 
                    //System.out.printf("received message: %s\n", record.value());
                }
            }
            if(jugador.isGanador() == true) {
            	System.out.println("HAS GANADO");
            } else {
            	System.out.println("HAS PERDIDO");
            }
        } catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
