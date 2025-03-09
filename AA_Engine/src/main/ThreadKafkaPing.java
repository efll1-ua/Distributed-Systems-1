package main;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.Node;

public class ThreadKafkaPing extends Thread{
	private ThreadServidorAutenticacion autenticacion;
	private ThreadBotonInicioPartida boton;
	private ThreadLeerJugadas leer;
	private final AdminClient client;
	public ThreadKafkaPing(ThreadServidorAutenticacion autenticacion, ThreadBotonInicioPartida boton, ThreadLeerJugadas leer) {
        Properties props = new Properties();
        props.put("bootstrap.servers", Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
        props.put("request.timeout.ms", 1000);
        props.put("connections.max.idle.ms", 1000);

        this.client = AdminClient.create(props);
        this.autenticacion = autenticacion;
        this.boton = boton;
        this.leer = leer;
    }
	
	public boolean verifyConnection() throws ExecutionException, InterruptedException {
	    Collection<Node> nodes = this.client.describeCluster()
	      .nodes()
	      .get();
	    return nodes != null && nodes.size() > 0;
	}
	
	public void run() {
		boolean parar = false;
		while(!parar) {
			try {
				verifyConnection();
				sleep(1000);
			} catch (ExecutionException e) {
				System.out.println("Kafka ha muerto");
				parar = true;
				this.autenticacion.isParar();
				this.boton.isInterrupted();
				this.leer.isInterrupted();
				System.exit(-1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
