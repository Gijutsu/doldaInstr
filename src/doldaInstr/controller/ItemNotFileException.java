package doldaInstr.controller;

@SuppressWarnings("serial")
public class ItemNotFileException extends Exception
{
    public ItemNotFileException()
    {
        super(); // Anropar superklassens konstruktor
    }
    
    public ItemNotFileException(String s)
    {
        super(s); // Anropar superklassens konstruktor
    }
}