package doldaInstr.model;

@SuppressWarnings("serial")
public class CodeConvertionMethodException extends Exception
{
    public CodeConvertionMethodException()
    {
        super(); // Anropar superklassens konstruktor
    }
    
    public CodeConvertionMethodException(String s)
    {
        super(s); // Anropar superklassens konstruktor
    }
}
