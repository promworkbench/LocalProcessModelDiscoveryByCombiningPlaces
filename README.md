# Local Process Model (LPM) Discovery

A ProM plugin to discover and display Local Process Models. 

## Usage

### For End Users

#### ProM
To run the plugin in ProM, you first have to install the package _LocalProcessModelDiscoveryByCombiningPlaces_, and 
then you can search for the basic plugin in ProM by the name _Local Process Model Discovery_.

#### Docker
To use the plugin using Docker, navigate to the root of the repository and run the following commands:
`docker compose up --build`
`docker exec -it lpm-wonderland /bin/bash`

Once in the container, three jars are available: `lpm-discovery.jar`, `lpm-distance.jar`, and `lpm-clustering.jar`. 
Any can be run using `java -jar lpm-discovery.jar config-file.json`. Example config files for the different jars are 
given in `data/configs`.

### For Developers

To call the default discovery in Java use: 

`LPMDiscoveryResult result = LPMDiscovery.getInstance().from(xlog);`.
