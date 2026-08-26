# 🌦️ MeteoDash Pro - Application Météo Java Swing & Cartographie

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17+" />
  <img src="https://img.shields.io/badge/GUI-Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java Swing" />
  <img src="https://img.shields.io/badge/Map-Leaflet.js-199900?style=for-the-badge&logo=leaflet&logoColor=white" alt="Leaflet.js" />
  <img src="https://img.shields.io/badge/API-OpenWeatherMap-EB6E4B?style=for-the-badge&logo=openweathermap&logoColor=white" alt="OpenWeatherMap" />
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge" alt="License MIT" />
</p>

---

## 📖 À propos du projet

**MeteoDash Pro** est une application de bureau moderne développée en **Java (Swing)** permettant de consulter les prévisions météorologiques en temps réel pour n'importe quelle ville dans le monde, tout en offrant une visualisation cartographique interactive via **Leaflet.js** et **OpenStreetMap (CartoDB Dark Matter)**.

L'application respecte les meilleures pratiques de conception logicielle avec une architecture **MVC (Modèle-Vue-Contrôleur)** stricte, un système de requêtes asynchrones avec **`SwingWorker`** (pour garantir la fluidité de l'interface) et une interface soignée en **Dark Mode**.

---

## ✨ Fonctionnalités Principales

- 🔍 **Recherche Instantanée & Auto-complétion Intelligente** :
  - Suggestions en temps réel dès la saisie du nom de la ville parmi plus de 50+ métropoles mondiales.
  - Sélection au clavier (Touche Entrée) ou à la souris avec effets de survol modernes.
- 🌡️ **Données Météorologiques en Direct** :
  - Température actuelle (°C).
  - Taux d'humidité relative (%).
  - Description météo détaillée (Ciel dégagé, Pluie, Nuageux, etc.).
- 🗺️ **Visualisation Cartographique Interactive (Leaflet.js)** :
  - Bouton interactif *"Voir la carte"* ouvrant une vue cartographique plein écran.
  - Centrage automatique sur les coordonnées géographiques précises (Latitude / Longitude).
  - Thème sombre personnalisé (*CartoDB Dark Matter*), marqueurs de position et bandeau flottant d'informations météo.
- 🎨 **Interface Graphique Moderne (Dark Theme)** :
  - Palette de couleurs premium (*Slate Dark UI*, teintes `#0F172A`, `#1E293B`, accents Cyan `#38BDF8`).
  - Indicateur d'état du système dynamique (*Online*, *Loading*, *Success*, *Error*).
- ⚡ **Architecture Asynchrone & Robuste** :
  - Les appels à l'API REST s'exécutent en tâche de fond pour ne jamais figer l'interface utilisateur (*Zero UI Freezing*).
  - Gestion fine des codes d'erreurs HTTP (401 Clé invalide, 404 Ville introuvable, 500 Erreur serveur).

---

## 🏗️ Architecture du Projet (MVC)

Le projet est structuré selon le patron de conception **MVC** pour assurer la séparation des responsabilités et la maintenabilité du code :

```
weather-app/
├── pom.xml                               # Configuration Maven & dépendances
├── build_and_run.ps1                     # Script d'automatisation (PowerShell)
├── README.md                             # Documentation du projet
└── src/
    └── main/
        └── java/
            └── com/
                └── weatherapp/
                    ├── WeatherApp.java                    # Point d'entrée principal (Main)
                    ├── controller/
                    │   └── WeatherController.java         # Contrôleur MVC (écouteurs & SwingWorker)
                    ├── model/
                    │   └── WeatherData.java               # Modèle de données météo
                    ├── service/
                    │   └── WeatherApiClient.java          # Client HTTP pour l'API REST OpenWeatherMap
                    ├── ui/
                    │   ├── WeatherPanel.java              # Vue principale Dashboard Swing
                    │   └── MapWindow.java                 # Générateur de la vue cartographique Leaflet.js
                    └── util/
                        └── WeatherParser.java             # Parser JSON des réponses de l'API
```

