# Vulnerability Analysis

## A2:2017-Broken Authentication

### Description
Gebroken authenticatie wordt meestal veroorzaakt door slecht geimplementeerde authenticatie en sessiebeheerfuncties.
Denk bijvoorbeeld aan standaard/zwakke wachtwoorden of plain tekst in een database.
Een aanval is bedoeld om een of meer accounts over te nemen en op die manier de aanvaller dezelfde rechten te geven als de aangevallen gebruiker.
Een aanvaller doet zich dus voor als een andere gebruiker aan de hand van verkregen credentials.
Authenticatie wordt verbroken wanneer aanvallers gebruikersaccountinformatie, sleutels, wachtwoorden, sessietokens en andere details kunnen bemachtigen.

### Risk
In deze applicatie is er geen risico op "Broken Authentiation" omdat er geen gebruik gemaakt wordt van authenticatie.

### Risk Authentication and Authorization
Als deze applicatie gebruikt maakt van authentication is er een kans op broken authenticatie omdat er rekening gehouden moet worden met:

* Het toelaten van zwakke of algemeen bekende credentials
* Een slecht wachtwoord rotatiebeleid
* Sessies te lang houdbaar houden
* Plain tekst in de database

Authorization zorgt ervoor dat aanvallers mogelijk meer moeite moeten doen om de juiste (admin) rechten te verkrijgen.

### Counter-measures
Om het risico op gebroken authenticatie te verminderen kunnen de volgende maatregelen gehanteerd worden:

* Geen default credentials
* Het checken van wachtwoordcomplexiteit (geen zwakke en bekende wachtwoorden)
* Het beperken of vertragen van login-pogingen
* Multi-factor authentication
* Het gebruik maken van random complexe session IDs
* Het niet tonen van session IDs in de url


## A5:2017-Broken Access Control

### Description
Toegangscontrole zorgt ervoor dat gebruikers niet buiten hun bedoelde machtigingen kunnen handelen.
Door middel van toegangscontrole wordt toegang verleent aan inhoud en functies aan sommige gebruikers en niet aan anderen.
Wanneer er sprake is van gebroken toegangscontrole is het rechtenbeheer voor autorisatie niet goed afgedwongen.
Dit heeft als gevolg dat een aanvaller bijvoorbeeld de mogelijkheid heeft om andermans account, rechten en data in te zien, wijzigen of vernietigen.

### Risk
In deze applicatie is er geen risico op "Broken Access Control" omdat er geen gebruik gemaakt wordt van autorisatie.
Dit heeft wel als gevolg dat elke handeling voor elke gebruiker beschikbaar is.

### Risk Authentication and Authorization
Als deze applicatie gebruikt maakt van authentication en authorzation is het risco van "Broken Authentication" en "Broken Access Control" aanwezig.
Belangrijk is dat bij authentication rekening wordt gehouden met OWASP A2:2017-Broken Authentication.
Het risico van "Broken Access Control" is dat de rechten niet worden verleent aan sommige gebruikers maar aan iedereen.
Dit heeft als gevolg dat via een omweg (url) unauthenticated users bij pagina's (rechten) kunnen komen die niet voor hun bedoeld is.

Een goede "Counter-measure" voor deze applicatie is: @PreAuthorize("hasAnyRole('ROLE_....')")
Door middel van @PreAuthorize kunnen de rechten voor verschillende users worden bepaald.

### Counter-measures
Om "Broken Acces Control" tegen te gaan kunnen de volgende maatregelen gehanteerd worden:
* Bouw acces control in vanaf het begin en pas herhaaldelijk toe
* Deny acces by default
* Verberg de interne directory structuur van de web server
* Relateer resource-gebruik aan users
* Invalideer tokens na uitloggen


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
of dat er onbekende nieuwe/custom kwetsbaarheden zijn. Ook kunnen components niet goed geconfigureerd worden.
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