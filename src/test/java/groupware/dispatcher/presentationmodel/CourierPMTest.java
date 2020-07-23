package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CourierPMTest {
    private CourierInfo info;
    @BeforeEach
    void setUp(){
        info = new CourierInfo();
        info.setCourierName("John Smith");
        info.setStatus(CourierStatus.AVAILABLE);
        info.setConn(Conn.ONLINE);
        List<ContactInfo> contactInfos = new ArrayList<>();
        ContactInfo contactInfo1 = new ContactInfo();
        Email email1 = new Email(); email1.setEmail("hello@yahoo.com");
        contactInfo1.setEmail(email1);
        contactInfos.add(contactInfo1);
        info.setContactInfos(contactInfos);
    }

    @Test
    void testOfCourierInfo(){
        String courierId = "C108";
        CourierPM pm = CourierPM.of(courierId, info);
        assertNotNull(pm);
        assertEquals("Available", pm.getCourierStatus().toString());
        assertEquals("Online", pm.getCourierConnectionStatus().toString());
        assertEquals("John Smith", pm.getName());
        assertEquals("hello@yahoo.com", pm.getContactInfos().get(0).getEmail().getEmail());
    }

}
