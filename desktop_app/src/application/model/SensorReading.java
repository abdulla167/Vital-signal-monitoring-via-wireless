package application.model;

public class SensorReading {
	private static int count=0;
	private double value;
	private int readingNumber;
	private static boolean playing = true;
	
	public SensorReading(double value)
	{
		this.value = value;
		this.readingNumber= count;
		if (playing)
		{
		count++;
		}
		
	}
	
	public static boolean isPlaying() {
		return playing;
	}

	public static void setPlaying(boolean flag) {
		playing = flag;
	}

	public SensorReading(SensorReading reading)
	{
		this.value = reading.getValue();
		this.readingNumber= reading.getReadingNumber();
	}

	public  int getReadingNumber() {
		return this.readingNumber;
	}

	public void setReadingNumber(int readingNumber) {
		this.readingNumber = readingNumber;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	

}
