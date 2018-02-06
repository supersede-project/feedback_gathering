package monitoring.tools.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshConnectionTest {
	
	private final static String USER = "supersede";
	private final static String HOST = "tools.supersede.atos-sports.tv";
	private final static String INSTRUCTION = "snmpdf -v2c -c supersede.ovp prt.tbs";
	
	public static void main(String[] args) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("ssh");
		
		JSch jsch = new JSch();
		
		jsch.addIdentity(url.toURI().getPath());
		System.out.println("Added private key file");
		
		Session session = jsch.getSession(USER, HOST);
		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		System.out.println("Session started");
		
		session.connect();
		System.out.println("Session connected...");
		
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(INSTRUCTION);
		BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
		channel.connect();		
		
		String msg = null;
		StringBuilder sb = new StringBuilder();
		while( (msg = in.readLine()) !=null){
			sb.append(msg + "\n");
		}
				
		System.out.println("****OUTPUT*****");
		System.out.println(sb);
		System.out.println("***************");
		
		channel.disconnect();
		session.disconnect();
		
		System.out.println("Connection stopped");
		
	}

}
