package org.sberinsur.interruptDemo.join;

public class JoinDemoThread extends Thread{

        public JoinDemoThread(String name){
            super(name);
        }

        @Override
        public void run(){
            int i = 0;
            //ВАЖНО!!! Следует учитывать то, что если обрабатывать исключения внутри цикла, управелние переходит к блоку catch
            //и статус потока автоматиески сбрасывается, то есть !isInterrupted вернет true
            //Чтобы измежать такой ситуации, как варинат, внутри блока catch можно сразы вызвать break.
            //Либо же поместить бесконечный цикл в блок try.
            for (int j = 0; j < 5; j++){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("sout from " + Thread.currentThread().getName() + " " + i++);
            }
            System.out.println(Thread.currentThread().getName() + " finished");
        }
}
