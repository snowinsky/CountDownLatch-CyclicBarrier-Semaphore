package cn.snow.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        //5: 许可的个数
        //true: 是否是公平获取。公平获取的意思就是等的越久越先获取
        Semaphore semaphore = new Semaphore(2, false);

        for (int i = 1; i <= 5; i++) {
            new SubTask(semaphore, i*1000).start();
        }
    }

    /**
     * Semaphore更像是锁，在子线程里先获取acquire，然后执行业务处理，然后release。Semaphore就像是一个管理锁的池子。
     * acquire方法和release方法都是阻塞的。
     * 这可以用来做限流。
     *
     * D:\dev_tools\java8\jdk\bin\java.exe -javaagent:D:\dev_tools\ideaIU-2018.2.8.win\lib\idea_rt.jar=63278:D:\dev_tools\ideaIU-2018.2.8.win\bin -Dfile.encoding=UTF-8 -classpath D:\dev_tools\java8\jdk\jre\lib\charsets.jar;D:\dev_tools\java8\jdk\jre\lib\deploy.jar;D:\dev_tools\java8\jdk\jre\lib\ext\access-bridge-64.jar;D:\dev_tools\java8\jdk\jre\lib\ext\cldrdata.jar;D:\dev_tools\java8\jdk\jre\lib\ext\dnsns.jar;D:\dev_tools\java8\jdk\jre\lib\ext\jaccess.jar;D:\dev_tools\java8\jdk\jre\lib\ext\jfxrt.jar;D:\dev_tools\java8\jdk\jre\lib\ext\localedata.jar;D:\dev_tools\java8\jdk\jre\lib\ext\nashorn.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunec.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunjce_provider.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunmscapi.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunpkcs11.jar;D:\dev_tools\java8\jdk\jre\lib\ext\zipfs.jar;D:\dev_tools\java8\jdk\jre\lib\javaws.jar;D:\dev_tools\java8\jdk\jre\lib\jce.jar;D:\dev_tools\java8\jdk\jre\lib\jfr.jar;D:\dev_tools\java8\jdk\jre\lib\jfxswt.jar;D:\dev_tools\java8\jdk\jre\lib\jsse.jar;D:\dev_tools\java8\jdk\jre\lib\management-agent.jar;D:\dev_tools\java8\jdk\jre\lib\plugin.jar;D:\dev_tools\java8\jdk\jre\lib\resources.jar;D:\dev_tools\java8\jdk\jre\lib\rt.jar;D:\idea20180208ws\CountDownLatch-CyclicBarrier-Semaphore\semaphore\target\classes;D:\dev_tools\maven-local-repo\m2\com\google\guava\guava\28.1-jre\guava-28.1-jre.jar;D:\dev_tools\maven-local-repo\m2\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;D:\dev_tools\maven-local-repo\m2\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;D:\dev_tools\maven-local-repo\m2\com\google\code\findbugs\jsr305\3.0.2\jsr305-3.0.2.jar;D:\dev_tools\maven-local-repo\m2\org\checkerframework\checker-qual\2.8.1\checker-qual-2.8.1.jar;D:\dev_tools\maven-local-repo\m2\com\google\errorprone\error_prone_annotations\2.3.2\error_prone_annotations-2.3.2.jar;D:\dev_tools\maven-local-repo\m2\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;D:\dev_tools\maven-local-repo\m2\org\codehaus\mojo\animal-sniffer-annotations\1.18\animal-sniffer-annotations-1.18.jar;D:\dev_tools\maven-local-repo\m2\org\projectlombok\lombok\1.18.10\lombok-1.18.10.jar;D:\dev_tools\maven-local-repo\m2\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;D:\dev_tools\maven-local-repo\m2\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;D:\dev_tools\maven-local-repo\m2\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar cn.snow.semaphore.SemaphoreDemo
     * 16:39:25.900 [Thread-1] INFO cn.snow.semaphore.SubTask - acquire before.2000
     * 16:39:25.900 [Thread-0] INFO cn.snow.semaphore.SubTask - acquire before.1000
     * 16:39:25.902 [Thread-1] INFO cn.snow.semaphore.SubTask - acquire after.2000
     * 16:39:25.900 [Thread-4] INFO cn.snow.semaphore.SubTask - acquire before.5000
     * 16:39:25.900 [Thread-3] INFO cn.snow.semaphore.SubTask - acquire before.4000
     * 16:39:25.900 [Thread-2] INFO cn.snow.semaphore.SubTask - acquire before.3000
     * 16:39:25.903 [Thread-0] INFO cn.snow.semaphore.SubTask - acquire after.1000
     * 16:39:26.904 [Thread-0] INFO cn.snow.semaphore.SubTask - sub task execute complete.1000
     * 16:39:26.904 [Thread-0] INFO cn.snow.semaphore.SubTask - release before.1000
     * 16:39:26.904 [Thread-0] INFO cn.snow.semaphore.SubTask - release before.1000
     * 16:39:26.904 [Thread-4] INFO cn.snow.semaphore.SubTask - acquire after.5000
     * 16:39:27.903 [Thread-1] INFO cn.snow.semaphore.SubTask - sub task execute complete.2000
     * 16:39:27.903 [Thread-1] INFO cn.snow.semaphore.SubTask - release before.2000
     * 16:39:27.903 [Thread-1] INFO cn.snow.semaphore.SubTask - release before.2000
     * 16:39:27.903 [Thread-3] INFO cn.snow.semaphore.SubTask - acquire after.4000
     * 16:39:31.904 [Thread-4] INFO cn.snow.semaphore.SubTask - sub task execute complete.5000
     * 16:39:31.904 [Thread-3] INFO cn.snow.semaphore.SubTask - sub task execute complete.4000
     * 16:39:31.904 [Thread-4] INFO cn.snow.semaphore.SubTask - release before.5000
     * 16:39:31.904 [Thread-3] INFO cn.snow.semaphore.SubTask - release before.4000
     * 16:39:31.904 [Thread-4] INFO cn.snow.semaphore.SubTask - release before.5000
     * 16:39:31.904 [Thread-2] INFO cn.snow.semaphore.SubTask - acquire after.3000
     * 16:39:31.904 [Thread-3] INFO cn.snow.semaphore.SubTask - release before.4000
     * 16:39:34.904 [Thread-2] INFO cn.snow.semaphore.SubTask - sub task execute complete.3000
     * 16:39:34.904 [Thread-2] INFO cn.snow.semaphore.SubTask - release before.3000
     * 16:39:34.904 [Thread-2] INFO cn.snow.semaphore.SubTask - release before.3000
     *
     * Process finished with exit code 0
     */
}
