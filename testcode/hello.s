.section        .data
string:         .ascii "Hello, World!\n"

.section        .text                   # Segment med körbar kod
.global         _start                  # definiera _start som en global symbol
_start:                                 # Här börjar vår _start funktion
                mov $4, %eax            # Lägg koden för systemanropet write i eax
                mov $1, %ebx            # Första arg som anger att vi vill skriva på stdout
                mov $string, %ecx       # Andra arg som lägger adressen till strängen i ecx
                mov $14, %edx           # Tredje arg som lägger strängens längd i edx
                int $0x80               # Gör systemanropet genom mjukvaruavbrott

                mov $1, %eax            # Lägg koden för systemanropet exit i eax
                mov $0, %ebx            # Avsluta med statuskod 0 för korrekt avslutning
                int $0x80               # Gör systemanropet genom mjukvaruavbrott
