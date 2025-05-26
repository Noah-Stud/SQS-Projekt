# Wahl von MySql als Datenbank

## Kontext und Problemdarstellung

Wie können die Daten (Nutzer, Nachrichten, Kommentare) permanent (auch bei Neustart der Anwendung) erhalten bleiben? 
Dabei muss es permanent möglich sein auf die Daten zuzugreifen und diese gegebenenfalls anzupassen.

## Mögliche Optionen

* H2-Datenbank
* MySql
* MongoDB

## Entscheidung

Gewählte Option: "MySql", da es die Anforderungen an den permanenten Datenerhalt erfühlt, von Docker und 
SpringBoot unterstützt wird und von Entwicklerseite bereits Erfahrungen mit diesem Tool existieren. 
Kein anderes Tool erfühlt alle diese Ansprüche.
