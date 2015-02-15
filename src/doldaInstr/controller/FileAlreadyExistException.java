package doldaInstr.controller;

@SuppressWarnings("serial")
public class FileAlreadyExistException extends Exception
{
    public FileAlreadyExistException()
    {
        super(); // Anropar superklassens konstruktor
    }
    
    public FileAlreadyExistException(String s)
    {
        super(s); // Anropar superklassens konstruktor
    }
}
