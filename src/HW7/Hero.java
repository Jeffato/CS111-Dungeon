package HW7;

import java.util.Scanner;

public class Hero {
    private String name;
    private int baseDmg;
    private int hp;
    private int baseDex;
    private Weapons weapon;
    private Items[] inventory;
    private Rooms currLocation;
    public Hero(String name, Rooms start) {
        this.name = name;
        this.baseDmg = (int) (Math.random() * 6) + 1;
        this.hp = (int) (Math.random() * 11) + 10;
        this.baseDex = (int) (Math.random() * 6) + 3;
        this.weapon = Weapons.NONE;
        this.inventory = new Items[]{Items.EMPTY, Items.EMPTY, Items.EMPTY};
        this.currLocation = start;
    }

    public String heroStats() {
        String output = "";
        output += "Name: " + name;
        output += "\nBase HP: " + hp;
        output += "\nBase Damage: " + baseDmg;
        output += "\nBase Dex: " + baseDex;

        return output;
    }

    //Action (Turn) related Commands
    public void moveHero(String s) {     //Moves hero current room to the n/s/e/w based on command
        Rooms r = new Rooms(Events.WALL);
        switch (s) {
            case "n":
                r = this.getCurrLocation().getNorth(); break;
            case "e":
                r = this.getCurrLocation().getEast(); break;
            case "s":
                r = this.getCurrLocation().getSouth(); break;
            case "w":
                r = this.getCurrLocation().getWest(); break;

        }

        if(r.getType() == Events.WALL){
            System.out.printf("%n%s tries to move into a wall. %s cannot move through walls.%n", name, name);
        }

        else if(r.getType() == Events.BOSS){
            if(indexOfItem(Items.KEY) != -1){
                System.out.printf("%n%s moves into the boss room.%n", name);
                this.setCurrLocation(r);
            }

            else{
                System.out.println("The door is locked. A key is needed...%n");
            }
        }

        else{
            System.out.printf("%n%s moves into the next room.%n", name);
            this.setCurrLocation(r);
        }
    }
    public void getRoomItem(String s){
        //Decide if item or weapon
        switch(s){
            case "key": case "potion": case "ring": case "shield":
                if(this.currLocation.getAccessory() != Items.EMPTY){
                    Items i = this.currLocation.getAccessory();
                    addItem(i);
                } break;
            case "sword": case "flamethrower":
                if(this.currLocation.getWeapon() != Weapons.NONE) {
                    Weapons w = this.currLocation.getWeapon();
                    equipWeapon(w);
                } break;
        }
    }
    public void addItem(Items i){ //add item i to hero inventory
        Items[] temp = inventory.clone();
        int counter = 0;
        while(counter < 3 && temp[counter] != Items.EMPTY ) {
            counter++;
        }

        if(counter == 3){
            System.out.printf("%n%s inventory is full. Drop an item to pick up another.%n", name);
        }

        else{
            temp[counter] = i;
            System.out.printf("%n%s was added to %s's inventory %n", i, name);
            this.setInventory(temp);
            this.getCurrLocation().setAccessory(Items.EMPTY);
        }
    }
    public void dropItem(String s){//drop s from inventory.
        Items i = Items.EMPTY;

        switch(s){
            case "key": i = Items.KEY; break;
            case "potion": i = Items.POTION; break;
            case "ring": i = Items.RING; break;
            case "shield": i = Items.SHIELD; break;
        }

        int index = indexOfItem(i);

        if(index == -1){
            System.out.printf("%s does not have that item...%n", name);
        }

        else{
            Items[] temp = inventory.clone();
            temp[index] = Items.EMPTY;
            this.setInventory(temp);
            System.out.printf("%s dropped %s.%n", name, i);
        }
    }
    public void equipWeapon(Weapons w){//Get or swap
        Weapons temp = this.weapon;
        this.setWeapon(w);
        this.currLocation.setWeapon(temp);
        System.out.printf("%s obtained %s%n", name, weapon);
    }
    public int indexOfItem(Items i){
        int output = -1;

        for(int a= 0; a<inventory.length; a++){
            if(inventory[a] == i){
                return a;
            }
        }

        return output;
    }

