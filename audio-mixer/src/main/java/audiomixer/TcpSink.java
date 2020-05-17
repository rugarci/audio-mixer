package audiomixer;

import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class TcpSink extends BaseSink {

	private String host;
	private int port;
	private Socket socket;

	public TcpSink(List<ISource> sources, String host, int port) {
		super(sources);
		this.host = host;
		this.port = port;
	}

	@Override
	OutputStream getOutputStream() throws Exception {
		if (socket != null && socket.isConnected()) {
			try {
				socket.close();
			} catch (Exception ignore) {
			}
		}
		socket = new Socket(host, port);
		return socket.getOutputStream();
	}

	@Override
	String getLocation() {
		return host + ":" + port;
	}
}
