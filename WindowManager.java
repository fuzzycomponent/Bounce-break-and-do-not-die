public class WindowManager {
    private int width,height,centerX,centerY;
    public static MainWindow window= null;static Robot r= null;
    boolean booted= false,allowMouseMoveByRobot= true,waitformousedelta= false,grabbed= false;
    int testx=0,testy=0;EdgeDetect resize= new EdgeDetect();
    public WindowManager(String title,int WIDTH,int HEIGHT) {
        width= WIDTH;height= HEIGHT;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                window= new MainWindow(title, width, height);
            }});
        waitforlaunch();
        centerX= window.displayX+(width/2);centerY= window.displayY+(height/2);
    }
    public void waitforlaunch() {
        BufferedImage newimage= new BufferedImage(10,10,BufferedImage.TYPE_3BYTE_BGR);
        boolean loading= true;
        while(loading) {
            try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            try {
                window.render(newimage);
                loading= false;
            } catch (Exception e) {}
        }
    }
    public void render(BufferedImage input) {
        window.render(input);
    }
    public boolean isLooping() {return window.notshuttingdown;}
    public boolean setMouseGrab(boolean grab) {
        if(grabbed&grab) {
            return false;
        }else if(!grabbed&!grab){
            return false;
        }else if(!grabbed&grab) {
            grabbed= true;
            window.setCursorToCrosshair(true);
            return true;
        }else if(grabbed&!grab){
            grabbed= false;
            window.setCursorToCrosshair(false);
            return true;
        }
        return false;
    }
    public boolean wasResized() {boolean returns= resize.getRisingEdge();resize.tick(false);return returns;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public void tick() {
        if(!booted&allowMouseMoveByRobot) {
            if(testx!=window.mx|testy!=window.my) {booted= true;waitformousedelta= true;}
        }else {
            if(grabbed&allowMouseMoveByRobot) {r.mouseMove(centerX, centerY);}
            if(window.ACTIVATED) {window.ACTIVATED= false;allowMouseMoveByRobot= true;}//none of this pauses the game and must wait a frame until updating mouse move delta before continuing to update input and because mouse moving is update based must recallibrate delta
            if(window.DEACTIVATED) {window.DEACTIVATED= false;allowMouseMoveByRobot= false;}
            if(window.RESIZED) {window.RESIZED= false;resize.tick(true);waitformousedelta= true;testx= window.mx;testy=window.my;width= window.boundX;height= window.boundY;centerX= window.displayX+(width/2);centerY= window.displayY+(height/2);}
            if(window.MOVED) {window.MOVED= false;waitformousedelta= true;centerX= window.displayX+(width/2);centerY= window.displayY+(height/2);testx= window.mx;testy=window.my;}
            if(waitformousedelta&allowMouseMoveByRobot&(window.mx!=testx|window.my!=testy)&&hardwareCheck()) {
                waitformousedelta= false;
                int counter=0,var=-1;
                for(int a=0;a<gs.length;a++) {
                    scanForGameWindow(centerX, centerY,false,testy,counter);
                    try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
                    testy=window.my;
                    scanForGameWindow(centerX, centerY,true,testy,counter);
                    try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
                    if(testy!=window.my) {var= counter;}
                    if(var==-1) {
                        counter++;
                    }else {break;}
                }
            }
        }//its benificial to not make the render and input loop dependant on game physics and rendering, to improve on that an amount of time any key is held down can be measured as a percent of final outcome delta to next frame for the following player movement calculation in the event the game is slower than player input
    }
    private static void scanForGameWindow(int centerX,int centerY,boolean alternate,int oy,int monitor) {//if no graphics device shows up game should know not to close itself due to no display being plugged into the computer
        try {
            r= new Robot(gs[monitor]);
            if(alternate) {r.mouseMove(centerX, centerY+1);}else {r.mouseMove(centerX, centerY);}//now to feedback mouse delta into this procedure
            
        } catch (AWTException e) {}
    }
    private static GraphicsDevice[] gs;
    private static boolean hardwareCheck() {
        gs= GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        return gs.length>0;
    }
    public int getMouseX() {return window.mx;}
    public int getMouseY() {return window.my;}
    public int getMouseScroll() {return window.getScroll();}
    public boolean keyState(int key) {return window.getKeyState(key);}
    public boolean keyRisingEdge(int key) {return window.getKeyRisingEdge(key);}
    public boolean keyFallingEdge(int key) {return window.getKeyFallingEdge(key);}
    public boolean buttonState(int button) {return window.getMouseButtonState(button);}
    public boolean buttonRisingEdge(int button) {return window.getMouseButtonRisingEdge(button);}
    public boolean buttonFallingEdge(int button) {return window.getMouseButtonFallingEdge(button);}
    public void frametick() {
        window.mouseFrame();
        window.keyboardFrame();
    }
    public int mouseBind() {return window.buttonCode;}
    public int scrollBind() {return window.scrollCode;}
    public int keyBind() {return window.keyCode;}
}