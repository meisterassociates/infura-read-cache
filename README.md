# infura-read-cache

Infura Read Cache application, caching the following infura endpoints and proxying the rest:
* `/v1/jsonrpc/ropsten/eth_gasPrice`
* `/v1/jsonrpc/ropsten/eth_getBlockByHash`
* `/v1/jsonrpc/ropsten/eth_getBlockTransactionCountByHash`
* `/v1/jsonrpc/ropsten/eth_getTransactionByBlockHashAndIndex`

Note: Requests to the endpoints specified above with a network other than `ropsten` will result in a 503.


# Installation

## Dependencies
* Java 10 [download](https://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)
* Gradle [installation guide](https://docs.gradle.org/current/userguide/installation.html)

## IDE Support
* Run `./gradlew idea` if using IntelliJ IDEA
* Run `./gradlew eclipse` if using Eclipse

## Build
Run `./gradlew build` 

## Run Tests
Run `./gradlew test`


# Running The Application
## Default Arguments:
Run `./gradlew bootRun`

## Command Line Overrides:
Run `./gradlew bootRun -Pargs=<comma-separated arguments here>`
* Example: `./gradlew bootRun -Pargs=--server.port=12345,--gas.price.refresh.seconds=12`

## Optional Command Line Arguments
* `--server.port=9898` sets the API server's port to 9898 (default is 9999)
* `--gas.price.refresh.seconds=123` sets the Infura Gas Price sampling interval to every 123 seconds (default is 20)
* `--gas.cache.purge.after.seconds` configures the time period after which GasPrices cached in memory will be purged
* `--block.cache.purge.after.seconds` configures the time period after which Blocks cached in memory will be purged
* `--infura.base.url` the base url for all infura API calls
* `--infura.v1.path` the path to be appended to the `infura.base.url` for v1 requests
* `--infura.healthy.failed.read.limit` the number of consecutive failed reads from Infura to tolerate before health checks start failing

## Viewing Logs
Run logs can be found in `logs/` in the project's root directory. `app.log` is the application log is probably of the most interest, but `springboot.log` is there as well to diagnose any boot issues that arise. 


# Test Requests
The following requests can be made to manually test the endpoints:

### Cached Infura Endpoints:
`/v1/jsonrpc/:network/eth_gasPrice` 
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_gasPrice

`/v1/jsonrpc/:network/eth_getBlockByHash` 
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_getBlockByHash?params=%5B%220x3c29a133860d1c94a1cafcaea3c24193db97b007ba58e7eda115ecabe9cd063f%22%2Ctrue%5D

`/v1/jsonrpc/:network/eth_getBlockTransactionCountByHash` 
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_getBlockTransactionCountByHash?params=%5B%220x3c29a133860d1c94a1cafcaea3c24193db97b007ba58e7eda115ecabe9cd063f%22%5D

`/v1/jsonrpc/:nework/eth_getTransactionByBlockHashAndIndex` 
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_getTransactionByBlockHashAndIndex?params=%5B%220x3c29a133860d1c94a1cafcaea3c24193db97b007ba58e7eda115ecabe9cd063f%22%2C%220x0%22%5D

### Cache Endpoints:
After the previous requests have been run to populate the cache, the following may be run to retrieve cached data

`/v1/cache/ropsten/eth/gas/price/average/{:hours}h`
* http://127.0.0.1:9999/v1/cache/ropsten/eth/gas/price/average/24h
* http://127.0.0.1:9999/v1/cache/ropsten/eth/gas/price/average/168h

`/v1/cache/ropsten/eth/block/:blockHash/transactions`
* http://127.0.0.1:9999/v1/cache/ropsten/eth/block/0x3c29a133860d1c94a1cafcaea3c24193db97b007ba58e7eda115ecabe9cd063f/transactions

`/v1/cache/ropsten/eth/block/:blockHash/transactions/to/:recipientAddress`
* http://127.0.0.1:9999/v1/cache/ropsten/eth/block/0x3c29a133860d1c94a1cafcaea3c24193db97b007ba58e7eda115ecabe9cd063f/transactions/to/0x3224b140712dbb644caafb881b20d33bd1ddda5d

The `block` endpoints above accept optional `page` and `pageSize` query string parameters for pagination.

### Infura Proxy Endpoints:
This should include all endpoints listed [here](https://infura.io/docs/api/get/eth_accounts) that are not listed above.

Example Requests:
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_getTransactionReceipt?params=%5B%220x3040b16b1f507f10999f81c9beb83956a9d790e2e364000002df1a4869f051d4%22%5D
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_getBlockByNumber?params=%5B%220xfff0%22%2Cfalse%5D
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_getWork
* http://127.0.0.1:9999/v1/jsonrpc/ropsten/eth_hashrate


# Health Checks

### Endpoint
`/v1/health`
* http://127.0.0.1:9999/v1/health

### Response Payloads
Healthy responses will look like this:
```json
{
    "result": {
        "status": "healthy",
        "errorMessages": []
    },
    "id": 0,
    "jsonrpc": "2.0"
}
```

Unhealthy responses will look like this:
```json
{
    "result": {
        "status": "unhealthy",
        "errorMessages": [
          "Error message 1",
          "Error message 2",
          "..."
        ]
    },
    "id": 0,
    "jsonrpc": "2.0"
}
```
