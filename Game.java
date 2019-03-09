

import java.util.ArrayList;
import java.util.Random;
import java.io.*;


abstract class Warrior implements Runnable{
    private String name;
    private boolean immortal;
    private boolean movable;
    private boolean isalive;
    private boolean hasFins;
    private boolean notified=false;
    private Random chooser;
    private Controller ctrl;
    private Grid nozama;
    private int[] coordinates= new int[2]; //keeps the coordinates of the warrior
    private static int numOfWarriors=0;  //keep track of the warriors alive
    public Warrior(String name){
        this.name=name;
        this.immortal=false;
        this.movable=true;
        this.isalive=true;
        this.hasFins=true;
        numOfWarriors++;
        chooser=new Random();
       
    }
    public abstract void eat();
    public abstract void sleep();
    public abstract void drink();
    public static int getnumOfWarriors(){
        return Warrior.numOfWarriors;
    }
    public static void setnumOfWarriors(){
       Warrior.numOfWarriors--;
    }
    public boolean swim(Grid obj,int dir){//implements the action of swimming of  warrior
       int x_coor=this.coordinates[0];
       int y_coor=this.coordinates[1];
       
      if(dir==1 && x_coor!=obj.getColumns()-1  && obj.getObject(x_coor+1, y_coor, 0)==null){ //1-moves left
               obj.addObject(this,x_coor+1,y_coor,0);// Updating the location of the refered warrior on the grid
               obj.removeObject(x_coor, y_coor,0);
               //System.out.println(this.getName()+" swam from "+" "+"("+x_coor+","+y_coor+")"+" to "+"("+(x_coor+1)+","+y_coor+")");
               this.setCoordinates(x_coor+1, y_coor);//updating the coordinates of the warrior refered
               
               return true;
       }
       else if(dir==2 && x_coor!=0 && obj.getObject(x_coor-1, y_coor,0)==null){ //2-moves right
               obj.addObject(this, x_coor-1, y_coor,0);
               obj.removeObject(x_coor, y_coor,0);
               //System.out.println(this.getName()+" swam from "+" "+"("+x_coor+","+y_coor+")"+" to "+"("+(x_coor-1)+","+y_coor+")");
               this.setCoordinates(x_coor-1, y_coor);
               
               return true;
       }
       else if(dir==3 && y_coor!=obj.getRows()-1  && obj.getObject(x_coor, y_coor+1,0)==null ){ //3-moves forward
               obj.addObject(this, x_coor, y_coor+1,0);
               obj.removeObject(x_coor, y_coor,0);
               //System.out.println(this.getName()+" swam from "+" "+"("+x_coor+","+y_coor+")"+" to "+"("+x_coor+","+(y_coor+1)+")");
               this.setCoordinates(x_coor,y_coor+1);
               return true;
       }
       else if(dir==4 && y_coor!=0 && obj.getObject(x_coor, y_coor-1,0)==null ){
               obj.addObject(this,x_coor,y_coor-1,0);
               obj.removeObject(x_coor, y_coor,0);
               //System.out.println(this.getName()+" swam from "+" "+"("+x_coor+","+y_coor+")"+" to "+"("+x_coor+","+(y_coor-1)+")");
               this.setCoordinates(x_coor, y_coor-1);
               return true;
       }
       else{//if the waarior was unable to swim returns 'false'
           return false;
       }
    }
    
    public int[] getCoordinate(){//eturns the current coordinate of the warrior
        return this.coordinates;
    }
    public void setCoordinates(int x_coor,int y_coor){
        this.coordinates[0]=x_coor;
        this.coordinates[1]=y_coor;
    }
    public void setIsalive(){
        this.isalive=false;
    }
    public void setMoveable(){
        this.movable=false;
    }
    public void setImmortal(){
        this.immortal=true;
    }
    public void removeSwimFin(){
        this.hasFins=false;
    }
    public boolean getMoveable(){
        return this.movable;
    }
   
