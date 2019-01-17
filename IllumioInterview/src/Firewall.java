import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Firewall 
{
	private HashMap<String, ArrayList<String>> pairings = new HashMap<String, ArrayList<String>>();
	
	public Firewall(String path)
	{
		
		try {
			Scanner scan = new Scanner(new File(path)); //read in csv file
			while(scan.hasNext())
			{
				String next = scan.nextLine();
				String[] hold = next.split("\\,"); //split into [direction, protocol, port, ip]
				if(pairings.containsKey(hold[0] + hold[1])) //if entry exists, append
				{
					ArrayList<String> combos = pairings.get(hold[0] +"" + hold[1]);
					combos.add(hold[2] + ":" + hold[3]);
					pairings.put(hold[0] +"," + hold[1], combos);
				}
				else //doesn't exist, make new arraylist
				{
					ArrayList<String> gen = new ArrayList<String>();
					gen.add(hold[2] + ":" + hold[3]);
					pairings.put(hold[0]+ hold[1], gen);
				}
			}
			scan.close();
			//System.out.println(pairings);
			
			
		} catch (FileNotFoundException e) {
			System.out.println(".csv file not found");
		}
		
	}
	
	
	public boolean accept_packet(String direction, String protocol, int port, String ip_address)
	{
		ArrayList<String> matches = pairings.get(direction+protocol); //get direction+protocol matches
		//System.out.println(matches);
	
		for(String value: matches)
		{
			if(!value.contains("-")) // no ranges at all
			{
				String[] set = value.split(":");
				if(Integer.parseInt(set[0]) == port)//port check
				{
					
					String[] ipPartsReal = set[1].split("\\.");
					String[] ipPartsEntry = ip_address.split("\\.");
					
					
					if(ipPartsReal[0].equals(ipPartsEntry[0]) && ipPartsReal[1].equals(ipPartsEntry[1])
							&& ipPartsReal[2].equals(ipPartsEntry[2]) && ipPartsReal[3].equals(ipPartsEntry[3])) //ip check
					{
						return true;
					}
				}
				
			}
			else // ranges exists somewhere
			{
				boolean portVal = false;
				boolean ipVal = false;
				
				String[] set = value.split("\\:");
				
				
				if(set[0].contains("-")) // port range
				{
					
					String[] portRange = set[0].split("\\-");
					if(port >= Integer.parseInt(portRange[0]) && port <= Integer.parseInt(portRange[1]))
					{
						portVal = true;
					}
				}
				else //single port
				{
					if(Integer.parseInt(set[0]) == port)
					{
						portVal = true;
					}
				}
				
				if(set[1].contains("-")) // ip address range
				{
					String[] ipAddress = set[1].split("\\-");
					String[] low = ipAddress[0].split("\\.");
					String[] hi = ipAddress[1].split("\\.");
					
					String[] entryIP = ip_address.split("\\.");
					
					if(Integer.parseInt(entryIP[0]) >= Integer.parseInt(low[0]) && Integer.parseInt(entryIP[0]) <= Integer.parseInt(hi[0]))
					{
						if(Integer.parseInt(entryIP[1]) >= Integer.parseInt(low[1]) && Integer.parseInt(entryIP[1]) <= Integer.parseInt(hi[1]))
						{
							if(Integer.parseInt(entryIP[2]) >= Integer.parseInt(low[2]) && Integer.parseInt(entryIP[2]) <= Integer.parseInt(hi[2]))
							{
								if(Integer.parseInt(entryIP[3]) >= Integer.parseInt(low[3]) && Integer.parseInt(entryIP[3]) <= Integer.parseInt(hi[3]))
								{
									ipVal = true;
								}
							}
						}
					}
					
				}
				else //single ip
				{
					if(ip_address.equals(set[1]))
					{
						ipVal = true;
					}
				}
				
				if(ipVal && portVal) //both must be true to return true
				{
					return true;
				}
			}
			
			
		}
		return false;
		
		
		
	}
	
	public static void main(String[] args) 
	{
		Firewall fw = new Firewall("./src/test.csv");
		System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
		System.out.println(fw.accept_packet("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(fw.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(fw.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
		System.out.println(fw.accept_packet("inbound", "udp", 24, "52.12.48.92"));
		
		
	}

}
