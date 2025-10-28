public class SynchronyManager implements Runnable {
    protected int threads=1,availableThreads=1;
    public boolean exitSignal= false;
    
    public SynchronyManager() {
        try {Thread.sleep(1);} catch(InterruptedException d) {}
        System.out.println("booting up");

        preferredInitializer();//activate debug by running this method again. no wait a second rwar is temp for i am alone on this.
        threads= Runtime.getRuntime().availableProcessors();
        availableThreads= Thread.activeCount();
    }
    /*


    */
    private static int rwar=0;
    @Override
    public void run() {
        byte mode= 0;//play minigames too
        int displayContext= 0;//please note such this simple of a thing is practice from early on software development than to now.
        Boolean[] cycle;//Multithreading
        try {
            while(cycling()) {
                rwar++;






                if(rwar==10000) {exitSignal= true;System.out.println("completed task");break;}
            }
        } catch(NullPointerException f) {
            //null is ruled
        }
        System.out.println(rwar);
    }
    private boolean recycle= true;
    boolean cycles=true;
    boolean preferredInitializer() {
        return true;
    }
    boolean cycling() {
        boolean condition= true;
        int thredz= availableThreads;
        for(int x= 0;x<availableThreads;x++) {
            if(condition&!(thredz<=availableThreads)) {
                condition= false;

            }
            thredz--;
        }
        return condition;
    }
}