    public String getName(){
        return this.name;
    }
    public boolean getIsalive(){
        return this.isalive;
    }
    public boolean getImmortal(){
        return this.immortal;
    }
    public  static void  setNumOfWarrior(){//updates the number of warriors who is alive
        numOfWarriors--;
    }
    public synchronized void update(Warrior winner){
       this.ctrl.updateSummary(this.name+" IS NOTIFIED "+winner.name+" HAS WON");
       System.out.println(this.name+" IS NOTIFIED "+winner.name+" HAS WON");
       this.notified=true;
       
    }
    public boolean getNotified(){
        return this.notified;
    }
    public void addCtrl(Controller ctrl){
        this.ctrl=ctrl;
    }
    public Controller getctrl(){
       return this.ctrl; 
    }
    public void addGrid(Grid grid){
        this.nozama=grid;
    }
    public void run(){
        
        while(!this.getNotified() && this.getIsalive() && this.getMoveable()){
               int dir;
               boolean status=false;
               while(!status){
               if(this.getClass()==SuperWarrior.class && !this.getImmortal()){//check whether the refered warrior is a supper warrior
                   dir=((SuperWarrior)this).searchForLotus(nozama);//searchs for lotus and decides in which direction  the warrior should swim
                   if (dir!=0){
                        
                        status=this.swim(nozama,dir); 
                        
                       
                                 
                   }
                   else{
                        
                        dir=chooser.nextInt(4)+1;
                        status=this.swim(nozama,dir);
                   }
                       
                 }
                 else{
                    
                    dir=chooser.nextInt(4)+1;
                    status=this.swim(nozama,dir);
                    
                 }
               }
                 //After the warrior has swam,implements the action which occurs on that coordinate
                 // to implement the action the requied method is called
                 try{
                   Thread.sleep(10);
                 }catch(InterruptedException e){}
                 
                 
                    
              
             
        }
               
       }
    
    
    
   
}
  
    
            




class SuperWarrior extends Warrior{
   public static int numOfSuper=0;
   private Binocular bino;
   public SuperWarrior(String name){
       
       super(name);
       bino=new Binocular();//creates a Binocular object
       numOfSuper++;
       
   }
   public int searchForLotus(Grid grid){//implements the logic of searching the nearest line crossings for lotus objects
      int x_coor=this.getCoordinate()[0];
      int y_coor=this.getCoordinate()[1];
      return bino.search(this,grid,x_coor,y_coor);//gets the bino to search the nearest line crossings and tell the direction to swim
      
      
   }
   public void eat(){
        System.out.println(this.getName()+" EATS!");
    }
    public void sleep(){
        System.out.println(this.getName()+" SLEEPS!");
    }
    public void drink(){
        System.out.println(this.getName()+" DRINKS!");
    }

            
  }



class NormalWarrior extends Warrior {
    public static int numOfNormal=0; //to keep track of the number of normal warriors
    public NormalWarrior(String name){
      super(name);
      numOfNormal++;
      
    }
    public void eat(){ //implements the abstract method eat in the super class
        System.out.println(this.getName()+" EATS!");
    }
    public void sleep(){
        System.out.println(this.getName()+" SLEEPS!");
    }
    public void drink(){
        System.out.println(this.getName()+" DRINKS!");
    }

    
}


