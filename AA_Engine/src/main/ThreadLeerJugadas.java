package main;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ThreadLeerJugadas extends Thread {
	private Mapa mapa = null;
	private int id = 0;

	public ThreadLeerJugadas(Mapa mapa) {
		this.mapa = mapa;

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
		// System.out.println(jsonMapa);
		try (Producer<String, String> producer = new KafkaProducer<>(props)) {
			// actualizacion_mapa es el identificador de la cola
			for (Integer key : mapa.jugadores.keySet()) {
				producer.send(
						new ProducerRecord<String, String>("actualizacion_mapa_" + key, String.valueOf(id), jsonMapa));
				producer.flush();
			}

		} catch (Exception e) {
			System.out.println("Could not start producer: " + e);
		}
	}

	private int contarJugadoresVivos() {
		int jugadoresVivos = 0;
		for (Map.Entry<Integer, Jugador> entry : mapa.jugadores.entrySet()) {
			Integer key = entry.getKey();
			Jugador jugador = entry.getValue();
			if (jugador.isMuerto() == false) {
				jugadoresVivos++;
			}
		}
		return jugadoresVivos;
	}

	private void establecerGanador() {
		for (Map.Entry<Integer, Jugador> entry : mapa.jugadores.entrySet()) {
			Integer key = entry.getKey();
			Jugador jugador = entry.getValue();
			if (jugador.isMuerto() == false) {
				jugador.setGanador(true);
			}
		}
	}

	public void run() {
		// Create configuration options for our consumer
		Properties props = new Properties();
		props.setProperty("bootstrap.servers",
				Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
		// The group ID is a unique identified for each consumer group
		props.setProperty("group.id", "my-group-id");
		// Since our producer uses a string serializer, we need to use the corresponding
		// deserializer
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		// Every time we consume a message from kafka, we need to "commit" - that is,
		// acknowledge
		// receipts of the messages. We can set up an auto-commit at regular intervals,
		// so that
		// this is taken care of in the background
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.commit.interval.ms", "1000");

		// Since we need to close our consumer, we can use the try-with-resources
		// statement to
		// create it
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
			// Subscribe this consumer to the same topic that we wrote messages to earlier
			consumer.subscribe(Arrays.asList("jugadas"));

			// run an infinite loop where we consume and print new messages to the topic
			while (true) {
				// The consumer.poll method checks and waits for any new messages to arrive for
				// the
				// subscribed topic
				// in case there are no messages for the duration specified in the argument
				// (1000 ms
				// in this case), it returns an empty list
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
				for (ConsumerRecord<String, String> record : records) {
					Jugada jugada = prettyGson.fromJson(record.value(), Jugada.class); // leemos jugada
					mapa.ponerFicha(jugada.getX(), jugada.getY(), jugada.getJugador()); // ponemos la ficha
					if (jugada.isJugadaInicial() == false) {
						int jugadoresVivos = contarJugadoresVivos();
						if (jugadoresVivos == 1) {
							establecerGanador();
						}
					}
					enviarMapa(); // enviamos mapa
					// recibe el movimiento, actualiza el tablero
					// System.out.printf("received message: %s\n", record.value());
				}
			}
		}
	}
}
