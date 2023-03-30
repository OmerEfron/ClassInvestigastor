package reflection.api;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.reflect.*;
import java.util.*;

public class MyInvestigator implements Investigator{

    Class<?> theClass;

    @Override
    public void load(Object anInstanceOfSomething) {
        theClass = anInstanceOfSomething.getClass();
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
        Field[] fieldsArr = theClass.getDeclaredFields();
        Field[] superFieldsArr = theClass.getSuperclass().getDeclaredFields();

        for(Field field: fieldsArr){
            fieldsSet.add(field.getName());
        }
        for(Field field:superFieldsArr){
            fieldsSet.add(field.getName());
        }
        return fieldsSet;
    }


    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        Method[] methods = theClass.getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().equals(methodName) && method.getReturnType().equals(int.class)) {
                try {
                    method.setAccessible(true);
                    return (int)method.invoke(theClass.newInstance(), args);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
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
            if(ctor.getParameterTypes().length == args.length){
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
        return 0;
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
