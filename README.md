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
     url: https://api.dragonglass.me/hedera/api
     apiKey: %YOUR_API_KET%
     pollingInterval: ${POLLING_INTERVAL:2000}
   - name: hcs
     type: MIRROR
     url: hcs.testnet.mirrornode.hedera.com:5600
```
## Registering a Transaction Monitor

```yaml
transactionFilters:
- nodeName: kabuto
  type: "FROM_ADDRESS"
  transactionIdentifierValue: 0.0.46764
  statuses: ['FAILED']
- nodeName: dragonglass
  type: "FROM_ADDRESS"
  transactionIdentifierValue: 0.0.46764
  statuses: ['SUCCESS']
- nodeName: hcs
  nodeType: MIRROR
  type: "TOPIC"
  transactionIdentifierValue: 0.0.65133
```

### Transaction Events
When a new transaction that matches a transaction monitor is sealed / validated / mined the following json us generated:

For Kabuto:
``` 
{
  "_id": "5ee3b8f1bef20135030abf74",
  "txId": "0.0.8660@1590680538.643000000",
  "hash": "2a94c034e51169c56a227d420f0d6f55eeebcd4d9904cef26c3f12e879d3f030203f01023b997783f2c0a86c2fa54f34",
  "validStartAt": "2020-05-28T15:42:18.643Z",
  "consensusAt": "2020-05-28T15:42:29.695491001Z",
  "value": "100000000000",
  "fee": "86358909",
  "memo": "",
  "status": "SUCCESS",
  "node": "0.0.3",
  "type": "CONTRACT_CALL",
  "operator": "0.0.8660",
  "nodeType": "KABUTO",
  "transfers": [
    {
      "account": "0.0.9469",
      "amount": "100000000000"
    },
    {
      "account": "0.0.98",
      "amount": "81566607"
    },
    {
      "account": "0.0.3",
      "amount": "4792302"
    },
    {
      "account": "0.0.8660",
      "amount": "-100086358909"
    }
  ]
}
```

For Dragonglass:
``` 
{
  "_id": "5ee3b8fbbef20135030abf7d",
  "txId": "00197831591982298888333854-SUCCESS",
  "hash": "ad1dd41d23135c7ccefd3da7da5ed7f22ad81a07b61fb9ff532bf500cc9be8d493a35a6c6db66a130a104da89af6bfbf",
  "validStartAt": "2020-06-12T17:18:18.888+0000",
  "consensusAt": "2020-06-12T17:18:29.995+0000",
  "value": "477043",
  "fee": "387043",
  "memo": "",
  "status": "SUCCESS",
  "type": "CRYPTO",
  "nodeType": "DRAGONGLASS",
  "transfers": [
    {
      "account": "0.0.8",
      "amount": "15069"
    },
    {
      "account": "0.0.98",
      "amount": "371974"
    },
    {
      "account": "0.0.14684",
      "amount": "90000"
    },
    {
      "account": "0.0.14698",
      "amount": "-90000"
    },
    {
      "account": "0.0.19783",
      "amount": "-387043"
    }
  ]
}
```

For HCS:
``` 
{
  "mirrorNode": "hcs.testnet.mirrornode.hedera.com:5600",
  "topicId": "0.0.68412",
  "consensusTimestamp": 1591959712.501942,
  "message": "Oxiles HCS example message",
  "sequenceNumber": 4
}
```
