package com.company.map.service;

import com.company.map.dto.CreateCustomerRequest;
import com.company.map.dto.CustomerDto;
import com.company.map.dto.UpdateCustomerRequest;
import com.company.map.dto.converter.CustomerDtoConverter;
import com.company.map.exception.CustomerNotFoundException;
import com.company.map.model.City;
import com.company.map.model.Customer;
import com.company.map.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter converter;

    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter converter) {
        this.customerRepository = customerRepository;
        this.converter = converter;
    }

    public CustomerDto createCustomer(CreateCustomerRequest request){
        Customer customer = new Customer(
                request.getId(),
                request.getName(),
                request.getDateOfBirth(),
                City.valueOf(request.getCity().name()),
                request.getAddress()
        );
        return converter.convert(customerRepository.save(customer));
    }

    public List<CustomerDto> getAllCustomers(){
        return converter.convert(customerRepository.findAll());
    }

    public CustomerDto getCustomerById(String id) {
        Customer customer = findCustomerById(id);
        return converter.convert(customer);
    }

    public void deleteCustomer(String id) {
        findCustomerById(id);
        customerRepository.deleteById(id);
    }


    public CustomerDto updateCustomer(String id, UpdateCustomerRequest request) {
        Customer customer = findCustomerById(id);
        Customer updatedCustomer  = new Customer(
                customer.getId(),
                request.getName(),
                customer.getDateOfBirth(),
                City.valueOf(request.getCity().name()),
                request.getAddress()
        );

        return converter.convert(customerRepository.save(updatedCustomer));

    }

    protected Customer findCustomerById(String id){
        return customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("Customer could not find by id: " + id));
    }
}
