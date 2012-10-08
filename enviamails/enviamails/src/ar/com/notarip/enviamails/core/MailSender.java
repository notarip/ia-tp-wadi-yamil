package ar.com.notarip.enviamails.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	private Properties props;
	private ArrayList<String> listaPara = new ArrayList<String>();
	private String sender;
	private String asunto;
	private String mensaje;
	
	
	private String smtp;
	private String tls;
	private String port;
	private String user;
	private String auth;
	private String pass;
	
	
	
	public MailSender(String sender, String para, String asunto, String mensaje) {
		
		this.setSender(sender);
		this.setPara(para);
		this.setAsunto(asunto);
		this.setMensaje(mensaje);
		this.iniciar();
		
	}
	
	
	private void iniciar() {
		
		ResourceBundle rb = ResourceBundle.getBundle("conf"); 

		props = new Properties();
		
		// // Nombre del host de correo, es smtp.gmail.com
		props.setProperty("mail.smtp.host", rb.getString("smtp").trim()); //smtp.google.com

		// TLS si está disponible
		props.setProperty("mail.smtp.starttls.enable", rb.getString("tls").trim());//true

		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port",rb.getString("port").trim()); //587

		// Nombre del usuario
		props.setProperty("mail.smtp.user", rb.getString("user").trim());//notarip.com.ar@gmail.com

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", rb.getString("auth").trim());//true
		
		System.out.println(rb.getString("ssl").trim());
		
		props.setProperty("mail.smtp.ssl.enable", rb.getString("ssl").trim());//true
		
		
		setUser(rb.getString("user").trim());
		setPass(rb.getString("pass").trim());
		
	}

	public void enviar() {
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		
		MimeMessage message = new MimeMessage(session);
		
		// Quien envia el correo
		try {
			message.setFrom(new InternetAddress(getSender()));
		} catch (AddressException e1) {
			
			e1.printStackTrace();
		} catch (MessagingException e1) {
			
			e1.printStackTrace();
		}

		// A quien va dirigido
		for (Iterator<String> iterator = this.getListaPara().iterator(); iterator.hasNext();) {
			String para = (String) iterator.next();
			
			try {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(para));
			} catch (AddressException e) {
				
				e.printStackTrace();
			} catch (MessagingException e) {
				
				e.printStackTrace();
			}
			
		}
		
		
		try {
			message.setSubject(getAsunto());
			message.setText(getMensaje());
			
			Transport t = session.getTransport("smtp");
			
			
			t.connect(getUser(),getPass());
			t.sendMessage(message,message.getAllRecipients());
			
			t.close();
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
			
	}

	public ArrayList<String> getListaPara() {
		return listaPara;
	}

	public void setPara(String para) {
		this.listaPara.add(para);
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getAsunto() {
		return asunto;
	}


	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	

	public Properties getProps() {
		return props;
	}
	
	public void setProps(Properties props) {
		this.props = props;
	}
	
	public String getSmtp() {
		return smtp;
	}
	
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	
	public String getTls() {
		return tls;
	}
	
	public void setTls(String tls) {
		this.tls = tls;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setListaPara(ArrayList<String> listaPara) {
		this.listaPara = listaPara;
	}
	
	public MailSender() {
		
		this.iniciar();		
	}
	
}