package com.bosch.coding;

import com.bosch.coding.producers.Producer;
import com.bosch.coding.producers.ProducerFactory;
import com.bosch.coding.entity.InventoryItem;
import com.bosch.coding.entity.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerMain {
    private static final Logger logger = LoggerFactory.getLogger(ProducerMain.class);

    public static void main(String[] args) {
        while (true) {
            final InventoryRequestEvent event = InventoryRequestEventFactory.createEvent();
            for (final InventoryItem item : event.getInventoryItems()) {
                try {
                    final Producer producer = ProducerFactory.getProducer(item.getType());
                    producer.startProducing(item);
                } catch (Exception e) {
                    logger.info("Unexpected error while producer generation: {}", e.getMessage());
                }
            }
        }
    }
}
