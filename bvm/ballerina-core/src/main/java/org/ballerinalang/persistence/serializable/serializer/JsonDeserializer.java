/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.persistence.serializable.serializer;

import org.ballerinalang.model.types.BTypes;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BRefType;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.ListInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.MapInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.SerializableBMapInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.SerializableBRefArrayInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.SerializableContextInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.SerializableStateInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.SerializableWorkerDataInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.SerializedKeyInstanceProvider;
import org.ballerinalang.persistence.serializable.serializer.providers.instance.WorkerStateInstanceProvider;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ballerinalang.persistence.serializable.serializer.ObjectHelper.cast;

/**
 * Reconstruct Java object tree from JSON input.
 */
class JsonDeserializer implements BValueDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);
    private final TypeInstanceProviderRegistry instanceProviderRegistry;
    private final BValueProvider bValueProvider;
    private final HashMap<String, Object> identityMap;
    private final BRefType<?> treeHead;

    JsonDeserializer(BRefType<?> objTree) {
        treeHead = objTree;
        instanceProviderRegistry = TypeInstanceProviderRegistry.getInstance();
        bValueProvider = BValueProvider.getInstance();
        identityMap = new HashMap<>();
        registerTypeInstanceProviders(instanceProviderRegistry);
    }

    private void registerTypeInstanceProviders(TypeInstanceProviderRegistry registry) {
        registry.addTypeProvider(new SerializableStateInstanceProvider());
        registry.addTypeProvider(new SerializableWorkerDataInstanceProvider());
        registry.addTypeProvider(new SerializableContextInstanceProvider());
        registry.addTypeProvider(new MapInstanceProvider());
        registry.addTypeProvider(new ListInstanceProvider());
        registry.addTypeProvider(new WorkerStateInstanceProvider());
        registry.addTypeProvider(new SerializableBMapInstanceProvider());
        registry.addTypeProvider(new SerializedKeyInstanceProvider());
        registry.addTypeProvider(new SerializableBRefArrayInstanceProvider());
    }

    Object deserialize(Class<?> destinationType) {
        if (UnsafeObjectAllocator.isInstantiable(destinationType)) {
            return deserialize(treeHead, destinationType);
        } else {
            throw new BallerinaException(String.format("%s is not instantiable", destinationType.getName()));
        }
    }

    @SuppressWarnings("unchecked")
    public Object deserialize(BValue jValue, Class<?> targetType) {
        if (jValue instanceof BMap) {
            BMap<String, BValue> jBMap = (BMap<String, BValue>) jValue;
            Object obj = deserialize(jBMap, targetType);
            addObjReference(jBMap, obj);
            return obj;
        }
        if (jValue instanceof BRefValueArray) {
            return deserializeBRefValueArray((BRefValueArray) jValue, targetType);
        }
        if (jValue instanceof BString) {
            return jValue.stringValue();
        }
        if (jValue instanceof BInteger) {
            return ((BInteger) jValue).intValue();
        }
        if (jValue instanceof BFloat) {
            return ((BFloat) jValue).floatValue();
        }
        if (jValue instanceof BBoolean) {
            return ((BBoolean) jValue).booleanValue();
        }
        if (jValue == null) {
            return null;
        }
        throw new BallerinaException(
                String.format("Unknown BValue type to deserialize: %s", jValue.getClass().getSimpleName()));
    }

    /**
     * Create and populate array using {@param valueArray} and target type.
     *
     * @param valueArray
     * @param targetType
     * @return
     */
    private Object deserializeBRefValueArray(BRefValueArray valueArray, Class<?> targetType) {
        int size = (int) valueArray.size();
        Class<?> componentType = findComponentType(valueArray, targetType);
        Object target = Array.newInstance(componentType, size);
        return deserializeBRefValueArray(valueArray, target);
    }

    private Class<?> findComponentType(BRefValueArray valueArray, Class<?> targetType) {
        // if target type is a array then find its component type.
        // else (i.e. target type is Object) infer component type from JSON representation.
        if (valueArray.size() > 0 && !targetType.isArray()) {
            if (valueArray.get(0).getType().equals(BTypes.typeString)) {
                return String.class;
            } else if (valueArray.get(0).getType().equals(BTypes.typeInt)) {
                return Long.class;
            } else if (valueArray.get(0).getType().equals(BTypes.typeFloat)) {
                return Double.class;
            }
        }
        return targetType.getComponentType();
    }

    /**
     * Populate array using {@param valueArray} provided instance or array object.
     *
     * @param valueArray
     * @param destinationArray
     * @return
     */
    private Object deserializeBRefValueArray(BRefValueArray valueArray, Object destinationArray) {
        Class<?> componentType = destinationArray.getClass().getComponentType();
        for (int i = 0; i < valueArray.size(); i++) {
            Object obj = deserialize(valueArray.get(i), componentType);
            if (componentType == int.class && obj instanceof Long) {
                Array.set(destinationArray, i, ((Long) obj).intValue());
            } else if (componentType == byte.class && obj instanceof Long) {
                Array.set(destinationArray, i, (byte) ((Long) obj).intValue());
            } else if (componentType == char.class && obj instanceof Long) {
                Array.set(destinationArray, i, (char) ((Long) obj).intValue());
            } else {
                Array.set(destinationArray, i, obj);
            }
        }
        return destinationArray;
    }

    private Object deserialize(BMap<String, BValue> jBMap, Class<?> targetType) {
        Object existingReference = findExistingReference(jBMap);
        if (existingReference != null) {
            return existingReference;
        }

        // try BValueProvider
        String typeName = getTargetTypeName(targetType, jBMap);
        if (typeName != null) {
            SerializationBValueProvider provider = this.bValueProvider.find(typeName);
            if (provider != null) {
                return provider.toObject(jBMap, this);
            }
        }

        Object emptyInstance = createInstance(jBMap, targetType);
        addObjReference(jBMap, emptyInstance);
        Object object = deserializeObject(jBMap, emptyInstance, targetType);

        // check to make sure deserializeObject returns the populated 'emptyInstance'.
        // It's important  that it does not create own objects as it may interfere with handling of existing references.
        if (object != emptyInstance) {
            throw new BallerinaException("Internal error: deserializeObject should not create it's own objects.");
        }
        return object;
    }

    private String getTargetTypeName(Class<?> targetType, BMap<String, BValue> jBMap) {
        BValue jType = jBMap.get(JsonSerializerConst.TYPE_TAG);
        if (jType != null) {
            return jType.stringValue();
        } else if (targetType != null) {
            return targetType.getName();
        }
        return null;
    }

    public void addObjReference(BMap<String, BValue> jBMap, Object object) {
        BValue hash = jBMap.get(JsonSerializerConst.HASH_TAG);
        if (hash != null) {
            identityMap.put(hash.stringValue(), object);
        }
    }

    private Object findExistingReference(BMap<String, BValue> jBMap) {
        BValue existingKey = jBMap.get(JsonSerializerConst.EXISTING_TAG);
        if (existingKey != null) {
            String key = existingKey.stringValue();
            Object existingObjRef = getExistingObjRef(key);
            if (existingObjRef == null) {
                throw new BallerinaException("Can not find existing reference: " + existingKey);
            }
            return existingObjRef;
        }
        return null;
    }

    public Object getExistingObjRef(String key) {
        return identityMap.get(key);
    }


    /**
     * Create a empty java object instance for target type.
     *
     * @param jsonNode
     * @return
     */
    private Object createInstance(BMap<String, BValue> jsonNode, Class<?> target) {
        if (target != null && Enum.class.isAssignableFrom(target)) {
            return createEnumInstance(jsonNode);
        }
        BValue typeNode = jsonNode.get(JsonSerializerConst.TYPE_TAG);
        if (typeNode != null) {
            String type = typeNode.stringValue();
            if (type.equals(JsonSerializerConst.ARRAY_TAG)) {
                return createArrayInstance(jsonNode);
            }
            return getObjectOf(type);
        }
        if (target != null) {
            return getObjectOf(target);
        }
        return null;
    }

    private Object createArrayInstance(BMap<String, BValue> jsonNode) {
        BString ct = (BString) jsonNode.get(JsonSerializerConst.COMPONENT_TYPE);
        String componentType = ct.stringValue();

        BInteger bSize = (BInteger) jsonNode.get(JsonSerializerConst.LENGTH_TAG);
        int size = (int) bSize.intValue();

        Class<?> clazz = findClass(componentType);
        if (clazz != null) {
            return Array.newInstance(clazz, size);
        }
        throw new BallerinaException("Can not create array instance of: " + componentType + "[]");
    }

    @SuppressWarnings("unchecked")
    private Object createEnumInstance(BMap jsonNode) {
        String enumName = jsonNode.get(JsonSerializerConst.PAYLOAD_TAG).stringValue();
        int separatorPos = enumName.lastIndexOf('.');
        String type = enumName.substring(0, separatorPos);
        String enumConst = enumName.substring(separatorPos + 1);

        Class enumClass = instanceProviderRegistry.findInstanceProvider(type).getTypeClass();
        return Enum.valueOf(enumClass, enumConst);
    }

    private Object getObjectOf(Class<?> clazz) {
        String className = JsonSerializer.getTrimmedClassName(clazz);
        TypeInstanceProvider typeProvider = instanceProviderRegistry.findInstanceProvider(className);
        if (typeProvider != null) {
            return clazz.cast(typeProvider.newInstance());
        }
        return null;
    }

    private Object getObjectOf(String type) {
        TypeInstanceProvider typeProvider = instanceProviderRegistry.findInstanceProvider(type);
        if (typeProvider != null) {
            return typeProvider.newInstance();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object deserializeObject(BMap<String, BValue> jsonNode, Object instance, Class<?> targetType) {
        BValue payload = jsonNode.get(JsonSerializerConst.PAYLOAD_TAG);
        if (jsonNode.get(JsonSerializerConst.TYPE_TAG) != null) {
            String objType = jsonNode.get(JsonSerializerConst.TYPE_TAG).stringValue();
            if (JsonSerializerConst.MAP_TAG.equals(objType)) {
                return deserializeMap((BMap<String, BValue>) payload, (Map) instance);
            } else if (JsonSerializerConst.LIST_TAG.equals(objType)) {
                BInteger size = (BInteger) jsonNode.get(JsonSerializerConst.LENGTH_TAG);
                return deserializeList(payload, (List) instance, targetType, size);
            } else if (JsonSerializerConst.ENUM_TAG.equals(objType)) {
                return instance;
            } else if (JsonSerializerConst.ARRAY_TAG.equals(objType)) {
                return deserializeBRefValueArray((BRefValueArray) payload, instance);
            }
        }
        // if this is not a wrapped object
        if (payload == null) {
            payload = jsonNode;
        }
        if (payload instanceof BMap) {
            BMap<String, BValue> jMap = (BMap<String, BValue>) payload;
            setFields(instance, jMap, instance.getClass());
        }
        return instance;
    }

    private void setFields(Object target, BMap<String, BValue> jMap,
                           Class<?> targetClass) {
        HashMap<String, Field> allFields = ObjectHelper.getAllFields(target.getClass(), 0);

        for (String fieldName : jMap.keys()) {
            if (fieldName.equals(JsonSerializerConst.HASH_TAG)) {
                // it's a metadata entry.
                continue;
            }
            Field field = allFields.get(fieldName);
            if (field == null) {
                throw new BallerinaException(String.format("Can not find field %s from JSON in %s class",
                        fieldName, targetClass.getName()));
            }
            BValue value = jMap.get(fieldName);
            setField(target, field, value);
        }
    }

    private void setField(Object target, Field field, BValue value) {
        if (Modifier.isTransient(field.getModifiers())) {
            return;
        }
        Object obj = deserialize(value, field.getType());
        primeFinalFieldForAssignment(field);
        try {
            Object newValue = cast(obj, field.getType());
            field.set(target, newValue);
        } catch (IllegalAccessException e) {
            // Ignore it, this is fine.
            // Reason: Either security manager stopping us from setting this field
            // or this is a static final field initialized using compile time constant,
            // we can't assign to them at runtime, nor can we identify them at runtime.
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new BallerinaException(e);
        }
    }

    private void primeFinalFieldForAssignment(Field field) {
        try {
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new BallerinaException();
        }
    }

    @SuppressWarnings("unchecked")
    private Object deserializeList(BValue payload, List targetList, Class<?> targetType, BInteger size) {
        BRefValueArray jArray = (BRefValueArray) payload;
        Class<?> componentType = targetType.getComponentType();
        for (int i = 0; i < jArray.size(); i++) {
            Object item = deserialize(jArray.get(i), componentType);
            targetList.add(item);
        }
        return targetList;
    }

    @SuppressWarnings("unchecked")
    private Object deserializeMap(BMap<String, BValue> payload, Map target) {
        BValue complexKeyMap = payload.get(JsonSerializerConst.COMPLEX_KEY_MAP_TAG);
        if (complexKeyMap instanceof BMap) {
            return deserializeComplexKeyMap((BMap<String, BValue>) complexKeyMap, payload, target);
        }

        for (String key : payload.keys()) {
            if (key.equals(JsonSerializerConst.COMPLEX_KEY_MAP_TAG)) {
                // don't process this entry here, as this is the complex key-map entry
                continue;
            }
            BValue value = payload.get(key);

            Class<?> fieldType = Object.class;
            if (value instanceof BMap) {
                BMap<String, BValue> item = (BMap<String, BValue>) value;
                BValue val = item.get(JsonSerializerConst.TYPE_TAG);
                if (val != null) {
                    String typeName = val.stringValue();
                    fieldType = findClass(typeName);
                }
            } else if (value instanceof BBoolean) {
                fieldType = BBoolean.class;
            }
            target.put(key, deserialize(value, fieldType));
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    private Object deserializeComplexKeyMap(BMap<String, BValue> complexKeyMap,
                                            BMap<String, BValue> payload, Map targetMap) {
        for (String key : payload.keys()) {
            if (key.equals(JsonSerializerConst.COMPLEX_KEY_MAP_TAG)) {
                // don't process this entry here, as this is the complex key-map entry
                continue;
            }
            BValue value = payload.get(key);
            BValue complexKey = complexKeyMap.get(key);
            Object ckObj = deserialize(complexKey, Object.class);

            Class<?> fieldType = Object.class;
            if (value instanceof BMap) {
                BMap<String, BValue> item = (BMap<String, BValue>) value;
                String typeName = item.get(JsonSerializerConst.TYPE_TAG).stringValue();
                fieldType = findClass(typeName);
            } else if (value instanceof BBoolean) {
                fieldType = BBoolean.class;
            }
            targetMap.put(ckObj, deserialize(value, fieldType));
        }
        return targetMap;
    }

    private Class<?> findClass(String typeName) {
        switch (typeName) {
            case "byte":
                return byte.class;
            case "char":
                return char.class;
        }

        SerializationBValueProvider provider = this.bValueProvider.find(typeName);
        if (provider != null) {
            return provider.getType();
        }
        TypeInstanceProvider typeProvider = this.instanceProviderRegistry.findInstanceProvider(typeName);
        return typeProvider.getTypeClass();
    }
}
