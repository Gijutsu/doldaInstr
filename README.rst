###################
Dolda instruktioner
###################

Detta program illustrerar hur verkningslösa antivirus är mot ett system som
kan generera nya unika exemplar av ett program med bibehållen funktionalitet.
Ett ramverk för hantering av assembler-källkod har skapats för detta syfte
som kan behandla varje enskild instruktion och analysera vilka omvandlingar
som är möjliga att utföra i den omgivande kontexten i källkoden.
Omvandlingarna sker såväl iterativt som slumpmässigt enligt användarens val,
vilket bidrar till att antalet möjliga unika exemplar är mycket stort.

.. image:: https://github.com/Gijutsu/doldaInstr/raw/master/docs/screenshots/screenshot.png

Kända begränsningar
===================

- Endast GAS / AT&T assembler kod stöds.
- Programmet skrevs ursprungligen för i386 assembler och använder 
  t ex inte x86-64 register såsom rax, rbx osv.
- Inte all i386 assembler syntax är implementerad. Programmet ska
  dock ignorera de rader den inte förstår.
- Ingen syntax-kontroll genomförs innan den öppnade källkodsfilen
  börjar indelas i instruktioner och operander.
- Det grafiska gränssnittet tillför en del onödig komplexitet och
  framtida versioner kan därför istället komma att innehålla ett
  terminal-baserat gränssnitt istället.
- Programmet kan hantera den shell-kod och dylikt som jag har 
  givit det, men saknar i dagsläget ett ordentlig test-korpus med
  kod att köra igenom för att säkerställa och förbättra programmet.

Bidrag
======

Pull requests uppskattas!

Externa bibliotek som används
=============================

Programmet är beroende av libcommons-lang3-java som enklast
installeras i Debian / Ubuntu enligt följande::

    apt-get install libcommons-lang3-java

Tips
====

För att kompilera assembler-koden från programmet i en x86-64 
miljö kan det vara nödvändigt att ange -m32 för gcc enligt nedan::

    gcc -m32 -nostdlib -O0 -Wall hello.s -o hello

Struktur
=======
.
└── doldaInstr
    ├── controller
    │   ├── CodeConvertionException.java
    │   ├── Controller.java
    │   ├── FileAlreadyExistException.java
    │   ├── FileOperations.java
    │   ├── ItemNotFileException.java
    │   ├── SearchOperations.java
    │   ├── UnreadableFileException.java
    │   └── UnwriteableFileException.java
    ├── model
    │   ├── AddInstr.java
    │   ├── AddlInstr.java
    │   ├── CmplInstr.java
    │   ├── CodeConvertionMethodException.java
    │   ├── ConvertedCodeList.java
    │   ├── ConvertionProgress.java
    │   ├── DecInstr.java
    │   ├── DeclInstr.java
    │   ├── IncInstr.java
    │   ├── InclInstr.java
    │   ├── Instruction.java
    │   ├── JlInstr.java
    │   ├── MovbInstr.java
    │   ├── MovInstr.java
    │   ├── MovlInstr.java
    │   ├── MovwInstr.java
    │   ├── Observer.java
    │   ├── PopInstr.java
    │   ├── PoplInstr.java
    │   ├── PopwInstr.java
    │   ├── PushInstr.java
    │   ├── PushlInstr.java
    │   ├── PushwInstr.java
    │   ├── SourceCodeList.java
    │   ├── SubInstr.java
    │   ├── SublInstr.java
    │   ├── XorInstr.java
    │   ├── XorlInstr.java
    │   └── XorwInstr.java
    ├── startup
    │   └── Startup.java
    └── view
        └── View.java
