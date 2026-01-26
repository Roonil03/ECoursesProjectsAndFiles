
# This is a bash script that runs the different scripts that run during the WordCount Program
sudo docker-compose up -d
sudo docker cp ./mapred-site.xml hadoop_namenode_1:/opt/hadoop/etc/hadoop/mapred-site.xml
sudo docker cp ./words.txt hadoop_namenode_1:/opt/hadoop/words.txt
sudo docker exec -it hadoop_namenode_1 /bin/bash
cat hdfs_commands_1
sudo docker-compose down
