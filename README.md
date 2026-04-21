Luontopeli
Luontopeli on Android-sovellus luonnon tutkimiseen ja retkien seurantaan. Sovelluksen avulla käyttäjä voi seurata kuljettuja reittejä, laskea askeleita, tunnistaa kasveja tekoälyn avulla ja tallentaa havaintojaan kartalle.

Projektin tavoitteena oli toteuttaa moderni Android-sovellus, joka hyödyntää laitteen antureita, paikannusta ja pilvipalveluita.

Toteutus (Viikot 1-6)
Projekti on rakennettu "offline-first" -periaatteella ja noudattaa MVVM-arkkitehtuuria. Sovelluksen kehityksessä on keskitytty seuraaviin osa-alueisiin:

Viikko 1: UI & Navigointi – Käyttöliittymän rakentaminen Material Design 3 -tyylillä ja Jetpack Compose -navigoinnin toteutus.

Viikko 2: Anturit – Askelmittarin ja gyroskoopin integrointi SensorManager-rajapinnan avulla.

Viikko 3: Kartat – GPS-paikannus ja reitin piirtäminen osmdroid-kirjastolla.

Viikko 4: Kamera – CameraX-integraatio kuvien ottamiseen ja tallentamiseen.

Viikko 5: Tekoäly – Kasvien tunnistus ML Kit -kuvantunnistuksella.

Viikko 6: Firebase – Autentikointi, Firestore-tietokanta ja Firebase Storage havaintojen synkronointia varten.

Teknologiat
Kieli: Kotlin

UI: Jetpack Compose, Material Design 3

Arkkitehtuuri: MVVM, Hilt (Dependency Injection)

Tietokanta: Room (Local), Firebase (Cloud)

Kirjastot: * osmdroid (Kartat)

CameraX (Kameran hallinta)

ML Kit (Kuvan tunnistus)

Kotlin Coroutines & Flow (Asynkroninen ohjelmointi)

Arkkitehtuuri
Sovellus on jaettu selkeisiin kerroksiin tietovirran hallitsemiseksi:

UI Layer: Composable-näkymät, jotka tarkkailevat StateFlow-muuttujia.

ViewModel Layer: Liiketoimintalogiikan hallinta ja UI-tilan säilyttäminen.

Repository Layer: Datan yhdistäminen (Room-tietokanta ja verkkoyhteydet).

Data Layer: DAO-rajapinnat ja entiteettimäärittelyt.

Asennusohjeet
Kloonaa repositorio.

Avaa projekti Android Studiossa.

Varmista, että google-services.json (Firebase-konfiguraatio) on asetettu app-kansioon, jos haluat käyttää pilvipalveluita.

Synkronoi Gradle-tiedostot.

Rakenna ja aja sovellus emulaattorilla tai laitteella.
