package evenement;
import io.Direction;
import robot.*;
import plan.*;


public class Deplacer_Robot extends Evenement{
    private Direction direction;
    private Robot robot;
    private Carte carte;
    public Deplacer_Robot(Direction direction,Robot robot,long date,Carte carte){ 
        super(date);
        this.direction = direction;
        this.robot =robot;
        this.carte =carte;
    }
    
    public void execute(){
        robot.deplacer(direction,carte);
    }
}
