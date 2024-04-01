package com.brunomilford.picpaydesafiobackend.notification;

import org.springframework.stereotype.Service;

import com.brunomilford.picpaydesafiobackend.transaction.Transaction;

@Service
public class NotificationService {
  private final NotificationProducer notificationProducer;

  public NotificationService(NotificationProducer notificationProducer) {
    this.notificationProducer = notificationProducer;
  }

  public void notify(Transaction transaction) {
    notificationProducer.sendNotification(transaction);
  }
}