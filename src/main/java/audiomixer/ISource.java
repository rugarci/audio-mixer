package audiomixer;

public interface ISource {
	byte[] read();

	void flush();
	
	void end();
}