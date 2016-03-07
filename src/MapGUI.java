import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Stack;


public class MapGUI {
	public static void main(String[] args){
		MapGUI mGUI = new MapGUI();
		Map m = new Map("distance.txt","busline.txt","special.txt");
		mGUI.init(m);
	}
	void init(Map m){
		JFrame mainFrame = new JFrame("MapSearch");
		mainFrame.setBounds(100,100,1400,700);	
		mainFrame.getContentPane().setLayout(null);
		
	
				
		
		
		MapPanel mapPanel = new MapPanel();
		mapPanel.setVisible(true);
		

		
		Font font = new Font(Font.DIALOG,Font.BOLD,25);
		JLabel startLabel = new JLabel("起点：");
		startLabel.setFont(font);
		
		JLabel destLabel = new JLabel("终点：");
		destLabel.setFont(font);
		
		JTextField startTF = new JTextField(20);	
		JTextField destTF = new JTextField(20);
			
		JPanel startPanel = new JPanel();
		startPanel.setSize(200,40);
		startPanel.add(startLabel);
		startPanel.add(startTF);
		JPanel destPanel = new JPanel();
		destPanel.setSize(200,40);
		destPanel.add(destLabel);
		destPanel.add(destTF);
		
		JButton buttonDriving = new JButton("开车");
		
		JButton buttonWalking = new JButton("步行");
		JButton buttonBus = new JButton("公交");
		JButton buttonRe = new JButton("重置");
		JPanel buttonPanel = new JPanel();
	
		buttonPanel.add(buttonDriving);
		buttonPanel.add(buttonWalking);
		buttonPanel.add(buttonBus);
		buttonPanel.add(buttonRe);
		
		
		JPanel funPanel1 = new JPanel();
		funPanel1.setLayout(new GridLayout(2,1));
		funPanel1.add(startPanel);
		funPanel1.add(destPanel);
		
		JPanel funPanel2 = new JPanel();
		funPanel2.setLayout(new GridLayout(2,1));
		funPanel2.add(funPanel1);
		funPanel2.add(buttonPanel);
		
		JTextArea funPlan = new JTextArea();
		funPlan.setLineWrap(true);		
	//	new JScrollPane(funPlan);
		funPlan.setEditable(false);
		
		funPlan.setColumns(30);
		funPlan.setRows(5);
		funPlan.setFont(font);
		
		JPanel funPanel = new JPanel();
		funPanel.setLayout(new GridLayout(2,1));
		funPanel.setBounds(900,50,450,500);
		funPanel.add(funPanel2);
		funPanel.add(funPlan);
		
		
		mainFrame.getContentPane().add(mapPanel);
		
		
		mainFrame.getContentPane().add(funPanel);
		mainFrame.setVisible(true);
		
		
		buttonDriving.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapPanel.reset();
				funPlan.setText(null);
				String start = startTF.getText();
				String dest = destTF.getText();
				if ((start.length()<1)&&(dest.length()>0)){
					int dest1 = dest.charAt(0)-'A';
					try {
						m.getCertainDestPath(dest1, Map.DRIVING);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if ((start.length()<1)&&(dest.length()<1)){
					try {
						m.getAllPath(Map.DRIVING);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if ((start.length()>0)&&(dest.length()>0)){
					int start1 = start.charAt(0)-'A';
					int dest1 = dest.charAt(0)-'A';
					if (start1 == dest1) return;
					if ((start1>=0)&&(start1<=25)&&(dest1>=0)&&(dest1<=25)){
						SinglePath singlePath = m.getSinglePath(start1, dest1, Map.DRIVING);
						Stack<Integer> path = singlePath.path;
						if (path == null) return;
						int s1 = path.pop();
						int s2 = -1;
						funPlan.append(" "+(char)(s1+'A'));
						while(!path.isEmpty()){
						    s2 = path.pop();
						    mapPanel.drawRoute(s1,s2,0);
							funPlan.append(" -> " + (char)(s2+'A'));
							s1 = s2;
						}
						funPlan.append("\n 最短路程： "+singlePath.cost+"km");						
					}
					
				}
			}
		});
		buttonWalking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapPanel.reset();
				funPlan.setText(null);
				String start = startTF.getText();
				String dest = destTF.getText();
				if ((start.length()<1)&&(dest.length()>0)){
					int dest1 = dest.charAt(0)-'A';
					try {
						m.getCertainDestPath(dest1, Map.WALKING);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if ((start.length()<1)&&(dest.length()<1)){
					try {
						m.getAllPath(Map.WALKING);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if ((start.length()>0)&&(dest.length()>0)){
					int start1 = start.charAt(0)-'A';
					int dest1 = dest.charAt(0)-'A';
					if (start1 == dest1) return;
					if ((start1>=0)&&(start1<=25)&&(dest1>=0)&&(dest1<=25)){
						SinglePath singlePath = m.getSinglePath(start1, dest1, Map.WALKING);
						Stack<Integer> path = singlePath.path;
						int s1 = path.pop();
						int s2 = -1;
						funPlan.append(" "+(char)(s1+'A'));
						while(!path.isEmpty()){
						    s2 = path.pop();
						    mapPanel.drawRoute(s1,s2,0);
							funPlan.append(" -> " + (char)(s2+'A'));
							s1 = s2;
						}
						funPlan.append("\n 最短路程： "+singlePath.cost+"km");
					}
					
				}
			}
		});
		buttonBus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapPanel.reset();
				funPlan.setText(null);
				String start = startTF.getText();
				String dest = destTF.getText();
				if ((start.length()<1)&&(dest.length()>0)){
					int dest1 = dest.charAt(0)-'A';
					try {
						m.getCertainDestPath(dest1, Map.BUS);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if ((start.length()<1)&&(dest.length()<1)){
					try {
						m.getAllPath(Map.BUS);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				if ((start.length()>0)&&(dest.length()>0)){
					int start1 = start.charAt(0)-'A';
					int dest1 = dest.charAt(0)-'A';
					if (start1 == dest1) return;
					if ((start1>=0)&&(start1<=25)&&(dest1>=0)&&(dest1<=25)){
						SinglePath singlePath = m.getSinglePath(start1, dest1, Map.BUS);
						Stack<Integer> path = singlePath.path;
						int s1 = path.pop();
						int s2 = -1;
						funPlan.append(" "+(char)(s1+'A'));
						while(!path.isEmpty()){
						    s2 = path.pop();
						    mapPanel.drawRoute(s1,s2,1);
							funPlan.append(" -> " + (char)(s2+'A'));
							s1 = s2;
						}
						funPlan.append("\n 花费时间"+singlePath.cost+"min");
						
					}
					
				}
			}
		});
		buttonRe.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mapPanel.reset();
				funPlan.setText(null);
				startTF.setText(null);
				destTF.setText(null);
			}
			
		});
		
		for(int i=0;i<26;i++){
			mapPanel.points[i].addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					JLabel point = (JLabel)(e.getSource());
					String p = point.getText();
					if (startTF.hasFocus()){
						startTF.setText(p);
					}
					else if(destTF.hasFocus()){
						destTF.setText(p);
					}					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
		
	}
}
class MapPanel extends JPanel{
	final static int BUS_OPTION = 1;
	final static int NOMAL_OPTION = 0;

