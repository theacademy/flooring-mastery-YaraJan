import com.yj.flooringmastery.controller.OrderController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        OrderController controller = ctx.getBean("controller", OrderController.class);

        controller.run();
    }
}
