package com.company.map.service;

import com.company.map.dto.AccountDto;
import com.company.map.dto.CreateAccountRequest;
import com.company.map.dto.UpdateAccountRequest;
import com.company.map.dto.converter.AccountDtoConverter;
import com.company.map.exception.AccountNotFoundException;
import com.company.map.model.Account;
import com.company.map.model.Customer;
import com.company.map.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountDtoConverter converter;
    private final CustomerService customerService;


    public AccountService(AccountRepository accountRepository, AccountDtoConverter converter, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.converter = converter;
        this.customerService = customerService;
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

    public void deleteAccount(String id){
        findAccountById(id);
        accountRepository.deleteById(id);
    }

    protected Account findAccountById(String id){
        return accountRepository.findById(id)
                .orElseThrow(()->new AccountNotFoundException("Account could not find by id: " + id));
    }
}