### 🧩 Rôles des Composants :
- **`model/WeatherData`** : Encapsule les informations météo (ville, température, humidité, météo, latitude, longitude).
- **`service/WeatherApiClient`** : Effectue la requête HTTP GET vers l'API OpenWeatherMap avec gestion des timeouts et encodage d'URL.
- **`util/WeatherParser`** : Extrait et transforme les données JSON brutes en objets `WeatherData`.
- **`ui/WeatherPanel`** : Composant graphique Swing affichant les cartes de données, le champ de recherche et les suggestions.
- **`ui/MapWindow`** : Génère une page HTML dynamique intégrant Leaflet.js et l'affiche dans le navigateur par défaut.
- **`controller/WeatherController`** : Fait le pont entre la vue et les services à l'aide d'un `SwingWorker` asynchrone.

---

## 🛠️ Technologies Utilisées

| Technologie | Usage |
| :--- | :--- |
| **Java 17+** | Langage de programmation principal |
| **Java Swing / AWT** | Conception de l'interface graphique de bureau |
| **OpenWeatherMap API** | Fournisseur de données météorologiques en temps réel |
| **Leaflet.js & OpenStreetMap** | Affichage de la carte géographique interactive |
| **CartoDB Dark Matter** | Fonds de carte sombres haute définition |
| **org.json** | Bibliothèque de parsing et traitement JSON |
| **Apache Maven** | Gestionnaire de dépendances et de build |

---

## 🚀 Installation & Démarrage

### 1. Prérequis

Assurez-vous d'avoir installé sur votre machine :
- **Java JDK 17** ou supérieur (`java -version`)
- **Apache Maven** (optionnel si vous utilisez le script fourni)
- Un accès Internet pour la récupération des données météo en temps réel

### 2. Cloner le projet

```bash
git clone https://github.com/mohammmeeed/Weather-app.git
cd Weather-app
```

### 3. Exécution de l'application

#### Option A : Avec le script PowerShell (Recommandé sur Windows)
Un script prêt à l'emploi s'occupe de télécharger la dépendance, compiler et lancer l'application :

```powershell
.\build_and_run.ps1
```

#### Option B : Avec Maven

Compilez et lancez le projet avec les commandes Maven standard :

```bash
# Compilation du projet
mvn clean compile

# Lancement de l'application
mvn exec:java -Dexec.mainClass="com.weatherapp.WeatherApp"
```

#### Option C : Exécution manuelle

```bash
# 1. Compiler les sources Java
javac -d target/classes -cp lib/json-20231013.jar src/main/java/com/weatherapp/*.java src/main/java/com/weatherapp/*/*.java

# 2. Exécuter l'application
java -cp "target/classes;lib/json-20231013.jar" com.weatherapp.WeatherApp
```

---

## 🔑 Configuration de la clé API (OpenWeatherMap)

L'application utilise l'API **OpenWeatherMap** pour récupérer les données météo en temps réel.
Une clé de démonstration est préconfigurée dans la classe [`WeatherApiClient.java`](src/main/java/com/weatherapp/service/WeatherApiClient.java).

Pour utiliser votre propre clé API gratuite :
1. Créez un compte gratuit sur [OpenWeatherMap](https://openweathermap.org/).
2. Rendez-vous dans la section **API Keys** et copiez votre clé.
3. Ouvrez le fichier `WeatherApiClient.java` et modifiez la constante `API_KEY` :
   ```java
   private static final String API_KEY = "VOTRE_CLE_API_ICI";
   ```

---

## 🖥️ Utilisation

1. Lancez l'application.
2. Saisissez le nom d'une ville (ex: `Paris`, `Tokyo`, `Casablanca`, `New York`) ou sélectionnez-en une parmi les suggestions.
3. Cliquez sur **Search** ou appuyez sur **Entrée**.
4. Consultez instantanément la température, l'humidité et les conditions météo.
5. Cliquez sur le bouton **"Voir la carte"** pour ouvrir la localisation géographique de la ville sur une carte interactive en thème sombre.

---

## 📄 Licence

Ce projet est sous licence **MIT**. Vous êtes libre de l'utiliser, le modifier et le distribuer.

---

<p align="center">

</p>
