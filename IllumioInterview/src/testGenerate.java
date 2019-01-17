import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class testGenerate 
{
	public static void main (String[] args) throws IOException 
	{
		String[] direction = {"inbound", "outbound"};
		String[] protocol = {"tcp", "udp"};
		
		
		
		FileWriter writer = new FileWriter("test.csv",false);
        Random rand = new Random();
       
        
        for(int i = 0; i < 50000; i++)
        {
        	String entry = "";
        	entry = entry + direction[rand.nextInt(2)] + "," + protocol[rand.nextInt(2)];
        	
        	
        	int portChoice = rand.nextInt(2);
        	
        	
        	if(portChoice == 0) // rule a for port
        	{
        		entry = entry + "," + (rand.nextInt(65535) + 1);
        		
        	}
        	else // rule b for port
        	{
        		int lowerbound = (rand.nextInt(65535) + 1);
        		int upperbound = rand.nextInt((65535 - lowerbound) + 1) + lowerbound;
        		
        		
        		entry = entry + "," + lowerbound + "-" + upperbound;
        		
        	}
        	
        	int ipAdChoice = rand.nextInt(2);
        	
        	if(ipAdChoice == 0)
        	{
        		entry = entry + "," + ((rand.nextInt(255)+1) + "." + (rand.nextInt(255)+1) + "." + (rand.nextInt(255)+1) + "." + (rand.nextInt(255)+1));
        	}
        	else
        	{
        		int lp1 = rand.nextInt(255)+1;
        		int lp2 = rand.nextInt(255)+1;
        		int lp3 = rand.nextInt(255)+1;
        		int lp4 = rand.nextInt(255)+1;
        		
        		
        		int up1 = rand.nextInt((255 - lp1) + 1) + lp1;
        		
        	
        		int up2 = rand.nextInt(255)+1;
        		int up3 = rand.nextInt(255)+1;
        		int up4 = rand.nextInt(255)+1;
        		
        		entry = entry + "," + (lp1+"."+lp2+"." +lp3 +"." + lp4) + "-" + (up1+"."+up2+"." +up3 +"." + up4);
        		
        	}
        	
        	writer.append(entry + "\n");
        	
        }
        writer.flush();
        writer.close();
		
	}
}
