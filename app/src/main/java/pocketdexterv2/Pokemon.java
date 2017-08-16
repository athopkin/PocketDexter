package pocketdexterv2;

import java.io.Serializable;
import java.util.ArrayList;

class Pokemon implements Serializable {

	private static final long serialVersionUID = 3840949102041552388L;
	
	private int num;
	private String name;
	private String type;
	private String type2;
	private String evolves = null;
	private boolean legend;
	private boolean hasMega;
	private ArrayList<Pokemon> alts = new ArrayList<Pokemon>();
	private int total = 0;
	private int hp = 0;
	private int attack = 0;
	private int defense = 0;
	private int spatk = 0;
	private int spdef = 0;
	private int speed = 0;

	public Pokemon(int num, String name, String type, String type2, int total, int hp, int attack, int defense, int spatk, int spdef, int speed, boolean legend, boolean hasMega) {
		this.num = num;
		this.name = name;
		this.type = type;
		this.type2 = type2;
		this.legend = legend;
		this.hasMega = hasMega;
		this.total = total;
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spatk = spatk;
		this.spdef = spdef;
		this.speed = speed;
	}
	
	public Pokemon(int num, String name, String type, String type2, String evolves, int total, int hp, int attack, int defense, int spatk, int spdef, int speed, boolean legend, boolean hasMega) {
		this.num = num;
		this.name = name;
		this.type = type;
		this.type2 = type2;
		this.evolves = evolves;
		this.legend = legend;
		this.hasMega = hasMega;
		this.total = total;
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spatk = spatk;
		this.spdef = spdef;
		this.speed = speed;
	}

	public int getNum() {
		return num;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public String getType2() {
		return type2;
	}
	
	public String getEvo() {
		return evolves;
	}
	
	public boolean getLegend() {
		return legend;
	}
	
	public boolean hasMega() {
		return hasMega;
	}
	
	public String toString() {
		return num+" "+name+" "+type+" "+type2+" "+evolves+" "+legend+" "+hasMega;
	}
	
	public void addForm(Pokemon other) {
		alts.add(other);
	}
	
	public int getTotal() {
		return total;
	}

	public int getHp() {
		return hp;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getSpatk() {
		return spatk;
	}
	
	public int getSpdef() {
		return spdef;
	}

	public int getSpeed() {
		return speed;
	}

	public ArrayList<Pokemon> getAlts() {
		return alts;
	}
}
