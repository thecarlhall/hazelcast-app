# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

NUM_INSTANCES = 4

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # Fix docker not being able to resolve private registry in VirtualBox
  config.vm.provider :virtualbox do |vb, override|
    vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
  end

  config.vm.define("docker") do |cfg|
    cfg.vm.box = "ubuntu14.04"
    cfg.vm.provision :shell, inline: %(
apt-get -y update
apt-get -y install linux-image-extra-`uname -r`

apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9

sh -c "echo deb http://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list"
apt-get -y update
apt-get -y install lxc-docker
)
  end

  (1..NUM_INSTANCES).each do |i|
    config.vm.define("hazelcast#{i}") do |cfg|
      cfg.vm.box = "coreos-alpha"
      cfg.vm.box_url = "http://storage.core-os.net/coreos/amd64-usr/alpha/coreos_production_vagrant.box"

      #cfg.vm.network "public_network"
      #cfg.vm.network "private_network", ip: "192.168.50.1#{i}"
      cfg.vm.network :forwarded_port, guest: 8080, host: 8080 + i

      #cfg.vm.provider :virtualbox do |vb, override|
      #  vb.customize ["modifyvm", :id, "--uart1", "0x3F8", "4"]
      #  vb.customize ["modifyvm", :id, "--uartmode1", serialFile]
      #end
    end
  end
end
