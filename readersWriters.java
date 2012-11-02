import java.io.*;

class lock{}		//this dummy class acts as a monitor

class Rw extends Thread
{
	String type;
	static boolean writing = false;		//if writing is fasle => reading is allowed
	int id;					//process(r/w) id
	static int resource, i;
	static int nreaders,nrw; 			//by default resource = 0

	static lock L = new lock();		//acess permission object
	Rw (String str, int num)		//constructor
	{
		type=str;
		id=num;
	}
	void aqrReadLock()
	{
		synchronized(L)	//monitor locking		
		{	++nreaders;
			while(writing||(nrw>0))				//if writing currently doing,
				try  { L.wait();}		//suspends whatever the thread that being in execution and grab the lock from it
				catch(InterruptedException e){}
							// no.of reader incemented 
		}
	}
	void rlsReadLock()
	{
		synchronized(L)
		{
			//--nreaders;	//reader is decreased
			//if(nreaders==0)			//if no. of readers are 0 then notify all threads to handover the lock
				L.notifyAll();		//Suspends the current thread and handover the lock into the next thread
		}
	}
	void aqrWriteLock()
	{
		synchronized(L)
		{   	
			while(writing/*||(nreaders>0)*/)
				try  {L.wait();}
				catch(InterruptedException e) {}
			writing = true;
			++nrw;
		}
	}
	void rlsWriteLock()
	{
		synchronized(L)
		{	
			writing = false;	//writing is done
			--nrw;
			if(nrw==0)			
				L.notifyAll();		//handover the lock for next thread to execute
		}
	}
	void read()
	{ 
		for(;;)
		{
			aqrReadLock();
			System.out.println("\tReaders"+id+" read "+resource);	//reader just read the resouce
			rlsReadLock();
			try  {Thread.sleep(1000);}		//this whole process is allowed for 1000 milliseconds
			catch(InterruptedException e) {}
		}
	}
	void write()
	{
		for(;;)
		{
			aqrWriteLock();
			System.out.println("Writers"+id+" write "+ ++resource);	//here writer has the permission to increment resource
			rlsWriteLock();
			try  {Thread.sleep(1000);}		//this whole process is allowed for 1000 milliseconds
			catch(InterruptedException e) {} 
		}
	}
	public void run()			//override the run() funtion of thread
	{
		if(type.equals("Reader"))	//if the object is a reader then reading takes otherwise writing
			read();
		else
			write();
	}
}

class readersWriters
{ 
	public static void main(String[] args)
	{       		Rw[] rw = new Rw[6];			//suppose there are 3 readers and 3 writers
		int j=0;
		while(j<3)
			rw[j] = new Rw("Writer", j++);	//writer initialization from 0-2
		while(j<6)
			rw[j] = new Rw("Reader",j++ -3);//reader initialization from 0-2
		j=0;
		while(j<6)
			rw[j++].start();	//executes the rw[i++].run()
	
		/*rw[0].start();
		rw[3].start();
		rw[4].start();
		rw[1].start();
		rw[5].start();
		rw[2].start();*/
}
}
