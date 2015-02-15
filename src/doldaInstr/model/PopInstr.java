package doldaInstr.model;

import java.util.ArrayList;

public class PopInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till POP,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public PopInstr()
    {
        super();
        this.instruction = "pop";
    }

    /**
     * Konstruktor som sätter instruktionen till POP,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public PopInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "pop";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public PopInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("pop"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till POP");
        }
    }
    
    /**
     * Omvandlar POP instruktionen till motsvarande MOV & ADD instruktioner.
     * @return  En Arraylist med objekt av typen MovInstr & AddInstr.
     */
    public ArrayList<? extends Instruction> toInstrMovAndAdd()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> movAndAddArray = new ArrayList<Instruction>(2);
            
            MovInstr movInstr = new MovInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            movInstr.addOperand("(%esp)");
            movInstr.addOperand(operand[0]);
            
            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            movAndAddArray.add(movInstr);
            movAndAddArray.add(addInstr);
            
            return movAndAddArray;
        }
        else
        {
            ArrayList<PopInstr> originalInstr = new ArrayList<PopInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
    
    /**
     * Omvandlar POP instruktionen till motsvarande XOR & ADD instruktioner.
     * @return  En Arraylist med objekt av typen XorInstr & AddInstr.
     */
    public ArrayList<? extends Instruction> toInstrXorAndAdd()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> xorAndAddArray = new ArrayList<Instruction>(3);
            
            XorInstr xorInstrDstToSrc = new XorInstr(2);
            XorInstr xorInstrSrcToDst = new XorInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            xorInstrDstToSrc.addOperand(operand[0]);
            xorInstrDstToSrc.addOperand("(%esp)");
            
            xorInstrSrcToDst.addOperand("(%esp)");
            xorInstrSrcToDst.addOperand(operand[0]);

            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            xorAndAddArray.add(xorInstrDstToSrc);
            xorAndAddArray.add(xorInstrSrcToDst);
            xorAndAddArray.add(addInstr);
            
            return xorAndAddArray;
        }
        else
        {
            ArrayList<PopInstr> originalInstr = new ArrayList<PopInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}