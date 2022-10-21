package evenement;


public abstract class Evenement {
        private long date;

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

