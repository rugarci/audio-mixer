package audiomixer;

import java.io.OutputStream;
import java.util.List;

public abstract class BaseSink implements ISink {

	private final List<ISource> sources;

	private long lastOpenTime;

	private OutputStream outputStream;

	public BaseSink(List<ISource> sources) {
		this.sources = sources;
	}

	private boolean init() throws Exception {
		if (System.currentTimeMillis() - lastOpenTime < 5000) {
			return false;
		}
		lastOpenTime = System.currentTimeMillis();
		outputStream = getOutputStream();

		System.out.println("Sink started on " + getLocation());

		return true;
	}

	protected abstract String getLocation();

	protected abstract OutputStream getOutputStream() throws Exception;

	@Override
	public List<ISource> getSources() {
		return sources;
	}

	@Override
	public void write(byte[] frame) {
		if (frame == null) {
			return;
		}
		try {
			if (outputStream == null) {
				boolean open = init();

				if (!open) {
					return;
				}
			}
			outputStream.write(frame);
		} catch (Exception e) {

			e.printStackTrace();
			outputStream = null;
		}

	}

}
