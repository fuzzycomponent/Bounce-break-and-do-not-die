class EdgeDetect {
    public EdgeDetect(boolean beginingState) {
        state=beginingState;
    }
    public boolean state;
    public boolean rising=false,falling=false;
    public tick(boolean incomingState) {
        if(incomingState&state&falling) {
            falling=false;
            rising=true;
        }
        if(!incomingState&falling) {state=false;falling=false;}
        if(incomingState&!state) {rising= true;state=true;} 
    }
}