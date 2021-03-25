# Vulnerability Analysis

## A9:2017-Using Components with Known Vulnerabilities

### Description
Wanneer een applicatie van libraries, frameworks of dependencies 
gebruikt maakt worden deze bijna altijd met volledige rechten uitgevoerd.
Sommige versie's van een library, framework of dependency hebben een kwetsbaarheid.
Hetzelfde geld voor een outdated library, framework of dependency.
Deze applicatie maakt gebruikt van SpringBoot en Hibernate. Er komt een moment dat er een
kwetsbaarheid komt in een bepaalde versie van bijvoorbeeld Hibernate. 
Als een kwetsbaar onderdeel is uitgevoerd, 
wordt het voor een aanvaller makkelijker om gegevensverlies of serverovername te veroorzaken.

### Risk
In deze applicatie worden veel libraries, frameworks en dependencies gebruikt. 
Het risico is dat bepaalde components niet up to date gehouden worden 
of dat er onbekende nieuwe/custom kwetsbaarheden zijn. 
De impact is niet altijd even hoog, maar als er een lek in een component zit is de kans op aangevallen worden groter.

### Risk Authentication and Authorization
Als deze applicatie gebruikt maakt van authentication en authorzation is het risco van binnendringen minder groot.
Dit komt omdat er een extra muur is waar de aanvaller langs moet. 
De aanvaller heeft niet direct toegang tot de gehele applicatie omdat er ingelogd moet worden.
Binnen de applicatie kunnen er ook verschillende permissies zijn waardoor het voor een aanvaller nog lastiger is om
toegang te krijgen tot een bepaalde handeling.

### Counter-measures
Wanneer een kwetsbaarheid (vulnerability) in een library of framework bekend wordt, wordt deze vaak online bekend gemaakt. 
Ook wordt er zo snel mogelijk een nieuwe release uitgebracht met een security patch om de kwetsbaarheid weg te nemen.

De OWASP heeft een tool gemaakt die projectafhankelijkheden scant en vergelijkt met Common Vulnerabilities en Exposures (CVE)
die bekend zijn in de Amerikaanse National Vulnerability Database (NVD). 
Deze dependency checker kan als Maven plugin worden toegevoegd in de pom.xml.

Wanneer mvn verify is uitgevoerd in de terminal wordt dependency-check-report.html in de target map aangemaakt.
In dit rapport staat welke kwetsbaarheden er gevonden zijn. Aan de hand van dit rapport kunnen de meeste kwetsbaarheden opgelost worden.

Tot slot is Dependabot (zie dependabot.com) een uitstekende tool om via github te achterhalen welke versies in de pom outdated zijn.
Het voordeel hiervan is dat je gemakkelijk je outdated versies kunt updaten.

### Other Counter-measures:
    - Verwijder ongebruikte dependencies
    - Download components alleen van vertrouwde bronnen
    - Houd bij welke software dependencies gebruikt worden met welke versie

###
### Description

