public class Server {

	String type;
	String id;
	int limit;
	int bootTime;
	float hourlyRate;
	int coreCount;
	int memory;
	int disk;

	public Server(String type, String id, int limit, int bootTime, float hourlyRate, int coreCount, int memory,int disk) {
		this.type = type;
		this.limit = limit;
		this.bootTime = bootTime;
		this.hourlyRate = hourlyRate;
		this.coreCount = coreCount;
		this.memory = memory;
		this.disk = disk;
		this.id = id;
	}

	// id: ID of server
	public String getId() {
		return this.id;
	}

	// type: category of job
	public String getType() {
		return this.type;
	}

	// limit: limit of servers of unique type
	public int getLimit() {
		return this.limit;
	}

	// bootupTime: the amount of time taken to boot a server of a particular type
	public int getBootupTime() {
		return this.bootTime;
	}

	public Float getHourlyRate() {
		return this.hourlyRate;
	}

	// core: CPU cores
	public int getCores() {
		return this.coreCount;
	}

	// memory: RAM
	public int getMemory() {
		return this.memory;
	}

	// disk: disk space
	public int getDisk() {
		return this.disk;
	}

	public void printData() {
		System.out.println(this.type + "" + this.id + " " + this.limit + " " + this.bootTime + " " + this.hourlyRate
				+ " " + this.coreCount + " " + this.memory + " " + this.disk);
	}

}
