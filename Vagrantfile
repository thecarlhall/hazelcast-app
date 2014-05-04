# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

NUM_INSTANCES = 4

PROVISION_BASE = %(
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9

echo "deb http://get.docker.io/ubuntu docker main" > /etc/apt/sources.list.d/docker.list

apt-get -y update
apt-get -y install linux-image-extra-`uname -r` lxc-docker
)

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "ubuntu14.04"

  # Fix docker not being able to resolve private registry in VirtualBox
  config.vm.provider :virtualbox do |vb, override|
    vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
  end

  config.vm.define("docker") do |cfg|
    cfg.vm.provision :shell, inline: PROVISION_BASE
  end

  (1..NUM_INSTANCES).each do |i|
    config.vm.define("hazelcast-docker#{i}") do |cfg|
      cfg.vm.network :private_network, ip: "192.168.50.#{i + 10}"
      cfg.vm.network :forwarded_port, guest: 8080, host: 8080 + i
      cfg.vm.provision :shell, inline: %Q(
        #{PROVISION_BASE}
        echo 'DOCKER_OPTS=\"-r=false\"' > /etc/default/docker
        docker load -i /vagrant/tomcat.tar
        cp /vagrant/etc/tomcat-docker.conf /etc/init/tomcat.conf
        service tomcat restart
      )
    end
  end

  (1..NUM_INSTANCES).each do |i|
    config.vm.define("hazelcast#{i}") do |cfg|
      cfg.vm.network :private_network, ip: "192.168.50.#{i + 10}"
      cfg.vm.network :forwarded_port, guest: 8080, host: 8080 + i
      cfg.vm.provision :shell, inline: %Q(
        apt-get -y install openjdk-7-jre-headless
        mkdir -p /usr/share/tomcat
        curl http://apache.osuosl.org/tomcat/tomcat-8/v8.0.5/bin/apache-tomcat-8.0.5.tar.gz | tar zxf - --strip=1 -C /usr/share/tomcat/
        cp /vagrant/target/hazelcast-app.war /usr/share/tomcat/webapps/
        cp /vagrant/etc/tomcat.conf /etc/init/tomcat.conf
        service tomcat restart
      )
    end
  end
end
