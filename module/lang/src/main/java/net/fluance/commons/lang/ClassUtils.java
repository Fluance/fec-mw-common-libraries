package net.fluance.commons.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassUtils {

	private ClassUtils() {}

	private static Logger LOGGER = LogManager.getLogger(ClassUtils.class);

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object fieldValueComplex(Object object, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (fieldName == null) {
			String message = "Field name cannot be null";
			LOGGER.error("An IllegalArgumentException has occured: " + message);
			throw new IllegalArgumentException(message);
		}
		StringTokenizer strTokenizer = new StringTokenizer(fieldName, "\\.");
		Object vAnObject = object;
		Object value = null;
		if (strTokenizer.countTokens() > 0) {
			while (strTokenizer.hasMoreTokens()) {
				vAnObject = fieldValue(vAnObject, strTokenizer.nextToken());
			}
			return vAnObject;
		}
		return value;
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object fieldValue(Object object, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Object value = null;
		Field field = getAllFields(new HashMap<String, Field>(), object.getClass()).get(fieldName);
		if (field != null) {
			field.setAccessible(true);
			return field.get(object);
		}
		return value;
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object stringFieldValue(Object object, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Object value = fieldValueComplex(object, fieldName);
		return (value == null) ? null : value.toString();
	}

	/**
	 * 
	 * @param object
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		if (field != null) {
			field.set(object, value);
		}
	}

	/**
	 * 
	 * @param fields
	 * @param type
	 * @return
	 */
	public static Map<String, Field> getAllFields(Map<String, Field> fields, Class<?> type) {
		for (Field field : type.getDeclaredFields()) {
			fields.put(field.getName(), field);
		}
		if (type.getSuperclass() != null) {
			return getAllFields(fields, type.getSuperclass());
		}
		return fields;
	}

	/**
	 * 
	 * @param objectType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> genericTypeClass(Object objectType) {
		if (objectType instanceof ParameterizedType) {
			Type type = ((ParameterizedType) objectType).getActualTypeArguments()[0];
			if (type instanceof Class) {
				return (Class<T>) type;
			} else if (type instanceof ParameterizedType) {
				return (Class<T>) ((ParameterizedType) type).getRawType();
			}
		} else if (objectType instanceof Class) {
			return genericTypeClass(((Class<T>) objectType).getGenericSuperclass());
		}
		return null;
	}

	/**
	 * Knowing the (fully qualified) name of a class, returns a new instance. Using arrays/var-args instead of
	 * collections to reduce the reflection cost.
	 * 
	 * @param className
	 * @param arguments
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object newInstance(String className, Object... arguments)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = Class.forName(className);
		Constructor<?> constructor = clazz.getConstructor((arguments == null) ? new Class<?>[] {} : argTypes(arguments));
		return (arguments == null || arguments.length == 0) ? constructor.newInstance(arguments) : constructor.newInstance(arguments);
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public static Class<?>[] argTypes(final Object... args) {
		Class<?>[] argTypes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		return argTypes;
	}

	/**
	 * 
	 * @param clazz
	 * @param methodName
	 * @param paramTypes
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static boolean declaresMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException {
		return clazz.getDeclaredMethod(methodName, paramTypes) != null;
	}
}
