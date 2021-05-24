public class Server {

	String type;
	String id;
	int limit;
	int bootTime;
	float hourlyRate;
	int coreCount;
	int memory;
	int disk;
	int RemainingCores;
	int RemainingDisk;

	public Server(String type, String id, int limit, int bootTime, float hourlyRate, int coreCount, int memory,int disk) {
		this.type = type;
		this.limit = limit;
		this.bootTime = bootTime;
		this.hourlyRate = hourlyRate;
		this.coreCount = coreCount;
		this.memory = memory;
		this.disk = disk;
		this.id = id;
		// When the server is added to the list in the beginning of the program all the resources from it are available and will be updated once the job scheduling completes
		this.RemainingCores = this.coreCount;
		this.RemainingDisk = this.disk;
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

	public int getRemainingCores(){
		return RemainingCores;
	}

	public int getRemainingDisk() {
		return RemainingDisk;
	}

	public void UpdateRemainingCores(int RemainingCores){
		this.RemainingCores = RemainingCores;
	}

	public void UpdateRemainingDisk(int RemainingDisk){
		this.RemainingDisk = RemainingDisk;
	}

	public void printData() {
		System.out.println(this.type + "" + this.id + " " + this.limit + " " + this.bootTime + " " + this.hourlyRate
				+ " " + this.coreCount + " " + this.memory + " " + this.disk);
	}

}
