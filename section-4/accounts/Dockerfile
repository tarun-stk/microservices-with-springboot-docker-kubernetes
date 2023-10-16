# name of the file must be Dockerfile so that Docker can search for this file

# openjdk - image name
# 17-jdk-slim - tag name  -> similar to version name
# by doijng this we're saying to docker that we need one dependency or image, so that we can run this project
# in this case that is java
FROM openjdk:17-jdk-slim

# we're saying who is maintaining this docker image
MAINTAINER tarunstk

# in total we need two things to run this docker image successfully
# those are java, and jar file
# We're adding apps jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar


# executing the app
# below is similar to doing java -jar accounts-0.0.1-SNAPSHOT.jar in commmand line
# which runs jar file
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]


#Challenges of using Dockerfile approach
# 1. for small apps it's easy, but when app is big you need lot of configuration inside Dockerfile
# 2. docker commands, you need know all low level docker commands
# 3. You've to maintain Dockerfiles for all your microservices, which is nightmare
# 4. You cannot ignore security of your docker image, that also should be taken care of

#To overcome these challenges we've two more appraoches which are google jib and buildpacks
#above two will make creating docker images easy, without using dockerFile

















