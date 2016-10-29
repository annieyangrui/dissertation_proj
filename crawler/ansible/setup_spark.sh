#!/bin/bash

sudo add-apt-repository ppa:openjdk-r/ppa -y
sudo apt-get update
sudo apt-get install -y vim htop openjdk-8-jdk git

wget http://d3kbcqa49mib13.cloudfront.net/spark-2.0.0-bin-hadoop2.7.tgz
tar -xzf spark-2.0.0-bin-hadoop2.7.tgz
mv spark-2.0.0-bin-hadoop2.7 spark