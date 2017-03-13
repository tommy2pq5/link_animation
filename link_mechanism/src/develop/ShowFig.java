package develop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

public class ShowFig {
	double a=32;	//å¥ìÆêﬂ
	double b=90;	//íÜä‘êﬂ
	double c=52;	//è]ìÆêﬂ
	double d=90;	//å≈íËêﬂ
	
	double HVRatio=3.0/4.0;
	double phi=78.0;
	double theta=76.14158327;
	
	double[][] points;
	
	double drawScale=5;
	double divInterval=10.0;
	
	int offsetX=300;
	int offsetY=500;
	
	JFrame mainFrame=new JFrame();
	DrawPanel drawPanel=new DrawPanel();

	public static void main(String[] ar){
		new ShowFig();
	}
	public ShowFig(){
		mainFrame.setTitle("Link Mechanism");
		mainFrame.setSize(1024, 768);
		mainFrame.add(drawPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
		double gamma=Math.atan(HVRatio);
		points=calcPoints(a,b,c,d,gamma, Math.toRadians(phi), Math.toRadians(theta));
		
		mainFrame.repaint();
	}
	private class DrawPanel extends JPanel{
		public DrawPanel(){
			//setBackground(Color.MAGENTA);
		}
		public void paintComponent(Graphics g){
			Graphics2D g2=(Graphics2D)g;
			
			g2.setBackground(new Color(0, 0, 127));
			g2.clearRect(0, 0, 1024, 768);
			
			int[][] drawPoints=new int[4][2];
			for(int i=0;i<=3;i++){
				drawPoints[i][0]=(int)(drawScale*points[i][0]+offsetX);
				drawPoints[i][1]=(int)(-drawScale*points[i][1]+offsetY);
			}
			
			//draw axis
			float[] dash={3,3};
			g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10,dash,0));
			g2.setColor(Color.LIGHT_GRAY);
			for(int i=-200;i<200;i+=divInterval){
				g2.drawLine((int)(offsetX+i*drawScale), 0, (int)(offsetX+i*drawScale), 768);
				g2.drawLine(0, (int)(offsetY+i*drawScale),1024, (int)(offsetY+i*drawScale));
			}

			
			//draw divs
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.WHITE);
			g2.drawLine(offsetX, 0, offsetX, 768);
			g2.drawLine(0, offsetY,1024, offsetY);
			
			//draw link
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.RED);
			g2.drawLine(drawPoints[0][0], drawPoints[0][1], drawPoints[1][0], drawPoints[1][1]);
			g2.setColor(Color.YELLOW);
			g2.drawLine(drawPoints[1][0], drawPoints[1][1], drawPoints[2][0], drawPoints[2][1]);
			g2.setColor(new Color(0,200,0));
			g2.drawLine(drawPoints[2][0], drawPoints[2][1], drawPoints[3][0], drawPoints[3][1]);
			g2.setColor(Color.WHITE);
			g2.drawLine(drawPoints[3][0], drawPoints[3][1], drawPoints[0][0], drawPoints[0][1]);
			
			//draw info.
			int infoY=20;
			int fontSize=20;
			g2.setFont(new Font("Time New Roman", Font.PLAIN, fontSize));
			g2.setColor(Color.RED);
			g2.drawString(String.format("a=%g", a), 800, infoY);infoY+=fontSize;
			g2.setColor(Color.YELLOW);
			double b_shown=Math.sqrt(Math.pow((points[2][0]-points[1][0]), 2)-Math.pow((points[2][1]-points[1][1]), 2));
			g2.drawString(String.format("b=%g(%g)", b,b_shown), 800, infoY);infoY+=fontSize;
			g2.setColor(new Color(0,200,0));
			g2.drawString(String.format("c=%g", c), 800, infoY);infoY+=fontSize;
			g2.setColor(Color.WHITE);
			g2.drawString(String.format("d=%g", d), 800, infoY);infoY+=fontSize;
			
			g2.drawString(String.format("HVR=%g", HVRatio), 800, infoY);infoY+=fontSize;
			g2.drawString(String.format("É≥=%g", phi), 800, infoY);infoY+=fontSize;
			g2.drawString(String.format("É∆=%g", theta), 800, infoY);infoY+=fontSize;
			g2.drawString(String.format("div.=%g", divInterval), 800, infoY);infoY+=fontSize;
		}
	}
	
	public double[][] calcPoints(double a, double b, double c, double d, double gamma, double phi, double theta){
		double[][] result=new double[4][2];
		
		//å¥ìÆé≤
		result[0][0]=d*Math.cos(gamma); 
		result[0][1]=0;
		
		//å¥ìÆêﬂêÊí[
		result[1][0]=result[0][0]+a*Math.cos(Math.PI-phi);
		result[1][1]=result[0][1]+a*Math.sin(phi);
		
		//è]ìÆé≤
		result[3][0]=0;
		result[3][1]=d*Math.sin(gamma);
		
		//è]ìÆêﬂêÊí[
		result[2][0]=result[3][0]+c*Math.cos((2.0/2.0)*Math.PI+theta);
		result[2][1]=result[3][1]+c*Math.sin((2.0/2.0)*Math.PI+theta);	
		
		
		return result;
	}
}
