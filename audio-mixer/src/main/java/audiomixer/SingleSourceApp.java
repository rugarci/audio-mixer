package audiomixer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SingleSourceApp {

	List<ISink> sinks = new ArrayList<>();

	public SingleSourceApp() {

		ISource source = new ArecordSource("hw:CARD=SB,DEV=0", 50);
		ISink sink = new TcpSink(Collections.singletonList(source), "192.168.8.128", 4953);

		sinks.add(sink);
		run();
	}

	public void run() {

		while (true) {
			for (ISink sink : sinks) {
				boolean done = false;
				for (ISource source : sink.getSources()) {

					if (done) {
						source.flush();
					} else {
						byte[] frame = source.read();
						if (frame != null) {
							sink.write(frame);
							done = true;
						}
					}
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new SingleSourceApp();
	}

}
