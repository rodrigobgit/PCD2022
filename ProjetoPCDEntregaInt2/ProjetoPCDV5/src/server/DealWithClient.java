package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class DealWithClient extends Thread {

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public DealWithClient(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void doConnections(Socket socket) throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream())), true);
	}

	private void serve() throws IOException {
		while (true) {
			String str = in.readLine();
			if (str.equals("FIM"))
				break;
			System.out.println("Eco: " + str);
			out.println(str);
		}
	}

}