class Treasure implements Active,Runnable {
   private boolean taken=false;
   private Warrior Winner;
   private ArrayList<Object> observers;
   private int x_coor;
   private int y_coor;
   private Grid lake;
   public Treasure(int x_coor,int y_coor,Grid lake){
       this.taken=false;
       this.x_coor=x_coor;
       this.y_coor=y_coor;
       this.lake=lake;
       observers=new ArrayList<Object>();
       
   } 
   public boolean checkTreasure(){//returns whether the chest is taken or not
       return this.taken;
   }
   public void act(Warrior obj,Grid grid){//If the treasure is found update the grid and informs the user
       obj.getctrl().updateSummary(obj.getName()+" HAS FOUND TREASURE!!!!");
       System.out.println(obj.getName()+" HAS FOUND TREASURE!!!!");
       this.Winner=obj;
       this.notifyObservers(this.Winner);
       System.out.println("GAME OVER!!!");
       obj.getctrl().updateSummary("GAME OVER!!!");
       this.taken=true;
   }
   public void addObserver(Object obj){
       this.observers.add(obj);
       
               
   }
   public void removeObserver(Object obj){
       int count=0;
      
       while(true){
           if  (obj.equals(observers.get(count))){
                observers.remove(count);
                break;
           }
           count++;
             
       }
   }
   public  void  notifyObservers(Warrior winner){
       for(int i=0;i<observers.size();i++){
          if(observers.get(i) instanceof Warrior){
           ((Warrior)observers.get(i)).update(winner);
          }
          else if(observers.get(i) instanceof Lotus){
              ((Lotus)observers.get(i)).update(winner);
          }
          else if(observers.get(i) instanceof Fish){
              ((Fish)observers.get(i)).update(winner);
          }
       }
   }
   public void run(){
      while(Warrior.getnumOfWarriors()>0 && !this.taken){
          try{
          if(this.lake.getObject(x_coor, y_coor,0) instanceof Warrior){
              this.act((Warrior)this.lake.getObject(x_coor, y_coor,0),lake);
          }
          }catch(NullPointerException  e){}
          try{
            Thread.sleep(10);
          }catch(InterruptedException e){}
          
      }
   }
  
  
}






class Grid {
    private GridLocation grid[][][];  //3D array which holds objects in coordinates
    private int columns;
    private int rows;
    private int layers;
    public Grid(int columns,int rows,int layers){
        grid=new GridLocation[rows][columns][layers];
        this.columns=columns;
        this.rows=rows;
        this.layers=layers;
    }
    public void addObject(Object obj,int x_coor,int y_coor,int layer) {    //Adds new objects to the grid
        if(grid[x_coor][y_coor][layer]==null){
            grid[x_coor][y_coor][layer]=new GridLocation();
        }
        
       synchronized(grid[x_coor][y_coor][layer]){      
         grid[ x_coor][y_coor][layer].setOccupant(obj);
       }
          
    }
       
        
        
    
    public void removeObject(int x_coor,int y_coor,int layer){   //Removes an objects from the grid
       synchronized(grid[x_coor][y_coor][layer]){
           grid[x_coor][y_coor][layer].removeOccupant();
           
      }
    }
    public Object getType(int x_coor,int y_coor,int layer){  // Returns the class type of a particular object which is on the grid
       if (grid[x_coor][y_coor][layer]!=null){  //checks  whether the referd location  is unempty 
        return grid[x_coor][y_coor][layer].getType();
       }
       else{
           return null;
       }
    }
    public int getColumns() {
        return columns;
    }
    public int getRows(){
        
      return this.rows;
    }
    public Object getObject(int x_coor,int y_coor,int layer){// returns the objects which is on the refered coordinater
       try{
        return grid[x_coor][y_coor][layer].getObject();
       }
       catch(NullPointerException e){
         return null;
       }
    }
}


class GridLocation {
    private Object occupant;
    public GridLocation(){
        
    }
    public void setOccupant(Object obj){
        this.occupant=obj;
    }
    public void removeOccupant(){
        this.occupant=null;
    }
    public Object getType(){
        return occupant.getClass();
    }
    public Object getObject(){
        return occupant;
    }
}




interface Active {//This interface is to explicitly specify the Objects that can form an action on a particular coordinate
    public abstract void act(Warrior obj,Grid grid);//Releven action is implemented in releven classes
}














