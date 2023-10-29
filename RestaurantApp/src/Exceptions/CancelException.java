package Exceptions;

public class CancelException extends Exception{
    public CancelException(){
        super();
        System.out.println("Order nonexistent or in progress.");
    }
}
