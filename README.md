# Hazelcast Clustering Test App

I was curious of what a clustered Hazelcast setup looked like, so I built a Java webapp that just reads and writes from Hazelcast.

### Vagrant

[Vagrant](https://vagrantup.com) is used for building out a test infrastructure. `Vagrantfile` is included for setting up machines to see the magic happen.

1. **Multiple instances running the application directly**

   These instances run Java and Apache Tomcat locally. This was the easiest to setup and
   worked almost instantly. The web application is added to the VM as a synced folder rather
   than copying the application during provisioning.

2. **Multiple instances using Docker for application deployment**

   As I started things, I got curious of setting up Docker rather than fully provision each
   instance. The results were generally positive but I didn't spend enough time to get the
   clustering to work. The network seen by Hazelcast in the Docker container was not the same
   network that is described in `hazelcast.xml`.

### Docker

`Dockerfile` is used to build a [Docker](https://docker.io) image with Apache Tomcat and bundles the `war` file. I setup the Docker command to attach a volume from the host to install the application. This allows updating the application independently of the container and just requires a container restart to deploy new code.

### Testing

After starting the instances, add data with curl.

```
curl -d name=what -d value=yeah http://localhost:8081/hazelcast-app/
```

You can check that the data is copied to the other nodes:

```
for i in {1..4}; do curl "http://localhost:808$i/hazelcast-app/"; done
```