# Tron-Coalition-Battle
This game was developed in Java. It follows the MVC architecture, includes unit tests, and features dedicated classes for data analysis and statistics generation. Several AI strategies are available, including MaxN, Paranoid, and their extended versions using SOS (Secure Opponent Strategy).<br>The game is fully customizable via terminal input: players can set the number of participants, team sizes, map dimensions, and the type of AI. Players move across the map and automatically place a wall on their previous tile after each move.<br>Maps are generated procedurally based on a strategy pattern, with three modes (Chaos, Balanced and Optimal: strategic spawn points based on the number of players and map size).<br>The game can be played in the terminal and also features a graphical interface made with AWT and Swing in Java.<br>This project was originally intended for teams of around four students, but I chose to work on it solo. I faced significant challenges, especially in the AI implementation, but I’m proud of the progress I made on my own.

# Instructions
$ ant [javadoc, runWinrate, runTemps, runSurvie, run]

# Images
<table>
	<img src="images/game_4_1.png" width="100%"/>
  <tr>
  	<td><img src="images/game_4_2.png" width="300"/></td>
    <td><img src="images/game_4_3.png" width="300"/></td>
    <td><img src="images/game_4_4.png" width="300"/></td>
  </tr>
</table>