package audiomixer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * arecord --device=plughw:CARD=SB,DEV=0 --format=S16_LE --rate=48000
 * --channels=2 fifo
 */
public class PipeSource extends BaseSource {

	final String pipePath;

	public PipeSource(String pipePath, int threshold) {
		super(threshold);
		this.pipePath = pipePath;
	}

	@Override
	InputStream getInputStream() throws Exception {
		File file = new File(pipePath);

		if (!file.exists()) {
			return null;
		}

		return new FileInputStream(file);
	}

	@Override
	String getLocation() {
		return pipePath;
	}

}
