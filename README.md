# Python Kafka Consumer

### Dependencies
- pip install pandas
- pip install kafka-python
- pip install pyspark
- pip install numpy

***

# Kafka Setup
## Commands to run on a WSL

**Get WSL IP to set on the consumer, producer and also on kafka server.properties**
- `ip addr | grep wifi0`

### Environment setup
``` 
sudo apt update
sudo apt upgrade
sudo apt-get install curl
sudo apt-get install vim
curl https://dlcdn.apache.org/kafka/3.2.0/kafka_2.12-3.2.0.tgz -o kafka_2.12-3.2.0.tgz
tar -xvf ./kafka_2.12-3.2.0.tgz
cd kafka_2.12-3.2.0
mkdir logs
```

### Set the kafka ip
- `cd config`
- `vim server.porperties` and add on this file:
- `listeners=PLAINTEXT://<WSL-IP>:9092`
- `cd ..`

### On the kafka folder start the zookeper and kafka
- `./bin/zookeeper-server-start.sh ./config/zookeeper.properties > ./logs/start_zk.log &`

- `./bin/kafka-server-start.sh ./config/server.properties > ./logs/start_kafka.log &`

### Create a topic
- `./bin/kafka-topics.sh --create --topic streaming-bd --bootstrap-server <WSL-IP>:9092 --replication-factor 1 --partitions 1`
- `./bin/kafka-topics.sh --create --topic streaming-feedback-bd --bootstrap-server <WSL-IP>:9092 --replication-factor 1 --partitions 1`

### List topics to check if everything is fine
- `./bin/kafka-topics.sh --list --bootstrap-server <WSL-IP>:9092`

### Check kafka 
`ps -ef | grep kafka | wc -l`

***
## At this moment your kafka is ready to rock and roll
