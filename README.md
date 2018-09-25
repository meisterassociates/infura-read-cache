# infura-read-cache

Infura Read Cache application, caching the following infura endpoints and proxying the rest:
* `/v1/jsonrpc/ropsten/eth_gasPrice`
* `/v1/jsonrpc/ropsten/eth_getBlockByHash`
* `/v1/jsonrpc/ropsten/eth_getBlockTransactionCountByHash`
* `/v1/jsonrpc/ropsten/eth_getTransactionByBlockHashAndIndex`

Note: Requests to the endpoints specified above with a network other than `ropsten` will result in a 503.

## Installation

### Dependencies
* Java 10 [download](https://java.com/en/)
* Gradle [installation guide](https://docs.gradle.org/current/userguide/installation.html)


### IDE Support
* Run `./gradlew idea` if using IntelliJ IDEA
* Run `./gradlew eclipse` if using Eclipse

### Build
Run `./gradlew build` 

### Run Tests
Run `./gradlew test`

## Running The Application
### Default Arguments:
Run `./gradlew bootRun`

### Command Line Overrides:
Run `./gradlew bootRun -Pargs=<comma-separated arguments here>`
* Example: `./gradlew bootRun -Pargs=--server.port=12345,--gas.price.refresh.seconds=12`

### Command Line Arguments
* `--server.port=9898` sets the API server's port to 9898 (default is 9999)
* `--gas.price.refresh.seconds=123` sets the Infura Gas Price sampling interval to every 123 seconds (default is 20)


### Viewing Logs
Run logs can be found in `logs/` in the project's root directory. `app.log` is the application log is probably of the most interest, but `springboot.log` is there as well to diagnose any boot issues that arise. 