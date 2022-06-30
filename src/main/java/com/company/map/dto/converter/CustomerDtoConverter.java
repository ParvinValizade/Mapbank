package com.company.map.dto.converter;

import com.company.map.dto.CityDto;
import com.company.map.dto.CustomerDto;
import com.company.map.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerDtoConverter {

    public CustomerDto convert(Customer from){
        return new CustomerDto(
                from.getId(),
                from.getName(),
                from.getDateOfBirth(),
                CityDto.valueOf(from.getCity().name()),
                from.getAddress()
        );
    }

    public List<CustomerDto> convert(List<Customer> from){
        return from.stream().map(this::convert).collect(Collectors.toList());
    }

}
