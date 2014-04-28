# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

BOOT_SCRIPT = %(
apt-get -y install jetty8
sed -i "s/NO_START=1/NO_START=0/" /etc/default/jetty8
sed -i "s/#JETTY_HOST=\$(uname -n)/JETTY_HOST=0.0.0.0/" /etc/default/jetty8
cp /vagrant/target/hazelcast-app.war /var/lib/jetty8/webapps/
service jetty8 restart
)

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  (1..4).each do |i|
    config.vm.define("hazelcast#{i}") do |cfg|
      cfg.vm.box = "ubuntu14.04"
      cfg.vm.provision :shell, inline: BOOT_SCRIPT

      #cfg.vm.network "public_network"
      #cfg.vm.network "private_network", ip: "192.168.50.1#{i}"
      cfg.vm.network :forwarded_port, guest: 8080, host: 8080 + i
    end
  end
end
