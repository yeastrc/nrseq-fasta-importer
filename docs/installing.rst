===========================================
Installing nrseq-fasta-importer
===========================================

1. Install MySQL, Java, and Apache Tomcat (if necessary)
==========================================================

This documentation assumes that `Java <http://www.java.com/>`_ (1.7 or later) and the
`Apache Tomcat <http://tomcat.apache.org/>`_ (7 or later) servlet container are installed on the same
computer on which you are installing nrseq-fasta-importer.

This documentation also assumes that `MySQL <http://www.mysql.com/>`_ (5.6 or later) has been
installed and is accessible by the installation of Apache Tomcat. This does not need to be on the
same machine as Apache Tomcat.

These software (and nrseq-fasta-importer) should work equally well on any operating system for which
MySQL and Java are available (MS Windows, Apple OS X, or Linux). Other servlet containers and database
server software may work as well, though this documentation assumes that the above are installed.
Please refer to the respective websites for more information about MySQL, Java, or Apache Tomcat
installation.

You may need to download and install the MySQL JDBC driver. This is available from the 
`MySQL Connector/J <http://dev.mysql.com/downloads/connector/j/>`_ website. To install, copy
the downloaded jar file into ``$CATALINA_HOME/lib`` directory on the server on which Apache Tomcat
is installed (e.g. /usr/local/apache-tomcat-7.0.65/lib).



2. Set up the YRC_NRSEQ database
==========================================================

To set up this database, first download :download:`YRC_NRSEQ_create.sql <../Database/YRC_NRSEQ_create.sql>`.
To run this SQL script, you may log into your MySQL server and either paste in the contents of this
file to MySQL or use ``source /location/to/YRC_NRSEQ_create.sql``. To use the latter, the .sql file must
be in a directory to which MySQL has read access.


3. Set up the nrseq_fasta_importer database
==========================================================

To set up this database, first download :download:`database_schema_create.sql <../Database/database_schema_create.sql>`.
To run this SQL script, you may log into your MySQL server and either paste in the contents of this
file to MySQL or (preferably) use ``source /location/to/database_schema_create.sql``. To use the latter, the .sql file must
be in a directory to which MySQL has read access.

Update configuration table
---------------------------------------------------------------
Download and open :download:`config_inserts.sql <../Database/config_inserts.sql>` in a text editor. Edit the SQL statements
to reflect your configuration options--see the comments in the file for information about the options. Once finished either:

	1. Paste the file into MySQL
		1. Copy the contents of the file
		2. Type ``USE nrseq_fasta_importer;`` in MySQL
		3. Paste contents of the file.
	
	2. Save the file and ``source /location/to/config_inserts.sql``.


4. Configure Apache Tomcat database connection
==========================================================

set up mysql username and password
	create user, grant privleges

set up jndi name in context.xml


5. Configure security for Apache Tomcat (optional but recommended)
=====================================================================

6. Install WAR file into Apache Tomcat
==========================================================