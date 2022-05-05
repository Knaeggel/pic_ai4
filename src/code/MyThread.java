package code;

import gui.MainFrame;

public class MyThread implements Runnable{

    private boolean exit;

    Thread t;

    public MyThread()
    {

        t = new Thread(this);
        exit = false;
        t.start(); // Starting the thread
    }

    // execution of thread starts from run() method
    public void run()
    {
        while (!exit) {
            Decoder.obj .mainFrame.updateGui();
                Decoder.obj.decoder.nextStep();
        }
    }

    // for stopping the thread
    public void stop()
    {
        exit = true;
    }


}
