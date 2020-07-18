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
        Courier courier= new Courier();
        //when
        courierService.saveCourierInMemory(courierID, courier);
        //then
        Courier courier2= courierService.getCourier(courierID);
        assertNotNull(courier2);

        //TODO
    }


}
