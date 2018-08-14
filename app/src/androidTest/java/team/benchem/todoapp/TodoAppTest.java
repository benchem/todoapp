package team.benchem.todoapp;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class TodoAppTest {

    private Instrumentation mInstrumentation;
    private UiDevice mUiDevice;

    @Before
    public void setUp(){
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mUiDevice = UiDevice.getInstance(mInstrumentation);
    }

    @Test
    public void addItemTest() throws InterruptedException {

        //1. 回到桌面
        mUiDevice.pressHome();
        Thread.sleep(500);

        //2. 滑动到程序页
        mUiDevice.swipe(500, 200, 0, 200, 10);
        Thread.sleep(500);

        //3.启动APP
        mUiDevice.findObject(By.text("Todo备忘录")).click();
        Thread.sleep(500);

        //4. 点击 添加按钮
        mUiDevice.findObject(By.res("team.benchem.todoapp:id/moduleActivityMain_btnAdd")).click();
        Thread.sleep(500);

        //5. 输入 备忘事项
        String todoStr = UUID.randomUUID().toString();
        mUiDevice.findObject(By.res("team.benchem.todoapp:id/moduleActivityTodoDetail_etTodo")).setText(todoStr);
        Thread.sleep(500);

        //6. 点击 确认
        mUiDevice.findObject(By.res("team.benchem.todoapp:id/moduleActivityTodoDetail_btnConfirm")).click();
        Thread.sleep(500);

        //7. 验证数据是否添加成功
        Assert.assertNotNull(mUiDevice.findObject(By.text(todoStr)));
    }

}
