# OXILES

The oxiles concept has been incepted during the las 2 years as part of the daily basis work, research and common challehges that appeared on the several blockchain projects we have been working on.

Blockchain / DLT  is a powerful but complex ecosystem to start with. It encompasses different technologies, concepts and tools, like distributed networks, chain, blocks, consensus algorithms, cryptography, and p2p protocols, that challenges any project start.

We envisioned a set of components included on a blockchain agnostic orchestration platform, that facilitates project implementation, speeds up business value delivery, and includes production support and easy customization possibilities.
A product that shifts focus to business value and encapsule complexity on a simple use platform.

That's oxiles. The baseline of oxiles has been leveraged on [Eventeum](https://github.com/ConsenSys/Eventeum), the Ethereum Event Bridge. We are core committers of Eventeum, but We have forked the project, to extend the current capabilities to make it blockchain agnostic
and multi Input Listener and Broadcaster. All credits to Kauri, Consensys and specially Craig Williams for all this base work on Ethereum.
On the next months, we will try to keep the project linked but separated, using Oxiles as a Library and extending the core from outside.


Our start vision is to enable blockchain developers to easily connect the ledgers with enterprise broadcasters, and facilitate integration with IFTT concepts.

Nowadays we support Hashgraph and Ethereum as Blockchain origin, and Pulsar, HTTP, Rabbit, Kafka and Hedera HCS Topics as broadcasters. 


## Features

Added to current Eventeum functionalities, configuration, which is exactly the same (by now), we support:

* Hedera HCS input listener
* Hedera HCS broadcaster
* Hedera Kabuto transaction input listener
* Hedera DragonGlass transaction input listener
* Soon Hedera mirror node transaction and smart contract event listener 

## Demo / Example 

Please checkout our demo project, to run oxiles and experience a full working example with a basic usage

[Oxiles event bridge demo](https://github.com/oxiles/oxiles-event-bridge-demo)


## Configuring Nodes
Listening for events from multiple different nodes, from different blockchains is supported in Oxiles, and these nodes can be configured in the yaml file.

```yaml
ethereum:
  nodes:
    - name: default
      url: http://mainnet:8545
    - name: sidechain
      url: wss://sidechain/ws

hashgraph:
  nodes:
    - name: kabuto
      type: KABUTO  
      url: http://api.testnet.kabuto.sh/v1
      pollingInterval: ${POLLING_INTERVAL:2000}
   - name: hcs
     type: MIRROR
     url: hcs.mainnet.mirrornode.hedera.com:5600
   - name: dragonlass
     type: DRAGONGLASS
     url: hcs.mainnet.mirrornode.hedera.com:5600
     pollingInterval: ${POLLING_INTERVAL:2000}
```
## Registering a Transaction Monitor

transactionFilters:
- nodeName: kabuto
  type: "FROM_ADDRESS"
  transactionIdentifierValue: 0.0.46764
  statuses: ['FAILED']
transactionFilters:
- nodeName: dragonglass
  type: "FROM_ADDRESS"
  transactionIdentifierValue: 0.0.46764
  statuses: ['SUCCESS']


### Transaction Events
When a new transaction that matches a transaction monitor is sealed / validated / mined the following json us generated

```