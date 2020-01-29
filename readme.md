#Kafka learning repository
A personal git repository in order to learn kafka basics. 

##Notes

###Terminology
* A Stream is a sequence of records. In kafka they can be replayed, are ordered and are fault tolerant.
* A Stream processor is a node that transforms/consumes incoming streams record by record and can create a new stream.
* Topology is a graph of processors chained together by streams.
* A Source processor -> Takes data directly from a Topic, has no predecessors and does not transform data.
* A Sink processor -> Does not have children, sends stream data directly to a topic. 


### Configuration
bootstrap.server
auto.offset.reset.config
application.id
* Consumer group.id = application.id
* default client.id prefix
* prefix to internal changelog topics
