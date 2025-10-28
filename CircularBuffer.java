public class CircularBuffer<E> {
    private ArrayList<E> circular= new ArrayList<>();
    private volatile int read=0,write=0,allocated=0,length;
    public CircularBuffer(int size) {
        for(int a=0;a<size;a++) {
            circular.add(null);
        }
        length= size;
    }
    public static Exception pendingPleaseWait= new Exception("pending update from circular buffer"),//with this specific exception a limited number of wait attempts may be necessary
            nextTimeInform= new Exception("use setOutReady after reading, or at least at the end of your tick cycle"),
            awaitingFlow= new Exception("waiting for tick flow");
    volatile boolean out=false,in=false,getoutput=false;
    E output= null,input;
    public boolean isDataAvailable() {return allocated>0;}
    public void setOutReady() {getoutput= true;}
    public E getOutput() throws Exception {//in practice without setOutReady() a modification to out and polling out to determine ready state would risk thread safety. Execute setOutReady after each read of getOutput().
        if(getoutput&!out) {throw pendingPleaseWait;}else {
            if(out) {out= false;return output;}else {throw nextTimeInform;}
        }
    }
    public boolean write(E message) throws Exception {
        if(read==write&allocated>0) {return false;}else if(in) {//if returns false buffer was full. no write to beginning read index will occur.
            throw awaitingFlow;
        }else {
            in= true;
            input= message;
            return true;
        }
    }
    public boolean tick() {//use setOutReady every time after an output is taken.  
        boolean circulated= false;
        if(getoutput&!out) {
            if(allocated>0) {
                if(read>write) {
                    output= circular.get(read++);
                    read%=length;
                    circulated=out=true;
                    allocated--;
                }else {
                    output= circular.get(read++);
                    circulated=out=true;
                    allocated--;
                }
            }
            getoutput=false;
        }
        if(in) {
            if(write==read&allocated==0) {
                allocated++;
                in=false;circulated=true;
                circular.set(write++, input);
                write%=length;
                //if empty
            }else if(read>write) {
                circular.set(write++, input);
                in=false;circulated=true;
                allocated++;
            }else {
                circular.set(write++, input);
                write%=length;
                in=false;circulated=true;
                allocated++;
            }
        }
        return circulated;
    }
}