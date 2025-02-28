
public class GamePlay extends BasicShooter{

	private int x1,x2,y1,y2;
	
	public void play() {
		x1=0;
		y1=0;
		x2=15;
		y2=15;
		while(true) {
			if(checkDistance()) {
				System.out.println("The ball has been shot!");
			}
			x1++;
			y2++;
		}
	}
	
	public boolean checkDistance() {
		
		double distance = Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		if(distance==0) {
			return true;
		} else
			return false;
		
	}
	
}
