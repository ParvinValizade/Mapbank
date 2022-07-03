package com.company.map.service;

import com.company.map.dto.AccountDto;
import com.company.map.dto.CreateAccountRequest;
import com.company.map.dto.MoneyTransferRequest;
import com.company.map.dto.UpdateAccountRequest;
import com.company.map.dto.converter.AccountDtoConverter;
import com.company.map.exception.AccountNotFoundException;
import com.company.map.exception.BalanceNotEnoughException;
import com.company.map.model.Account;
import com.company.map.model.Customer;
import com.company.map.repository.AccountRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountDtoConverter converter;
    private final CustomerService customerService;

    private final DirectExchange exchange;

    private final AmqpTemplate rabbitTemplate;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;


    public AccountService(AccountRepository accountRepository, AccountDtoConverter converter, CustomerService customerService, DirectExchange exchange, AmqpTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.converter = converter;
        this.customerService = customerService;
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<AccountDto> getAllAccounts(){
        return converter.convert(accountRepository.findAll());
    }

    public AccountDto createAccount(CreateAccountRequest request){
        Customer customer = customerService.findCustomerById(request.getCustomerId());

        Account account = new Account(
                request.getId(),
                request.getCustomerId(),
                request.getBalance(),
                customer.getCity(),
                request.getCurrency()
        );
        return converter.convert(accountRepository.save(account));
    }

    public AccountDto updateAccount(String id, UpdateAccountRequest request){
                Account account = findAccountById(id);
                Account updatedAccount = new Account(
                        account.getId(),
                        request.getCustomerId(),
                        request.getBalance(),
                        account.getCity(),
                        request.getCurrency()
                );
                return converter.convert(accountRepository.save(updatedAccount));
    }

    public AccountDto addMoney(String id,Double amount){
        Account account = findAccountById(id);
        account.setBalance(account.getBalance()+amount);
        return  converter.convert(accountRepository.save(account));
    }

    public AccountDto withdrawMoney(String id,Double amount){
        Account account = findAccountById(id);
        if (account.getBalance()>amount){
            account.setBalance(account.getBalance()-amount);
        } else {
            throw new BalanceNotEnoughException("Not have "+amount+" in account, balance:"+account.getBalance());
        }
        return converter.convert(accountRepository.save(account));
    }

    public void transferMoney(MoneyTransferRequest transferRequest){
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, transferRequest);
    }

    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void transferMoneyMessage(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
                    if (account.getBalance() > transferRequest.getAmount()) {
                        account.setBalance(account.getBalance() - transferRequest.getAmount());
                        accountRepository.save(account);
                        rabbitTemplate.convertAndSend(exchange.getName(), "secondRoute", transferRequest);
                    } else {
                        System.out.println("Insufficient funds -> accountId: " + transferRequest.getFromId() + " balance: " + account.getBalance() + " amount: " + transferRequest.getAmount());
                    }},
                () -> System.out.println("Account not found")
        );
    }

    @RabbitListener(queues = "secondStepQueue")
    public void updateReceiverAccount(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getToId());
        accountOptional.ifPresentOrElse(account -> {
                    account.setBalance(account.getBalance() + transferRequest.getAmount());
                    accountRepository.save(account);
                    rabbitTemplate.convertAndSend(exchange.getName(), "thirdRoute", transferRequest);
                },
                () -> {
                    System.out.println("Receiver Account not found");
                    Optional<Account> senderAccount = accountRepository.findById(transferRequest.getFromId());
                    senderAccount.ifPresent(sender -> {
                        System.out.println("Money charge back to sender");
                        sender.setBalance(sender.getBalance() + transferRequest.getAmount());
                        accountRepository.save(sender);
                    });

                }
        );
    }

    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeTransfer(MoneyTransferRequest transferRequest){
        Account fromAccount = findAccountById(transferRequest.getFromId());
        System.out.println("Sender("+fromAccount.getId()+") new account balance: "+fromAccount.getBalance());

        Account toAccount = findAccountById(transferRequest.getToId());
        System.out.println("Receiver("+toAccount.getId()+") new account balance: "+toAccount.getBalance());

    }

    public void deleteAccount(String id){
        findAccountById(id);
        accountRepository.deleteById(id);
    }

    protected Account findAccountById(String id){
        return accountRepository.findById(id)
                .orElseThrow(()->new AccountNotFoundException("Account could not find by id: " + id));
    }
}
