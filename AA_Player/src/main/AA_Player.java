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

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author eva
 */

//rebirá el mapa y actualizara sus posisciones
public class AA_Player implements NativeKeyListener {
	
	/*
	private int filaInicial, columnaInicial; //para saber su posicion (fila, col)
	private String[] alias; //hasta 20 caracteres
	private int level_player = 1;
	private int eFrio; //random entre -10 y 10
	private int eCalor; //random entre -10 y 10
	*/	
	private static Jugador jugador = new Jugador();
	
	private static void enviarJugada(Jugada jugada) {
		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String jsonJugada = prettyGson.toJson(jugada);
        
		Properties props = new Properties();
        props.put("bootstrap.servers", Parametros.getInstance().getDireccionKafka() + ":" + Parametros.getInstance().getPuertoKafka());
        // We configure the serializer to describe the format in which we want to produce data into
        // our Kafka cluster
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("metadata.fetch.timeout.ms", "1000");
        props.put("request.timeout.ms", "1000");
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
	
	public void nativeKeyPressed(NativeKeyEvent e) {
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if(NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("w")) {
        	if (jugador.getX()-1 < 0) {
				jugador.setX(19);
			} else {
				jugador.setX((jugador.getX()-1));
			}
        	Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
        	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("s")) {
        	jugador.setX((jugador.getX()+1)%20);
        	Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
        	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("a")) {
        	if (jugador.getY()-1 < 0) {
				jugador.setY(19);
			} else {
				jugador.setY((jugador.getY()-1));
			}
        	Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
        	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("d")) {
        	jugador.setY((jugador.getY()+1)%20);
        	Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
        	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("q")) {
        	jugador.setX(jugador.getX()-1);
			jugador.setY(jugador.getY()-1);
			if(jugador.getX() < 0) {
				jugador.setX(19);
			} 
			if(jugador.getY() < 0) {
				jugador.setY(19);
			}
			Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
	    	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("e")) {
        	jugador.setX(jugador.getX()-1);
			jugador.setY((jugador.getY()+1)%20);
			if(jugador.getX() < 0) {
				jugador.setX(19);
			}  
			Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
	    	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("z")) {
        	jugador.setX((jugador.getX()+1)%20);
			jugador.setY(jugador.getY()-1);
			if(jugador.getY() < 0) {
				jugador.setY(19);
			}
			Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
	    	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("c")) {
        	jugador.setX((jugador.getX()+1)%20);
			jugador.setY((jugador.getY()+1)%20);
			Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
	    	enviarJugada(jugada);
        } else if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("f")) {
        	System.out.println("CERRANDO APLICACION");
        	System.exit(0);
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }
    
    public static void main(String[] args) {
    	Parametros.getInstance().setDireccionEngine(args[0]);
    	Parametros.getInstance().setPuertoEngine(Integer.parseInt(args[1]));
    	Parametros.getInstance().setDireccionKafka(args[2]);
    	Parametros.getInstance().setPuertoKafka(Integer.parseInt(args[3]));
    	Parametros.getInstance().setDireccionRegistry(args[4]);
    	Parametros.getInstance().setPuertoRegistry(Integer.parseInt(args[5]));
    	
    	
    	
    	Scanner sc = new Scanner(System.in);
    	MensajeAutenticacion mensaje = null;
    	ObjectInputStream input = null;
    	ObjectOutputStream output = null;
    	Socket cliente = null; 
    	System.out.println("Elige una opcion:");
    	System.out.println("1. Log in");
    	System.out.println("2. Registrarse");
    	System.out.println("3. Editar perfil");
    	int opcion = sc.nextInt();
    	sc.nextLine();
    	if(opcion == 1) {
    		//autenticacion
        	boolean autenticacion = false;
    		try {
    			cliente = new Socket(Parametros.getInstance().getDireccionEngine(), Parametros.getInstance().getPuertoEngine());
    			output = new ObjectOutputStream(cliente.getOutputStream());
    	    	input = new ObjectInputStream(cliente.getInputStream());
    	    	
    	    	while(!autenticacion) { 
    	    		System.out.println("Introduzca el usuario: ");
    	        	String userName = sc.nextLine();
    	        	System.out.println("Introduzca la contraseña: ");
    	        	String contraseña = sc.nextLine();
    	        	User user = new User(userName, contraseña, false);
    	        	output.writeObject(user);
    	        	mensaje = (MensajeAutenticacion) input.readObject();
    	        	if(mensaje.isAutenticado()) {
    	        		System.out.println("AUTENTICACION CORRECTA");
    	        		
    	        		autenticacion = true;
    	        	} else {
    	        		//System.out.println("AUTENTICACION INCORRECTA");
    	        		if(mensaje.isAutenticado() == false) {
    	            		System.out.println(mensaje.getErrorDescrip());
    	            		System.exit(-1);
    	            	}
    	        	}
    	    	}
    		} catch(IOException ex) {
    			System.out.println("No ha sido posible realizar la conexión con el Engine, inténtelo más tarde");
    			System.exit(-1);
    			//ex.printStackTrace();
    		} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else if (opcion == 2) {
    		String alias = null;
    		String passw = null;
    		int nivel = 0;
    		int ef = 0; 
    		int ec = 0;
    		//registro
    		try {
    			Socket clienteRegistro;
    	    	clienteRegistro = new Socket(Parametros.getInstance().getDireccionRegistry(), Parametros.getInstance().getPuertoRegistry());
    			ObjectInputStream inputRegistro = new ObjectInputStream(clienteRegistro.getInputStream());
    			ObjectOutputStream outputRegistro = new ObjectOutputStream(clienteRegistro.getOutputStream());
    			System.out.println("Introduce el alias del jugador: ");
    			alias = sc.nextLine();
    			System.out.println("Introduce la contraseña del jugador: ");
    			passw = sc.nextLine();
    			System.out.println("Introduce el nivel del jugador: ");
    			nivel = sc.nextInt();
    			System.out.println("Introduce el efecto ante el calor del jugador: ");
    			ec = sc.nextInt();
    			System.out.println("Introduce el efecto ante el frio del jugador: ");
    			ef = sc.nextInt();
    			sc.nextLine();
    			outputRegistro.writeObject(new DatosJugador(alias, passw, nivel, ec, ef));
    			inputRegistro.close();
    			outputRegistro.close();
    			clienteRegistro.close();
    			Thread.sleep(1000);
    			cliente = new Socket(Parametros.getInstance().getDireccionEngine(), Parametros.getInstance().getPuertoEngine());
    			output = new ObjectOutputStream(cliente.getOutputStream());
    	    	input = new ObjectInputStream(cliente.getInputStream());
    	    	User user = new User(alias, passw, false);
	        	output.writeObject(user);
	        	mensaje = (MensajeAutenticacion) input.readObject();
	        	
	        	if(mensaje.isAutenticado()) {
	        		System.out.println("AUTENTICACION CORRECTA");
	        	} else {
	        		System.out.println("AUTENTICACION INCORRECTA");
	        	}
    	    	
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			System.out.println("No ha sido posible realizar la conexión con el Registry, inténtelo más tarde");
    			System.exit(-1);
    			//e1.printStackTrace();
    		} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else if (opcion == 3) {
    		Socket clienteRegistro;
	    	try {
				clienteRegistro = new Socket(Parametros.getInstance().getDireccionRegistry(), Parametros.getInstance().getPuertoRegistry());
				ObjectInputStream inputRegistro = new ObjectInputStream(clienteRegistro.getInputStream());
				ObjectOutputStream outputRegistro = new ObjectOutputStream(clienteRegistro.getOutputStream());
	    		//preguntamos por usuario y contraseña para ver si esta en la bbdd
	    		System.out.println("Introduzca el usuario: ");
	        	String userName = sc.nextLine();
	        	System.out.println("Introduzca la contraseña: ");
	        	String contraseña = sc.nextLine();
	        	String alias = null;
	    		String passw = null;
	    		int nivel = 0;
	    		int ef = 0; 
	    		int ec = 0;
	    		System.out.println("Introduce el alias del jugador (si no quieres modificarlo pulsa intro): ");
    			alias = sc.nextLine();
    			System.out.println("Introduce la nueva contraseña del jugador (si no quieres modificarlo pulsa intro): ");
    			passw = sc.nextLine();
    			System.out.println("Introduce el nuevo nivel del jugador (si no quieres modificarlo pulsa intro): ");
    			try {
    				nivel = Integer.parseInt(sc.nextLine());
    			} catch(Exception ex) {
    				nivel = 0;
    			}
    			System.out.println("Introduce el nuevo efecto ante el calor del jugador (si no quieres modificarlo pulsa intro): ");
    			try {
    				ec = Integer.parseInt(sc.nextLine());
    			} catch(Exception ex) {
    				ec = 0;
    			}
    			System.out.println("Introduce el nuevo efecto ante el frio del jugador (si no quieres modificarlo pulsa intro): ");
    			try {
    				ef = Integer.parseInt(sc.nextLine());
    			} catch(Exception ex) {
    				ef = 0;
    			}
    			EditarPerfil editarPerfil = new EditarPerfil(userName, contraseña, alias, passw, nivel, ec, ef);
    			outputRegistro.writeObject(editarPerfil);
    			RespuestaEditarPerfil respuesta = (RespuestaEditarPerfil) inputRegistro.readObject();
    			if (respuesta.isCorrecto()) {
    				System.out.println("Perfil actualizado correctamente");
    			} else {
    				System.out.println(respuesta.getErrorDescription());
    			}
    			if (outputRegistro != null) outputRegistro.close();
    			if (inputRegistro != null) inputRegistro.close();
    			if (clienteRegistro != null) clienteRegistro.close();
    			System.exit(0);
			} catch (IOException e) {
				System.exit(-1);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	} else {
    		System.out.println("Opcion incorrecta, las opciones son 1, 2 o 3");
    		System.exit(-1);
    	}
    	
    	jugador.setEc(mensaje.getEc());
    	jugador.setEf(mensaje.getEf());
    	jugador.setNivel(mensaje.getNivel());
    	
    	try {
	    	System.out.println("Esperando a que comience la partida");
	    	InicioPartida inicio = (InicioPartida) input.readObject();
	    	
	    	
	    	
	    	try {
	            GlobalScreen.registerNativeHook();
	            
	        } catch (NativeHookException ex) {
	            System.err.println("There was a problem registering the native hook.");
	            System.err.println(ex.getMessage());

	            System.exit(1);
	        }

	        GlobalScreen.addNativeKeyListener(new AA_Player());
  	
	    	
	    	jugador.setId(mensaje.getId());
	    	//System.out.println("ID: " + jugador.getId());
	    	jugador.setNpc(false);
	    	ThreadLeerMapa threadd = new ThreadLeerMapa(jugador);
	    	//lanza el hilo
	    	threadd.start();
	    	ThreadPing ping = new ThreadPing(output, input, threadd);
			ping.start();
			ThreadKafkaPing kafkaPing = new ThreadKafkaPing(threadd, ping);
	    	kafkaPing.start();
	    	Random random = new Random();
	    	int xInicial = random.nextInt(20);
	    	int yInicial = random.nextInt(20);
	    	jugador.setX(xInicial);
	    	jugador.setY(yInicial);
	    	Jugada jugadaInicial = new Jugada(xInicial, yInicial, jugador);
	    	jugadaInicial.setJugadaInicial(true);
	    	enviarJugada(jugadaInicial);
	    	/*while(jugador.isMuerto() == false) {
	    		String c = sc.nextLine();
	    		if (c.equals("w")) {
	    			if (jugador.getX()-1 < 0) {
	    				jugador.setX(19);
	    			} else {
	    				jugador.setX((jugador.getX()-1));
	    			}
	    		} else if (c.equals("s")) {
	    			jugador.setX((jugador.getX()+1)%20);
	    		} else if (c.equals("a")) {
	    			if (jugador.getY()-1 < 0) {
	    				jugador.setY(19);
	    			} else {
	    				jugador.setY((jugador.getY()-1));
	    			}
	    		} else if (c.equals("d")) {
	    			jugador.setY((jugador.getY()+1)%20);
	    		} else if (c.equals("wd")) {
	    			jugador.setX(jugador.getX()-1);
	    			jugador.setY((jugador.getY()+1)%20);
	    			if(jugador.getX() < 0) {
	    				jugador.setX(19);
	    			}    			
	    		} else if (c.equals("wa")) {
	    			jugador.setX(jugador.getX()-1);
	    			jugador.setY(jugador.getY()-1);
	    			if(jugador.getX() < 0) {
	    				jugador.setX(19);
	    			} 
	    			if(jugador.getY() < 0) {
	    				jugador.setY(19);
	    			}
	    		} else if (c.equals("sd")) {
	    			jugador.setX((jugador.getX()+1)%20);
	    			jugador.setY((jugador.getY()+1)%20);
	    		} else if (c.equals("sa")) {
	    			jugador.setX((jugador.getX()+1)%20);
	    			jugador.setY(jugador.getY()-1);
	    			if(jugador.getY() < 0) {
	    				jugador.setY(19);
	    			}
	    		}
	        	Jugada jugada = new Jugada(jugador.getX(), jugador.getY(), jugador);
	        	enviarJugada(jugada);
	    	}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
