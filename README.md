# HomeSystem

Ein schlankes Bukkit/Spigot Plugin, das die Homes aus EssentialsX in einem benutzerfreundlichen GUI darstellt. Spieler können mit `/home` oder `/homes` ihre Homes als Menü öffnen und werden durch Anklicken sofort teleportiert.

## Features

- Nutzt die bestehenden Homes aus EssentialsX – keine doppelte Datenhaltung.
- Öffnet per `/home` oder `/homes` ein Inventar mit allen verfügbaren Homes.
- Teleportiert den Spieler beim Anklicken eines Items direkt zum ausgewählten Home.
- Verhindert das Herausnehmen, Verschieben oder Droppen der Items innerhalb des Menüs.
- Unterstützt bis zu 54 Homes gleichzeitig (weitere Homes werden nicht angezeigt).

## Installation

1. Lade das fertige Plugin (JAR) per `mvn package`.
2. Lege die JAR-Datei in den `plugins`-Ordner deines Spigot/Paper Servers.
3. Stelle sicher, dass [EssentialsX](https://essentialsx.net/) installiert ist.
4. Starte den Server neu oder führe `/reload confirm` aus.

## Benutzung

- `/home` – Öffnet das Menü mit allen Homes.
- `/homes` – Alias für `/home`.

## Entwicklung

Dieses Projekt nutzt Maven. Die wichtigsten Befehle:

```bash
mvn clean package
```

Die Abhängigkeiten `spigot-api` und `Essentials` werden als `provided` markiert und müssen zur Laufzeit auf dem Server vorhanden sein.
