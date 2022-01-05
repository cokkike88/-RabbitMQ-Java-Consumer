import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.rmi.server.ExportException;

public class Worker {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for message. To exit press CTRL+C");
        // Prefetch means that the queue only can to receive a message at a time, and it can receive another message
        // until the current message has processed (acknowledged)
        var preFetch = 1;
        channel.basicQos(preFetch);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            try{
                doWork(message);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
    }

    private static void doWork(String task) throws InterruptedException {
        for(char ch: task.toCharArray()){
            if(ch == '.') Thread.sleep(1000);
        }
    }
}
