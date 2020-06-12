# DL-Independent_data_storage

## Summary
The data collected from the various IoT platforms, are stored in a distributed manner, where the data of each platform and deployment site are stored to its own database, while for those platforms that do not provide data storage services, the ACTIVAGE independent data storage is used. This ensures that all data are stored in online databases, following the relevant requirements of the DSs. Here are the services provided by the Independent Data Storage component

## Config & use
The Independent Data Storage docker image is pre-configured to be deployed on your machine in the port number 4567. This image also includes the deployment of a InfluxDB data stoge in the port 8086. 
You can change the configuration of the ports in the file docker-compose.yml (placed in the root folder of this repository) by changing the port attribute in the corresponding service.

### Swagger 
Swagger description can be consulted at http://localhost:4567/swagger

For more information about the use of the component consult its Wiki.


## Deployment
First, clone this repository:

`git clone https://git.activageproject.eu/Data_Analytics/DL-Independent_data_storage.git`

In case you want to change the configuration, performd the appropiate changes in the docker-compose.yml file.

Next, build the docker image following these steps:
1) Open a terminal

2) Run `docker --version` to ensure that you have a supported version of Docker
 
3) Run `docker-compose --version` to ensure that you have a supported version of Docker Compose

4) Go to the root folder of this repository (where the file docker-compose.yml is located)

5) Run `docker build -t independentdatastorage:<version_number> . `

6) Run `docker-compose up`