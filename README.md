# Music-Store-Web-App

Note: to run this project on development system with HSQLDB (easiest way):
1. Start the HSQLDB server as explained in JdbcCheckup.java or use
   "ant start-hsqldb" after cd'ing to the database directory here.
   then "ant load-hsqldb" (or "ant drop-hsqldb" followed by "ant load-hsqldb" if it's running)
   
2. Use "ant config-clientserver-hsqldb" when cd'd to this directory
   
3. Use "ant sysTest" to run SystemTest
 
 To run this project as a web app with HSQLDB
 1. Make sure the HSQLDB server is started (step 1 above) and loaded (step 2 above)
 
 2. In this directory use "ant config-web-hsqldb"
 
 3. Make sure tomcat is running and CATALINA_HOME and TOMCAT_URL are defined as environment variables
  
 4. Use "ant deploy" to copy the WebContent area to tomcat's webapps area (check this)
 
 5. After a minute, use "ant webSysTest" to run SystemTest inside tomcat's JVM
 
 6. Also try "ant webTest1" and "ant webTest2"
 
To use a real DB: you need to define environment variables as discussed in build.xml
 For Oracle database, set up env variables ORACLE_USER, ORACLE_PW, ORACLE_SITE, etc.
 You also need to edit tomcat's conf/context.xml as described in 
 $cs636/UsingDBfromWebApp.html.

If you have the project set up in eclipse, be sure to refresh the project when
you get back to using it in eclipse, to make eclipse read the files again.
