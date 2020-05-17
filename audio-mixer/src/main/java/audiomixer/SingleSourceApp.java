package audiomixer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleSourceApp {

	private final List<ISink> sinks = new ArrayList<>();

	public SingleSourceApp() {

	}

	public void add(ISink sink) {
		sinks.add(sink);
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

		if (args.length != 4) {
			String exampleArgs = "hw:CARD=SB,DEV=0 50 192.168.8.128 4953";
			System.out.println("Try with " + exampleArgs);
			return;
		}
		SingleSourceApp singleSourceApp = new SingleSourceApp();

		ISource source = new ArecordSource(args[0], Integer.parseInt(args[1]));
		ISink sink = new TcpSink(Collections.singletonList(source), args[2], Integer.parseInt(args[3]));

		singleSourceApp.add(sink);
		singleSourceApp.run();
	}

}
