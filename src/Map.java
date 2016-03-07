import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Stack;


public class Map {
	final static int MAX = 26;
	
	final static int WALKING = 0;
	final static int DRIVING = 1;
	final static int BUS = 2;

	double[][] walkingMap = new double[MAX][MAX];
	double[][] walkingD = new double[MAX][MAX];
	int[][] walkingPI = new int[MAX][MAX];
	
	double[][] drivingMap = new double[MAX][MAX];
	double[][] drivingD = new double[MAX][MAX];
	int[][] drivingPI = new int[MAX][MAX];
		
	double[][] busMap = new double[MAX][MAX];
	double[][] busD = new double[MAX][MAX];
	int[][] busPI = new int[MAX][MAX];
	
	int[][] bus = new int[MAX][MAX];
	final double WALK_SPEED = 0.07;
	
	
	
	Map(String distence,String busline,String special){
		
		for (int i=0;i<MAX;i++){
			for (int j=0;j<MAX;j++){
				walkingMap[i][j] = Double.MAX_VALUE;
				drivingMap[i][j] = Double.MAX_VALUE;
				busMap[i][j] = Double.MAX_VALUE;
			}
		}
		for (int i=0;i<MAX;i++){
			walkingMap[i][i] = 0;
			drivingMap[i][i] = 0;
			busMap[i][i] = 0;
		}
		
		File disFile = new File(distence);
		File busFile = new File(busline);
		File specFile = new File(special);
		
		try {
			
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(disFile)));
			