abstract class Fish implements Runnable{
    public static int numOfFish=0;
    private int x_coor;
    private int y_coor;
    private Grid lake;
    private boolean gamestatus;
    public Fish(int x_coor,int y_coor,Grid lake){
        numOfFish++;
        this.x_coor=x_coor;
        this.y_coor=y_coor;
        this.lake=lake;
        this.gamestatus=true;
    }
    public void update(Warrior winner){
      this.gamestatus=false;
    }
    public Grid getLake(){
        return this.lake;
    }
    public int getx_coor(){
       return this.x_coor;
    }
     public int gety_coor(){
       return this.y_coor;
    }
    public void run(){
       while(Warrior.getnumOfWarriors()>0 && this.gamestatus){
           try{
           if(lake.getObject(this.x_coor,this.y_coor,0) instanceof Warrior &&  this instanceof Active){
               if(this instanceof KillerFish){
                   ((KillerFish)this).act((Warrior)lake.getObject(x_coor, y_coor,0),lake);
               }
               else if(this instanceof RubberFish){
                       ((RubberFish)this).act((Warrior)lake.getObject(x_coor, y_coor,0),lake);
               }
           }
           
       }
       catch(NullPointerException e){
           
       }
       try{
           Thread.sleep(10);
       }
       catch(InterruptedException e){}
          
       
    }
    }
}


class KillerFish extends Fish implements Active{
   public static int numOfKiller=0;
   public KillerFish(int x_coor,int y_coor,Grid lake){ 
       super(x_coor,y_coor,lake);
       numOfKiller++;
   }
   public void kill(Warrior obj){//Kills the warrior
    
      if(!obj.getImmortal()){
       obj.setIsalive();
       obj.setMoveable();
       obj.getctrl().updateSummary(obj.getName()+" WAS KILLED!");
       System.out.println(obj.getName()+" WAS KILLED!");
       Warrior.setnumOfWarriors();
      }
     
       
   }

    public void act(Warrior obj,Grid grid) {
      synchronized(obj){
       grid.removeObject(this.getx_coor(),this.gety_coor(),0);//updating the location on thrgrid
       this.kill(obj);//calls the kill method
       
      }
    }
    
}



class RubberFish extends Fish  implements Active {
    public static int numOfRubber;
    public RubberFish(int x_coor,int y_coor,Grid lake){
        super(x_coor,y_coor,lake);
        numOfRubber++;
    }
    public void pullFins(Warrior obj){//pulls the swim fins of the warrior
        (obj.getctrl()).updateSummary(obj.getName()+" HAS LOST SWIM FINS!");
        obj.removeSwimFin();
        obj.setMoveable();
        System.out.println(obj.getName()+" HAS LOST SWIM FINS!");
        
        this.getLake().removeObject(this.getx_coor(),this.gety_coor(),0);
        Warrior.setnumOfWarriors(); 
       
    }
    public void act(Warrior obj,Grid grid){//implement the act method of Active interface(
       synchronized(obj){
        this.pullFins(obj);//calls to pull the swimfins of the refered warrior
       }
    }
}


class InnocentFish extends Fish {
    public static int numOfInnocent=0;
    public InnocentFish(int x_coor,int y_coor,Grid lake){
        super(x_coor,y_coor,lake);
        numOfInnocent++;
    }
   
}



