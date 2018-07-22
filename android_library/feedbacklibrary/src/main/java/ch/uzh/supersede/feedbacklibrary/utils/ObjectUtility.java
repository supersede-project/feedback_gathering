package ch.uzh.supersede.feedbacklibrary.utils;

import android.util.Log;

import java.io.*;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.List;

public class ObjectUtility {

    private ObjectUtility() {
    }

    public static <T> T nvl(T value, T valueIfNull) {
        if (value == null) {
            return valueIfNull;
        }
        return value;
    }

    @SuppressWarnings("squid:S2093")
    public static <T extends Serializable> byte[] toByteArray(T object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            Log.i(ObjectUtility.class.getSimpleName(), "Serialization failed: " + e.getMessage(), e);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                Log.i(ObjectUtility.class.getSimpleName(), "Serialization failed: " + e.getMessage(), e);
            }
        }
        return new byte[0];
    }

    @SuppressWarnings("squid:S2093")
    public static <T extends Serializable> T toSerializable(byte[] bytes, Class<T> clazz) {
        if (bytes == null){
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return clazz.cast(in.readObject());
        } catch (IOException  | ClassNotFoundException e) {
            Log.i(ObjectUtility.class.getSimpleName(), "Deserialization failed: " + e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.i(ObjectUtility.class.getSimpleName(), "Deserialization failed: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(final Object array) {
        if (!array.getClass().isArray())
            throw new IllegalArgumentException("Not an array");
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                return (T) Array.get(array, index);
            }

            @Override
            public int size() {
                return Array.getLength(array);
            }
        };
    }
}
