# pic_ai4

Watchdogzeit ca 18ms

Befehlszykluszeit bei 4MHz 1ns; 8MHz 0,5ns

2 Befehlstakte bei GOTO,CALL und Manipulation des 
PCL-Register, weil im Befehlsregister steht falscher 
Befehl -> NOP.

Laufzeitzähler Zeit in mykroSekunde,
4 Quarztakte/Befehl. 

Zu addierende Zeit = 4/fQuarz

Bei decfsz den programmcounter manipuliert wenn es nicht null ist
Schaue bei Pc-1 nach ob es ein decfsz oder incfsz ist,
welches nicht null als ergebnis hat,
lösche dann bestehendes goto aus der cyclelist

