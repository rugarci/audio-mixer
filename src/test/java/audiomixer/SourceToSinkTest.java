package audiomixer;

import java.util.Collections;

public class SourceToSinkTest {

	public void run() {
		// ISource source = new ArecordSource("hw:CARD=SB,DEV=0", 50);
		ISource source = new TcpSource(5555, 50);
		ISink sink = new TcpSink(Collections.singletonList(source), "192.168.8.128", 4953);

		while (true) {
			sink.write(source.read());
		}
	}

	public static void main(String[] args) {
		new SourceToSinkTest().run();
	}
}