class Lotus implements Active,Runnable{
    private int petals;
    public static int  numOfLotus;
    private int x_coor;
    private int y_coor;
    private Grid lake;
    private boolean gamestatus;
    public Lotus(int x_coor,int y_coor,Grid lake){
        this.x_coor=x_coor;
        this.y_coor=y_coor;
        this.lake=lake;
        this.petals=100;
        this.gamestatus=true;
        numOfLotus++;
    }
    public void removePetal(){//decreasing the number of petals when a warrior pluck a petal
        this.petals--;
    }
    public void act(Warrior obj,Grid grid){ //implements the logic to make a warrior immortal after plucking a petal
       synchronized(obj){ 
        if(!obj.getImmortal() && obj.getMoveable()){//checks whether the warrior is already immortal or not
           this.removePetal();
           obj.setImmortal();
           obj.getctrl().updateSummary(obj.getName()+" HAS BECOME IMMORTAL!");
           System.out.println(obj.getName()+" HAS BECOME IMMORTAL!");
           if(this.getPetals()==0){
               grid.removeObject(obj.getCoordinate()[0],obj.getCoordinate()[1],1);//if all the petal of the refered lotus object has benn pluck the lotus object is removed from the grid
           }
        }   
      }
       
        
    }
    public int getPetals(){
        return this.petals;//returns the number of petals left of a lotus object
    }
    public void update(Warrior winner){
        this.gamestatus=false;
    }
    public void run(){
        while(Warrior.getnumOfWarriors()>0 && this.gamestatus){
           try{
           if(lake.getObject(this.x_coor,this.y_coor,0) instanceof Warrior &&  this instanceof Active){
              this.act((Warrior)lake.getObject(this.x_coor,this.y_coor,0),lake);
           }
           
       }
       catch(NullPointerException e){
           
       }
       try{
           Thread.sleep(10);
       }
       catch(InterruptedException e){}
    }
   }
         
}



class Binocular {
    public Binocular(){
       
    }
    public int search(SuperWarrior obj,Grid grid,int x_coor,int y_coor){//implements the searching method for lotus
     try{
      if(x_coor!=grid.getColumns()-1 && grid.getType(x_coor+1,y_coor,1)==Lotus.class ){//checks whether the nearest line crossings aren't  occupied and it has a lotus object on it
          return 1;  // 1 is to move right
      }
      else if (y_coor!=grid.getRows()-1 && grid.getType(x_coor,y_coor+1,1)!=null &&grid.getType(x_coor, y_coor+1,1)==Lotus.class ){
          return 3;//3 is to move forward
      }
      else if(x_coor!=0 && grid.getType(x_coor-1,y_coor,1)!=null && grid.getType(x_coor-1, y_coor,1)==Lotus.class  ){
          return 2;//2 is to move right
      }
      else if(y_coor!=0 && grid.getType(x_coor,y_coor-1,1)!=null && grid.getType(x_coor,y_coor-1,1)==Lotus.class ){
          return 4; // 3 is to move ackward
      }
      else{
          return 0;
      } 
      
    }
    catch(NullPointerException e){
        return 0;
    }
}
}


//This class controls the game
class Controller {
    private Random chooser=new Random();  //creates a Random type object with the intension to generate random numbers which give warriors direction to swim
    private Grid nozama;
    private   ArrayList<Object> inhabitants=new ArrayList<Object> ();//inclludes all the inhabitantants on the grid
    private ArrayList<Warrior> refer_warrior=new ArrayList<Warrior>();//To keep track of warriors
    private Treasure treasure;
    private ArrayList<Fish> refer_Fish=new ArrayList<Fish>();
    private ArrayList<Lotus> refer_Lotus=new ArrayList<Lotus>();
    private ArrayList<String> summary;
    public Controller(int[] warriors,int[] fish,int numofLotus,int[] grid,String[] names ,int[] treasure){// gets the information to generate the gaming environment
          nozama=new Grid(grid[0],grid[1],grid[2]);
          
          this.generateWarriors(warriors,names);
          this.generateFish(fish);
          this.generateLotus(numofLotus);
          this.generateTreasure(treasure[0],treasure[1]);
          this.summary=new ArrayList<String>();
          
    }
    public void generateWarriors(int[] warriors,String[] names){  //Generate warriors as the user intended
       
        int count=0;
        
        while(Warrior.getnumOfWarriors()<warriors[0]+warriors[1]){ //check if the required number of warriors are created
          
          int x_coor=chooser.nextInt(10);   //picks a random x_coordinate
          int y_coor=0;
          if(SuperWarrior.numOfSuper<warriors[1] && nozama.getType(x_coor, y_coor,0)==null){//Checks whether the created coordinate is empty
              refer_warrior.add(new SuperWarrior(names[count])); //Adds the objects in to the Array list which keep track of warriors
              System.out.println("initial-"+names[count]+" "+x_coor+" "+y_coor);
              refer_warrior.get(refer_warrior.size()-1).addGrid(nozama);
              refer_warrior.get(refer_warrior.size()-1).addCtrl(this);
              inhabitants.add(refer_warrior.get(refer_warrior.size()-1));  //add the created object to the inhabitants arrylist
              nozama.addObject(refer_warrior.get(count),x_coor, y_coor,0);  //Requesting to add the object to the grid
              refer_warrior.get(count).setCoordinates(x_coor, y_coor);   //Update the coordinates of  warrior
              count++;
          }
          if(NormalWarrior.numOfNormal<warriors[0] && nozama.getType(x_coor, y_coor,0)==null){
               refer_warrior.add(new NormalWarrior(names[count]));
               System.out.println("initial-"+names[count]+" "+x_coor+" "+y_coor);
               refer_warrior.get(refer_warrior.size()-1).addGrid(nozama);
               refer_warrior.get(refer_warrior.size()-1).addCtrl(this);
               inhabitants.add(refer_warrior.get(refer_warrior.size()-1));
               nozama.addObject(refer_warrior.get(count),x_coor, y_coor,0);
               refer_warrior.get(count).setCoordinates(x_coor, y_coor);
               count++;
           }
          
        }
        
            
        
    }
    
