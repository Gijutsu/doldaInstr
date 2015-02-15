package doldaInstr.controller;

@SuppressWarnings("serial")
public class UnwriteableFileException extends Exception
{
    public UnwriteableFileException()
    {
        super(); // Anropar superklassens konstruktor
    }
    
    public UnwriteableFileException(String s)
    {
        super(s); // Anropar superklassens konstruktor
    }
}
