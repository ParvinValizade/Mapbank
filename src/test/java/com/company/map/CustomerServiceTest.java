package com.company.map;

import com.company.map.dto.CityDto;
import com.company.map.dto.CreateCustomerRequest;
import com.company.map.dto.CustomerDto;
import com.company.map.dto.UpdateCustomerRequest;
import com.company.map.dto.converter.CustomerDtoConverter;
import com.company.map.exception.CustomerNotFoundException;
import com.company.map.model.City;
import com.company.map.model.Customer;
import com.company.map.repository.CustomerRepository;
import com.company.map.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceTest {
    private CustomerRepository customerRepository;
    private  CustomerDtoConverter converter;

    private CustomerService customerService;

    @BeforeEach
    public void setUp(){
        customerRepository = mock(CustomerRepository.class);
        converter = mock(CustomerDtoConverter.class);

        customerService = new CustomerService(customerRepository,converter);
    }

    @Test
    void  testCreateCustomer_itShouldReturnCustomerDto(){
        String customerId ="1fg";
        CreateCustomerRequest request = new CreateCustomerRequest(customerId,"Anar",1999, CityDto.Baku,"Zabrat");
        Customer customer = new Customer(customerId,"Anar",1999, City.Baku,"Zabrat");
        Customer savedCustomer = new Customer(customerId,"Anar",1999, City.Baku,"Zabrat");
        CustomerDto customerDto = new CustomerDto(customerId,"Anar",1999,CityDto.Baku,"Zabrat");

        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(converter.convert(savedCustomer)).thenReturn(customerDto);

        CustomerDto result = customerService.createCustomer(request);

        assertEquals(customerDto,result);

        verify(customerRepository).save(customer);
        verify(converter).convert(savedCustomer);


    }

    @Test
    void testUpdateCustomer_whenCustomerIdExist_itShouldReturnCustomerDto(){
        String customerId ="1fg";
        Customer customer = new Customer(customerId,"Anar",1999, City.valueOf("Baku"),"Zabrat");
        UpdateCustomerRequest request = new UpdateCustomerRequest("Anar", CityDto.London,"London");
        Customer updatedCustomer = new Customer(customer.getId(),"Anar",customer.getDateOfBirth(),City.London,"London");
        Customer savedCustomer = new Customer(customer.getId(),"Anar",customer.getDateOfBirth(),City.London,"London");
        CustomerDto customerDto = new CustomerDto(customer.getId(),"Anar",customer.getDateOfBirth(),CityDto.London,"London");


        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(updatedCustomer)).thenReturn(savedCustomer);
        when(converter.convert(savedCustomer)).thenReturn(customerDto);

        CustomerDto result = customerService.updateCustomer(customerId,request);

        assertEquals(customerDto,result);

        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(updatedCustomer);
        verify(converter).convert(savedCustomer);

    }

    @Test
    public void testUpdateCustomer_whenCustomerIdDoesNotExist_itShouldThrowCustomerNotFoundException(){
        String customerId ="1fg";
        UpdateCustomerRequest request = new UpdateCustomerRequest("Anar", CityDto.London,"London");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                ()->customerService.updateCustomer(customerId,request));

        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(converter);
    }


}
