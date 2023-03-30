package reflection.api;

public class TypeIdentifier {


    public Class<Integer> getType(int i){
        return int.class;
    }

    public Class getType(short s){
        return short.class;
    }

    public Class<?> getType(long l){
        return long.class;
    }

    public Class<?> getType( char c){
        return char.class;
    }

    public Class<?> getType(byte b){
        return byte.class;
    }

    public Class<?> getType(float f){
        return float.class;
    }

    public Class<?> getType(double d){
        return double.class;
    }

    public Class<?> getType(boolean b){
        return boolean.class;
    }
    public Class<?> getType(Object o){
        return o.getClass();
    }

}
