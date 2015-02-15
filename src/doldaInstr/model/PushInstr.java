package doldaInstr.model;

import java.util.ArrayList;

public class PushInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till PUSH,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public PushInstr()
    {
        super();
        this.instruction = "push";
    }

    /**
     * Konstruktor som sätter instruktionen till PUSH,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public PushInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "push";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public PushInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("push"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till PUSH");
        }
    }
    
    /**
     * Omvandlar PUSH instruktionen till motsvarande SUB & MOV, 
     * alternativt SUB, MOVL, XORL och MOV instruktioner.
     * @return  En Arraylist med objekt av typen SubInstr & MovInstr,
     *          eller PushInstr, XorlInstr och Mov*Instr.
     */
    public ArrayList<? extends Instruction> toInstrSubAndMov()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> instructionArray = new ArrayList<Instruction>(3);
            
            Instruction movInstr;
            
            int sizeOfmovOperand = sizeOfOperand(operand[0]);
            
            switch (sizeOfmovOperand) 
            {
                case 32:
                    movInstr = new MovlInstr(2);
                    break;
                case 16:
                    movInstr = new MovwInstr(2);
                    break;
                case 8:
                    movInstr = new MovbInstr(2);
                    break;
                default:
                    movInstr = new MovlInstr(2);
                    break;
            }
            
            if (sizeOfmovOperand < 32) // Utfyllnad till 32 bitar behövs
            {   
                SubInstr subInstr = new SubInstr(2);
                MovlInstr movlInstr = new MovlInstr(2);
                XorlInstr xorlInstr = new XorlInstr(2);
                
                subInstr.addOperand("$4");
                subInstr.addOperand("%esp");
                movlInstr.addOperand("%esp");
                movlInstr.addOperand("(%esp)");
                
                xorlInstr.addOperand("%esp");
                xorlInstr.addOperand("(%esp)");
                movInstr.addOperand(operand[0]);
                movInstr.addOperand("(%esp)");
                
                instructionArray.add(subInstr);
                instructionArray.add(movlInstr);
                instructionArray.add(xorlInstr);
                instructionArray.add(movInstr);
            }
            else // Ingen utfyllnad behövs, alla 32 bitar används
            {
                SubInstr subInstr = new SubInstr(2);
                
                subInstr.addOperand("$4");
                subInstr.addOperand("%esp");
                movInstr.addOperand(operand[0]);
                movInstr.addOperand("(%esp)");
            
                instructionArray.add(subInstr);
                instructionArray.add(movInstr);
            }
            
            return instructionArray;
        }
        else
        {
            ArrayList<PushInstr> originalInstr = new ArrayList<PushInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}