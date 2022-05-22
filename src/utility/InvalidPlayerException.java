package utility;

public class InvalidPlayerException extends Exception{
    public InvalidPlayerException(String type){
        super(type);
    }
}