    public void drink(Dungeon d, String s){
        if(s.equals("Y")){
            double rng = Math.random();
            d.setFountainDrank(true);

            if(rng < 0.20){ // +2 damage
                setBaseDmg(getBaseDmg()+2);
                System.out.printf("%s feels invigorated by the water. DAMAGE +2!%n", name);
            }

            else if(rng < 0.40){ // +1 dex
                setBaseDex(getBaseDex()+1);
                System.out.printf("%s feels invigorated by the water. DEX +1!%n", name);
            }

            else if(rng < 0.60){ // -2 hp
                setHp(getHp()-2);
                System.out.printf("Despite the clear color, the water had a foul taste. HP -2!%n");
            }

            else{
                System.out.println("Nothing happened.");
            }
        }
    }
    public void battle(Dungeon d, Monster m){
        Scanner input = new Scanner(System.in);
        System.out.printf("A %s blocks your path!", m.getName());

        //pre-battle: Equipment modifiers
        int dexMod = 0;
        int dmgRed = 0;
        int dmgMod = 0;
        int regen = 0;

        // weapons:
        Weapons w = this.getWeapon();
        switch(w){
            case FLAMETHROWER:
                dmgMod += 10;
                dexMod -= 2;
                break;
            case SWORD:
                dmgMod += 3;
                break;
        }

        //items
        for(Items i : inventory){
            if(i != Items.EMPTY){
                switch(i){
                    case RING:
                        dexMod += 1;
                        break;
                    case SHIELD:
                        dmgRed +=1;
                        break;
                    case POTION:
                        regen += 1;
                        break;
                }
            }
        }

        //player + enemy modifiers
        boolean dodge = false;
        double monDmgRed = 1;
        boolean fleed = false;

        while(hp > 0 && m.getHp()>0 && !fleed){
            System.out.printf("%nWhat will %s do?", name);
            String s = input.next();

            //player phase
            switch(s){
                case "attack":
                    int damageCalc = (int)((dmgMod + baseDmg) * monDmgRed);
                    m.setHp(m.getHp() - damageCalc);
                    System.out.printf("%n%s attacks! Hit for %d!", name, damageCalc);
                    break;

                case "dodge":
                    double successRoll = (baseDex + dexMod) * 0.1;
                    double rng = Math.random();
                    if(rng < successRoll){
                        dodge = true;
                        System.out.printf("%n%s prepares to dodge the next attack!", name);
                    }

                    else{
                        System.out.printf("%n%s trips! Can't dodge the next attack!", name);
                    }
                    break;

                case "run":
                    double successRun = (baseDex + dexMod) * 0.01;
                    double rng2 = Math.random();
                    if(rng2 < successRun){
                        System.out.printf("%n%s ran away!%n", name);
                        run();
                        fleed = true;
                    }
                    else{
                        System.out.printf("%n%s couldn't run away!", name);
                    }
                    break;

            }

            //enemy phase
            if(m.getHp()>0 && !fleed) {

                double rng = Math.random();
                monDmgRed = 1;

                if (rng < 0.7) { //attack
                    if (dodge == true) {
                        System.out.printf("%n%s missed! %n", m.getName());
                        dodge = false;
                    } else {
                        int damageCalcMon = m.getDmg() - dmgRed;
                        this.setHp(this.getHp() - damageCalcMon);
                        System.out.printf("%n%s attacks! Hit for %d!%n", m.getName(), damageCalcMon);
                    }
                } else { // defend
                    monDmgRed = 0.5;
                    System.out.printf("%n%s defends!", m.getName());
                }
            }

            //end phase
            hp += regen;
        }

        if(hp >0 && !fleed) {
            if(currLocation.getType() == Events.BOSS){
                d.setBossDefeated(true);
            }
            System.out.printf("%n%s won!%n", name);
        }
    }
    public void run(){
        boolean flee = false;

        while(!flee){
            int rng = (int) (Math.random()*4);
            Rooms r = new Rooms(Events.WALL);

            switch(rng){
                case 0: r = currLocation.getNorth(); break;
                case 1: r = currLocation.getEast(); break;
                case 2: r = currLocation.getSouth(); break;
                case 3: r = currLocation.getWest(); break;
            }
            if(r.getType() != Events.WALL && r.getType() != Events.BOSS){
                this.setCurrLocation(r);
                flee = true;
            }
        }
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBaseDmg() {
        return baseDmg;
    }

    public void setBaseDmg(int baseDmg) {
        this.baseDmg = baseDmg;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getBaseDex() {
        return baseDex;
    }

    public void setBaseDex(int baseDex) {
        this.baseDex = baseDex;
    }

    public Items[] getInventory() {
        return inventory;
    }

    public void setInventory(Items[] inventory) {
        this.inventory = inventory;
    }

    public Rooms getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(Rooms currLocation) {
        this.currLocation = currLocation;
    }

    public Weapons getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapons weapon) {
        this.weapon = weapon;
    }
}