			reader.readLine();
			reader.readLine();
			reader.readLine();
			String temp;
			while((temp = reader.readLine())!=null){				
				
				int ver1 = temp.charAt(0)-'A';
				int ver2 = temp.charAt(2)-'A';
				String temp2 = temp.substring(4);
				double dis = Double.parseDouble(temp2);
				walkingMap[ver1][ver2] = dis;
				walkingMap[ver2][ver1] = dis;
				drivingMap[ver1][ver2] = dis;
				drivingMap[ver2][ver1] = dis;
				busMap[ver1][ver2] = dis/WALK_SPEED;
				busMap[ver2][ver1] = dis/WALK_SPEED;				
			}
			reader.close();
			
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(specFile)));
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			while((temp = reader.readLine())!=null){
				int state = temp.charAt(0);
				int ver1 = temp.charAt(1)-'A';
				int ver2 = temp.charAt(2)-'A';
				if (state=='0'){
					drivingMap[ver2][ver1] = Double.MAX_VALUE;
				}
				else if (state=='1'){
					walkingMap[ver1][ver2] = Double.MAX_VALUE;
					walkingMap[ver2][ver1] = Double.MAX_VALUE;
					busMap[ver1][ver2] = Double.MAX_VALUE;
					busMap[ver2][ver1] = Double.MAX_VALUE;
				}
				else if (state=='2'){
					drivingMap[ver1][ver2] = Double.MAX_VALUE;
					drivingMap[ver2][ver1] = Double.MAX_VALUE;
				}
			}
			reader.close();
			
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(busFile)));
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			int busNum = 1;
			while((temp = reader.readLine())!=null){					
				if (temp.length()>0) {
					int ver1 = temp.charAt(0)-'A';
					int ver2 = temp.charAt(1)-'A';
					String temp2 = temp.substring(3);
					double t = Double.parseDouble(temp2);
					if (t < busMap[ver1][ver2]){
						busMap[ver1][ver2] = t;
						busMap[ver2][ver1] = t;
						bus[ver1][ver2] = busNum;
						bus[ver2][ver1] = busNum;
						
					}					
				}
				else {
					busNum++;							
				}
			}
			reader.close();
			
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		floydWarshall(drivingMap,drivingD,drivingPI);
		floydWarshall(walkingMap,walkingD,walkingPI);
		floydWarshall(busMap,busD,busPI);
	}
	public void floydWarshall(double[][] W, double[][] D, int[][] pi){
	
		for (int i=0;i<MAX;i++){
			for (int j=0;j<MAX;j++){
				D[i][j] = W[i][j];
				if (((i==j)||W[i][j] == Double.MAX_VALUE)){
					pi[i][j] = -1;
				}
				else{
					pi[i][j] = i;
				}
			}
			
		} 
		for (int k=0;k<MAX;k++){
			for (int i=0;i<MAX;i++){
				for (int j=0;j<MAX;j++){
					
					double d = 0;
					if ((D[i][k]==Double.MAX_VALUE)||(D[k][j]==Double.MAX_VALUE)){
						d = Double.MAX_VALUE;
					}
					else d = D[i][k] + D[k][j];
					
					if (D[i][j]>d){
						pi[i][j] = pi[k][j];
						D[i][j] = d;
					}
					
				}
			}
		}
		
		
	}
	public Stack<Integer> getPath(int s,int t,int option){
		Stack<Integer> path = new Stack<Integer>();
		int[][] pi = new int[0][0];
		switch (option){		
			case DRIVING:
				pi = drivingPI;
				break;
			case WALKING:
				pi = walkingPI;
				break;
			case BUS:
				pi = busPI;
				break;
		}		 
		path.push(t);
	//	System.out.println(t);
		int x = pi[s][t];
		if (x==-1){
			return null;
		}
		while(x!=s){
			path.push(x);
	//		System.out.println(x);
			x = pi[s][x];
		}
		path.push(x);
	//	System.out.println(x);
		return path;		
	}
	
	public SinglePath getSinglePath(int i,int j,int option){
		double[][] D = null;
		switch (option){
			case DRIVING:
				D = drivingD;
				break;
			case WALKING:
				D = walkingD;
				break;
			case BUS:
				D = busD;
				break;
		}
			
		SinglePath singlePath = new SinglePath(getPath(i,j,option),D[i][j]);
		return singlePath;		
	}
	public void getCertainDestPath(int j,int option) throws IOException{
		String opt = "";
		String unit = "";
		switch(option){
			case DRIVING:
				opt = " Driving.txt";
				unit = " km";
				break;
			case WALKING:
				opt = " Walking.txt";
				unit = " km";
				break;
			case BUS:
				opt = " Bus.txt";
				unit = " min";
				break;
		}
		
		File f = new File("PrintFile\\All Path To "+(char)(j+'A')+ opt);
		if (!f.exists()){
			f.createNewFile();
		}
		else return;
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		for (int i=0;i<MAX;i++){
			SinglePath singlePath = getSinglePath(i,j,option);
			
			Stack<Integer> path = singlePath.path;
			if (path == null) continue;
			writer.write((char)(i+'A') + " -> " +(char)(j+'A'));
			writer.write(" "+singlePath.cost+unit+"\r\n");
		}
		writer.close();
	}
	public void getAllPath(int option) throws IOException{
		String opt = "";
		String unit = "";
		switch(option){
			case DRIVING:
				opt = " Driving.txt";
				unit = " km";
				break;
			case WALKING:
				opt = " Walking.txt";
				unit = " km";
				break;
			case BUS:
				opt = " Bus.txt";
				unit = " min";
				break;
		}
		
		File f = new File("PrintFile\\All Path "+opt);
		if (!f.exists()){
			f.createNewFile();
		}
		else return;
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		for (int i=0;i<MAX;i++){
			for (int j=0;j<MAX;j++){
				SinglePath singlePath = getSinglePath(i,j,option);
			
				Stack<Integer> path = singlePath.path;
				if (path == null) continue;
				writer.write((char)(i+'A') + " -> " +(char)(j+'A'));
				writer.write(" "+singlePath.cost+unit+"\r\n");
			}
			
		}
		writer.close();
	}
	
	
/*	public static void main(String[] args){
		Map m = new Map("distance.txt","busline.txt","special.txt");
		/*for (int i=0;i<26;i++){
			for (int j=0;j<26;j++){
				double d = m.drivingD[i][j];
				if (d==Double.MAX_VALUE){
					System.out.print("mmmmmm ");
				}
				else{
					DecimalFormat df = new DecimalFormat("000.00");
					String num = df.format(d);
					System.out.print(num+" ");
				}
			}
			System.out.println();
		}
		m.getPath(0,9,0);
	}*/
	
}
class SinglePath{
	Stack<Integer> path;
	double cost;
	SinglePath(Stack<Integer> path,double cost){
		this.path = path;
		BigDecimal b = new BigDecimal(cost);
		this.cost = b.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
