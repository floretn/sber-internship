package org.sberinsur.interruptDemo;

public class InterruptDemoMain {
    public static void main(String[] args){
        //Создаем поток, переопределяем run()
        Thread interruptDemoThread = new Thread("interruptedDemoThread"){
            @Override
            public void run(){
                System.out.println(getState());
                int i = 0;
                //ВАЖНО!!! Следует учитывать то, что если обрабатывать исключения внутри цикла, управелние переходит к блоку catch
                //и статус потока автоматиески сбрасывается, то есть !isInterrupted вернет true
                //Чтобы измежать такой ситуации, как варинат, внутри блока catch можно сразы вызвать break.
                //Либо же поместить бесконечный цикл в блок try.
                while(!isInterrupted()){
                    System.out.println("sout from " + Thread.currentThread().getName() + i++);
                }
                System.out.println(Thread.currentThread().getName() + " finished");
            }
        };
        System.out.println(interruptDemoThread.getState());
        //Запускаем поток
        interruptDemoThread.start();

        try {
            Thread.sleep(100);
            interruptDemoThread.interrupt();

        } catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
            //public boolean interrupted();
            System.out.println(interruptDemoThread.getState());
        }
        System.out.println("Main thread finished");

    }

}
