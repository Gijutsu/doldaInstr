package doldaInstr.model;

import java.util.ArrayList;

public class PoplInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till POPL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public PoplInstr()
    {
        super();
        this.instruction = "popl";
    }

    /**
     * Konstruktor som sätter instruktionen till POPL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public PoplInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "popl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public PoplInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("popl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till POPL");
        }
    }
    
    /**
     * Omvandlar POPL instruktionen till motsvarande MOVL & ADD instruktioner.
     * @return  En Arraylist med objekt av typen MovlInstr & AddInstr.
     */
    public ArrayList<? extends Instruction> toInstrMovlAndAdd()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> movlAndAddArray = 
                new ArrayList<Instruction>(2);
            
            MovlInstr movlInstr = new MovlInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            movlInstr.addOperand("(%esp)");
            movlInstr.addOperand(operand[0]);
            
            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            movlAndAddArray.add(movlInstr);
            movlAndAddArray.add(addInstr);
            
            return movlAndAddArray;
        }
        else
        {
            ArrayList<PoplInstr> originalInstr = new ArrayList<PoplInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
    
    /**
     * Omvandlar POPL instruktionen till motsvarande XORL & ADD instruktioner.
     * @return  En Arraylist med objekt av typen XorlInstr & AddInstr.
     */
    public ArrayList<? extends Instruction> toInstrXorlAndAdd()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> xorlAndAddArray =
                new ArrayList<Instruction>(3);
            
            XorlInstr xorlInstrDstToSrc = new XorlInstr(2);
            XorlInstr xorlInstrSrcToDst = new XorlInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            xorlInstrDstToSrc.addOperand(operand[0]);
            xorlInstrDstToSrc.addOperand("(%esp)");
            
            xorlInstrSrcToDst.addOperand("(%esp)");
            xorlInstrSrcToDst.addOperand(operand[0]);

            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            xorlAndAddArray.add(xorlInstrDstToSrc);
            xorlAndAddArray.add(xorlInstrSrcToDst);
            xorlAndAddArray.add(addInstr);
            
            return xorlAndAddArray;
        }
        else
        {
            ArrayList<PoplInstr> originalInstr = new ArrayList<PoplInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
