package amqcob.producer;

import javax.jms.*;
import javax.naming.InitialContext;


public class FailoverProducer {

    public static void main(final String[] args) throws Exception {
        final int numMessages = 20;
        Connection connection = null;
        InitialContext initialContext = null;

        try {
            initialContext = new InitialContext();
            Queue queue = (Queue) initialContext.lookup("queue/exampleQueue");
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            connection.start();
            MessageProducer producer = session.createProducer(queue);

            for (int i = 0; i < numMessages; i++) {
                TextMessage message = session.createTextMessage("This is text message " + i);
                producer.send(message);
                System.out.println("Sent message: " + message.getText());
            }

        } finally {

            if (connection != null) {
                connection.close();
            }

            if (initialContext != null) {
                initialContext.close();
            }
        }
    }
}
