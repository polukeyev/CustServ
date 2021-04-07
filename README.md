# CustServ
test task with Spring-boot REST API, PostgreSQL, ReactJS frontend and Docker-compose
-----

### How to use project: ###
#### 1. Take a project to your PC by ####
> $ git clone --recursive https://github.com/polukeyev/CustServ

#### 2. Then enter into project dir (CustServ) ####
> $ cd CustServ

#### 3. Build .jar file by ####
> $ ./mvnw clean package

or
> $ ./mvnw clean package -DskipTests

#### 4. Start web-app with ####
> $ docker-compose up --build

#### 5. Check it in your browser by ####
> http://localhost:3000/
