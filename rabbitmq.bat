@REM Pull the rabbitmq image to run it without installation
@REM With real system, the rabbitmq should be installed on the Gateway

docker pull rabbitmq:3-management
docker run --rm -it -p 15672:15672 -p 5672:5672 --name Gateway rabbitmq:3-management