

class Robot (){
    private Case position;
    private double vitesse;

    public Robot(Case c, double v){
        this.position = c;
        this.vitesse = v;
    }

    public Case getPosition(){
        return this.position;
    }

    public void setPosition(Case c){
        this.position = c;
    }

    public double getVitesse(NatureTerrain){
        
    }
}


class Drone extends Robot {

    public Drone(Case c){
        super(c, 100);
    }

    public double getVitesse(){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<150) this.vitesse = v;
        else  throw new IllegalArgumentExeption("Vitesse Drone < 150 km/h !");
    }
}

class R_Chenille extends Robot {

    public R_Chenille(Case c){
        super(c, 60);
    }

    public double getVitesse(){
        return this.vitesse;
    }
    public double setVitesse(double v){
        if(v<80){
            if(this.position.getNature()==FORET) this.vitesse = v/2;
            else this.vitesse = v;
        }

        else  throw new IllegalArgumentExeption("Vitesse R_Chenille < 80 km/h !");
        
    }
}