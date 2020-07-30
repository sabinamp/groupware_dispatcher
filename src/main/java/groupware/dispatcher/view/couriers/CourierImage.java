package groupware.dispatcher.view.couriers;

import javafx.scene.image.Image;

import java.io.*;

public class CourierImage {


    public static Image getImage(String courierId){
     String filename = "/images/avatar"+courierId+".png";

        try(InputStream reader = getInputStream(filename)){
         return new Image(reader);
        } catch (NullPointerException | IOException e){
            e.printStackTrace();
        }
       return null;
    }

    private static InputStream getInputStream(String fileName) {
        InputStream reader = CouriersTable.class.getResourceAsStream(fileName);
        return reader;
    }
}
