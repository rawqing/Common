package yq.test.utils;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读写yaml文件
 */
public class YamlRW {

    private static List list = null;
    private static String ymlPath = null;

    /**
     * 读取yaml文件中包含的 document ,并通过List方式返回
     * @param absolutePath yaml 文件的绝对路径
     * @return document list
     */
    public static List readYamlFile(String absolutePath){
        if (absolutePath.equals(ymlPath)) {
            if (list != null) {
                return list;
            }
        }
        list = new ArrayList();
        File file = new File(absolutePath);
        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            Object contact = null;
            try {
                assert reader != null;
                contact = reader.read();
            } catch (YamlException e) {
                e.printStackTrace();
            }
            if (contact == null) break;
            list.add(contact);
        }
        ymlPath = absolutePath;
        return list;
    }

    /**
     * 从yml文件中读取一个map , 只会读取一个doc ,并且必须是map格式的
     * @param absolutePath
     * @return
     */
    public static Map readYamlMap(String absolutePath){
        File file = new File(absolutePath);
        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            return reader.read(Map.class);
        } catch (FileNotFoundException | YamlException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readYamlInstance(String absolutePath ,Class<T> clz){
        File file = new File(absolutePath);
        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            return reader.read(clz);
        } catch (FileNotFoundException | YamlException e) {
            e.printStackTrace();
        }
        return null;
    }
}
