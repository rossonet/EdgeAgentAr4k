# Ar4k Edge Agent by Rossonet

![Build QA jar](https://github.com/rossonet/EdgeAgentAr4k/workflows/Build%20QA%20jar/badge.svg)
![Gradle test and build maven archive](https://github.com/rossonet/EdgeAgentAr4k/workflows/Gradle%20test%20and%20build%20maven%20archive/badge.svg)

![alt text](https://www.rossonet.net/wp-content/uploads/2015/01/logoRossonet4.png "Rossonet")

[https://www.rossonet.net](https://www.rossonet.net)

## Run as a service

### Install as local init.d service

The script supports the following features:

- Starts the services as the user that owns the jar file

- Tracks the application’s PID by using /var/run/ar4kAgent/ar4kAgent.pid

- Writes console logs to /var/log/ar4kAgent.log

Assuming that you have a Ar4k Agent application installed in /var/ar4kAgent, to install it as an init.d service, create a symlink, as follows:

```
$ sudo ln -s /var/ar4kAgent/ar4kAgent.jar /etc/init.d/ar4kAgent
```

Once installed, you can start and stop the service in the usual way. For example, on a Debian-based system, you could start it with the following command:

```
$ service ar4kAgent start
```

### Install as Systemd service

systemd is the successor of the System V init system and is now being used by many modern Linux distributions. Although you can continue to use init.d scripts with systemd, it is also possible to launch Ar4kAgent by using systemd ‘service’ scripts.

Assuming that you have a Ar4kAgent installed in /var/ar4kAgent, to install a Spring Boot application as a systemd service, create a script named ar4kAgent.service and place it in /etc/systemd/system directory. The following script offers an example:

```
[Unit]
Description=ar4kAgent
After=syslog.target

[Service]
User=ar4kAgent
ExecStart=/var/ar4kAgent/ar4kAgent.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

Note that, unlike when running as an init.d service, the user that runs the application, the PID file, and the console log file are managed by systemd itself and therefore must be configured by using appropriate fields in the ‘service’ script. Consult the service unit configuration man page for more details.

To flag the application to start automatically on system boot, use the following command:

```
$ systemctl enable ar4kAgent.service
```

Refer to man systemctl for more details.

### Install as Windows service

The windows distribution uses [winsw](https://github.com/kohsuke/winsw).

Warning

You need to install the .NET framework on the target machine first.

To install the sample application as a service, extract the distribution somewhere and open a shell with administrative rights in that directory. Then invoke the following command

```
$ ar4kAgent install
```

Once the service has been installed, you can start it the usual way, that is:

```
$ net start ar4kAgent
```

The logs are available in the logs directory of the distribution.

### Docker build

```
docker build --rm -t ar4k-agent:latest git@github.com:rossonet/EdgeAgentAr4k.git

[...]

docker run -ti --rm ar4k-agent:latest
```
