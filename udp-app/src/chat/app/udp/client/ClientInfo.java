package chat.app.udp.client;

import java.net.InetAddress;
public class ClientInfo {

	InetAddress inetAdr ;
	public InetAddress getInetAdr() {
		return inetAdr;
	}
	public void setInetAdr(InetAddress inetAdr) {
		this.inetAdr = inetAdr;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	int port ;
	public ClientInfo(InetAddress adr,int pr){
		this.inetAdr = adr;
		this.port = pr;
	}
}