Web REST client for testing REST services.
------------------------------------------

To run (you need maven) execute <b>mvn jetty:run</b> 
and application will run on port 8090 - http://localhost:8090

If you do not need to test internal services (behind firewall), you can use 
<a href="http://rest-client.com">rest-client.com</a> as well.

Create deb package
------------------

To create deb package, you need __oh-deb-build__ 
(https://github.com/pete911/oh-deb-build) installed. Run:

    ./build <build_number>

Which will generate __oh-rest-client_0.1-<build_number>_all.deb__ package.
Before you install the package you will need __oh-tomcat-app__ 
(https://github.com/pete911/oh-tomcat-app) installed.

Application is installed to __/opt/oh-rest-client__ directory. 
Logs are in __/var/log/oh-rest-client__ and configuration is in 
__/etc/default/oh-rest-client__.
