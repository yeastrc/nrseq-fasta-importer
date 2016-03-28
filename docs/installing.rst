===========================================
Installing nrseq-fasta-importer
===========================================

1. Install MySQL, Java, and Apache Tomcat (if necessary)
==========================================================

This documentation assumes that `Java <http://www.java.com/>`_ (JDK version, 1.7 or later) and the
`Apache Tomcat <http://tomcat.apache.org/>`_ (7 or later) servlet container are installed on the same
computer on which you are installing ProXL. (Note: Apache Tomcat requires the JDK version of Java be
installed.)

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
This section describes how to connect Tomcat to the YRC_NRSEQ and nrseq_fasta_importer databases.

On the MySQL side:
---------------------------
|	Log in to MySQL as root:
|	``shell> mysql --user=root mysql``
|	
|	Create the MySQL user:
|	``mysql> CREATE USER 'nrseq_user'@'localhost' IDENTIFIED BY 'password';``	
|
|	Replace ``nrseq_user`` with the username you would prefer, ``localhost`` with the
|	relative hostname of the machine connecting to the MySQL database (usually localhost),
|	and ``password`` with your preferred password.
|
|	Grant the necessary privileges in MySQL:
|	``GRANT ALL ON YRC_NRSEQ.* TO 'nrseq_user'@'localhost'``
|	``GRANT ALL ON nrseq_fasta_importer.* TO 'nrseq_user'@'localhost'``	
|
|	Replace ``nrseq_user`` and ``localhost`` with the username and hostname you used
|	when creating the user.
|
On the Tomcat side:
-----------------------------
	Add the following to ``$CATALINA_HOME/conf/context.xml``, inside the ``<Context></Context>`` root
	element. Be sure to change ``nrseq_user`` and ``password`` to the username and password you set
	up above. If necessary, change ``localhost`` and ``3306`` to the hostname and port of your
	MySQL server.
	
	.. code-block:: xml
	
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


                                                

                                                

                        username="nrseq_user"
                        password="password"
                        driverClassName="com.mysql.jdbc.Driver"
                        url="jdbc:mysql://localhost:3306/nrseq_fasta_importer?autoReconnect=true&amp;tcpKeepAlive=true&amp;useUnicode=true&amp;characterEncoding=ISO8859_1&amp;characterSetResults=ISO8859_1"/>

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

						
						
                        username="nrseq_user"
                        password="password"
                        driverClassName="com.mysql.jdbc.Driver"
                        url="jdbc:mysql://localhost:3306/YRC_NRSEQ?autoReconnect=true"/>


5. Install WAR file into Apache Tomcat
==========================================================
Download the latest release of nrseq-fasta-importer from github at https://github.com/yeastrc/nrseq-fasta-importer/releases

Unzip the downloaded file and copy nrseq-fasta-importer.war into ``$CATALINA_HOME/webapps/``. The WAR file should
automatically deploy. If not, restart Tomcat to force the file to deploy.

Your web application should now be available at http://your.host:8080/nrseq-fasta-importer/
(Depending on how you have configured your web server, the ``:8080`` may not be different or
not required.) If you have a firewall running, will need to allow access through this port.


6. Configure security for Apache Tomcat (optional but recommended)
=====================================================================
To prevent unauthorized access to your nrseq-fasta-importer web application, it is recommended
that you set up user authentication. These instructions describe how to set up basic 
authentication for Tomcat. For more detailed instructions, see https://tomcat.apache.org/tomcat-7.0-doc/realm-howto.html

First, add the following lines within the ``<tomcat-users></tomcat-users>`` element in
``$CATALINA_HOME/conf/tomcat-users.xml``. Substitute ``USERNAME`` and ``PASSWORD`` with
the username and password you wish to use to secure access to your web application.

.. code-block:: xml

  <role rolename="nrseq-fasta-upload-group"/>
  <user username="USERNAME" password="PASSWORD" roles="nrseq-fasta-upload-group"/>


Second, add the following lines within the ``<web-app></web-app>`` root element in
``$CATALINA_HOME/webapps/nrseq-fasta-importer/WEB-INF/web.xml``
(the web.xml for your deployed nrseq-fasta-importer web app).

.. code-block:: xml

        <security-constraint>
                <web-resource-collection>
                        <web-resource-name>NRSEQ FASTA Upload Server</web-resource-name>
                        <url-pattern>/*</url-pattern>
                </web-resource-collection>
                <auth-constraint>
                        <description>Authorized NRSEQ FASTA Upload User</description>
                        <role-name>nrseq-fasta-upload-group</role-name>
                </auth-constraint>
        </security-constraint>
        <security-role>
                <role-name>nrseq-fasta-upload-group</role-name>
        </security-role>
        <login-config>
                <auth-method>BASIC</auth-method>
                <realm-name>nrseq-fasta-upload-server</realm-name>
        </login-config>
 
You will need to restart Tomcat for these changes to take effect.
