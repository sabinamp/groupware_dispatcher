package groupware.dispatcher.service;

import groupware.dispatcher.service.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp(){
        orderService = new OrderServiceImpl();
    }

    @Test
    void testOrderServiceSaveOrderInMemory(){
        //given an order
        OrderDescriptiveInfo order = new OrderDescriptiveInfo();
        order.setCustomerName("Anastasia G.");
        order.setCurrencyCode("CHF");
        order.setOrderId("OR1133");
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPlacedWhen( LocalDateTime.of(2020, 7, 23, 11,23, 10));
        order.setOrderInfo(orderInfo);
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setCompanyName("C Company");
        Email emailAddress = new Email();
        emailAddress.setEmail("anastasia@gmail.com");
        contactInfo.setEmail(emailAddress);
        Address address = new Address();
        address.setAddressLine("Tulpenstr 12");
        address.setCityName("Zürich");
        address.setCountryName("Schweiz");
        address.setStateProv("ZH");
        address.setPostalCode("8400");
        contactInfo.setAddress(address);
        List<ContactInfo> contactInfos = new ArrayList<>();
        contactInfos.add( contactInfo);
        order.setContactInfos(contactInfos);

        //when
        orderService.updateOrder("OR1133", order);
        //then
        OrderDescriptiveInfo savedOrder= orderService.getOrder("OR1133");
        assertNotNull(savedOrder);

        assertEquals( "OR1133", savedOrder.getOrderId());
       assertEquals("anastasia@gmail.com", savedOrder.getContactInfos().get(0).getEmail().getEmail());
    }
}
