package ar.com.notarip.enviamails.core;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Main_Mail {

	/**
	 * @param args
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	public static void main(String[] args) throws AddressException, MessagingException {
		

		if (args.length == 1 && args[0].toString().equals("-h")){
			System.out.println("Ingrese: [nombre] [mail destino] [asunto] [mensaje]");
		}else{
		
			MailSender sender;
			
				
			if (args.length == 0)
				sender = new MailSender("Mi@Aviso", "notarip@gmail.com", "Prueba 1", "Esta es una prueba de envio de mail");
			else
				sender = new MailSender(args[0], args[1], args[2], args[3]);
			
			sender.enviar();
		
		}
		
	}

}
