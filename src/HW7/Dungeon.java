package HW7;

import java.util.Scanner;

public class Dungeon {
    private boolean bossDefeated = false;
    private boolean fountainDrank = false;

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        //Create gameboard
        Rooms start = Rooms.initializeGameBoard();
        Dungeon game = new Dungeon();

        //Game Start:
        System.out.println("Welcome to Wizards and Heroes!");
        System.out.println("\nWhat is the name of your Hero?");
        String name = userInput.next();
        Hero player = new Hero(name, start);

        System.out.println(player.heroStats());

        System.out.printf("\n%s enters the dungeon in an attempt to destroy the Wizard! ", player.getName());
        int counter = 0;

        //room logic
        while(player.getHp()>0 && !game.bossDefeated){
            //Code to prevent errors with nextLine
            if(counter == 0){
                String j = userInput.nextLine();
                counter++;
            }

            //Check if room has monster -> if so battle!
            Monster m = player.getCurrLocation().getMonster();
            if(m != null && m.getHp() > 0){
                player.battle(game, m);
            }

            //Check if room has an item -> State the item
            Items i = player.getCurrLocation().getAccessory();
            if(i != null && i != Items.EMPTY){
                System.out.printf("There is a %s in the room. ", i);
            }

            Weapons w = player.getCurrLocation().getWeapon();
            if(w != null && w != Weapons.NONE){
                System.out.printf("There is a %s in the room. ", w);
            }

            if(player.getCurrLocation().getType() == Events.FOUNTAIN){
                if(!game.fountainDrank) {
                    System.out.println("There is a FOUNTAIN in the center of the room. Its waters could do anything...");
                    System.out.printf("%nShould %s drink from the FOUNTAIN? (Y/N)", name);
                    String s = userInput.nextLine();
                    player.drink(game, s);
                }

                else{
                    System.out.println("There is a FOUNTAIN in the center of the room. There is no more water.");
                }
            }

            //State all exits that are present in the room
            Rooms r = player.getCurrLocation();

            if(r.getType() != Events.BOSS && player.getHp() > 0) {
                String exits = r.adjExits();
                System.out.printf("There are exits to the %s...", exits);

                System.out.printf("%nWhat should %s do?", player.getName());
                String s = userInput.nextLine();

                //split string based on space
                String command = s.substring(0, s.indexOf(" "));
                String arg = s.substring(s.indexOf(" ") + 1);

                //use switch to match to command
                switch (command) {
                    case "go":
                        player.moveHero(arg);
                        break;
                    case "get":
                        player.getRoomItem(arg);
                        break;
                    case "drop":
                        player.dropItem(arg);
                        break;
                }
            }
        }

        if(player.getHp() <= 0){
            System.out.printf("%s died...", name);
        }
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }

    public void setFountainDrank(boolean fountainDrank) {
        this.fountainDrank = fountainDrank;
    }
}
