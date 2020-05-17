package audiomixer;

import java.io.InputStream;

public class ArecordSource extends BaseSource {

	private final String device;
	private Process process;

	public ArecordSource(String device, int threshold) {
		super(threshold);
		this.device = device;
	}

	@Override
	protected InputStream getInputStream() throws Exception {
		process = new ProcessBuilder("bash", "-c", "/usr/bin/arecord --device=" + device + " --format=S16_LE --rate=48000 --channels=2").start();
		return process.getInputStream();
	}

	@Override
	protected String getLocation() {
		return "arecord " + device;
	}

	@Override
	public void end() {
		super.end();
		if (process != null) {
			process.destroy();
		}
	}

}
