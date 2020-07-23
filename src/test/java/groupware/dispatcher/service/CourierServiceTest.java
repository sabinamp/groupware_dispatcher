package groupware.dispatcher.service;

import groupware.dispatcher.service.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        CourierInfo courier= new CourierInfo();
        //when
        courierService.saveCourier(courierID, courier);
        //then
        CourierInfo courier2= courierService.getCourierInfo(courierID);
        assertNotNull(courier2);

        //TODO
    }


}
