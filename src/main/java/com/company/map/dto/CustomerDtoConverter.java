package com.company.map.dto;

import com.company.map.model.Customer;
import org.springframework.stereotype.Component;

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

}
