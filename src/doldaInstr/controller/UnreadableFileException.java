package doldaInstr.controller;

@SuppressWarnings("serial")
public class UnreadableFileException extends Exception
{
    public UnreadableFileException()
    {
        super(); // Anropar superklassens konstruktor
    }
    
    public UnreadableFileException(String s)
    {
        super(s); // Anropar superklassens konstruktor
    }
}
