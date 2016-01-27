package com.test.poiword;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.test.poiword.utils.FileUtils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    // 模板文集地址
    private static final String demoPath =  "/mnt/sdcard/doc/test.doc";
    // 创建生成的文件地址
    private static final String newPath = "/mnt/sdcard/doc/testS.doc";
    private Button btn,btns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.btn);
        btns=(Button)findViewById(R.id.btns);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    InputStream inputStream = getAssets().open("test.doc");
                    FileUtils.writeFile(new File(demoPath), inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                doScan();
            }
        });
        btns.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this,WordHtmlActivity.class);
                startActivity(intent);
            }
        });

    }

    private void doScan(){
        //获取模板文件
        File demoFile=new File(demoPath);
        //创建生成的文件
        File newFile=new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$QYMC$", "xxx科技股份有限公司");
        map.put("$QYDZ$", "上海市杨浦区xx路xx号");
        map.put("$QYFZR$", "张三");
        map.put("$FRDB$", "李四");
        map.put("$CJSJ$", "2000-11-10");
        map.put("$SCPZMSJWT$", "5");
        map.put("$XCJCJBQ$", "6");
        map.put("$JLJJJFF$", "7");
        map.put("$QYFZRQM$", "张三");
        map.put("$CPRWQM$", "赵六");
        map.put("$ZFZH$", "100001");
        map.put("$BZ$", "无");
        writeDoc(demoFile,newFile,map);
        //查看
        doOpenWord();
    }
    /**
     * 调用手机中安装的可打开word的软件
     */
    private void doOpenWord(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try{
            MainActivity.this.startActivity(intent);
        } catch(ActivityNotFoundException e) {
            //检测到系统尚未安装OliveOffice的apk程序
            Toast.makeText(MainActivity.this, "未找到软件", Toast.LENGTH_LONG).show();
            //请先到www.olivephone.com/e.apk下载并安装
        }
    }
    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     * */
    public void writeDoc(File demoFile ,File newFile ,Map<String, String> map)
    {
        try
        {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // 读取word文本内容
            Range range = hdt.getRange();
            // System.out.println(range.text());

            // 替换文本内容
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
