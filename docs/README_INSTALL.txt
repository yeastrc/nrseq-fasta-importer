

1.  Create a "work" directory where files can be stored and referenced later.
        - Do not use directories like /tmp where the files are randomly deleted.
        - This directory must be writable by the user account that runs the 
        - application server (ie. Tomcat)

2.  Create the database, use the SQL in the file "database_schema_create.sql"

3.  Insert the configuration into the database.
		- copy the file "config_inserts.sql"
		- in the copy, update the file as directed by the comments in the file
		- run the SQL in the copy
		

4.	Configure the application server JNDI names to connect to the database
		- See below "Configuring JDNI database connection"
		
5.	Configuring security of the web app:
		- See below "Configuring security of the web app"		
		
6.  Insert the war file nrseq-fasta-importer.war into the application server (ie. Tomcat)
		- if using Tomcat, put the file in the "webapps" directory





	****************************************************************
	

	
	Configuring JDNI database connection for Web app container:
	
		Update "username" and "password" to values for your database
	
	  For Tomcat:
	  
	    add to conf/context.xml
	    
	    


          <Resource     name="jdbc/nrseq_fasta_importer"
                        auth="Container"
                        type="javax.sql.DataSource"
                        factory="org.apache.commons.dbcp.BasicDataSourceFactory"
                        maxActive="50"
                        maxIdle="1"
                        maxWait="10000"
                                                
                                                minIdle="0"
                                                minEvictableIdleTimeMillis="21600000"
                                                timeBetweenEvictionRunsMillis="30000"
                                                validationQuery="select 1 from dual"
                                                testOnBorrow="true"


                                                

                                                

                        username="username"
                        password="password"
                        driverClassName="com.mysql.jdbc.Driver"
                        url="jdbc:mysql://localhost:3306/nrseq_fasta_importer?autoReconnect=true&amp;tcpKeepAlive=true&amp;useUnicode=true&amp;characterEncoding=ISO8859_1&amp;characterSetResults=ISO8859_1"/>



<!--  'YRC_NRSEQ' on Local  -->

<!--
-->
          <Resource     name="jdbc/nrseq"
                        auth="Container"
                        type="javax.sql.DataSource"
                        factory="org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory"
                        maxActive="10"
                        maxIdle="1"
                        maxWait="10000"
						
						minIdle="0"
						minEvictableIdleTimeMillis="21600000"
						timeBetweenEvictionRunsMillis="30000"

						validationQuery="select 1 from dual"
						testOnBorrow="true"

						
						
                        username="username"
                        password="password"
                        driverClassName="com.mysql.jdbc.Driver"
                        url="jdbc:mysql://localhost:3306/YRC_NRSEQ?autoReconnect=true"/>

					    


	****************************************************************
	
	Configuring security of the web app:
	
		The web app has been configured in the web.xml to use 
		the "realm" based authentication provided in Tomcat.
		
		If using another application server, 
	
	
	The web.xml in the web app has the following:
	
	    <security-constraint>....
	
	
	Configuring security for Tomcat:
	
	
	Update the file "conf/tomcat-users.xml".
	
	Add to the bottom of the file (example with the role and one user):
	
	    <role rolename="nrseq-fasta-upload-group"/>
	
	    <user username="nr-seq-user" password="FJDKSLYUW#ERHIW" roles="nrseq-fasta-upload-group"/>
	
