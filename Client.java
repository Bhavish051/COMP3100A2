
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.*;
public class Client {
	// Initialize socket and input output streams
	private static Socket socket = null;
	private BufferedReader input = null;
	private DataOutputStream out = null;
	private BufferedReader in = null;
	// constructor to put ip address and port
	public Client(String address, int port) throws IOException {
		
		connect(address, port);
		input = new BufferedReader(new InputStreamReader(System.in));
		out = new DataOutputStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	private void start () {

		boolean connected = false;
		
		sendMessage("HELO\n");
		readMessage();

		sendMessage("AUTH bhavish\n");
		readMessage();
		//Reads ds-system and populates it into array Server 't'
		ArrayList<Server> t = new ArrayList<Server>();
		t = readXML("ds-system.xml");
		
		//Finds index of largest server; most cores
		int largestServer = findLargestServer(t);
		
		connected = true;

		sendMessage("REDY\n");

		String msg = readMessage();

		if (msg.contains("NONE")) { 
			sendMessage("QUIT\n");
			connected = false;
		}
		
		//Job scheduling happens here
		while(!msg.contains("NONE")){
			if (msg.contains("JOBN")){
				sendMessage("GETS Capable" + " " +t.get(largestServer).getCores() + " " + t.get(largestServer).getMemory() + " " + t.get(largestServer).getDisk() + "\n" ) ;
				msg = readGetsCapable();
				sendMessage("OK");
			} else {
				sendMessage(ScheduletheJob(msg, t ));
				msg = readMessage();
				sendMessage("REDY");

				msg = readMessage();
			}
	}
	
		while (connected){
			if (msg.contains("NONE")){
				connected = false;
				sendMessage("QUIT\n");
				break;
			}

			readMessage();
		}
		try {
			//Closes input, output and socket connection once QUIT message has been received; terminates connection
			if (readMessage().contains("QUIT")){
				input.close();
				out.close();
				socket.close();
			}	
		} catch (IOException i) {
			System.out.println(i);
		}
		System.exit(1);
	}
	
	/*
	NEXT FIT 
	4 is core
	5 is mem
	*/
	private String ScheduletheJob(String job, ArrayList<Server> t) {
		String[] strSplit = job.split("\\s+");
		for(Server s: t){	

			if(strSplit.length >= 4)
			{
				int ReqCores = Integer.parseInt(strSplit[4].replaceAll("[^0-9]", ""));
				int ReqMem = Integer.parseInt(strSplit[5].replaceAll("[^0-9]", ""));
		
			if (strSplit.length > 2 && ReqCores <= s.getRemainingCores() && ReqMem <= s.getRemainingDisk()) {

				s.UpdateRemainingCores(s.getRemainingCores() - ReqCores);
				s.UpdateRemainingDisk(s.getRemainingDisk() - ReqMem);

				return "SCHD " + strSplit[2] + " " + s.getType() + " " + 0 + "\n";
				}
			}
	}
	return "SCHD " + strSplit + " " + t.get(t.size()-1).getType() + " " + 0 + "\n";
	//return "OK";
	}

	//Sends all jobs to largest server 
	/*
	This is where it needs to be changed 
	At this stage the client schedules all the jobs to the largest sever straightaway
	*/
	// private String AllToLargest(String job, Server s){
	// 	int jobID = 0;
	// 	String[] strSplit = job.split("\\s+");
	// 	if(strSplit.length > 2)
	// 		return "SCHD " + strSplit[2] + " " + s.getType() + " " + jobID + "\n";
	// 	return "SCHD " + strSplit + " " + s.getType() + " " + jobID + "\n";
	// }


	//Finds the largest server; counts through cores until largest is found then returns largest
	//Only Updates the largest server if the number of cores is different
	private int findLargestServer(ArrayList<Server> s){
		int largest = 0;
		for (int i = 0; i < s.size(); i++){
			if (s.get(i).getCores() > largest){
				if(s.get(i).getCores() != s.get(largest).getCores())
					largest = i;
			}
		}
		return largest;
	}
	
	//Send message to server
	private void sendMessage (String outStr) {
		byte[] byteMsg = outStr.getBytes();
		try {
			out.write(byteMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Display output from client
		System.out.println("OUT: " + outStr);
	}
	
	//Read message from server
	private String readMessage () {
		String inStr = "";
		char[] cbuf = new char[65535];
		try {
			in.read(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		inStr = new String(cbuf, 0, cbuf.length);

		//Display input from server
		System.out.println("INC: " + inStr);

		return inStr;
	}

	private String readGetsCapable() {
		String S = "";
		char[] cbuf = new char[65535];
		try {
			in.read(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		S = new String(cbuf, 0, cbuf.length);
		String[] Data = new String[4];
		Data = S.split(" ");
		// Display input from server
		System.out.println("INC: " + Data);

		return S;
	}
	
	//Establishes connection to initiate handhsake
	private static void connect(String address, int port) {
		double secondsToWait = 1;
		int tryNum = 1;
		while (true) {
			try {
				System.out.println("Connecting to server at: " + address + ":" + port);
				socket = new Socket(address, port);
				System.out.println("Connected");
				break; 
			} catch (IOException e) {
				secondsToWait = Math.min(30, Math.pow(2, tryNum));
				tryNum++;
				System.out.println("Connection timed out, retrying in  " + (int) secondsToWait + " seconds ...");
				try {
					TimeUnit.SECONDS.sleep((long) secondsToWait);
				} catch (InterruptedException ie) {
          ie.printStackTrace();
				}
			}
		}
	}

	public static void main(String args[]) throws IOException {
		Client client = new Client("127.0.0.1", 50000);
		client.start();
	}

	public static ArrayList<Server> readXML(String fileName){
        ArrayList<Server> serverList = new ArrayList<Server>();
		
		try {
			//Read XML file
			File systemXML = new File(fileName);

			//Document parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(systemXML);

			doc.getDocumentElement().normalize();
			NodeList servers = doc.getElementsByTagName("server");
			for (int i = 0; i < servers.getLength(); i++) {
				Element server = (Element) servers.item(i);

				String type = server.getAttribute("type");
				String id = new String(server.getAttribute("id"));
				int limit = Integer.parseInt(server.getAttribute("limit"));
				int bootupTime = Integer.parseInt(server.getAttribute("bootupTime"));
				float hourlyRate = Float.parseFloat(server.getAttribute("hourlyRate"));
				int coreCount = Integer.parseInt(server.getAttribute("coreCount"));
				int memory = Integer.parseInt(server.getAttribute("memory"));
				int disk = Integer.parseInt(server.getAttribute("disk"));
				
				Server s = new Server(type,id,limit,bootupTime,hourlyRate,coreCount,memory,disk);
				serverList.add(s);
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	return serverList;
    }

	
}