    public void generateFish(int[] fish){
        
        while(Fish.numOfFish<fish[0]+fish[1]+fish[2]){
            Fish f;
            int x_coor=chooser.nextInt(10)+1;   //picks a random x_coordinate
            int y_coor=chooser.nextInt(10)+1;
            if(KillerFish.numOfKiller<fish[1] && nozama.getType(x_coor, y_coor,1)==null){  //Creates killer fish objects if the refered isn't occupied
                f=new KillerFish(x_coor,y_coor,nozama);
                refer_Fish.add(f);
                nozama.addObject(f,x_coor, y_coor,1);
                System.out.println("initial killer fish-"+Fish.numOfFish+" "+x_coor+" "+y_coor);
                  
                
                
            }
            if(InnocentFish.numOfInnocent<fish[0] && nozama.getType(x_coor, y_coor,1)==null){
                f=new InnocentFish(x_coor,y_coor,nozama);
                refer_Fish.add(f);
                nozama.addObject(f, x_coor, y_coor,1);
                System.out.println("initial Innocent fish-"+Fish.numOfFish+" "+x_coor+" "+y_coor);
                
            }
            if(RubberFish.numOfRubber<fish[2] && nozama.getType(x_coor, y_coor,1)==null){
                f=new RubberFish(x_coor,y_coor,nozama);
                refer_Fish.add(f);
                nozama.addObject(f, x_coor, y_coor,1);
                System.out.println("initial rubber fish-"+Fish.numOfFish+" "+x_coor+" "+y_coor);
            }
            
            
        }
        
    }
    public void generateLotus(int num){
        while(Lotus.numOfLotus<num){
          Lotus l;
          int x_coor=chooser.nextInt(10)+1;
          int y_coor=chooser.nextInt(10)+1;
          if(nozama.getType(x_coor, y_coor,1)==null){
              l=new  Lotus(x_coor,y_coor,nozama);
              refer_Lotus.add(l);
              nozama.addObject(l,x_coor, y_coor,1);
              System.out.println("initial lotus-"+Lotus.numOfLotus+" "+x_coor+" "+y_coor);
          }
         
         
        }
    
        
    }
    public void generateTreasure(int x_coor,int y_coor){
      
        treasure=new Treasure(x_coor,y_coor,nozama);
        nozama.addObject(treasure, x_coor, y_coor,1);
        for(int i=0;i<refer_warrior.size();i++){
               this.treasure.addObserver(refer_warrior.get(i));
        }
        for(int j=0;j<refer_Lotus.size();j++){
           this.treasure.addObserver(refer_Lotus.get(j)) ;
        }
        for(int k=0;k<refer_Fish.size();k++){
            this.treasure.addObserver(refer_Fish.get(k));
        }
      
       
    }
    public void Play(){// The logic of  the game is applied in this method
              Thread wr1=new Thread(refer_warrior.get(0));
              Thread wr2=new Thread(refer_warrior.get(1));
              Thread wr3=new Thread(refer_warrior.get(2));
              Thread wr4=new Thread(refer_warrior.get(3));
              Thread f1=new Thread(refer_Fish.get(0));
              Thread f2=new Thread(refer_Fish.get(1));
              Thread f3=new Thread(refer_Fish.get(2));
              Thread f4=new Thread(refer_Fish.get(3));
              Thread f5=new Thread(refer_Fish.get(4));
              Thread f6=new Thread(refer_Fish.get(5));
              Thread l1=new Thread(refer_Lotus.get(0));
              Thread l2=new Thread(refer_Lotus.get(1));
              Thread l3=new Thread(refer_Lotus.get(2));
              Thread l4=new Thread(refer_Lotus.get(3));
              Thread l5=new Thread(refer_Lotus.get(4));
              Thread t1=new Thread(treasure);
              
              wr1.start();
              wr2.start();
              wr3.start();
              wr4.start();
              f1.start();
              f2.start();
              f3.start();
              f4.start();
              f5.start();
              f6.start();
              l1.start();
              l2.start();
              l3.start();
              l4.start();
              l5.start();
              t1.start();
              
              while(true){// if the refered  warrior was killed on action,the number of the warriors is decreased
                 if(Warrior.getnumOfWarriors()==0){
                  System.out.println("ALL WARRIORS HAVE BEEN COMPROMIZED!!");
                  this.updateSummary("ALL WARRIORS HAVE BEEN COMPROMIZED!!");
                  System.out.println("GAME OVER!!");
                  this.updateSummary("GAME OVER!!");
                  break;
                 
                 }
                 if(this.treasure.checkTreasure()){
                     break;
                 }
                 try{
                     Thread.sleep(10);
                 }
                 catch(InterruptedException e){}
              }
              this.generateSummary();
              
    }
    public void updateSummary(String sen){
        this.summary.add(sen);
    }
    public void generateSummary(){
        try{
            FileWriter writer=new FileWriter("GameSummary.txt");
            BufferedWriter buffer=new BufferedWriter(writer);
            buffer.write("!!!!GAME SUMMARY!!!!");
            buffer.newLine();
            buffer.write("--------------------");
            for(int i=0;i<this.summary.size();i++){
                buffer.newLine();
                buffer.write(summary.get(i));
            }
            //writer.close();
            buffer.close();
        }catch(IOException ioex){}
    }
               
       
            
    
    
}

public class Game{
    public static void main(String[] args) {
        Controller ctrl;
        int [] warriors=new int[]{2,2};                             //initializing the number of super warriors and normla warriors that are going to be played.{Normal,Super}
        int [] fish=new int[]{2,2,2};                              //initializing the number offish{Innocent,Killer,Rubber}
        int numofLotus=5;                                          //initializing the number of lotus that are going to be placed
        int [] grid=new int[]{11,11,2};                                          //initializing the size of the grid that holds(refers) objects in the game{columns,rows,layers}
        String[] names=new String[]{"NED","SNOW","DANERIS","JORAH"};              //initializing the names of warriors according to the number of warriors
        int[] treasurecoor=new int[]{5,5};                                          //initializing the coordinate of the treasure {x_coordinate,y_coordinate} 
        ctrl=new  Controller(warriors,fish,numofLotus,grid,names,treasurecoor);   //creating the Controller object ctrl which controls  the whole game
        ctrl.Play();   //plays the game
        
    }   
}


