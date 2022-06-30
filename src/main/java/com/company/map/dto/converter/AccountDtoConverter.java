package com.company.map.dto.converter;

import com.company.map.dto.AccountDto;
import com.company.map.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountDtoConverter {
    public AccountDto convert(Account from){
        return new AccountDto(
                from.getId(),
                from.getCustomerId(),
                from.getBalance(),
                from.getCurrency()
        );
    }

    public List<AccountDto> convert(List<Account> from){
        return from.stream().map(this::convert).collect(Collectors.toList());
    }
}
