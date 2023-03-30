package reflection.api;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.reflect.*;
import java.util.*;

public class MyInvestigator implements Investigator{

    Class<?> theClass; // the class that is going to be investigated.
    Object obj; // the object that was sent, to keep his data.

    @Override
    public void load(Object anInstanceOfSomething) {
        theClass = anInstanceOfSomething.getClass();
        obj = anInstanceOfSomething;
    }

    @Override
    public int getTotalNumberOfMethods() {
        return theClass.getDeclaredMethods().length;
    }

    @Override
    public int getTotalNumberOfConstructors() {
        return theClass.getConstructors().length;
    }

    @Override
    public int getTotalNumberOfFields() {
        return theClass.getDeclaredFields().length;
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        Class<?>[] interfaces = theClass.getInterfaces();
        Set<String> interfacesSet = new HashSet<>();
        for(Class<?> inter:interfaces){
            interfacesSet.add(inter.getSimpleName());
        }
        return interfacesSet;
    }

    @Override
    public int getCountOfConstantFields() {
        Field[] fields = theClass.getDeclaredFields();
        int numOfConstantFields = 0;
        for(Field field:fields){
            int modifier = field.getModifiers();
            if(Modifier.isFinal(modifier)) { numOfConstantFields++; }
        }
        return numOfConstantFields;
    }

    @Override
    public int getCountOfStaticMethods() {
        Method[] methods = theClass.getDeclaredMethods();
        int numOfStaticMethods = 0;
        for(Method method:methods){
            if(Modifier.isStatic((method.getModifiers())))
                numOfStaticMethods++;
        }
        return numOfStaticMethods;
    }

    @Override
    public boolean isExtending() {
        Class superClass = theClass.getSuperclass();
        return superClass.equals(Object.class) ? false:true;
    }

    @Override
    public String getParentClassSimpleName() {
        return theClass.getSuperclass().getSimpleName();
    }

    @Override
    public boolean isParentClassAbstract() {
        Class superClass = theClass.getSuperclass();
        return Modifier.isAbstract(superClass.getModifiers());
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        Set<String> fieldsSet = new HashSet<>();
        Field[] fieldsArr;;
        Class<?> currClass = theClass;
        while(!currClass.equals(Object.class)){ //runs and adding fields to set from each class in the inheritance chain, excluding Object.
            fieldsArr = currClass.getDeclaredFields();
            for(Field field: fieldsArr){
                fieldsSet.add(field.getName());
            }
            currClass = currClass.getSuperclass();
        }
        return fieldsSet;
    }


    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        Method[] methods = theClass.getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().equals(methodName) && method.getReturnType().equals(int.class)) { // checks the name and the return type.
                try {
                    method.setAccessible(true);
                    return (int)method.invoke(obj, args);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return 0;
    }

    @Override
    public Object createInstance(int numberOfArgs, Object... args) {
        int numOfArgs = args.length;
        for(Constructor ctor:theClass.getConstructors()){
            if(ctor.getParameterTypes().length == args.length){ // if the len's are equals that mean it found the right c'tor.
                try {
                    return ctor.newInstance(args);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return 0; // never gets there
    }

    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        try {
            Method method = theClass.getDeclaredMethod(name, parametersTypes);
            method.setAccessible(true);
            return method.invoke(theClass.newInstance(),args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getInheritanceChain(String delimiter) {
        Class<?> cls = theClass;
        List<String> chain = new ArrayList<>();
        boolean flag = !theClass.equals(Object.class);
        chain.add(theClass.getSimpleName());
        while(flag){
            cls = cls.getSuperclass();
            chain.add(0, cls.getSimpleName());
            if(cls.equals(Object.class)){
                flag = false;
            }
        }
        String result = "";
        for(String name:chain){
            result = result.concat(name);
            if(!name.equals(theClass.getSimpleName()))
                result = result.concat(delimiter);
        }
        return result;
    }
}
