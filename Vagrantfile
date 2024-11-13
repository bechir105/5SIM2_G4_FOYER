# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  # Set the box
  config.vm.box = "ubuntu/bionic64"

  # Sync project folder to the VM
  config.vm.synced_folder ".", "/vagrant"



  # Allocate more memory for SonarQube to run smoothly
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "8192"  # Allocate 4GB to ensure enough memory for SonarQube
  end

config.vm.network "private_network", ip: "192.168.167.10"


  # Provisioning script
  config.vm.provision "shell", inline: <<-SHELL
    # Update and install essential packages
    sudo apt-get update
    sudo apt-get install -y git maven apt-transport-https ca-certificates curl software-properties-common

    # Install Docker
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
    sudo apt-get update
    sudo apt-get install -y docker-ce
    sudo systemctl start docker
    sudo systemctl enable docker

    # Install Jenkins
    wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
    sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
    sudo apt-get update
    sudo apt-get install -y jenkins
    sudo systemctl start jenkins
    sudo systemctl enable jenkins
  SHELL

  # Forward Jenkins default port
  config.vm.network "forwarded_port", guest: 8080, host: 1234

  # Forward SonarQube port
  config.vm.network "forwarded_port", guest: 9000, host: 9000
end
