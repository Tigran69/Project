Strategy Game Project

Authors:
- Tigran
- Lilit
- Shahen

Overview:
This project is a strategy game where players control units on a grid-based map. The game features both a Console Mode (CLI) and a Graphical User Interface (GUI) built using JavaFX.

Key Features:
- Players can choose units, buy, move, and attack.
- Save and load game states, including terrain and player information.
- The game progresses through turns, and the current player alternates after each turn.
- The GUI provides an interactive experience, while the CLI offers a text-based alternative.

---

Running the Program

1. CLI Mode
You can run the game in Console Mode by passing the -cli argument when running the main class.

Steps:
1. Compile and build the project.
2. Run the program with the -cli argument.

java -jar strategy_game.jar -cli

This will start the game in Console Mode, where you can interact with the game using text-based input and output.

---

2. GUI Mode (JavaFX)
If you don't pass the -cli argument, the program will start in Graphical User Interface (GUI) Mode. This mode is built using JavaFX, providing a visual map, unit selection, and interactive gameplay.

Steps to Run the GUI Mode:
1. Ensure you have JavaFX set up on your system.
2. Compile and build the project.
3. Run the program without any arguments.

java -jar strategy_game.jar

The program will launch a window with the JavaFX-based interface. You can interact with the game using the visual interface, including selecting units, moving them, and seeing the map.

---

Requirements:
- Java 8 or higher: The program is built with Java and requires at least JDK 8 for both the CLI and GUI versions.
- JavaFX: For GUI Mode, you need JavaFX libraries. Ensure that JavaFX is properly installed and configured in your project.
  - If you are using Java 11 or higher, you may need to include JavaFX separately. You can download it from JavaFX Downloads (https://openjfx.io/).

---

Building the Project

Steps:
1. Clone or download the repository.
2. Open the project in your preferred IDE (such as IntelliJ IDEA, Eclipse, or NetBeans).
3. Build the project using your IDE's build tools, or run the following command in the terminal to create a JAR file:

mvn clean package

This will create the strategy_game.jar file, which you can run in both CLI and GUI modes.

---

Save and Load Functionality
The game includes a save/load feature, allowing you to save the game state and resume later. The saved data includes the game map, units, player resources, and the current player.

- To save the game, use the in-game options (either through the CLI or GUI).
- To load a saved game, use the load option in the menu or use the corresponding command in the CLI.

---

Known Issues:
- JavaFX setup may require additional configuration for certain environments (e.g., Java 11+).
- Game logic may not be fully optimized in the current version.

---

License:
This project is licensed under the MIT License - see the LICENSE file for details.

---

Contact:
For questions or feedback, feel free to reach out to any of the authors.
