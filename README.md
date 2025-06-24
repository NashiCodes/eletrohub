Welcome to EletroHub!

The EletroHub project is a platform for selling and managing electronic products. It provides a comprehensive solution for both customers and administrators to interact with the platform.

### 🌟 Setting Up the Environment for the First Time

#### 1️⃣  Fork this repository
1. Go to the [EletroHub repository](https://github.com/NashiCodes/eletrohub).
2. Click on the "Fork" button at the top right corner of the page to create a copy of the repository in your GitHub account.
3. Clone your forked repository to your local machine:
   ```sh
   mkdir ~/repositories -p
    cd ~/repositories
    git clone (your-fork-url)
    cd eletrohub
    ```
#### 2️⃣ Install openjdk 17
##### 🐧 Linux:
```sh
sudo apt update && sudo apt upgrade -y
apt-cache search openjdk
sudo apt-get install openjdk-17-jdk -y
```
##### Set it to default
```sh
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

##### Install Maven
```sh
sudo apt install maven
```

##### 🪟 Windows:
JDK 17: [Download here](https://www.oracle.com/java/technologies/downloads/#jdk17-windows)

Maven: [Download here](https://maven.apache.org/download.cgi)

> 📌 _Set environment variables `JAVA_HOME` and `MAVEN_HOME` as per [this guide](https://dicasdeprogramacao.com.br/como-instalar-o-maven-no-windows/)._

### 3️⃣ Check if JAVA_HOME is set so that maven finds the correct java

```sh
echo $JAVA_HOME
```

### 5️⃣ Setup Infra (postgresql)


1. **Install Docker**: Ensure you have Docker installed on your machine. You can follow the installation guide at [Docker Installation](https://docs.docker.com/engine/install/).
2. **Add your user to the Docker group**: This allows you to run Docker commands without `sudo`. Run the following commands in your terminal:
   ```bash
   sudo groupadd docker
   sudo usermod -aG docker $USER
   ```
   After running these commands, log out and log back in to apply the changes.
3. **Start the EletroHub infrastructure**: Open your terminal and navigate to the EletroHub directory. Then run:
   ```bash
   ./tools/infra.sh start
   ```
   This command will start the postgres database infrastructure for EletroHub.


### ✅ Useful Commands

#### Infra help
```sh
./tools/infra.sh help
```

#### Check infra status
```sh
./tools/infra.sh status
```
#### Stop infra
```sh
./tools/infra.sh stop
```
#### Reset database
```sh
./tools/infra.sh resetdb
```