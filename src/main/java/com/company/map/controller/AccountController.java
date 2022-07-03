package com.company.map.controller;

import com.company.map.dto.AccountDto;
import com.company.map.dto.CreateAccountRequest;
import com.company.map.dto.MoneyTransferRequest;
import com.company.map.dto.UpdateAccountRequest;
import com.company.map.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest request){
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String id,
                                                    @RequestBody UpdateAccountRequest request){

        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDto> withdrawMoney(@PathVariable String id, @PathVariable Double amount) {
        return ResponseEntity.ok(accountService.withdrawMoney(id, amount));
    }

    @PutMapping("/add/{id}/{amount}")
    public ResponseEntity<AccountDto> addMoney(@PathVariable String id, @PathVariable Double amount) {
        return ResponseEntity.ok(accountService.addMoney(id, amount));
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest) {
        accountService.transferMoney(transferRequest);
        return ResponseEntity.ok("İşleminiz başarıyla alınmıştır!");
    }



}
