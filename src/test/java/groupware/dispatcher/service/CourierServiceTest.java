package groupware.dispatcher.service;

import groupware.dispatcher.service.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CourierServiceTest {
    private CourierServiceImpl courierService;

    @BeforeEach
    void setUp(){
        courierService = new CourierServiceImpl();
    }

    @Test
    void testCourierServiceSaveCourierInMemory(){
        //given a courier
       String courierID="C107";
        CourierInfo info= new CourierInfo();
        info.setCourierName("John Smith");
        info.setStatus(CourierStatus.AVAILABLE);
        info.setConn(Conn.ONLINE);
        List<ContactInfo> contactInfos = new ArrayList<>();
        ContactInfo contactInfo1 = new ContactInfo();
        Email email1 = new Email(); email1.setEmail("hello@yahoo.com");
        contactInfo1.setEmail(email1);
        contactInfos.add(contactInfo1);
        info.setContactInfos(contactInfos);
        //when
        courierService.saveCourier(courierID, info);
        //then
        CourierInfo courier2= courierService.getCourierInfo(courierID);
        assertNotNull(courier2);
        assertEquals("Available", courier2.getStatus().toString());
        assertEquals("Online", courier2.getConn().toString());

    }


}
