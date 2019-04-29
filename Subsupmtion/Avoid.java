import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class Avoid implements Behavior {

	private boolean suppressed;
	NXTUltrasonicSensor USS;
	MovePilot pilot;
	SampleProvider USSReading;
	float[] sample;
	


	public Avoid(NXTUltrasonicSensor ultraSensorFront, MovePilot p) {
		suppressed = false;
		USS = ultraSensorFront;
		pilot = p;
		USSReading = USS.getDistanceMode();
		sample = new float[USSReading.sampleSize()];
	}

	@Override
	public boolean takeControl() {
		USSReading.fetchSample(sample, 0);
		return (sample[0] < 0.22);
	}

	@Override
	public void action() {
		System.out.println(sample[0]);
		suppressed = false;
		
		while(!suppressed)
		{
			USSReading.fetchSample(sample, 0);

			System.out.println(sample[0]);
			if(sample[0] < 0.22)
			{
				pilot.rotate(67);
				pilot.forward();
				
				
			}
			pilot.travel(100);
			pilot.forward();
			USSReading.fetchSample(sample, 0);
			if(sample[0] > 0.25)
			{
				suppressed = true;
				break;
			}
		}

	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}