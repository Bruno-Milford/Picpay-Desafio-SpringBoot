package com.brunomilford.picpaydesafiobackend.transaction;

import java.util.List;

import javax.management.Notification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brunomilford.picpaydesafiobackend.Authorization.AuthorizerService;
import com.brunomilford.picpaydesafiobackend.notification.NotificationService;
import com.brunomilford.picpaydesafiobackend.wallet.Wallet;
import com.brunomilford.picpaydesafiobackend.wallet.WalletRepository;
import com.brunomilford.picpaydesafiobackend.wallet.WalletType;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;
  private final AuthorizerService authorizerService;
  private final NotificationService notificationService;

  public TransactionService(
    TransactionRepository transactionRepository,
    WalletRepository walletRepository,
    AuthorizerService authorizerService,
    NotificationService notificationService
  ) {
    this.transactionRepository = transactionRepository;
    this.walletRepository = walletRepository;
    this.authorizerService = authorizerService;
    this.notificationService = notificationService;
  }

  @Transactional
  public Transaction create(Transaction transaction) {
    validate(transaction);

    var newTransaction = transactionRepository.save(transaction);

    var walletPayer = walletRepository.findById(transaction.payer()).get();
    var walletPayee = walletRepository.findById(transaction.payee()).get();

    walletRepository.save(walletPayer.debit(transaction.value()));
    walletRepository.save(walletPayee.credit(transaction.value()));

    authorizerService.authorize(transaction);

    notificationService.notify(transaction);

    return newTransaction;
  }

  private void validate(Transaction transaction) {
    walletRepository.findById(transaction.payee())
      .map(payee -> walletRepository.findById(transaction.payer())
        .map(payer -> isTransactionValid(transaction, payer) ? true : null)
        .orElseThrow(() -> new InvalidTransacationException("Invalid Transaction" + transaction)))
      .orElseThrow(() -> new InvalidTransacationException("Invalid Transaction" + transaction));
  }

  private boolean isTransactionValid(Transaction transaction, Wallet payer) {
    return payer.type() == WalletType.COMUM.getValue() &&
        payer.balance().compareTo(transaction.value()) >= 0 &&
        !payer.id().equals(transaction.payee());
  }

  public List<Transaction> list() {
    return transactionRepository.findAll();
  }
}