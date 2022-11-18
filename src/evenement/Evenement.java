package evenement;



public abstract class Evenement {
        private long date;
        private boolean isExecuted;

        public Evenement(long date){
                this.date = date;
                this.isExecuted = false;
        }

        public long getDate(){
            return this.date;
        }
        public void setDate(long d){
            this.date = d;
        }
        public boolean getisExecuted(){
            return this.isExecuted;
        }
        public void setisExecuted(boolean t){
            this.isExecuted = t;
        }

        public abstract void execute(long date);
}