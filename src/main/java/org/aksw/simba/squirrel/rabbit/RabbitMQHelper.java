package org.aksw.simba.squirrel.rabbit;

import java.nio.ByteBuffer;

import org.aksw.simba.squirrel.data.uri.serialize.Serializer;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.google.gson.Gson;

/**
 * A simple helper class that manages the translation from Java objects into
 * JSON byte arrays and back.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 * @deprecated Use one of the {@link Serializer} instead
 */
@Deprecated
public class RabbitMQHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQHelper.class);

    private final Gson gson;

    public RabbitMQHelper() {
        this(new Gson());
    }

    public RabbitMQHelper(Gson gson) {
        this.gson = gson;
    }

    /**
     * Serializes the given object by creating a byte array with the following
     * content:
     * <ul>
     * <li>length of class name</li>
     * <li>class name of the given object</li>
     * <li>length of JSON representation</li>
     * <li>the given object as JSON</li>
     * </ul>
     * 
     * @param o
     *            the object that should be serialized
     * @return a byte array representing its serialization
     */
    public byte[] writeObject(Object o) {
        if (o == null) {
            return new byte[0];
        }
        String className = o.getClass().getName();
        return RabbitMQUtils.writeByteArrays(
                new byte[][] { RabbitMQUtils.writeString(className), RabbitMQUtils.writeString(gson.toJson(o)) });
    }

    /**
     * A method for deserializing an object that has been serialized using the
     * {@link #writeObject(Object)} method.
     * 
     * @param data
     *            the serialized representation of the object generated by the
     *            {@link WritableObjectId} method
     * @return the deserialized object or null if the given byte array is empty
     */
    public Object parseObject(byte[] data) {
        if ((data == null) || (data.length == 0)) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        String className = RabbitMQUtils.readString(buffer);
        Class<?> objectClass = null;
        try {
            objectClass = Class.forName(className);
        } catch (Exception e) {
            LOGGER.error("Couldn't find class for class name \"" + className + "\". returning null.", e);
            return null;
        }
        return gson.fromJson(RabbitMQUtils.readString(buffer), objectClass);
    }

}
