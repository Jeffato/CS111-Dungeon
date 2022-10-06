package HW7;

import java.util.ArrayList;
import java.util.Collections;

public class Rooms {
    private Events type;
    private Monster monster;
    private Items accessory;
    private Weapons weapon;
    private Rooms north;
    private Rooms east;
    private Rooms south;
    private Rooms west;

    public Rooms(Events type){
        this.type = type;
        this.accessory = Items.EMPTY;
        this.weapon = Weapons.NONE;
    }
    public static Rooms initializeGameBoard(){ //Creates game board and returns the starting room
        Rooms start = new Rooms(Events.START);
        Rooms wall = new Rooms(Events.WALL);

        //Monster
        Rooms monsterNorth = new Rooms(Events.MONSTER);
        Rooms monsterEast = new Rooms(Events.MONSTER);
        Rooms monsterSouth = new Rooms(Events.MONSTER);
        Rooms monsterWest = new Rooms(Events.MONSTER);

        Rooms[] monsterRooms = new Rooms[] {monsterNorth, monsterEast, monsterSouth, monsterWest};
        generateMonsters(monsterRooms);

//        for(Rooms r: monsterRooms){
//            System.out.println(r.getMonster().monsterStats());
//        }

        //Accessory
        Rooms accNE = new Rooms(Events.ACCESSORY);
        Rooms accSE = new Rooms(Events.ACCESSORY);
        Rooms accSW = new Rooms(Events.ACCESSORY);
        Rooms accNW = new Rooms(Events.ACCESSORY);

        Rooms[] accRooms = new Rooms[] {accNE, accSE, accSW, accNW};
        generateAccessories(accRooms);

//        for(Rooms r: accRooms){
//            System.out.println(r.getAccessory());
//        }

        //Fountain
        Rooms fountain = new Rooms(Events.FOUNTAIN);

        //Weapon
        Rooms weaponL = new Rooms(Events.WEAPON);
        Rooms weaponR = new Rooms(Events.WEAPON);

        Rooms[] wepRooms = new Rooms[] {weaponL, weaponR};
        generateWeapons(wepRooms);

//        for(Rooms r: wepRooms) {
//            System.out.println(r.getWeapon());
//        }

        //Boss
        Rooms wizard = new Rooms(Events.BOSS);
        generateBoss(wizard);

        //Assign Room Directions
        start.setAdj(monsterSouth, wall, wall, wall);
        monsterSouth.setAdj(fountain, accSE, start, accSW);
        accSW.setAdj(monsterWest, monsterSouth, wall, wall);
        accSE.setAdj(monsterEast, wall, wall, monsterSouth);
        weaponL.setAdj(wall, monsterWest, wall, wall);
        monsterWest.setAdj(accNW, fountain, accSW, weaponL);
        fountain.setAdj(monsterNorth, monsterEast, monsterSouth, monsterWest);
        monsterEast.setAdj(accNE, weaponR, accSE, fountain);
        weaponR.setAdj(wall, wall, wall, monsterEast);
        accNW.setAdj(wall, monsterNorth, monsterWest, wall);
        monsterNorth.setAdj(wizard, accNE,fountain, accNW);
        accNE.setAdj(wall, wall, monsterEast, monsterNorth);
        wizard.setAdj(wall, wall, monsterNorth, wall);

        return start;
    }

    //Generator Helper Functions
    public static void generateMonsters(Rooms[] monsterRooms){
        String[] monsterNames = {"Goblin", "Skeleton", "Zombie", "Wolf", "Vampire", "Orc"};

        for(Rooms n : monsterRooms){
            int rng = (int) (Math.random()*6);
            Monster generated = new Monster(monsterNames[rng]);
            n.setMonster(generated);
        }
    }

    public static void generateAccessories(Rooms[] accRooms){
        ArrayList<Integer> assignments = new ArrayList<Integer>();
        assignments.add(1);
        assignments.add(2);
        assignments.add(3);
        assignments.add(4);

        Collections.shuffle(assignments);
        int counter = 0;

        for(Rooms n : accRooms){
            int genAccessoryIndex = assignments.get(counter);
            counter++;
            Items i = Items.EMPTY;

            switch(genAccessoryIndex){
                case 1: i = Items.KEY; break;
                case 2: i = Items.POTION; break;
                case 3: i = Items.RING; break;
                case 4: i = Items.SHIELD; break;
            }

            n.setAccessory(i);
        }
    }

    public static void generateWeapons(Rooms[] wepRooms){
        ArrayList<Integer> assignments = new ArrayList<Integer>();
        assignments.add(1);
        assignments.add(2);

        Collections.shuffle(assignments);
        int counter = 0;

        for(Rooms n : wepRooms){
            int genAccessoryIndex = assignments.get(counter);
            counter++;
            Weapons i = Weapons.NONE;

            switch(genAccessoryIndex){
                case 1: i = Weapons.FLAMETHROWER; break;
                case 2: i = Weapons.SWORD; break;
            }

            n.setWeapon(i);
        }
    }

    public static void generateBoss(Rooms r){
        r.setMonster(new Monster());
    }

    public void setAdj(Rooms n, Rooms e, Rooms s, Rooms w){
        this.setNorth(n);
        this.setEast(e);
        this.setSouth(s);
        this.setWest(w);

    }

    //Other Functions
    public String adjExits(){
        String output = "";
        if(this.getNorth().getType() != Events.WALL){
            output += "north ";
        }

        if(this.getEast().getType() != Events.WALL){
            output += "east ";
        }

        if(this.getSouth().getType() != Events.WALL){
            output += "south ";
        }

        if(this.getWest().getType() != Events.WALL){
            output += "west ";
        }

        output = output.substring(0,output.length()-1);

        return output;
    }

    //Getters and Setters
    public Events getType() {
        return type;
    }

    public void setType(Events type) {
        this.type = type;
    }

    public Monster getMonster() {
        return monster;
    }
    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public Items getAccessory() {
        return accessory;
    }

    public void setAccessory(Items accessory) {
        this.accessory = accessory;
    }

    public Rooms getNorth() {
        return north;
    }

    public void setNorth(Rooms north) {
        this.north = north;
    }

    public Rooms getEast() {
        return east;
    }

    public void setEast(Rooms east) {
        this.east = east;
    }

    public Rooms getSouth() {
        return south;
    }

    public void setSouth(Rooms south) {
        this.south = south;
    }

    public Rooms getWest() {
        return west;
    }

    public void setWest(Rooms west) {
        this.west = west;
    }

    public Weapons getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapons weapon) {
        this.weapon = weapon;
    }
}