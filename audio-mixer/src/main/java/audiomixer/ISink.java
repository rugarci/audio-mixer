package audiomixer;

import java.util.List;

import audiomixer.ISource;

public interface ISink {
	List<ISource> getSources();

	void write(byte[] frame);
}