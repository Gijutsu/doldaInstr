package doldaInstr.model;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;

public class Instruction
{
    // Instansvariabler
    protected String instruction;
    protected String[] operand;
    protected int numberOfOperand;
    protected String[] generalPurposeReg = {"%eax", "%ebx", "%ecx",
                                            "%edx", "%edi", "%esi"};
    
    /**
     * Tom konstruktor som initierar vektorn operand med standardkapaciteten.
     */
    public Instruction()
    {
        this.operand = new String[3];
    }
    
    /**
     * Konstruktor som initierar vektorn operand med angiven kapacitet.
     * 
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public Instruction(int operandCapacity)
    {
        this.operand = new String[operandCapacity];
        this.numberOfOperand = 0;
    }
    
    /**
     * Konstruktor som initierar instruktion & operand-kapaciteten.
     * 
     * @param instruction     Instruktion, t ex MOV, ADD, SUB, etc.
     * @param operandCapacity Antalet operander som instruktionen tillåter.
     */
    public Instruction(String instruction, int operandCapacity)
    {
        this.instruction = instruction;
        this.operand = new String[operandCapacity];
        this.numberOfOperand = 0;
    }
    
    /**
     * Konstruktor som initierar instruktion & operander, samt
     * sätter numberOfOperand till antalet angivna operander.
     * 
     * @param instrAndOperand   Instruktion, t ex MOV, ADD, SUB, etc.
     */
    public Instruction(String[] instrAndOperand)
    {
        this.instruction = instrAndOperand[0];
        
        // Säkerställ att operander har angivits för instruktionen
        if (instrAndOperand.length > 1)
        {
            // Antalet operander är lika med (instruktion + operander) - 1
            this.operand = new String[instrAndOperand.length - 1];
            this.numberOfOperand = instrAndOperand.length - 1;
            
            // Initiera samtliga operander
            for (int i = 1; i < instrAndOperand.length; i++)
            {
                this.operand[i - 1] = instrAndOperand[i];
            }
        }
        else
        {
            this.operand = null; // Om inga operander finns för instruktionen
        }
    }

    /**
     * Returnerar instruktionen
     *  
     * @return  Instruktionen i form av en sträng.
     */
    public String getInstruction()
    {
        return instruction;
    }
    
