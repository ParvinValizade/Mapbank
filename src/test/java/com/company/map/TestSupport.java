package com.company.map;

import com.company.map.dto.CityDto;
import com.company.map.dto.CustomerDto;
import com.company.map.model.City;
import com.company.map.model.Customer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {

    public static List<Customer> generateCustomers(){
        return IntStream.range(0,5).mapToObj(
                i-> new Customer(i+"a",
                        "TestName"+i,
                        2000,
                        City.Baku,
                        "Yasamal"+i)
        ).collect(Collectors.toList());
    }

    public List<CustomerDto> generateCustomerDtoList(List<Customer> customerList){
        return customerList.stream()
                .map(from-> new CustomerDto(
                        from.getId(),
                        from.getName(),
                        from.getDateOfBirth(),
                        CityDto.valueOf(from.getCity().name()),
                        from.getAddress()
                )).collect(Collectors.toList());
    }
}
