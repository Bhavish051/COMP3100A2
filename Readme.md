## Cloud-Job Scheduler

# Bhavish Dhanda - 45525005

# INTRODUCTION
This cloud job scheduler aims to overcome the limitations of the basic job scheduling algorithm first fit which works on the priciple that the incoming jobs are assigned in order to the closest available server. This is basically an implementaion based on first fit but improvises on its drawback that only one job is scheduled to a particular server.
# PROBLEM DEFINITION
The basic algorithm first fit basically has the servers sorted in a list in the order of their resources or so called processing capacity. And after that list is retrieved it starts taking jobs one by one and starts assigning them in the order of the servers in the list only scheduling one job on one server. This algorithm called Next Fit works on the specific drawback of FF that multiple jobs can be scheduled to a single server.
# ALGORITHM DESCRIPTION
* The core functionality of this algorithm is similar to first fit along with a few other parameters to overcome its limitation of one job per server. 
* This algorithm takes into account the remaining resources inside a server at any point to allocate the remaining part of the server to the job submitted so as to not block the entire server with just one job.
* This algorithm still goes one by one in the order of the resouce capacity of the servers from the list. 
* When it is going through the list of all the servers one by one to check their resources for the next job if it allocates a job to a particular server it updates its remaining capacity by subtracting the remaining cores and disk from the ones required by the job that is scheduled.
* Also when the server sends a "JCPL" message to the client indicating the completion of a job it then also updates the remaining capacity of the server by adding back the resources it previously used.
# IMPLEMENTATION DETAILS
* At the start the program reads the system.xml file to get the list of servers in the order of their resources which is their number of cores and the disk capacity.
* In this case we have added 2 additional fields in Server class which are the "remainingDisk" and "remainingCore" which at any point represent the number of cores free and the disk capacity free of the server itself.
* After this the entire list of servers is passed to the scheduling function along with the job itself. So the scheduling method loops through the list of servers and keeps compairing the resources available to the resources required by the job that was passed and there are enough of them the job gets scheduled to that particular even if it busy at that point cause it will be able to run multiple jobs this time.
# EVALUATION
* In comparison to first fit algorithm this algorithm aims to reduce the waiting time of all the jobs submitted.
* Also in comparison to all to largest this would significantly improve the waiting time of the jobs submitted as they wont have to wait for the largest server to free up and can use partial server capacity so different jobs can share servers independent of each other.

# CONCLUSION

# REFERENCES
https://github.com/Bhavish051/COMP3100A2.git 