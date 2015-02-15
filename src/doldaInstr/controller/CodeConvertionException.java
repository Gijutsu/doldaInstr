package doldaInstr.controller;

@SuppressWarnings("serial")
public class CodeConvertionException extends Exception
{
    public CodeConvertionException()
    {
        super(); // Anropar superklassens konstruktor
    }
    
    public CodeConvertionException(String s)
    {
        super(s); // Anropar superklassens konstruktor
    }
}
