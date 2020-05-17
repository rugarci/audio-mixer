package audiomixer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * arecord --device=plughw:CARD=SB,DEV=0 --format=S16_LE --rate=48000
 * --channels=2 | nc 127.0.0.1 5555
 */
public class TcpSource extends BaseSource {

	private final int port;

	private final ServerSocket serverSocket;
	private Socket socket;

	public TcpSource(int port, int threshold) {
		super(threshold);
		this.port = port;

		try {
			serverSocket = new ServerSocket(port);

			Thread serverSocketThread = new Thread() {

				@Override
				public void run() {

					while (!serverSocket.isClosed()) {
						try {
		
							Socket newSocket = serverSocket.accept();
							try {
								if (socket != null) {
									socket.close();
								}
							} catch (IOException ignore) {
							}
							
							socket = newSocket;
							System.out.println("Client connected");

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};

			serverSocketThread.start();

		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

	@Override
	protected InputStream getInputStream() throws Exception {
		if (socket == null || socket.isClosed()) {
			return null;
		}
		return socket.getInputStream();
	}

	@Override
	protected String getLocation() {
		return "tcp://0.0.0.0:" + port;
	}

	@Override
	public void end() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException ignore) {
		}
		try {
			serverSocket.close();
		} catch (IOException ignore) {
		}
		super.end();
	}

}
