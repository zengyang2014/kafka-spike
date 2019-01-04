//import util.properties packages
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

//import simple producer packages
//import KafkaProducer packages
//import ProducerRecord packages

//Create java class named "SimpleProducer"
public class SimpleProducer {
    private Properties props;
    private Producer<String, String> producer;
    private static Properties setProperty() {
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", StringSerializer.class.getName());

        props.put("value.serializer", StringSerializer.class.getName());

        return props;
    }

    public SimpleProducer() {
        this.props = setProperty();

        this.producer = new KafkaProducer<String, String>(this.props);
    }

    public void produceMsg(String msg) {
        this.producer.send(new ProducerRecord<String, String>("hello-kafka", msg));
        System.out.println("Message sent successfully");
    }

    public void closeProducer() {
        this.producer.close();
    }
}