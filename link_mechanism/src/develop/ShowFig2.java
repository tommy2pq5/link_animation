package develop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShowFig2 {
	//åvéZÉfÅ[É^
	/*
	double a=32;	//å¥ìÆêﬂ
	double b=90;	//íÜä‘êﬂ
	double c=52;	//è]ìÆêﬂ
	double d=90;	//å≈íËêﬂ
	
	
	double phi=78.0;
	double theta=76.14158327;
	*/
	
	double HVRatio=3.0/4.0;
	double gamma=Math.atan(HVRatio);
	
	//double data[]={a,b,c,d,gamma, Math.toRadians(phi),Math.toRadians(theta)};
	
	List<List<Double>> dataList;
	
	
	//ï`âÊóp
	double[][] points;
	
	double drawScale=5;
	double divInterval=10.0;
	
	int offsetX=300;
	int offsetY=500;
	
	int index=2;
	
	//GUI
	JFrame mainFrame=new JFrame();
	DrawPanel drawPanel=new DrawPanel();

	public static void main(String[] ar){
		new ShowFig2();
	}
	public ShowFig2(){
		dataList=readLinkSimCSV("link.csv");
		
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	points=calcPoints(dataList.get(0).get(0),dataList.get(0).get(1),dataList.get(0).get(2),dataList.get(0).get(3),gamma, Math.toRadians(dataList.get(index).get(0)),Math.toRadians(dataList.get(index).get(1)));
	        	mainFrame.repaint();
	        	if(index>=dataList.size()-1){
	        		index=2;
	        	}else{
	        		index++;
	        	}
	        }
	    };
	    
	    Timer timer = new Timer();
	    timer.schedule(task, 0L, 1000L);
		
		mainFrame.setTitle("Link Mechanism");
		mainFrame.setSize(1024, 768);
		mainFrame.add(drawPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setVisible(true);
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
			
			//âÊñ ç¿ïWÇ÷ÇÃïœä∑
			int[][] drawPoints=new int[4][2];
			for(int i=0;i<=3;i++){
				drawPoints[i][0]=(int)(drawScale*points[i][0]+offsetX);
				drawPoints[i][1]=(int)(-drawScale*points[i][1]+offsetY);
			}
			
			//draw div.
			float[] dash={3,3};
			g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10,dash,0));
			g2.setColor(Color.LIGHT_GRAY);
			for(int i=-200;i<200;i+=divInterval){
				g2.drawLine((int)(offsetX+i*drawScale), 0, (int)(offsetX+i*drawScale), 768);
				g2.drawLine(0, (int)(offsetY+i*drawScale),1024, (int)(offsetY+i*drawScale));
			}

			
			//draw axis
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
			g2.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
			g2.setColor(Color.RED);
			g2.drawString(String.format("a=%g", dataList.get(0).get(0)), 800, infoY);infoY+=fontSize;
			g2.setColor(Color.YELLOW);
			double b_shown=Math.sqrt(Math.pow((points[2][0]-points[1][0]), 2)-Math.pow((points[2][1]-points[1][1]), 2));
			g2.drawString(String.format("b=%g(%g)", dataList.get(0).get(1),b_shown), 800, infoY);infoY+=fontSize;
			g2.setColor(new Color(0,200,0));
			g2.drawString(String.format("c=%g", dataList.get(0).get(2)), 800, infoY);infoY+=fontSize;
			g2.setColor(Color.WHITE);
			g2.drawString(String.format("d=%g", dataList.get(0).get(3)), 800, infoY);infoY+=fontSize;
			
			g2.drawString(String.format("HVR=%g", HVRatio), 800, infoY);infoY+=fontSize;
			g2.drawString(String.format("É≥=%g", dataList.get(index).get(0)), 800, infoY);infoY+=fontSize;
			g2.drawString(String.format("É∆=%g", dataList.get(index).get(1)), 800, infoY);infoY+=fontSize;
			g2.drawString(String.format("div.=%g", divInterval), 800, infoY);infoY+=fontSize;
			
			System.out.println(index);
		}
	}
	
	public double[][] calcPoints(double a, double b, double c, double d, double gamma, double phi, double theta){
		double[][] result=new double[4][2];
		
		//å¥ìÆé≤
		result[0][0]=d*Math.cos(gamma); 
		result[0][1]=0;
		
		//å¥ìÆêﬂêÊí[
		result[1][0]=result[0][0]+a*Math.cos(Math.PI-theta);
		result[1][1]=result[0][1]+a*Math.sin(Math.PI-theta);
		
		//è]ìÆé≤
		result[3][0]=0;
		result[3][1]=d*Math.sin(gamma);
		
		//è]ìÆêﬂêÊí[
		result[2][0]=result[3][0]+c*Math.cos((2.0/2.0)*Math.PI+phi);
		result[2][1]=result[3][1]+c*Math.sin((2.0/2.0)*Math.PI+phi);	
		
		
		return result;
	}
	
	/*
	 * double data[]={a,b,c,d,gamma, Math.toRadians(phi),Math.toRadians(theta)};
	 
	public double[][] calcPoints(double[] data){
		double[][] result=new double[4][2];
		
		//å¥ìÆé≤
		result[0][0]=data[3]*Math.cos(data[4]); 
		result[0][1]=0;
		
		//å¥ìÆêﬂêÊí[
		result[1][0]=result[0][0]+data[0]*Math.cos(Math.PI-data[5]);
		result[1][1]=result[0][1]+data[0]*Math.sin(data[5]);
		
		//è]ìÆé≤
		result[3][0]=0;
		result[3][1]=data[3]*Math.sin(data[4]);
		
		//è]ìÆêﬂêÊí[
		result[2][0]=result[3][0]+data[2]*Math.cos((2.0/2.0)*Math.PI+data[6]);
		result[2][1]=result[3][1]+data[2]*Math.sin((2.0/2.0)*Math.PI+data[6]);	
		
		
		return result;
	}
	*/
	public List<List<Double>> readLinkSimCSV(String path){
		List<List<Double>> dataList=new ArrayList<List<Double>>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String line;
			StringTokenizer token;
			while((line = br.readLine())!=null){
				token = new StringTokenizer(line, ",");

				List<Double> data=new ArrayList<Double>();
				while(token.hasMoreTokens()){
					try{
						Pattern p=Pattern.compile("[0-9|.]+");
						Matcher m=p.matcher(token.nextToken());
						m.find();
						String matched=m.group();
						data.add(Double.parseDouble(matched));
					}catch(IllegalStateException ise){
						data.add(0.0d);
						System.err.println("No match found");
					}
					
				}
				
				dataList.add(data);
			}
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}		
		
		return dataList;
	}
}
