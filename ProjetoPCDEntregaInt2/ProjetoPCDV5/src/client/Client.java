package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {
	private Socket socket;
	private InetAddress ip;
	private int port;
	private boolean useArrows;

	public Client(InetAddress ip, int port,boolean useArrows) {
		this.ip=ip;
		this.port=port;
		this.useArrows=useArrows;
	}
	
	public static void main(String[] args) throws IOException {
		InetAddress serverip=InetAddress.getByName(args[0]);
		int serverport=Integer.parseInt(args[1]);
		boolean choice=true;
		int which=JOptionPane.showConfirmDialog(null, "Choose Yes to play with arrow keys or No for WSAD keys");
		if(which==0)choice=true;
		else choice=false;
		
		Client client=new Client(serverip,serverport,choice);
		client.runClient();
	}

	public void runClient() throws IOException {
		try {
			connectToServer();
			DealWithServer dws = new DealWithServer(socket);
			dws.start();

			// para manter a ligacao aberta
			while (!dws.isGameOver()) {
				try {
					Thread.sleep(2000);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally {

			try {
				socket.close();
				JOptionPane.showMessageDialog(null, "O jogo terminou!");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void connectToServer() throws IOException {		
		System.out.println("Endereco = " + ip);
		socket = new Socket(ip, port);
		System.out.println("Socket = " + socket);
	}

}
