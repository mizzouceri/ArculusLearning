package com.cyberrange.api.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Component
@PropertySource("classpath:remoteconnection.properties")
public class RemoteConnection {

	@Value("${host}")
    private String host;
	
	@Value("${port}")
	private int port;
	
	@Value("${geniusername}")
	private String username;
	
	@Value("${privateKey}")
	private String privateKey;
	
	@Value("${passphrase}")
	private String passphrase;

	@Value("${connectionType}")
    private String connectionType; 
	
	public String remoteConnection(String command) throws Exception {

		String output = "";
		
		if("L".equalsIgnoreCase(connectionType)) {
		
			output = doLocalConnection(command);
		
		}else {
			
			output = doRemoteConnection(command);
		}
		System.out.println(output);
		
		return output;
	}
	
	public String doLocalConnection(String command) throws Exception {

		BufferedReader reader = null;
		InputStreamReader in=null;
		String finalOutput = "";

		try {
			Process process = Runtime.getRuntime().exec(command);
			in = new InputStreamReader(process.getInputStream());

			reader = new BufferedReader(in);
			String output = "";
			while ((output = reader.readLine()) != null) {
				finalOutput = finalOutput + output;
			}
		} catch (Exception e) {
			throw e;
		}finally {
			
			if(null != reader )
				reader.close();
			if(null != in)
				in.close();
		
		}
		
		return finalOutput;

	}
	public String doRemoteConnection(String command) throws IOException {
		
		
		InputStream in=null;
		Session session = null;
		Channel channel = null;
		String output = null;
		
		
		try {

			JSch jsch = new JSch();

			jsch.addIdentity(privateKey, passphrase);
			session = jsch.getSession(username, host, port);
			
			System.out.println("session created.");
			
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect();
			System.out.println("session connected.....");

				
				
			channel = session.openChannel("exec"); 
			((ChannelExec)channel).setCommand(command);
			
			in = channel.getInputStream(); 
			((ChannelExec)channel).setErrStream(System.err);
			
			channel.connect();
			
			output = getResponseFromRemoteServer(in, channel);
			//System.out.println(output);
				
				
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {

			in.close();
			channel.disconnect();
			session.disconnect();
		}
		
		return output;
	}

	public String getResponseFromRemoteServer(InputStream in, Channel channel) throws IOException {
		
		String output = null;
		String finalOutput = "";
		try {
			
			byte[] tmp = new byte[1024];
			
			while (true) {
				while (in.available() > 0) {
					
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					
					output = new String(tmp, 0, i);
					finalOutput= finalOutput + output;
				}
				if (channel.isClosed()) {

					if (in.available() > 0)
						continue;
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
			
				Thread.sleep(1000);
			}	
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return finalOutput;
	}

}
