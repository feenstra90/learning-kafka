package com.gmail.feenstra90.learning;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyDescription;
import org.apache.kafka.streams.kstream.Named;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Starter {
    private static final Logger logger = Logger.getLogger(Starter.class.getCanonicalName());

    public static void main(String[] args) {
        Properties configuration = getConfiguration();
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // Input: word-count-input, split by space (get individual words), use the word as a key, count by key, output to word-count-output/
        streamsBuilder.<String, String>stream("word-count-input")
                .mapValues(value -> value.toLowerCase())
                .flatMapValues(value -> Arrays.asList(value.split(" "))) // In the tutorial space is used, regex pattern for whitespace might be 'nicer' to use.
                .selectKey((ignoredKey, word) -> word)
                .groupByKey()
                .count(Named.as("Counts"))
                .toStream().to("word-count-output");

        final Topology topology = streamsBuilder.build();
        KafkaStreams streams = new KafkaStreams(topology, configuration);
        streams.start();

        registerShutDownHook(streams);
        printTopologyToLog(topology);
    }

    private static Properties getConfiguration() {
        // XXX tutorial we are following put's the configuration here...
        // shouldn't we use a application.properties file or launch it with the properties though?
        // At the very least the server?

        // TODO: look at other configuration options to get a better understanding of the options.

        Properties configuration = new Properties();
        configuration.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-starter-app");
        configuration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configuration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configuration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        configuration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return configuration;
    }

    private static void registerShutDownHook(final KafkaStreams streams) {
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static void printTopologyToLog(final Topology topology) {
        final TopologyDescription topologyDescription = topology.describe();
        logger.log(Level.CONFIG, topology.describe().toString());
    }
}
