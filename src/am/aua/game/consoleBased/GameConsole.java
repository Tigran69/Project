//package am.aua.game.consoleBased;
//
//import am.aua.game.gameLogic.GameCore;
//import am.aua.game.players.Player;
//import am.aua.game.units.Archer;
//import am.aua.game.units.Soldier;
//import am.aua.game.units.Tank;
//import am.aua.game.units.Unit;
//
//import java.util.Scanner;
//
//public class GameConsole {
//
//    public void startGame(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter your name");
//        String playerOneName = scanner.nextLine();
//        Player player1 = new Player(playerOneName);
//        System.out.println("Enter your name");
//        String playerTwoName = scanner.nextLine();
//        Player player2 = new Player(playerTwoName);
//        this.players[0] = player1;
//        this.players[1] = player2;
//        this.currentPlayer = player1;
//        System.out.println("Welcome " + player1.getName() + " " + player2.getName());
//        scanner.nextLine();
//        while (true){
//
//            System.out.println(currentPlayer.getName() + "'s turn");
//            System.out.println("Choose your move");
//            System.out.println("1. Buy a unit");
//            System.out.println("2. Sell a unit");
//            System.out.println("3. See your units");
//            System.out.println("4. Move a unit");
//            System.out.println("5. Attack a unit");
//            int command = scanner.nextInt();
//            scanner.nextLine();
//            switch (command) {
//                case 1:
//                    System.out.println("Enter unit");
//                    System.out.println("T,A,S");
//                    String unitClass = scanner.nextLine();
//                    if (unitClass.equals("T")){
//                        Unit tank = new Tank(currentPlayer);
//                        currentPlayer.buyUnit(tank);
//                        System.out.println("You bought a tank");
//                    }
//                    else if (unitClass.equals("A")){
//                        Unit archer = new Archer(currentPlayer);
//                        currentPlayer.buyUnit(archer);
//                        System.out.println("You bought an archer");
//                    }
//                    else if (unitClass.equals("S")){
//                        Unit soldier = new Soldier(currentPlayer);
//                        currentPlayer.buyUnit(soldier);
//                        System.out.println("You bought a soldier");
//                    }
//                    else {
//                        System.out.println("Invalid unit");
//                        break;
//                    }
//                    break;
//
//                case 2:
//                    break;
//
//            }
//
//
//        }
//
//    }
//}