package groupware.dispatcher.service;

import groupware.dispatcher.service.model.Address;
import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.Email;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        OrderServiceImpl.saveOrderInMemory("OR1133", order);
        //then
        OrderDescriptiveInfo savedOrder= orderService.getOrder("OR1133");
        assertNotNull(savedOrder);

        assertEquals( "OR1133", savedOrder.getOrderId());
        //TODO
    }
}
