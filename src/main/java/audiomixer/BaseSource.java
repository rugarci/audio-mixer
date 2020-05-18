package audiomixer;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public abstract class BaseSource implements ISource {

	private final int threshold;
	private final AudioFormat audioFormat;
	private final byte[] buffer;

	private	long lastOpenTime;
	private boolean silent = false;
	private int soundCount = 0;
	private int silenceCount = 0;

	private AudioInputStream audioInputStream;

	public BaseSource(int threshold) {
		this.threshold = threshold;
		audioFormat = new AudioFormat(48000f, 16, 2, true, false);
		buffer = new byte[audioFormat.getFrameSize()];
	}

	private boolean init() throws Exception {
		if (System.currentTimeMillis() - lastOpenTime < 5000) {
			return false;
		}
		lastOpenTime = System.currentTimeMillis();
		InputStream is = getInputStream();

		if (is == null) {
			System.out.println("Source not ready on " + getLocation());
			return false;
		}

		audioInputStream = new AudioInputStream(getInputStream(), audioFormat, AudioSystem.NOT_SPECIFIED);
		System.out.println("Source ready on " + getLocation());
		silent = false;
		soundCount = 0;
		silenceCount = 0;
		return true;
	}

	protected abstract String getLocation();

	protected abstract InputStream getInputStream() throws Exception;

	@Override
	public byte[] read() {

		try {
			if (audioInputStream == null) {
				boolean open = init();
				if (!open) {
					return null;
				}
			}
			int available = audioInputStream.available();
			 //System.out.println("available " + available);
			if (available >= buffer.length) {
				audioInputStream.read(buffer);
				int level = getFrameLevel(buffer, 0);

				//System.out.println("level " + level);

				if (level > threshold) {
					soundCount++;
					silenceCount = 0;
				} else {
					silenceCount++;
					soundCount = 0;
				}

				if (silent && soundCount > 50) {
					System.out.println("Sound");
					silent = false;
				}

				if (!silent && silenceCount > 1000) {
					System.out.println("Silence");
					silent = true;
				}

				if (!silent) {
					return buffer;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			audioInputStream = null;
		}

		return null;
	}

	private static int getFrameLevel(byte[] buffer, int offset) {
		short ch1 = (short) (buffer[offset + 1] << 8 | buffer[offset]);
		short ch2 = (short) (buffer[offset + 3] << 8 | buffer[offset + 2]);

		return Math.abs(ch1) + Math.abs(ch2) / 2;
	}

	@Override
	public void flush() {
		if (audioInputStream != null) {
			try {
				audioInputStream.readAllBytes();
			} catch (IOException ignore) {
			}
		}
	}

	@Override
	public void end() {
		if (audioInputStream != null) {
			try {
				audioInputStream.close();
			} catch (IOException ignore){
			}
		}
	}

}
