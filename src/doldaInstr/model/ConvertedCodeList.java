package doldaInstr.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ett register för omvandlad kod i form objekt av Instruction eller subklass.
 * Klassen i sig är en observerad klass som skickar information om sina
 * tillståndsändringar till angivna observerare.
 * 
 * Klassen är final (och underförstått alla dess metoder) för att omöjliggöra
 * att subklasser åsidosätter metoderna och inte synkroniserar korrekt.
 */
public final class ConvertedCodeList
{
    private List<? extends Instruction> code;
    private final List<Observer> observers = new CopyOnWriteArrayList<Observer>();
    
    public ConvertedCodeList()
    {
        this.code = Collections.synchronizedList(new ArrayList<Instruction>());
    }
    
    /**
     * Lägger till en observer som ska bli underrättad om ändringar
     * i den omvandlade koden.
     * 
     * @param observer  Den observerande klassen som ska läggas till
     */
    public void addObserver(Observer observer)
    {
        observers.add(observer); // CopyOnWriteArrayList är trådsäker
    }
    
    private void notifyObservers()
    {   
        // En CopyOnWriteArrayList behöver inte synkroniseras vid iteration
        // eftersom det är en kopia av den interna vektorn som används.
        for(Observer observer : observers)
        {   
            observer.convertedCode(code);
        }
    }
    
    /**
     * Registrerar omvandlad kod och underättar observerande klasser
     * 
     * @param code  Omvandlade kod som ska registreras i form av en ArrayList
     */
    public void setCode(ArrayList<? extends Instruction> code)
    {
        // Synkronisera på this.code så att koden inte kan ändras innan
        // klassen hinner meddela tillståndsändringar till angivna observerare.
        synchronized(this.code)
        {
            this.code = Collections.synchronizedList(code);
            notifyObservers();
        }
    }
    
    /**
     * Returnerar den omvandlade koden
     * 
     * @return  Omvandlad kod i form av List<? extends Instruction>.
     */
    public List<? extends Instruction> getCode()
    {
        // Returnera enligt värde genom att skapa ett nytt objekt
        synchronized (this.code)
        {
            return new ArrayList<Instruction>(this.code);
        }
    }
}