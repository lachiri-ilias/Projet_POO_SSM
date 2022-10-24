package evenement;
import robot.*;




public abstract class Evenement {
        private long date;
        // private long duree;
        // private Robot robot;

        public Evenement(long date){
                this.date = date;
        }

        public long getDate(){
            return this.date;
        }
        public void setDate(long d){
            this.date = d;
        }

        public abstract void execute(long date);
}




/*
public abstract class Evenement {
        private long date;
        // private Robot robot;

        public Evenement(long date){
                this.date = date;
        }

        public long getDate(){
            return this.date;
        }
        public void setDate(long d){
            this.date = d;
        }

        public abstract void execute(long date);
}
*/
