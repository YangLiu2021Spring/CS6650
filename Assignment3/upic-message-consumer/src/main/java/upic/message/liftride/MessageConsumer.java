package upic.message.liftride;

public class MessageConsumer implements Runnable {
    private final QueueManager queueManager;
    private final DeliverCallbackProvider deliverCallbackProvider;

    public MessageConsumer(
            QueueManager queueManager,
            DeliverCallbackProvider deliverCallbackProvider
    ) {
        this.queueManager = queueManager;
        this.deliverCallbackProvider = deliverCallbackProvider;
    }

    @Override
    public void run() {
        queueManager.startNewConsumeChannel(deliverCallbackProvider);
    }
}
