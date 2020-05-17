package audiomixer;

import java.io.InputStream;

public class ArecordSource extends BaseSource {

	final String device;
	Process process;

	public ArecordSource(String device, int threshold) {
		super(threshold);
		this.device = device;
	}

	@Override
	InputStream getInputStream() throws Exception {
		process = new ProcessBuilder("bash", "-c", "/usr/bin/arecord --device=" + device + " --format=S16_LE --rate="
				+ (int) audioFormat.getFrameRate() + " --channels=" + audioFormat.getChannels()).start();
		return process.getInputStream();
	}

	@Override
	String getLocation() {
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
