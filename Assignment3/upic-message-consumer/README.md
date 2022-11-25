# Prerequisites
1. setup a new EC2 instance (Amazon Linux 2, x86_64)
2. sudo yum install java-1.8.0

# Deploy
1. mvn clean
2. mvn package
3. scp entire target folder to your EC2 via command:
```
scp -i "/System/Volumes/Data/codespace/keys/linux-key.pem" -r target ec2-user@ec2-52-33-213-75.us-west-2.compute.amazonaws.com:/home/ec2-user/upic-message-consumer
```
4. run the app in a screen session
```
with CLI application screen, we are able to esialy restore the SSH session after reconnect it.
$ ssh -i "/System/Volumes/Data/codespace/keys/linux-key.pem" ec2-user@ec2-52-13-7-192.us-west-2.compute.amazonaws.com
$ screen (or screen -rd to re-attache the session. to quit a screen, use Control + a -> d)
$ ### --> start message consumer with 200 threads/channles
$ ~/upic-message-consumer/appassembler/bin/app <mq-host-ip> <redis-host-ip> 200
```