package application;

import java.awt.*;
import java.awt.event.InputEvent;

public class Mouse {

	private Robot bot;

	public Mouse() {
		try {
			bot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void moveMouse(int x, int y){
		bot.mouseMove(x,y);
	}

	public void leftClick() {
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		bot.delay(50);
	}

	public void doubleClick(){
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		bot.delay(50);
	}

	public void rightClick(){
		bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		bot.delay(50);
	}

	public void scroll(int notches){
		bot.mouseWheel(notches);
	}

	public Point getCoordinates(){
		PointerInfo info = MouseInfo.getPointerInfo();
		return info.getLocation();
	}
	
	public void perform(String action) {
		Point curr = MouseInfo.getPointerInfo().getLocation();
		String [] cmd = action.split(":");
		try {
			if(cmd.length > 0) {
				String command = cmd[0];
				if(command.equals("l")) {
					leftClick();
				}else if(command.equals("r")) {
					rightClick();
				}else if(command.equals("d")) {
					doubleClick();
				}else if(command.equals("m")) {
					int x = (int) Float.parseFloat(cmd[1]);
					int y = (int) Float.parseFloat(cmd[2]);

					moveMouse((int)(curr.getX()+x),(int)(curr.getY()+y));
				}else if(command.equals("s")) {
					int dir = (int) Integer.parseInt(cmd[1]);
					scroll(dir);
				}
			}
		}catch(NumberFormatException n) {
			System.out.println(n);
		}
		
	}
	
}
