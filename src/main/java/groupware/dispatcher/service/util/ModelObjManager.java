package groupware.dispatcher.service.util;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import groupware.dispatcher.service.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class ModelObjManager {
    private static Logger logger;
    private static final ObjectMapper objectMapper;

    private static ClassLoader classLoader;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        logger= LogManager.getLogManager().getLogger(String.valueOf(ModelObjManager.class));
        classLoader = ClassLoader.getSystemClassLoader();
    }


    public static DeliveryStep convertDeliveryStepData(String jsonValue){
        JsonNode node= null;
        try {
            DeliveryStep step = objectMapper.readValue(jsonValue, DeliveryStep.class);
            return step;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

   public static OrderInfo convert(String jsonString){
       JsonNode node= null;
       try {
           OrderInfo orderInfo = objectMapper.readValue(jsonString, OrderInfo.class);

           return orderInfo;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return  null;
   }

    public static OrderDescriptiveInfo convertJsonToOrderDescriptiveInfo(String jsonString) {
        JsonNode node= null;
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                return objectMapper.readValue(jsonString, OrderDescriptiveInfo.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static String convertToJSON(Object obj) {
           JsonNode tree= objectMapper.valueToTree(obj);
           return tree.toPrettyString();
    }

    public static TaskRequest convertJsonToTaskRequest(String jsonString) {
        JsonNode node= null;
        try {
            if(jsonString != null && !jsonString.isEmpty()) {
                TaskRequest task = objectMapper.readValue(jsonString, TaskRequest.class);
                return task;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static Courier convertJsonToCourier(String jsonString) {
        JsonNode node= null;
        try {
            if(jsonString != null && !jsonString.isEmpty() && jsonString.startsWith("C")){
                Courier courier = objectMapper.readValue(jsonString, Courier.class);
                return courier;
            }
        }catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
