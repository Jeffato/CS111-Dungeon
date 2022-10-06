package HW7;

public class Monster {
    private int dmg;
    private int hp;
    private String name;

    public Monster(String name){
        this.name = name;
        this.dmg = (int) (Math.random()*4)+ 1;
        this.hp = (int) (Math.random()*6) + 5;
    }

    public Monster(){
        this.name = "Wizard";
        this.dmg = (int) (Math.random()*5) + 4;
        this.hp = (int) (Math.random()*13) + 12;
    }

    public String monsterStats(){
        String output = "";
        output += "Name: " + name;
        output += "\nBase HP: " + hp;
        output += "\nBase Damage: " + dmg;

        return output;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
