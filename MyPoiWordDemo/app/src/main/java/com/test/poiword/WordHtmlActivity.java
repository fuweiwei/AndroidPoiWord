package com.test.poiword;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.test.poiword.utils.FileUtils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by fuweiwei on 2015/11/28.
 */
public class WordHtmlActivity extends FragmentActivity {
    //文件存储位置
    private String docPath = "/mnt/sdcard/doc/";
    //文件名称
    private String docName = "test.doc";
    //html文件存储位置
    private String savePath = "/mnt/sdcard/doc/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html);
        String name = docName.substring(0, docName.indexOf("."));
        try {
            convert2Html(docPath + docName, savePath + name + ".html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //WebView加载显示本地html文件
        WebView webView = (WebView)this.findViewById(R.id.office);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl("file:/"+savePath+name+".html");
    }

    /**
     * word文档转成html格式
     * */
    public void convert2Html(String fileName, String outPutFile) {
        HWPFDocument wordDocument = null;
        try {
            wordDocument = new HWPFDocument(new FileInputStream(fileName));
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            //设置图片路径
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                public String savePicture(byte[] content,
                                          PictureType pictureType, String suggestedName,
                                          float widthInches, float heightInches) {
                    String name = docName.substring(0, docName.indexOf("."));
                    return name + "/" + suggestedName;
                }
            });
            //保存图片
            List<Picture> pics=wordDocument.getPicturesTable().getAllPictures();
            if(pics!=null){
                for(int i=0;i<pics.size();i++){
                    Picture pic = (Picture)pics.get(i);
                    System.out.println( pic.suggestFullFileName());
                    try {
                        String name = docName.substring(0,docName.indexOf("."));
                        String file = savePath+ name + "/"
                                + pic.suggestFullFileName();
                        FileUtils.makeDirs(file);
                        pic.writeImageContent(new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            out.close();
            //保存html文件
            writeFile(new String(out.toByteArray()), outPutFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将html文件保存到sd卡
     * */
    public void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }
}