    /**
     * Registrerar instruktionen
     * 
     * @param instruction   En sträng med instruktionen, t ex MOV, ADD, etc.
     */
    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }
    
    /**
     * Returnerar samtliga operander för instruktionen
     * 
     * @return  Operanderna för instruktionen i form av en strängvektor.
     */
    public String[] getOperand()
    {
        return operand;
    }
    
    /**
     * Lägger till en operand för instruktionen
     * 
     * @param  operandToAdd En sträng med den operand som ska tilläggas
     * @throws IndexOutOfBoundsException Om max antal redan är registrerade
     */
    public void addOperand(String operandToAdd) 
           throws IndexOutOfBoundsException
    {
        // Säkerställ att antalet operander är mindre än max tillåtna
        if (numberOfOperand < operand.length)
            // Lägg till och öka räknaren
            operand[numberOfOperand++] = operandToAdd;
        else
            throw new IndexOutOfBoundsException("Max antal operander för " +
                      "instruktionen " + instruction + "redan uppfyllt");
    }
    
    /**
     * Returnerar de allmänna register som inte används av instruktionen
     * 
     * @return  En String Array med register som inte används av instruktionen.
     */
    protected String[] getUnusedRegister()
    {
        ArrayList<String> unusedRegister = new ArrayList<String>();
        
        // Gå igenom samtliga allmänna register och påvisa de som inte används
        for (String register : generalPurposeReg)
        {
            boolean regIsUsed = false; // Sätts till falskt innan kontroll nedan
            
            for (int i = 0; i < operand.length; i++)
            {
                // Kontrollera om registret används av instruktionen
                if (register.contains(operand[i]))
                {
                    regIsUsed = true;
                    break;
                }
            }
            
            if (!regIsUsed) // Lägg till registret om det inte används
                unusedRegister.add(register);
        }
        
        // OBS, den skapade String-vektorn i argumentet är den som
        // erhåller elementen från listan och därefter returneras.
        return unusedRegister.toArray(new String[unusedRegister.size()]);
    }
    
    /**
     * Räknar hur många bitar som behövs för att representera operanden.
     * 
     * @param   operand Den operand som ska undersökas.
     * @return  En int med antalet bitar som kan representera operanden.
     */
    protected int sizeOfOperand(String operand)
    {
        if (isRegister(operand))
            return sizeOfRegister(operand);
        else if (isLabel(operand))
            return 32;
        else if (isImmediate(operand))
            return sizeOfImmediate(operand);
        else
            return -1;
    }
    
    /**
     * Kontrollerar om angiven operand är ett register.
     * 
     * @param   operand Den operand i form av en String som ska kontrolleras.
     * @return  En boolean som svarar på frågan, är operanden ett register?
     */
    protected boolean isRegister(String operand)
    {
        return operand.startsWith("%");
    }
    
    /**
     * Kontrollerar om angiven operand är en label.
     * 
     * @param   operand Den operand i form av en String som ska kontrolleras.
     * @return  En boolean som svarar på frågan, är operanden en label?
     */
    protected boolean isLabel(String operand)
    {
        return operand.matches("^\\$(?!(0x|0b))(?![0-9]+$)[a-zA-Z0-9]+$");
    }
    
    /**
     * Kontrollerar om angiven operand är en immediate.
     * 
     * @param   operand Den operand i form av en String som ska kontrolleras.
     * @return  En boolean som svarar på frågan, är operanden en immediate?
     */
    protected boolean isImmediate(String operand)
    {
        return operand.startsWith("$");
    }
    
    /**
     * Kontrollera om angiven operand är en minnesreferens i form av ett offset
     * 
     * @param   operand Den operand i form av en String som ska kontrolleras.
     * @return  En boolean som svarar på frågan, är operanden en minnesreferens?
     */
    protected boolean isMemoryReference(String operand)
    {
        return operand.contains("(");
    }
    
    /**
     * Returnerar storleken på ett registret av en operand i antal bitar
     * 
     * @param   operand Det register i form av en String som ska kontrolleras
     * @return  Antal bitar som utgör storleken på registret
     */
    protected int sizeOfRegister(String operand)
    {
        // 32 bitars register om operand är ex. %eax
        if (operand.length() == 4)
            return 32;
        
        // 8 bitars register om operand är ex. %al eller %ah
        if (operand.endsWith("l") || operand.endsWith("h"))
            return 8;
        
        // Övriga såsom %ax är 16 bitars register
        return 16;
    }
    
    /**
     * Returnerar storleken på en immediate av en operand i antal bitar
     * 
     * @param   operand Det immediate i form av en String som ska kontrolleras
     * @return  Antal bitar som utgör storleken på immediate
     */
    protected int sizeOfImmediate(String operand)
    {
        int decimalValue = decimalValueOfImmediate(operand);
       
        // Omvandla eventuellt negativt värde till positivt för jämförelse
        if (decimalValue < 0)
            decimalValue = decimalValue * -1;
        
        if (decimalValue <= 255) // <= 0xff som kan återges med 8 bitar?
            return 8;
        if (decimalValue <= 65535) // <= 0xffff som kan återges med 16 bitar?
            return 16;
        
        return 32;  // Övriga värden kan endast återges med 32 bitar.
    }
    
    /**
     * Returnerar det decimala värdet på en immediate av en String operand
     * som kan ursprungligen vara angiven i baserna 2, 16 och 10.
     * 
     * @param   operand Det immediate i form av en String som ska beräknas
     * @return  Decimalt värde för angiven immediate
     */
    protected int decimalValueOfImmediate(String operand)
    {
        int decimalValue;
        
        if (operand.startsWith("$0x")) // Är immediate angiven i basen 16?
            decimalValue = Integer.parseInt(operand.substring(3), 16);
        else if (operand.startsWith("$0b")) // Är immediate angiven i basen 2?
            decimalValue = Integer.parseInt(operand.substring(3), 2);
        else // Immediate är angiven i basen 10.
            decimalValue = Integer.parseInt(operand.substring(1), 10);
        
        return decimalValue;
    }
}