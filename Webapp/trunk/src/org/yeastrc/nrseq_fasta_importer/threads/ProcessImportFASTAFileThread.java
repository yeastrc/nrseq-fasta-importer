package org.yeastrc.nrseq_fasta_importer.threads;

import org.apache.log4j.Logger;
import org.yeastrc.nrseq_fasta_importer.process_fasta_file.ProcessImportFASTAFile;

/**
 * 
 *
 */
public class ProcessImportFASTAFileThread extends Thread {

	private static final Logger log = Logger.getLogger(ProcessImportFASTAFileThread.class);
	
	private static ProcessImportFASTAFileThread instance = null;
	
	private static int threadCreateCount = 0;
	
	
	
	private volatile ProcessImportFASTAFile processImportFASTAFile;
	


	private volatile boolean keepRunning = true;
	
	
	private volatile boolean skipWait = false;
	
	

	/**
	 * @return
	 */
	public static synchronized ProcessImportFASTAFileThread getInstance(){
		
		if ( instance == null ) {
			
			createThread();
		
		} else if ( ! instance.isAlive() ) {

			
			Exception exception = new Exception( "Fake Exception to get call stack" );

			log.error( "ProcessImportFASTAFileThread has died and will be replaced.", exception );
			
			createThread();
		}
		
		return instance;
	}
	
	private static void createThread() {
		
		
		threadCreateCount++;
		
		instance = new ProcessImportFASTAFileThread();
		instance.setName( "ProcessImportFASTAFile-Thread-" + threadCreateCount );
		
		instance.processImportFASTAFile = ProcessImportFASTAFile.getInstance();
	}

	/**
	 * awaken thread to process request, calls "notify()"
	 */
	public void awaken() {

		if ( log.isDebugEnabled() ) {

			log.debug("awaken() called:  " );
		}

		synchronized (this) {
			
			skipWait = true;

			notify();
		}

	}




	/**
	 * shutdown was received from the operating system.  This is called on a different thread.
	 */
	public void shutdown() {


		log.info("shutdown() called");


		synchronized (this) {

			this.keepRunning = false;

		}


		//  awaken this thread if it is in 'wait' state ( not currently processing a job )

		this.awaken();
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while ( keepRunning ) {

			/////////////////////////////////////////
			
			try {
				
				skipWait = false; //  set true in awaken()
				
				processImportFASTAFile = ProcessImportFASTAFile.getInstance();
				
				processImportFASTAFile.processNextFASTAFile();
				
				processImportFASTAFile = null;

			} catch (Throwable e) {

				log.error("ProcessImportFASTAFile.getInstance().processNextFASTAFile()", e );

			}
			
			////////////////////////////////////

			//  Then put thread to sleep until more work to do

			synchronized (this) {

				try {
					
					if ( skipWait ) {  //  set true in awaken()
						
						
						
					} else {

						log.debug( "before 'while ( keepRunning )', before wait() called" );

						wait();

						if ( log.isDebugEnabled() ) {

							log.debug("before 'while ( keepRunning )', after wait() called:  ProcessImportFASTAFileThread.getId() = " + this.getId() );
						}
					}

				} catch (InterruptedException e) {

					log.info("wait() interrupted with InterruptedException");

				}
			}


		}
		
		log.info("Exitting run()" );



	}
	
	


	public ProcessImportFASTAFile getProcessImportFASTAFile() {
		return processImportFASTAFile;
	}

}