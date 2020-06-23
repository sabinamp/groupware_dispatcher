package groupware.dispatcher.service.util;
import groupware.dispatcher.service.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
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




    //read from JSON
    public static <T> T readJSONObject(String folderName, String objectId, Class<T> clazz) {

        try {
            File file  = new File(classLoader.getResource(folderName + "/" + objectId + ".json").getFile());
            return objectMapper.readValue(file, clazz);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //read from JSON
    private static JsonNode readJSONNode(String folderName, String objectId, String nodePath) {

       try {
            File file  = new File(classLoader.getResource(folderName + "/" + objectId + ".json").getFile());
           // if(file != null){
                JsonNode root= objectMapper.readValue(file, JsonNode.class);
                return root.at(nodePath);
            //}else{
            //    logger.info("readJSONNode(" +folderName+" " +objectId+ " "+nodePath +"the json file is null");
           // }

        }
       catch (NullPointerException e) {
           e.printStackTrace();
       }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            OrderDescriptiveInfo orderInfo = objectMapper.readValue(jsonString, OrderDescriptiveInfo.class);

            return orderInfo;
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
            TaskRequest task = objectMapper.readValue(jsonString, TaskRequest.class);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