	JLabel[] points = new JLabel[26];
	JLabel[][] routes = new JLabel[26][26];
	JLabel[][] bus_routes = new JLabel[26][26];

	JLabel mapLabel = new JLabel();
	
	MapPanel(){
		setBounds(50, 50, 800, 500);
		setLayout(null);
		
		
		
		
		mapLabel.setBounds(0, 10, 800, 500);
		mapLabel.setIcon(new ImageIcon("map.png"));
		
		
		
		points[0] = new JLabel("A");				
		points[0].setHorizontalAlignment(SwingConstants.CENTER);
		points[0].setFont(new Font("Elephant", Font.BOLD, 18));
		points[0].setBounds(41, 192, 28, 14);		
		add(points[0]);
		
		points[1] = new JLabel("B");
		points[1].setHorizontalAlignment(SwingConstants.CENTER);
		points[1].setFont(new Font("Elephant", Font.BOLD, 18));
		points[1].setBounds(82, 388, 28, 14);
		add(points[1]);
		
		points[2] = new JLabel("C");
		points[2].setHorizontalAlignment(SwingConstants.CENTER);
		points[2].setFont(new Font("Elephant", Font.BOLD, 18));
		points[2].setBounds(95, 476, 28, 14);
		add(points[2]);
		
		points[3] = new JLabel("D");
		points[3].setHorizontalAlignment(SwingConstants.CENTER);
		points[3].setFont(new Font("Elephant", Font.BOLD, 18));
		points[3].setBounds(176, 141, 28, 14);
		add(points[3]);
		
		points[4] = new JLabel("E");
		points[4].setHorizontalAlignment(SwingConstants.CENTER);
		points[4].setFont(new Font("Elephant", Font.BOLD, 18));
		points[4].setBounds(187, 207, 28, 14);
		add(points[4]);
		
		points[5] = new JLabel("F");
		points[5].setHorizontalAlignment(SwingConstants.CENTER);
		points[5].setFont(new Font("Elephant", Font.BOLD, 18));
		points[5].setBounds(197, 272, 28, 14);
		add(points[5]);
		
		points[6] = new JLabel("G");
		points[6].setHorizontalAlignment(SwingConstants.CENTER);
		points[6].setFont(new Font("Elephant", Font.BOLD, 18));
		points[6].setBounds(204, 351, 28, 14);
		add(points[6]);
		
		points[7] = new JLabel("H");
		points[7].setHorizontalAlignment(SwingConstants.CENTER);
		points[7].setFont(new Font("Elephant", Font.BOLD, 18));
		points[7].setBounds(221, 460, 28, 14);
		add(points[7]);
		
		points[8] = new JLabel("I");
		points[8].setHorizontalAlignment(SwingConstants.CENTER);
		points[8].setFont(new Font("Elephant", Font.BOLD, 18));
		points[8].setBounds(309, 74, 28, 14);
		add(points[8]);
		
		points[9] = new JLabel("J");
		points[9].setHorizontalAlignment(SwingConstants.CENTER);
		points[9].setFont(new Font("Elephant", Font.BOLD, 18));
		points[9].setBounds(311, 176, 28, 14);
		add(points[9]);
		
		points[10] = new JLabel("K");
		points[10].setHorizontalAlignment(SwingConstants.CENTER);
		points[10].setFont(new Font("Elephant", Font.BOLD, 18));
		points[10].setBounds(324, 251, 28, 14);
		add(points[10]);
		
		points[11] = new JLabel("L");
		points[11].setHorizontalAlignment(SwingConstants.CENTER);
		points[11].setFont(new Font("Elephant", Font.BOLD, 18));
		points[11].setBounds(324, 329, 28, 14);
		add(points[11]);
		
		points[12] = new JLabel("M");
		points[12].setHorizontalAlignment(SwingConstants.CENTER);
		points[12].setFont(new Font("Elephant", Font.BOLD, 18));
		points[12].setBounds(324, 435, 28, 14);
		add(points[12]);
		
		points[13] = new JLabel("N");
		points[13].setHorizontalAlignment(SwingConstants.CENTER);
		points[13].setFont(new Font("Elephant", Font.BOLD, 18));
		points[13].setBounds(409, 50, 28, 14);
		add(points[13]);
		
		points[14] = new JLabel("O");
		points[14].setHorizontalAlignment(SwingConstants.CENTER);
		points[14].setFont(new Font("Elephant", Font.BOLD, 18));
		points[14].setBounds(435, 129, 28, 14);
		add(points[14]);
		
		points[15] = new JLabel("P");
		points[15].setHorizontalAlignment(SwingConstants.CENTER);
		points[15].setFont(new Font("Elephant", Font.BOLD, 18));
		points[15].setBounds(457, 207, 28, 14);
		add(points[15]);
		
		points[16] = new JLabel("Q");
		points[16].setHorizontalAlignment(SwingConstants.CENTER);
		points[16].setFont(new Font("Elephant", Font.BOLD, 18));
		points[16].setBounds(500, 261, 28, 14);
		add(points[16]);
		
		points[17] = new JLabel("R");
		points[17].setHorizontalAlignment(SwingConstants.CENTER);
		points[17].setFont(new Font("Elephant", Font.BOLD, 18));
		points[17].setBounds(457, 329, 28, 14);
		add(points[17]);
		
		points[18] = new JLabel("S");
		points[18].setHorizontalAlignment(SwingConstants.CENTER);
		points[18].setFont(new Font("Elephant", Font.BOLD, 18));
		points[18].setBounds(551, 312, 28, 14);
		add(points[18]);
		
		points[19] = new JLabel("T");
		points[19].setHorizontalAlignment(SwingConstants.CENTER);
		points[19].setFont(new Font("Elephant", Font.BOLD, 18));
		points[19].setBounds(510, 176, 28, 14);
		add(points[19]);
		
		points[20] = new JLabel("U");
		points[20].setHorizontalAlignment(SwingConstants.CENTER);
		points[20].setFont(new Font("Elephant", Font.BOLD, 18));
		points[20].setBounds(591, 107, 28, 14);
		add(points[20]);
		
		points[21] = new JLabel("V");
		points[21].setHorizontalAlignment(SwingConstants.CENTER);
		points[21].setFont(new Font("Elephant", Font.BOLD, 18));
		points[21].setBounds(671, 33, 28, 14);
		add(points[21]);
		
		points[22] = new JLabel("W");
		points[22].setHorizontalAlignment(SwingConstants.CENTER);
		points[22].setFont(new Font("Elephant", Font.BOLD, 18));
		points[22].setBounds(724, 90, 28, 14);
		add(points[22]);
		
		points[23] = new JLabel("X");
		points[23].setHorizontalAlignment(SwingConstants.CENTER);
		points[23].setFont(new Font("Elephant", Font.BOLD, 18));
		points[23].setBounds(671, 103, 28, 14);
		add(points[23]);
		
		points[24] = new JLabel("Y");
		points[24].setHorizontalAlignment(SwingConstants.CENTER);
		points[24].setFont(new Font("Elephant", Font.BOLD, 18));
		points[24].setBounds(694, 261, 28, 14);
		add(points[24]);
		
		points[25] = new JLabel("Z");
		points[25].setHorizontalAlignment(SwingConstants.CENTER);
		points[25].setFont(new Font("Elephant", Font.BOLD, 18));
		points[25].setBounds(591, 295, 28, 14);
		add(points[25]);
				
	    for (int i=0;i<25;i++){
	    	for (int j=i+1;j<26;j++){
	    		String fileName = "Route\\"+(char)(i+'A')+(char)(j+'A')+".png";
	 
	    		File file = new File(fileName);
	    		if (file.exists()){
	    			routes[i][j] = new JLabel();
	    			routes[i][j].setBounds(0, 10, 800, 500);
	    			routes[i][j].setIcon(new ImageIcon(fileName));
	    			add(routes[i][j]);
	    			routes[i][j].setVisible(false);
	    		}
	    				
	    	}
	    }
	    for (int i=0;i<25;i++){
	    	for (int j=i+1;j<26;j++){
	    		String fileName = "Route\\bus"+(char)(i+'A')+(char)(j+'A')+".png";
	 
	    		File file = new File(fileName);
	    		if (file.exists()){
	    			bus_routes[i][j] = new JLabel();
	    			bus_routes[i][j].setBounds(0, 10, 800, 500);
	    			bus_routes[i][j].setIcon(new ImageIcon(fileName));
	    			add(bus_routes[i][j]);
	    			bus_routes[i][j].setVisible(false);
	    		}
	    				
	    	}
	    }
		
		add(mapLabel);
		
	
		
	}
	public void drawRoute(int s, int t,int option){
		if (s>t){
			int m = t;
			t = s;
			s = m;
			
		}
		if (option == BUS_OPTION){
			if (bus_routes[s][t]!=null){
				bus_routes[s][t].setVisible(true);
			}
			else{
				routes[s][t].setVisible(true);
			}
		}
		else{
			routes[s][t].setVisible(true);
		}
		
	}
	public void reset(){
		for (int i=0;i<25;i++){
	    	for (int j=i+1;j<26;j++){
	    		if (routes[i][j]!=null){
	    			routes[i][j].setVisible(false);	    			
	    		}
	    		if (bus_routes[i][j]!=null){
	    			bus_routes[i][j].setVisible(false);
	    		}
	    	}
	    }
	}
	    
}
